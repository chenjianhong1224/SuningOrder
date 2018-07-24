package com.cjh.suning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@PropertySource(value = { "file:config.txt" }, encoding = "utf-8")
public class ApplicationConfig {

	@Value("${task.begintime:}")
	private String taskBeginTime;

	@Value("${task.endtime:}")
	private String taskEndTime;

	@Value("${task.num:1}")
	private String taskNum;

	@Value("${driver.model:1}")
	private String model; // 1:chromeDriver

	@Value("${driver.wait:6}")
	private String waitTime;

	@Value("${driver.headless:1}")
	private String headless;

	@Value("${driver.noImages:1}")
	private String noImages;

	@Value("${web.userName:}")
	private String userName;

	@Value("${web.password:}")
	private String password;

	@Value("${sku.url:}")
	private String skuUrl;

	@Value("${sku.color:}")
	private String skuColor;

	@Value("${sku.version:}")
	private String skuVersion;

	@Value("${sku.phonedl:裸机版}")
	private String skuPhonedl;

	@Value("${sku.buyNum:1}")
	private String skuBuyNum;

	@Value("${sku.checkPayAmount:-1}")
	private String skuCheckPayAmount;

	@Value("${task.closeDefaultTask:1}")
	private String closeDefaultTask;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHeadless() {
		return headless;
	}

	public void setHeadless(String headless) {
		this.headless = headless;
	}

	public String getNoImages() {
		return noImages;
	}

	public void setNoImages(String noImages) {
		this.noImages = noImages;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSkuUrl() {
		return skuUrl;
	}

	public void setSkuUrl(String skuUrl) {
		this.skuUrl = skuUrl;
	}

	public String getSkuColor() {
		return skuColor;
	}

	public void setSkuColor(String skuColor) {
		this.skuColor = skuColor;
	}

	public String getSkuVersion() {
		return skuVersion;
	}

	public void setSkuVersion(String skuVersion) {
		this.skuVersion = skuVersion;
	}

	public String getSkuPhonedl() {
		return skuPhonedl;
	}

	public void setSkuPhonedl(String skuPhonedl) {
		this.skuPhonedl = skuPhonedl;
	}

	public String getSkuBuyNum() {
		return skuBuyNum;
	}

	public void setSkuBuyNum(String skuBuyNum) {
		this.skuBuyNum = skuBuyNum;
	}

	public String getSkuCheckPayAmount() {
		return skuCheckPayAmount;
	}

	public void setSkuCheckPayAmount(String skuCheckPayAmount) {
		this.skuCheckPayAmount = skuCheckPayAmount;
	}

	public String getTaskBeginTime() {
		return taskBeginTime;
	}

	public void setTaskBeginTime(String taskBeginTime) {
		this.taskBeginTime = taskBeginTime;
	}

	public String getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	public String getCloseDefaultTask() {
		return closeDefaultTask;
	}

	public void setCloseDefaultTask(String closeDefaultTask) {
		this.closeDefaultTask = closeDefaultTask;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
}
