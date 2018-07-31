package com.cjh.suning.task;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cjh.suning.bean.ReturnResultBean;
import com.cjh.suning.bean.spring.SpringContextUtils;
import com.cjh.suning.config.ApplicationConfig;
import com.cjh.suning.service.I360Service;

public class I360OrderTask implements Runnable {

	public I360OrderTask(Date beginTime, Date endTime) {
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	Date beginTime;

	Date endTime;

	private static AtomicBoolean taskFinishFlag = new AtomicBoolean(false);

	private Logger log = LoggerFactory.getLogger(I360OrderTask.class);

	@Override
	public void run() {
		I360Service service = null;
		try {
			ApplicationConfig applicationConfig = (ApplicationConfig) SpringContextUtils.getContext()
					.getBean("applicationConfig");
			Date now = new Date();
			if (applicationConfig.getModel().equals("1")) {
				service = (I360Service) SpringContextUtils.getContext().getBean("i360Service");
			}
			ReturnResultBean result = service.login(applicationConfig.getUserName(), applicationConfig.getPassword());
			if (result.getResultCode() == 0) {
				service.refresh("8131550");
				now = new Date();
				if (now.getTime() < beginTime.getTime()) {
					log.info("还未到点, 休息中...");
				}
				if (now.getTime() > endTime.getTime()) {
					log.info("到点了, 收工...");
					return;
				}
				while (now.getTime() < beginTime.getTime()) {
					if ((beginTime.getTime() - now.getTime()) < (2 * 1000 * 60)) { // 小于两分钟
						Thread.sleep(1);
						now = new Date();
						continue; // 提高刷新频率
					}
					Thread.sleep(1000 * 60 * 1); // 1分钟刷新一次，避免session失效
					if (applicationConfig.getSku().equals("1")) {
						service.refresh(applicationConfig.getPhoneUrl());
					}
					if (applicationConfig.getSku().equals("2")) {
						service.refresh(applicationConfig.getMaotaiUrl());
					}
					now = new Date();
				}
				log.info("到点了, 开始干活...");
				while (!taskFinishFlag.get()) {
					now = new Date();
					if (now.getTime() > endTime.getTime()) {
						log.info("到点了, 收工...");
						return;
					}
					long startTime = System.currentTimeMillis();
					result = service.order("8131550", "1");
					double excTime = (double) (System.currentTimeMillis() - startTime) / 1000;
					log.info("本次作业花费时间：" + excTime + "秒");
					if (result.getResultCode() == 0) {
						log.info("下单成功, 收队咯");
						taskFinishFlag.set(true);
						break;
					}
					Thread.sleep(1);
				}
			}
			log.info(result.getReturnMsg());
		} catch (InterruptedException e1) {
			log.info("任务中断...");
		} catch (Exception e2) {
			log.info("任务异常", e2);
		} finally {
			log.info("任务退出");
			if (service != null) {
				service.destroy();
			}
		}
	}

	public static AtomicBoolean getTaskFinishFlag() {
		return taskFinishFlag;
	}

	public static void setTaskFinish() {
		taskFinishFlag.set(true);
	}
}
