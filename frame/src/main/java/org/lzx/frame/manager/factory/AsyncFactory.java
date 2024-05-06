package org.lzx.frame.manager.factory;


import lombok.extern.slf4j.Slf4j;
import org.lzx.common.domain.entity.SysOperationLog;
import org.lzx.common.utils.IpUtil;
import org.lzx.common.utils.SpringUtils;
import org.lzx.system.service.SysOperationLogService;

import java.util.TimerTask;

@Slf4j
public class AsyncFactory {

	/**
	 * 操作日志记录
	 * @param operLog 操作日志信息
	 * @return 任务task
	 */
	public static TimerTask recordOper(final SysOperationLog operLog) {
		return new TimerTask() {
			@Override
			public void run() {
				String address = IpUtil.getAddress(operLog.getReqIp());
				// 远程查询操作地点
				operLog.setOperLocation(address);
				SpringUtils.getBean(SysOperationLogService.class).save(operLog);
			}
		};
	}

}