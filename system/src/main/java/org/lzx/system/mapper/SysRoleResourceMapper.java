package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysRole;

@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRole> {


    int checkMenuExistRole(@Param("resourceId") Long resourceId);

    int removeByRoleIds(Long[] ids);

    int removeByRoleID(Long roleId);
}
