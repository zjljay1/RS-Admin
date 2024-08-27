package org.lzx.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.lzx.common.annotation.SysLogInterface;
import org.lzx.common.domain.entity.SysUser;
import org.lzx.common.domain.param.UserParam;
import org.lzx.common.domain.vo.SysUserVO;
import org.lzx.common.enums.BusinessType;
import org.lzx.common.response.Result;
import org.lzx.system.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.lzx.common.utils.DataUtil.getUserId;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SysUserController", description = "用户信息控制层")
//@RequestMapping("/sys/user")
@RequestMapping("/systemManage")
public class SysUserController {

    private final SysUserService sysUserService;

    @Operation(summary = "list 分页列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10"),
            @Parameter(name = "username", description = "用户名称"),
    })
    @GetMapping(value = "/getUserList")
    public Result<IPage<SysUserVO>> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        IPage<SysUserVO> sysUsers = sysUserService.getList(params);
        return Result.success(sysUsers);
    }

    @Operation(summary = "创建用户信息")
    @PostMapping(value = "/createUser")
    @SysLogInterface(title = "创建用户信息", businessType = BusinessType.INSERT)
    @PreAuthorize("@RS.hasPermission('manage:user:add')")
    public Result<String> createUser(@RequestBody @Valid UserParam userParam) throws Exception {
        return sysUserService.createUser(userParam);
    }

    @Operation(summary = "删除用户信息")
    @DeleteMapping(value = "/deleteUser")
    @SysLogInterface(title = "删除用户信息", businessType = BusinessType.DELETE)
    @PreAuthorize("@RS.hasPermission('manage:user:remove')")
    public Result<Boolean> deleteUser(@RequestBody @NotNull(message = "id不能为空") Long[] ids) {
        if (ArrayUtils.contains(ids, getUserId()))
        {
            return Result.failed("当前用户不能删除");
        }
        return Result.toAjax(sysUserService.deleteUser(ids));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping(value = "/updateUser")
    @SysLogInterface(title = "更新用户信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("@RS.hasPermission('manage:user:edit')")
    public Result<Boolean> updateUser(@RequestBody @Valid SysUserVO userVO) {

        if (!sysUserService.checkUserNameUnique(userVO))
        {
            return Result.failed("修改用户'" + userVO.getUserName() + "'失败，登录账号已存在");
        }
        return Result.toAjax(sysUserService.updateUser(userVO));
    }

}
