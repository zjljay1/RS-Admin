package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.vo.SysRoleVO;
import org.lzx.common.response.Result;


import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页获取数据
     *
     * @param params 查询参数
     * @return IPage<SysRole>
     */
    IPage<SysRole> getPage(Map<String, Object> params);

    Result<List<SysRoleVO>> getAllRoles(String authorizationHeader);

    List<String> getUserRole(Long id);
}
