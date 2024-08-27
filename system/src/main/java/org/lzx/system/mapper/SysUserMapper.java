package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.common.domain.entity.SysUser;
import org.springframework.security.core.parameters.P;

/**
 * 用户表
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    int insertUser(SysUser sysUser);

    int deleteUser(Long[] ids);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
     SysUser checkUserNameUnique(String userName);

    int updateuser(SysUser sysUser);
}
