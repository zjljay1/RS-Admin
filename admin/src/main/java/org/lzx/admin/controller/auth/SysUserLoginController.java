package org.lzx.admin.controller.auth;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.annotation.SysLogInterface;
import org.lzx.common.domain.param.SysUserLoginParam;
import org.lzx.common.domain.vo.LoginResult;
import org.lzx.common.domain.vo.UserInfoVO;
import org.lzx.common.enums.BusinessType;
import org.lzx.common.response.Result;
import org.lzx.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SysUserLoginController")
@RequestMapping("/auth")
public class SysUserLoginController {

    private final SysUserService sysUserService;

    @Operation(summary = "登录")
    @PostMapping(value = "/login")
    @SysLogInterface(title = "登录", businessType = BusinessType.GRANT)
    public Result<LoginResult> login(@RequestBody SysUserLoginParam sysUserLoginParam) {
        log.info("登录信息：{}", sysUserLoginParam.toString());
        // 获取系统验证码开关
        // boolean sw = Boolean.parseBoolean(ParamResolver.getStr(ConfigEnums.SYS_CAPTCHA_IMG.name(), "true"));
        // if (sw) {
        // 验证码校验
        // boolean captcha = sysCaptchaService.validate(sysUserLoginParam.getUuid(),
        //         sysUserLoginParam.getCode());
        // if (!captcha) {
        //     return Result.failed("验证码不正确");
        // }
        // }
        return Result.success(sysUserService.login(sysUserLoginParam.getUserName(),
                sysUserLoginParam.getPassword()));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/getUserInfo")
    public Result<UserInfoVO> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        return sysUserService.getUserInfo(authorizationHeader);
    }



}
