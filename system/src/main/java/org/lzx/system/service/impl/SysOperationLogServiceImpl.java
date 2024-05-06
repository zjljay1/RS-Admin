package org.lzx.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.domain.entity.SysOperationLog;
import org.lzx.system.mapper.SysOperationLogMapper;
import org.lzx.system.service.SysOperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog>
        implements SysOperationLogService {

    @Override
    public int clean() {
        return this.baseMapper.clean();
    }

    @Override
    public List<SysOperationLog> getExportList(SysOperationLog sysOperationLog) {
        return null;
    }
}
