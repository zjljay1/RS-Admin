package org.lzx.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.constant.UserConstants;
import org.lzx.common.domain.vo.SysMenuTreeVO;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.response.Result;
import org.lzx.common.utils.StringUtils;
import org.lzx.system.service.SysResourceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SysMenuController", description = "菜单信息控制层")
//@RequestMapping("/sys/menu")
@RequestMapping("/systemManage")
@Validated
public class SysMenuController {

    private final SysResourceService sysResourceService;

    @Operation(summary = "查询菜单信息")
    @GetMapping(value = "/getMenuList/v2")
    @Parameters({
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10"),
    })
    // @PreAuthorize("@RS.hasPermission('sys:menu:list')")
    public Result<IPage<SysMenuVO>> getMenuList(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        return sysResourceService.getMenuList(params);
    }

    @Operation(summary = "查询全部页面组件名称")
    @GetMapping(value = "/getAllPages")
    public Result<List<String>> getAllPages() {
        return sysResourceService.getAllPages();
    }

    /**
     * 编辑菜单
     */
    @PreAuthorize("@RS.hasPermission('manage:menu:edit')")
    @Operation(summary = "编辑菜单")
    @PutMapping(value = "/editMenu")
    public Result<Boolean> editMenu(@Validated @RequestBody SysMenuVO sysMenuVO) {
        if (!sysResourceService.checkMenuNameUnique(sysMenuVO)) {
            return Result.failed("修改菜单'" + sysMenuVO.getMenuName() + "'失败，菜单名称已存在");
        } else if (sysMenuVO.getId().equals(sysMenuVO.getParentId().longValue())) {
            return Result.failed("修改菜单'" + sysMenuVO.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return Result.toAjax(sysResourceService.editMenu(sysMenuVO));
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @PreAuthorize("@RS.hasPermission('manage:menu:remove')")
    @Operation(summary = "删除菜单")
    @DeleteMapping(value = "/removeMenu/{id}")
    public Result<Boolean> removeMenu(@PathVariable("id") long id) {
        if (sysResourceService.hasChildByMenuId(id)) {
            return Result.failed("存在子菜单,不允许删除");
        }
        if (sysResourceService.checkMenuExistRole(id)) {
            return Result.failed("菜单已分配,不允许删除");
        }
        return Result.toAjax(sysResourceService.deleteMenu(id));
    }

    /**
     * 添加菜单
     *
     * @param sysMenuVO
     * @return
     */
    @PreAuthorize("@RS.hasPermission('manage:menu:add')")
    @Operation(summary = "添加菜单")
    @PostMapping(value = "/addMenu")
    public Result<Boolean> addMenu(@Valid @RequestBody SysMenuVO sysMenuVO) {
        if (!sysResourceService.checkMenuNameUnique(sysMenuVO)) {
            return Result.failed("新增菜单'" + sysMenuVO.getMenuName() + "'失败，菜单名称已存在");
        }
        return Result.toAjax(sysResourceService.addMenu(sysMenuVO));
    }

    @Operation(summary = "查询菜单树结构")
    @GetMapping("/getMenuTree")
    public Result<List<SysMenuTreeVO>> getMenuTree() {
        return Result.success(sysResourceService.getMenuTree());
    }

}
