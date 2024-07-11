package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.common.domain.entity.SysUserRole;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int getByRoleId(long id);
}
