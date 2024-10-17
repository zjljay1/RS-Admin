package org.lzx.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.constant.UserConstants;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.entity.SysUser;
import org.lzx.common.domain.entity.SysUserRole;
import org.lzx.common.domain.param.UserParam;
import org.lzx.common.domain.vo.LoginResult;
import org.lzx.common.domain.vo.SysRoleVO;
import org.lzx.common.domain.vo.SysUserVO;
import org.lzx.common.domain.vo.UserInfoVO;
import org.lzx.common.enums.CacheConstants;
import org.lzx.common.enums.DelStatusEnums;
import org.lzx.common.exception.GlobalException;
import org.lzx.common.exception.GlobalExceptionEnum;
import org.lzx.common.response.Result;
import org.lzx.common.response.ResultCode;
import org.lzx.common.utils.*;
import org.lzx.system.mapper.SysRoleMapper;
import org.lzx.system.mapper.SysUserMapper;
import org.lzx.system.mapper.SysUserRoleMapper;
import org.lzx.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    final AuthenticationManager authenticationManager;

    final JwtTokenUtil jwtTokenUtil;

    final UserDetailsService userDetailsService;

    final SysRoleServiceImpl sysRoleService;


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public IPage<SysUserVO> getList(Map<String, Object> params) {
        int pageSize = Integer.parseInt(String.valueOf(params.get("size")));
        int pageNum = Integer.parseInt(String.valueOf(params.get("current")));
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(params);

        IPage<SysUser> sysUserPage = page(new Page<>(pageNum, pageSize), wrapper);

        // 将查询结果中的SysUser对象转换为SysUserVO对象
        List<SysUserVO> sysUserVOList = sysUserPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());


        if (!sysUserVOList.isEmpty()) {
            sysUserVOList.forEach(userVO -> {
                Long id = userVO.getId();
                List<SysRoleVO> userRole = sysRoleService.getUserRole(id);
                userVO.setRoles(userRole);
            });
        }

        // 创建新的IPage对象，其中包含转换后的SysUserVO对象列表
        IPage<SysUserVO> sysUserVOPage = new Page<>();
        sysUserVOPage.setRecords(sysUserVOList);
        sysUserVOPage.setCurrent(sysUserPage.getCurrent());
        sysUserVOPage.setSize(sysUserPage.getSize());
        sysUserVOPage.setTotal(sysUserPage.getTotal());

        return sysUserVOPage;
    }

    public SysUserVO convertToVO(SysUser sysUser) {
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtil.copyProperties(sysUser, sysUserVO);
        return sysUserVO;
    }

    private LambdaQueryWrapper<SysUser> createWrapper(Map<String, Object> params) {
        String username = (String) params.get("userName");
        String status = (String) params.get("status");
        String nickName = (String) params.get("nickName");
        String userGender = (String) params.get("userGender");
        String userPhone = (String) params.get("userPhone");
        String userEmail = (String) params.get("userEmail");

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(username), SysUser::getUserName, username);
        wrapper.eq(StrUtil.isNotEmpty(status), SysUser::getStatus, status);
        wrapper.eq(SysUser::getIsDeleted, DelStatusEnums.DISABLE.getCode());
        wrapper.like(StrUtil.isNotEmpty(nickName), SysUser::getNickName, nickName);
        wrapper.eq(StrUtil.isNotEmpty(userGender), SysUser::getUserGender, userGender);
        wrapper.like(StrUtil.isNotEmpty(userPhone), SysUser::getUserPhone, userPhone);
        wrapper.like(StrUtil.isNotEmpty(userEmail), SysUser::getUserEmail, userEmail);

        return wrapper;
    }

    /**
     * 更新最后一次登录时间/登录IP
     */
    private void updateLastLoginInfo(SysUser sysUser) {
        sysUser.setLastLoginIp(IpUtil.getHostIp());
        sysUser.setLastLoginTime(DateUtil.date().toLocalDateTime());
        updateById(sysUser);
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#username")
    public LoginResult login(String username, String password) {
        LoginResult res = new LoginResult();
        String token;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            token = jwtTokenUtil.generateToken(authenticate.getName());

            SysUser sysUser = SecurityUtil.getSysUser();

            // 更新最后一次登录时间
            updateLastLoginInfo(sysUser);

            res.setToken(token);
            res.setRefreshToken(token);

            return res;
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage());
            throw new GlobalException(e.getMessage());
        }
    }

    public SysUser getByName(String nickName) {
        return getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getNickName, nickName));
    }

    public SysUser getByUsername(String userName) {
        return getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserName, userName));
    }

    private Result<String> checkoutUser(UserParam userParam) {
        SysUser dbUserNameInfo;
        dbUserNameInfo = getByName(userParam.getNickName());
        if (dbUserNameInfo != null) {
            return Result.failed(ResultCode.ERROR_NAME_REPEAT);
        }
        dbUserNameInfo = getByUsername(userParam.getUserName());
        if (dbUserNameInfo != null) {
            return Result.failed(ResultCode.ERROR_USER_NAME_REPEAT);
        }
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(UserParam userParam) throws Exception {
//        String password = userParam.getPassword();
//        // 处理加密密码
//        String enPassword = encoder.encode(password);
        //把CreateUserParam转换为SysUser
        SysUser sysUser = BeanUtil.copyProperties(userParam, SysUser.class);
//        sysUser.setPassword(enPassword);
        //插入用户
        int row = sysUserMapper.insertUser(sysUser);
        //根据createUserParam.getUserRoles()插入用户角色关联表
        if (!userParam.getUserRoles().isEmpty()) {
            //根据角色编码获取角色ID
            insertUserRole(sysUser.getId(), userParam.getUserRoles());
        }
        return row > 0;
    }

    private void insertUserRole(Long id, List<String> userRoles) {
        if (Objects.isNull(userRoles) || userRoles.isEmpty()) {
            return;
        }
        List<SysUserRole> list = new ArrayList<>(userRoles.size());
        for (String role : userRoles) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(id);
            sysUserRole.setRoleId(Long.valueOf(role));
            sysUserRole.setCreateTime(DateUtil.date().toLocalDateTime());
            list.add(sysUserRole);
        }
        sysUserRoleMapper.batchUserRole(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createUser(UserParam userParam) throws Exception {
        Result<String> checkoutResult = checkoutUser(userParam);
        if (!Objects.equals(checkoutResult.getCode(), ResultCode.SUCCESS.getCode())) {
            return checkoutResult;
        }
        return insertUser(userParam) ? Result.success() : Result.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserInfoVO> getUserInfo(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtTokenUtil.getTokenHead())) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_UNAUTHORIZED.getMessage());
        }

        UserInfoVO userInfoVO = new UserInfoVO();

        try {
            String authToken = authorizationHeader.substring(jwtTokenUtil.getTokenHead().length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            if (username != null) {
                // 从数据库中获取用户信息
                SysUserDetail userDetails = (SysUserDetail) this.userDetailsService
                        .loadUserByUsername(username);

                userInfoVO.setRoles(userDetails.getRoles());
                userInfoVO.setUserName(userDetails.getUsername());
                userInfoVO.setUserId(userDetails.getSysUser().getId());

                return Result.success(userInfoVO);
            }
        } catch (Exception e) {
            log.info("获取用户信息失败: {}", e.getMessage());
            ExceptionUtil.throwEx(GlobalExceptionEnum.ERROR_UNABLE_GET_USER);
        }
        return Result.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUser(Long[] ids) {
        for (Long id : ids) {
            checkUserAllowed(new SysUser(id));
        }
        //删除用户与角色的关联
        sysUserRoleMapper.deleteUserRoleByUserId(ids);
        //删除用户
        return sysUserMapper.deleteUser(ids);
    }


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin()) {
            ExceptionUtil.throwEx(GlobalExceptionEnum.ERROR_NOT_ALLOW_OPERATE_SUPER_ADMIN);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUserVO userVO) {
        SysUser sysUser = BeanUtil.copyProperties(userVO, SysUser.class);
        checkUserAllowed(sysUser);
        //更新用户与角色的关联
        Long[] longs = {sysUser.getId()};
        sysUserRoleMapper.deleteUserRoleByUserId(longs);
        insertUserRole(sysUser.getId(), userVO.getUserRoles());
        return sysUserMapper.updateUser(sysUser);
    }

    @Override
    public boolean checkUserNameUnique(SysUserVO user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


}
