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
	
	@Value("${web.sku:1}")
	private String sku;

	@Value("${phone.url:}")
	private String phoneUrl;

	@Value("${phone.color:}")
	private String phoneColor;

	@Value("${phone.version:}")
	private String phoneVersion;

	@Value("${phone.phonedl:裸机版}")
	private String phonePhonedl;

	@Value("${phone.buyNum:1}")
	private String phoneBuyNum;

	@Value("${phone.checkPayAmount:-1}")
	private String phoneCheckPayAmount;

	@Value("${task.closeDefaultTask:1}")
	private String closeDefaultTask;
	
	@Value("${maotai.url:}")
	private String maotaiUrl;

	public String getMaotaiUrl() {
		return maotaiUrl;
	}

	public void setMaotaiUrl(String maotaiUrl) {
		this.maotaiUrl = maotaiUrl;
	}

	@Value("${maotai.serial:}")
	private String maotaiSerial;
	
	@Value("${maotai.spec:}")
	private String maotaiSpec;
	
	@Value("${maotai.buyNum:}")
	private String maotaiBuyNum;

	@Value("${maotai.checkPayAmount:}")
	private String maotaiCheckPayAmount;

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

	public String getPhoneUrl() {
		return phoneUrl;
	}

	public void setPhoneUrl(String phoneUrl) {
		this.phoneUrl = phoneUrl;
	}

	public String getPhoneColor() {
		return phoneColor;
	}

	public void setPhoneColor(String phoneColor) {
		this.phoneColor = phoneColor;
	}

	public String getPhoneVersion() {
		return phoneVersion;
	}

	public void setPhoneVersion(String phoneVersion) {
		this.phoneVersion = phoneVersion;
	}

	public String getPhonePhonedl() {
		return phonePhonedl;
	}

	public void setPhonePhonedl(String phonePhonedl) {
		this.phonePhonedl = phonePhonedl;
	}

	public String getPhoneBuyNum() {
		return phoneBuyNum;
	}

	public void setPhoneBuyNum(String phoneBuyNum) {
		this.phoneBuyNum = phoneBuyNum;
	}

	public String getPhoneCheckPayAmount() {
		return phoneCheckPayAmount;
	}

	public void setPhoneCheckPayAmount(String phoneCheckPayAmount) {
		this.phoneCheckPayAmount = phoneCheckPayAmount;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMaotaiSerial() {
		return maotaiSerial;
	}

	public void setMaotaiSerial(String maotaiSerial) {
		this.maotaiSerial = maotaiSerial;
	}

	public String getMaotaiSpec() {
		return maotaiSpec;
	}

	public void setMaotaiSpec(String maotaiSpec) {
		this.maotaiSpec = maotaiSpec;
	}

	public String getMaotaiBuyNum() {
		return maotaiBuyNum;
	}

	public void setMaotaiBuyNum(String maotaiBuyNum) {
		this.maotaiBuyNum = maotaiBuyNum;
	}

	public String getMaotaiCheckPayAmount() {
		return maotaiCheckPayAmount;
	}

	public void setMaotaiCheckPayAmount(String maotaiCheckPayAmount) {
		this.maotaiCheckPayAmount = maotaiCheckPayAmount;
	}
}
