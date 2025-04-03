package org.lzx.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.vo.SysRoleVO;
import org.lzx.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.system.service.SysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SysRoleController", description = "用户角色控制层")
//@RequestMapping("/sys/role")
@RequestMapping("/systemManage")
@Validated
public class SysRoleController {

    private final SysRoleService sysRoleService;


    @Operation(summary = "list 分页列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10"),
            @Parameter(name = "username", description = "用户名称"),
    })
    @GetMapping(value = "/getRoleList")
    public Result<IPage<SysRole>> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        IPage<SysRole> sysUsers = sysRoleService.getPage(params);
        return Result.success(sysUsers);
    }

    @Operation(summary = "查询角色信息")
    @GetMapping(value = "/getAllRoles")
    public Result<List<SysRoleVO>> getAllRoles(@RequestHeader("Authorization") String authorizationHeader) {
        return sysRoleService.getAllRoles(authorizationHeader);
    }

    /**
     * 新增用户
     *
     * @param sysRole
     * @return
     */
    @Operation(summary = "新增角色信息")
    @PostMapping("/addRole")
    @PreAuthorize("@RS.hasPermission('manage:role:add')")
    public Result<Boolean> addRole(@RequestBody @Valid SysRole sysRole) {
        return sysRoleService.addRole(sysRole);
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色ID
     * @return boolean
     */
    @Operation(summary = "删除角色信息")
    @DeleteMapping("/batchRemoveRole")
    @PreAuthorize("@RS.hasPermission('manage:role:remove')")
    public Result<Boolean> batchRemoveRole(@RequestBody @NotEmpty(message = "角色id不能为空") Long[] ids) {
        return Result.toAjax(sysRoleService.batchRemoveRole(ids));
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return boolean
     */
    @Operation(summary = "删除角色信息")
    @DeleteMapping("/removeRole/{id}")
    @PreAuthorize("@RS.hasPermission('manage:role:remove')")
    public Result<Boolean> removeRole(@PathVariable @NotNull(message = "角色id不能为空") Long id) {
        return Result.toAjax(sysRoleService.removeRole(id));
    }

    @Operation(summary = "修改角色信息")
    @PutMapping("/editRole")
    @PreAuthorize("@RS.hasPermission('manage:role:edit')")
    public Result<Boolean> updateRole(@RequestBody @Valid SysRole sysRole) {
//        超级管理员不能修改
        sysRoleService.checkRoleAllowed(sysRole);
//
        if (sysRoleService.checkRoleNameUnique(sysRole)) {
            return Result.failed("修改角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        }

        return Result.toAjax(sysRoleService.updateRole(sysRole));
    }

    //获取用户角色与资源的关联
    @Operation(summary = "角色与资源关联信息")
    @GetMapping("/getRuleResource/{ruleId}")
//    @PreAuthorize("@RS.hasPermission('manage:role:get')")
    public Result<List<Long>> getRuleResource(@PathVariable @NotNull(message = "角色ID不能为空") Long ruleId){
        return Result.success(sysRoleService.getRuleResource(ruleId));
    }

}
