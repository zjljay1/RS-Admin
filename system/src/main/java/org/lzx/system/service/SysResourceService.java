package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysResource;
import org.lzx.common.domain.vo.SysMenuTreeVO;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.domain.vo.SysRoutesVO;
import org.lzx.common.response.Result;


import java.util.List;
import java.util.Map;

public interface SysResourceService extends IService<SysResource> {

    Result<Map<String, Object>> getUserRoutes(String authorizationHeader);

    Result<IPage<SysMenuVO>> getMenuList(Map<String, Object> params);

    Result<List<SysRoutesVO>> getConstantRoutes();

    List<String> getUserPermissions(Long id);

    Result<List<String>> getAllPages();

    int editMenu(SysMenuVO sysMenuVO);

    int deleteMenu(long menuId);

    int batchDeleteMenuById(long[] menuIds);


    /**
     * 添加菜单
     * @param sysMenuVO
     * @return
     */
    int addMenu(SysMenuVO sysMenuVO);
    /**
     * 是否存在菜单子节点
     *
     * @param id 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(long id);

    /**
     * 查询菜单是否存在角色
     *
     * @param id 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long id);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysMenuVO menu);

    /**
     * 获取菜单树
     * @return SysMenuTreeVO
     */
    List<SysMenuTreeVO> getMenuTree();

}
