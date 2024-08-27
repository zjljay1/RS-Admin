package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.bo.SysMenuBO;
import org.lzx.common.domain.entity.SysResource;
import org.lzx.common.domain.vo.SysMenuVO;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> getUserRoutes(String id);

    List<String> getUserPermissions(Long id);

    int updateMenu(@Param("item") SysMenuBO sysMenuBO);

    int hasChildByMenuId(@Param("id") long id);

    int deleteMenuById(@Param("id") long id);

    int batchDeleteMenuById(@Param("ids") long[] ids);


    SysResource checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") long parentId);

    List<SysResource> getList();

    int insertMenu(@Param("item") SysMenuBO sysMenuBO);
}
