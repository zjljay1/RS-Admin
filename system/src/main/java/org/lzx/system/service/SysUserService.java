package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysUser;
import org.lzx.common.domain.param.CreateUserParam;
import org.lzx.common.domain.vo.LoginResult;
import org.lzx.common.domain.vo.SysUserVO;
import org.lzx.common.domain.vo.UserInfoVO;
import org.lzx.common.response.Result;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    /**
     * 分页获取数据
     *
     * @param params 查询参数
     * @return IPage<SysUserVO>
     */
    IPage<SysUserVO> getList(Map<String, Object> params);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    LoginResult login(String username, String password);

    /**
     * 登录
     *
     * @param createUserParam 创建用户信息
     * @return 创建结果
     */
    Result<String> createUser(CreateUserParam createUserParam);

    /**
     * 获取用户信息
     *
     * @return UserInfoVO
     */
    Result<UserInfoVO> getUserInfo(String authorizationHeader);
}
