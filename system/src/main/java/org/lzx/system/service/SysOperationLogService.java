package org.lzx.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.common.domain.entity.SysOperationLog;

import java.util.List;

public interface SysOperationLogService extends IService<SysOperationLog> {

    /**
     * 清空
     *
     * @return
     */
    int clean();

    /**
     * 导出
     *
     * @param sysOperationLog
     * @return
     */
    List<SysOperationLog> getExportList(SysOperationLog sysOperationLog);

}
