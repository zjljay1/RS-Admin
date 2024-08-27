package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<String> getUserRole(Long id);

    int addRole(SysRole sysRole);

    /**
     * 删除角色
     *
     * @param roleIds 需要删除的角色ID
     * @return sql执行数量
     */
    int removeRoleByIds(Long[] roleIds);

    /**
     * 根据角色名称查询角色信息
     *
     * @param roleName 角色名称
     * @return 结果
     */
    SysRole checkRoleNameUnique(String roleName);

    int updateRole(@Param("item") SysRole sysRole);

    int removeRoleByID(Long id);
}
