package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.common.domain.entity.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper  extends BaseMapper<SysRole> {

    List<String> getUserRole(Long id);

}
