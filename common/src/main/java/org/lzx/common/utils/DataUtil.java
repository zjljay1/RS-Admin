package org.lzx.common.utils;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.domain.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetails;


@SuppressWarnings("ALL")
@Slf4j
public class DataUtil {

    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        return getLoginUser().getId();
    }

    /**
     * 获取当前登录用户信息
     */
    public static SysUser getLoginUser() {
        return SecurityUtil.getSysUser();
    }
}
