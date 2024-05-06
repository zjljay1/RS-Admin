package org.lzx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.common.domain.entity.SysOperationLog;

@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    int clean();

}
