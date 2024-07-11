package org.lzx.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.lzx.common.constant.UserConstants;
import org.lzx.common.domain.bo.SysMenuBO;
import org.lzx.common.domain.entity.SysResource;
import org.lzx.common.domain.model.Meta;
import org.lzx.common.domain.vo.SysMenuTreeVO;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.domain.vo.SysRoutesVO;
import org.lzx.common.enums.DelStatusEnums;
import org.lzx.common.enums.MenuTypeEnums;
import org.lzx.common.exception.GlobalException;
import org.lzx.common.exception.GlobalExceptionEnum;
import org.lzx.common.response.Result;
import org.lzx.common.utils.*;
import org.lzx.system.mapper.SysResourceMapper;
import org.lzx.system.mapper.SysRoleResourceMapper;
import org.lzx.system.service.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource>
        implements SysResourceService {

    private final JwtTokenUtil jwtTokenUtil;
    private static final RouteUtil routeUtil = new RouteUtil();

    final UserDetailsService userDetailsService;

    @Autowired
    final private SysResourceMapper sysResourceMapper;

    @Autowired
    final private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public Result<Map<String, Object>> getUserRoutes(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtTokenUtil.getTokenHead())) {
            throw new GlobalException(GlobalExceptionEnum.ERROR_UNAUTHORIZED.getMessage());
        }

        Map<String, Object> map = new HashMap<>();

        try {
            String authToken = authorizationHeader.substring(jwtTokenUtil.getTokenHead().length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            if (username != null) {
                // 从数据库中获取用户信息
                SysUserDetail userDetails = (SysUserDetail) this.userDetailsService
                        .loadUserByUsername(username);

                String id = userDetails.getSysUser()
                        .getId().toString();

                List<SysResource> userRoutes = baseMapper.getUserRoutes(id);

                List<SysRoutesVO> routesVOList = routeUtil.processRoute(userRoutes);

                map.put("home", "home");
                map.put("routes", routesVOList);
                log.info("用户路由信息：{}", map.toString());
                return Result.success(map);
            }
        } catch (Exception e) {
            log.info("获取资源信息异常: {}", e.getMessage());
            ExceptionUtil.throwEx(GlobalExceptionEnum.ERROR_GAIN_RESOURCE);
        }

        return Result.success();
    }

    @Override
    public Result<IPage<SysMenuVO>> getMenuList(Map<String, Object> params) {
        int pageNum = 1;
        int pageSize = 10;

        if (ObjUtil.isNotEmpty(params)) {
            pageSize = Integer.parseInt(String.valueOf(params.get("size")));
            pageNum = Integer.parseInt(String.valueOf(params.get("current")));
        }
        LambdaQueryWrapper<SysResource> lambdaQueryWrapper = getBaseQueryWrapper();

        List<SysResource> list = list(lambdaQueryWrapper);

        List<SysMenuVO> menuVOS = convertSysResourceToMenuVO(list);

        List<SysMenuVO> processedMenuVOS = routeUtil.processMenu(menuVOS);

        List<SysMenuVO> pagedMenuVOS = paginate(processedMenuVOS, pageNum, pageSize);

        IPage<SysMenuVO> sysMenuVOIPage = new Page<>();
        sysMenuVOIPage.setRecords(pagedMenuVOS);
        sysMenuVOIPage.setCurrent(pageNum);
        sysMenuVOIPage.setSize(pageSize);
        sysMenuVOIPage.setTotal(processedMenuVOS.size());
        log.info("菜单信息：{}", sysMenuVOIPage.toString());
        return Result.success(sysMenuVOIPage);
    }

    // Method to create base LambdaQueryWrapper
    private LambdaQueryWrapper<SysResource> getBaseQueryWrapper() {
        LambdaQueryWrapper<SysResource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysResource::getIsDeleted, DelStatusEnums.DISABLE.getCode());
        lambdaQueryWrapper.notIn(SysResource::getMenuType, MenuTypeEnums.BASIC_MENU.getCode(), MenuTypeEnums.BUTTON.getCode());
        return lambdaQueryWrapper;
    }

    // Method to convert SysResource to SysMenuVO
    private List<SysMenuVO> convertSysResourceToMenuVO(List<SysResource> resources) {
        List<SysMenuVO> menuVOList = new ArrayList<>();
        resources.forEach(item -> {
            String meta = item.getMeta();
            SysMenuVO sysMenuVO = convertMetaJsonToSysMenuVO(meta);
            sysMenuVO.setId(item.getId());
            sysMenuVO.setParentId(Long.valueOf(item.getParentId()));
            sysMenuVO.setUiPath(item.getUiPath());
            sysMenuVO.setMenuType(item.getMenuType());
            sysMenuVO.setStatus(item.getStatus());
            sysMenuVO.setMenuName(item.getMenuName());
            sysMenuVO.setRouteName(item.getRouteName());
            sysMenuVO.setRoutePath(item.getRoutePath());
            sysMenuVO.setComponent(item.getComponent());
            sysMenuVO.setWeight(item.getWeight());
            sysMenuVO.setCreateId(item.getCreateId());
            sysMenuVO.setCreateBy(item.getCreateBy());
            sysMenuVO.setCreateTime(item.getCreateTime());

            sysMenuVO.setUpdateId(item.getUpdateId());
            sysMenuVO.setUpdateBy(item.getUpdateBy());
            sysMenuVO.setUpdateTime(item.getUpdateTime());

            sysMenuVO.setIsDeleted(item.getIsDeleted());
            sysMenuVO.setDeleteTime(item.getDeleteTime());
            menuVOList.add(sysMenuVO);
        });
        return menuVOList;
    }

    // Method to paginate a list
    private <T> List<T> paginate(List<T> list, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        return list.subList(start, end);
    }

    public static SysMenuVO convertMetaJsonToSysMenuVO(String metaJson) {
        // 创建Meta对象并填充属性
        Meta meta = new Meta(metaJson);
        // 创建并初始化SysMenuVO对象
        SysMenuVO sysMenuVO = new SysMenuVO();
        // 将Meta对象的属性赋值给SysMenuVO对象
        BeanUtil.copyProperties(meta, sysMenuVO, false);

        return sysMenuVO;
    }

    @Override
    public Result<List<SysRoutesVO>> getConstantRoutes() {

        LambdaQueryWrapper<SysResource> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysResource::getIsDeleted, DelStatusEnums.DISABLE.getCode());
        lambdaQueryWrapper.eq(SysResource::getMenuType, MenuTypeEnums.BASIC_MENU.getCode());

        List<SysResource> list = list(lambdaQueryWrapper);

        List<SysRoutesVO> routesVOList = routeUtil.processRoute(list);
        return Result.success(routesVOList);
    }

    @Override
    public List<String> getUserPermissions(Long id) {
        return baseMapper.getUserPermissions(id);
    }

    @Override
    public Result<List<String>> getAllPages() {
        LambdaQueryWrapper<SysResource> lambdaQueryWrapper = getBaseQueryWrapper();
        List<SysResource> list = list(lambdaQueryWrapper);

        List<String> routeNames = list.stream()
                .map(SysResource::getRouteName)
                .collect(Collectors.toList());

        return Result.success(routeNames);
    }

    @Override
    public int editMenu(SysMenuVO sysMenuVO) {
        SysMenuBO sysMenuBO = new SysMenuBO();
        BeanUtil.copyProperties(sysMenuVO, sysMenuBO);
        String metaJson = convertSysMenuVOtoMetaJson(sysMenuVO);
        String queryJson = JSONUtil.toJsonStr(sysMenuVO.getQuery());
        sysMenuBO.setMeta(metaJson);
        sysMenuBO.setQuery(queryJson);
        return sysResourceMapper.updateMenu(sysMenuBO);
    }

    @Override
    public int deleteMenu(long menuId) {
        return sysResourceMapper.deleteMenuById(menuId);
    }

    @Override
    public int addMenu(SysMenuVO sysMenuVO) {
        return 0;
    }

    @Override
    public boolean hasChildByMenuId(long menuId) {
        int result = sysResourceMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = sysRoleResourceMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenuVO menu) {
        Long menuId = StringUtils.isNull(menu.getId()) ? -1L : menu.getId();
        SysResource info = sysResourceMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取系统菜单树
     *
     * @return SysMenuTreeVO
     */
    @Override
    public List<SysMenuTreeVO> getMenuTree() {
        //获取所有菜单资源
        List<SysResource> list = sysResourceMapper.getList();
        List<SysMenuTreeVO> convert;
        try {
            //将List<SysResource>菜单资源转为List<SysMenuTreeVO>菜单树
            Map<String, String> map = new HashMap<>();
            map.put("id", "id");
            map.put("parentId", "pId");
            map.put("menuName", "label");
            convert = RouteUtil.convert(list, SysMenuTreeVO.class, map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convert;
    }

    /**
     * 将 系统菜单 VO 转换为 Meta Json
     *
     * @param sysMenuVO
     * @return string
     */
    public static String convertSysMenuVOtoMetaJson(SysMenuVO sysMenuVO) {
        Meta meta = new Meta();
        BeanUtil.copyProperties(sysMenuVO, meta);
        return JSONUtil.toJsonStr(meta);
    }


}
