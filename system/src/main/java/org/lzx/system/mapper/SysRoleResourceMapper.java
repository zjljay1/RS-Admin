package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.entity.SysRoleResource;
import org.lzx.common.domain.param.SysRoleResourceParam;

import java.util.List;

@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {


    int checkMenuExistRole(@Param("resourceId") Long resourceId);

    int removeByRoleIds(Long[] ids);

    int removeByRoleID(Long roleId);

    List<Long> getRuleResource(@Param("ruleId")Long ruleId);

    void deleteRoleResourceByRoleId(@Param("roleId") Long roleId);

    int insertRoleResource(List<SysRoleResource> params);
}
