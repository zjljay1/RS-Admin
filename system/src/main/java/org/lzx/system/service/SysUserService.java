package org.lzx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysUser;
import org.lzx.common.domain.param.UserParam;
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
     * @param userParam 创建用户信息
     * @return 创建结果
     */
    Result<String> createUser(UserParam userParam) throws Exception;

    /**
     * 获取用户信息
     *
     * @return UserInfoVO
     */
    Result<UserInfoVO> getUserInfo(String authorizationHeader);

    int deleteUser(Long[] ids);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);

    int updateUser(SysUserVO sysUser);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(SysUserVO user);

}
