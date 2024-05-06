package org.lzx.common.utils;


import org.lzx.common.domain.entity.SysUser;
import org.lzx.common.exception.GlobalException;
import org.lzx.common.response.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SecurityUtil
 */
public class SecurityUtil {

    public static SysUser getSysUser() {
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            SysUserDetail sysUserDetail = (SysUserDetail) auth.getPrincipal();
            return sysUserDetail.getSysUser();
        } catch (Exception e) {
            throw new GlobalException(ResultCode.UNAUTHORIZED);
        }
    }

    public static UserDetails getUserDetails() {
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new GlobalException(ResultCode.UNAUTHORIZED);
        }
        return userDetails;
    }

}
