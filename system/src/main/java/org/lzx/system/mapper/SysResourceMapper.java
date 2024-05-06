package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.common.domain.entity.SysResource;

import java.util.List;

@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> getUserRoutes(String id);

    List<String> getUserPermissions(Long id);

}
