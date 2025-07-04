package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.param.SysRoleResourceParam;
import org.lzx.common.domain.vo.SysRoleVO;
import org.lzx.common.response.Result;


import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页获取数据
     *
     * @param params 查询参数
     * @return IPage<SysRole>
     */
    IPage<SysRole> getPage(Map<String, Object> params);

    Result<List<SysRoleVO>> getAllRoles(String authorizationHeader);

    List<SysRoleVO> getUserRole(Long id);

    Result<Boolean> addRole(SysRole sysRole);

    int batchRemoveRole(Long[] id);

    int removeRole(Long id);

    int updateRole(SysRole sysRole);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);


    /**
     * 校验角色名称是否唯一
     * @param sysRole 角色信息
     * @return 结果
     */
    boolean checkRoleNameUnique(SysRole sysRole);

    List<Long> getRuleResource(Long ruleId);

    int editRoleResource(SysRoleResourceParam params);

    /**
     * 检查角色是否存在
     * @param roleId 角色ID
     * @return 结果
     */
    boolean checkRoleExist(Long roleId);
}
