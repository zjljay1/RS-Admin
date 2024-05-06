package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.common.domain.entity.SysUser;

/**
 * 用户表
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
