package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysResource;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.domain.vo.SysRoutesVO;
import org.lzx.common.response.Result;


import java.util.List;
import java.util.Map;

public interface SysResourceService extends IService<SysResource> {

    Result<Map<String, Object>> getUserRoutes(String authorizationHeader);

    Result<IPage<SysMenuVO>> getMenuList(Map<String, Object> params);

    Result<List<SysRoutesVO>> getConstantRoutes();

    List<String> getUserPermissions(Long id);

    Result<List<String>> getAllPages();
}
