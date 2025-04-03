package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.entity.SysRoleResource;

import java.util.List;

@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {


    int checkMenuExistRole(@Param("resourceId") Long resourceId);

    int removeByRoleIds(Long[] ids);

    int removeByRoleID(Long roleId);

    List<Long> getRuleResource(@Param("ruleId")Long ruleId);
}
