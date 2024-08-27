package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysUserRole;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int getByRoleId(long id);

    void batchUserRole(List<SysUserRole> list);

    void deleteUserRoleByUserId(Long[] ids);

    void insertUserRole(Long id, List<Long> userRoles);

}
