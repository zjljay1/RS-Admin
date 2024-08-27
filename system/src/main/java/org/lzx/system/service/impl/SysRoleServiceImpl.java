package org.lzx.system.service.impl;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.constant.UserConstants;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.vo.SysRoleVO;
import org.lzx.common.enums.DelStatusEnums;
import org.lzx.common.enums.StatusEnums;
import org.lzx.common.exception.GlobalException;
import org.lzx.common.exception.GlobalExceptionEnum;
import org.lzx.common.response.Result;
import org.lzx.common.utils.JwtTokenUtil;
import org.lzx.common.utils.StringUtils;
import org.lzx.system.mapper.SysRoleMapper;
import org.lzx.system.mapper.SysRoleResourceMapper;
import org.lzx.system.mapper.SysUserRoleMapper;
import org.lzx.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    final JwtTokenUtil jwtTokenUtil;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public IPage<SysRole> getPage(Map<String, Object> params) {
        int pageSize = Integer.parseInt(String.valueOf(params.get("size")));
        int pageNum = Integer.parseInt(String.valueOf(params.get("current")));
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(params);

        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    private LambdaQueryWrapper<SysRole> createWrapper(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        String roleCode = (String) params.get("roleCode");
        String status = (String) params.get("status");
        // 解释 这段SysRole::getRoleName代码
        // 表示使用SysRole类中的getRoleName方法作为条件，如果条件不为空，则使用like方法进行模糊查询
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(roleName), SysRole::getRoleName, roleName);
        wrapper.like(StrUtil.isNotEmpty(roleCode), SysRole::getRoleCode, roleCode);
        wrapper.eq(StrUtil.isNotEmpty(status), SysRole::getStatus, status);
        wrapper.eq(SysRole::getIsDeleted, DelStatusEnums.DISABLE.getCode());

        return wrapper;
    }

    @Override
    public Result<List<SysRoleVO>> getAllRoles(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtTokenUtil.getTokenHead())) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_UNAUTHORIZED.getMessage());
        }

        List<SysRoleVO> sysRoleVOS = new ArrayList<>();

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getStatus, StatusEnums.ENABLE.getKey());

        List<SysRole> list = list(wrapper);
        if (!list.isEmpty()) {
            for (SysRole sysRole : list) {
                SysRoleVO sysRoleVO = new SysRoleVO();
                sysRoleVO.setRoleName(sysRole.getRoleName());
                sysRoleVO.setRoleCode(sysRole.getRoleCode());
                sysRoleVO.setRoleDesc(sysRole.getRoleDesc());
                sysRoleVOS.add(sysRoleVO);
            }
            return Result.success(sysRoleVOS);
        }
        return Result.success();
    }

    @Override
    public List<String> getUserRole(Long id) {
        return baseMapper.getUserRole(id);
    }

    @Override
    public Result<Boolean> addRole(SysRole sysRole) {
        log.info("数据：{}", sysRole.toString());
        System.out.println(sysRoleMapper == null);
        int i = sysRoleMapper.addRole(sysRole);
        return Result.toAjax(i);
    }

    @Override
    @Transactional
    public int batchRemoveRole(Long[] ids) {
        for (Long id : ids) {
            IsCheckRole(id);
        }
        //删除角色关联的资源
        sysRoleResourceMapper.removeByRoleIds(ids);
        // 删除角色
        return sysRoleMapper.removeRoleByIds(ids);
    }

    @Override
    public int removeRole(Long id) {
        IsCheckRole(id);
        //删除角色关联的资源
        sysRoleResourceMapper.removeByRoleID(id);
        // 删除角色
        return sysRoleMapper.removeRoleByID(id);
    }


    @Override
    public int updateRole(SysRole sysRole) {
        //查看角色是否存在
        SysRole role = sysRoleMapper.selectById(sysRole.getId());
        //修改角色
        if (!Objects.isNull(role)) {
            return sysRoleMapper.updateRole(sysRole);
        }
        return 0;
    }

    /**
     * 判断角色是否已经分配给用户
     *
     * @param id 角色ID
     * @return 是否分配
     */
    public boolean isRoleAssignedUser(long id) {
        int i = sysUserRoleMapper.getByRoleId(id);
        return i > 0;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getId()) && role.isAdmin()) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_NOT_ALLOW_OPERATE_SUPER_ADMIN.getMessage());
        }
    }

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long id = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        SysRole info = sysRoleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
            return UserConstants.UNIQUE;
        }
        return UserConstants.NOT_UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param id
     */
    public void IsCheckRole(Long id) {
        //判断是否是超级管理员
        if (1 == id) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_CAN_NOT_DELETE_RESOURCE.getMessage());
        }
        //是否已经分配给用户 assigned
        if (isRoleAssignedUser(id)) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_ROLE_HAS_USER.getCode());
        }
    }


}
