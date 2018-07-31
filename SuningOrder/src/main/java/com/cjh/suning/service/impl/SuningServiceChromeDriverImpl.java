package com.cjh.suning.service.impl;

import java.io.File;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.cjh.suning.bean.ReturnResultBean;
import com.cjh.suning.config.ApplicationConfig;
import com.cjh.suning.service.SuningService;
import com.cjh.suning.utils.WebDriverJsHelper;
import com.google.common.collect.Maps;

@Scope("prototype")
@Service("SuningServiceChromeDriver")
public class SuningServiceChromeDriverImpl extends WebDriverJsHelper implements SuningService {

	private Logger log = LoggerFactory.getLogger(SuningServiceChromeDriverImpl.class);

	private WebDriver driver = null;

	private boolean isInit = false;

	private String waitTime = "5";

	@Autowired
	private ApplicationConfig applicationConfig;

	@PostConstruct
	void init() {
		log.info("ChromeDriver 初始化开始... " + this);
		try {
			String driverPath = "";
			try {
				if (applicationConfig.getSystem().equals("1")) {
					File file = ResourceUtils.getFile("classpath:chromedriver.exe");
					if (file.exists()) {
						driverPath = file.getPath();
					}
				} else {
					File file = ResourceUtils.getFile("classpath:chromedriver");
					if (file.exists()) {
						driverPath = file.getPath();
					}
				}
			} catch (Exception e3) {
				if (applicationConfig.getSystem().equals("1")) {
					driverPath = "chromedriver.exe";
				}else {
					driverPath = "chromedriver";
				}
			}
			System.setProperty("webdriver.chrome.driver", driverPath); // 指定驱动路径
			ChromeOptions chromeOptions = new ChromeOptions();
			if (applicationConfig.getHeadless().equals("1")) { // 无头模式
				chromeOptions.addArguments("--headless");
			}
			chromeOptions.addArguments("--no-sandbox");
			if (applicationConfig.getNoImages().equals("1")) { // 禁止加载图片
				Map<String, Object> contentSettings = Maps.newHashMap();
				contentSettings.put("images", 2);
				Map<String, Object> preferences = Maps.newHashMap();
				preferences.put("profile.default_content_setting_values", contentSettings);
				chromeOptions.setExperimentalOption("prefs", preferences);
			}
			driver = new ChromeDriver(chromeOptions);
			isInit = true;
			log.info("ChromeDriver 初始化结束...");
		} catch (Exception e) {
			log.info("ChromeDriver 初始化异常!!!", e);
			isInit = false;
		}
	}

	@PreDestroy
	public void destroy() {
		log.info("ChromeDriver 终止...");
		if (isInit && driver != null) {
			driver.quit();
		}
	}

	@Override
	public ReturnResultBean login(String userName, String password) {
		ReturnResultBean returnResult = new ReturnResultBean();
		returnResult.setResultCode(-1);
		returnResult.setReturnMsg("ChromeDriver 初始化失败, 不能登录");
		if (isInit && driver != null) {
			WebDriverWait wait = null;
			WebElement webElement = null;
			try {
				driver.get("https://passport.suning.com/ids/login");
				wait = new WebDriverWait(driver, Long.parseLong(waitTime));
				JavascriptExecutor j = (JavascriptExecutor) driver;
				j.executeScript("document.getElementsByClassName('pc-login')[0].style.display='block';");
				driver.findElement(By.id("userName")).sendKeys(new String[] { applicationConfig.getUserName() });
				driver.findElement(By.id("password")).sendKeys(new String[] { applicationConfig.getPassword() });
				driver.findElement(By.id("submit")).click();
				wait.until(ExpectedConditions.titleContains("苏宁易购(Suning.com)-送货更准时、价格更超值、上新货更快"));
				wait.until(isPageLoaded());
				returnResult.setResultCode(0);
			} catch (org.openqa.selenium.TimeoutException e) {
				try {
					webElement = driver.findElement(By.cssSelector(".login-error"));
					if (webElement != null && webElement.getCssValue("display").equals("block")) {
						webElement = webElement.findElement(By.cssSelector("span"));
						if (webElement != null) {
							returnResult.setReturnMsg("登录失败: " + webElement.getText());
						}
					}
					returnResult.setReturnMsg("用户名密码方式输入登录失败了");
				} catch (org.openqa.selenium.NoSuchElementException e1) {
					returnResult.setReturnMsg("好像登录失败了");
				}
			}
		}
		return returnResult;
	}

	@Override
	public ReturnResultBean orderPhone(String skuUrl, String skuColor, String skuVersion, String skuPhonel,
			String skuBuyNum, String checkPayAmount) {
		long startTime = System.currentTimeMillis();
		long endTime = startTime;
		ReturnResultBean returnResult = new ReturnResultBean();
		returnResult.setResultCode(-1);
		returnResult.setReturnMsg("下单失败");
		WebDriverWait wait = null;
		WebElement webElement = null;
		if (isInit && driver != null) {
			wait = new WebDriverWait(driver, Long.parseLong(waitTime));
			driver.get(skuUrl);
			wait.until(isPageLoaded());
			try {
				webElement = driver.findElement(By.cssSelector(".clr-item[title='" + skuColor + "']"));
				if (webElement != null) {
					if (webElement.getAttribute("class").contains("disabled")) {
						returnResult.setReturnMsg("\"" + skuColor + "\", 现在还不能购买");
						return returnResult;
					}
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				} else {
					returnResult.setReturnMsg("没发现\"" + skuColor + "\", 现在还不能购买");
					return returnResult;
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"" + skuColor + "\", 现在还不能购买");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
			wait.until(isPageLoaded());
			try {
				webElement = driver
						.findElement(By.cssSelector("#versionItemList>dd>ul>li[title='" + skuVersion + "']"));
				if (webElement != null) {
					if (webElement.getAttribute("class").contains("disabled")) {
						returnResult.setReturnMsg("没发现\"" + skuVersion + "\", 现在还不能购买");
						return returnResult;
					}
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				} else {
					returnResult.setReturnMsg("没发现\"" + skuVersion + "\", 现在还不能购买");
					return returnResult;
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"" + skuVersion + "\", 现在还不能购买");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
			wait.until(isPageLoaded());
			try {
				webElement = driver.findElement(By.cssSelector("#phonedl"));
				if (webElement == null) {
					returnResult.setReturnMsg("没发现\"" + skuPhonel + "\", 现在还不能购买");
					return returnResult;
				} else {
					if (webElement.getCssValue("display").equals("none")) {
						returnResult.setReturnMsg("没发现\"" + skuPhonel + "\", 现在还不能购买");
						return returnResult;
					}
				}
				webElement = webElement.findElement(By.cssSelector("dd>ul>li[title='" + skuPhonel + "']"));
				if (webElement == null) {
					returnResult.setReturnMsg("没发现\"" + skuPhonel + "\", 现在还不能购买");
					return returnResult;
				} else {
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"" + skuPhonel + "\", 现在还不能购买");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
			returnResult = orderOperation(startTime, wait, skuBuyNum, checkPayAmount, returnResult);
		}
		return returnResult;
	}

	@Override
	public void refresh(String url) {
		driver.get(url);
	}

	@Override
	public ReturnResultBean orderMaotai(String skuUrl, String skuSerial, String skuSpec, String skuBuyNum,
			String checkPayAmount) {
		long startTime = System.currentTimeMillis();
		long endTime = startTime;
		ReturnResultBean returnResult = new ReturnResultBean();
		returnResult.setResultCode(-1);
		returnResult.setReturnMsg("下单失败");
		WebDriverWait wait = null;
		WebElement webElement = null;
		if (isInit && driver != null) {
			wait = new WebDriverWait(driver, Long.parseLong(waitTime));
			driver.get(skuUrl);
			wait.until(isPageLoaded());
			try {
				webElement = driver.findElement(By.cssSelector(".clr-item[title='" + skuSerial + "']"));
				if (webElement != null) {
					if (webElement.getAttribute("class").contains("disabled")) {
						returnResult.setReturnMsg("\"" + skuSerial + "\", 现在还不能购买");
						return returnResult;
					}
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				} else {
					returnResult.setReturnMsg("没发现\"" + skuSerial + "\", 现在还不能购买");
					return returnResult;
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"" + skuSerial + "\", 现在还不能购买");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
			wait.until(isPageLoaded());
			try {
				webElement = driver.findElement(By.cssSelector("#versionItemList>dd>ul>li[title='" + skuSpec + "']"));
				if (webElement != null) {
					if (webElement.getAttribute("class").contains("disabled")) {
						returnResult.setReturnMsg("没发现\"" + skuSpec + "\", 现在还不能购买");
						return returnResult;
					}
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				} else {
					returnResult.setReturnMsg("没发现\"" + skuSpec + "\", 现在还不能购买");
					return returnResult;
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"" + skuSpec + "\", 现在还不能购买");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
			wait.until(isPageLoaded());
			returnResult = orderOperation(startTime, wait, skuBuyNum, checkPayAmount, returnResult);
		}
		return returnResult;
	}

	@Override
	public void setWaitTime(String time) {
		this.waitTime = time;
	}

	private ReturnResultBean orderOperation(long startTime, WebDriverWait wait, String skuBuyNum, String checkPayAmount,
			ReturnResultBean returnResult) {
		wait.until(isPageLoaded());
		WebElement webElement = null;
		try {
			webElement = driver.findElement(By.cssSelector("#buyNum"));
			if (webElement == null) {
				returnResult.setReturnMsg("没发现\"数量\"选项, 现在还不能购买");
				return returnResult;
			} else {
				String max = webElement.getAttribute("max");
				skuBuyNum = (Integer.valueOf(max) > Integer.valueOf(skuBuyNum)) ? skuBuyNum : max;
				webElement.sendKeys(Keys.CONTROL + "a"); // 清空
				webElement.sendKeys(new String[] { skuBuyNum });
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			returnResult.setReturnMsg("没发现\"数量\"选项, 现在还不能购买");
			return returnResult;
		} catch (org.openqa.selenium.WebDriverException e) {
			returnResult.setReturnMsg("加载出现问题, 将重试");
			return returnResult;
		}
		wait.until(isPageLoaded());
		try {
			webElement = driver.findElement(By.cssSelector("#buyNowAddCart"));
			if (webElement == null) {
				returnResult.setReturnMsg("没发现\"立即购买\"按钮, 现在还买不不能购买");
				return returnResult;
			} else {
				if (webElement.getCssValue("display").equals("none")) {
					returnResult.setReturnMsg("没发现\"立即购买\"按钮, 现在还买不不能购买");
					return returnResult;
				}
				wait.until(ExpectedConditions.visibilityOf(webElement));
				webElement.click();
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			returnResult.setReturnMsg("没发现\"立即购买\"按钮, 现在还买不不能购买");
			return returnResult;
		} catch (org.openqa.selenium.WebDriverException e) {
			returnResult.setReturnMsg("加载出现问题, 将重试");
			return returnResult;
		}
		try {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector("#submit-btn[name='new_icart2_account_submit']")));
		} catch (org.openqa.selenium.TimeoutException e) {
			if (driver.getCurrentUrl().startsWith("https://shopping.suning.com/trafficLimitError.do")) {
				returnResult.setResultCode(-2);
				returnResult.setReturnMsg("凉凉，被苏宁盯上了, 说操作太过频繁， 预计300秒后可以正常结算");
				return returnResult;
			}
			returnResult.setReturnMsg("没加载\"提交订单\"按钮, 无法购买");
			return returnResult;
		} catch (org.openqa.selenium.WebDriverException e) {
			returnResult.setReturnMsg("加载出现问题, 将重试");
			return returnResult;
		}
		WebElement submitWebElement = driver
				.findElement(By.cssSelector("#submit-btn[name='new_icart2_account_submit']"));
		try {
			wait.until(ExpectedConditions.visibilityOf(submitWebElement));
		} catch (org.openqa.selenium.TimeoutException e) {
			returnResult.setReturnMsg("\"提交订单\"按钮不可见, 无法购买");
			return returnResult;
		} catch (org.openqa.selenium.WebDriverException e) {
			returnResult.setReturnMsg("加载出现问题, 将重试");
			return returnResult;
		}
		if (!checkPayAmount.equals("-1")) {
			try {
				webElement = driver.findElement(By.cssSelector("#payAmountID"));
				if (webElement == null) {
					returnResult.setReturnMsg("没发现\"应付金额\", 无法校验");
					return returnResult;
				} else {
					String amount = webElement.getText().trim();
					if (Double.valueOf(amount) > Double.valueOf(checkPayAmount)) {
						returnResult.setReturnMsg("应付金额 = " + amount + "大于设定金额" + checkPayAmount + ", 暂不购买");
						return returnResult;
					}
				}
			} catch (org.openqa.selenium.NoSuchElementException e) {
				returnResult.setReturnMsg("没发现\"应付金额\", 无法校验");
				return returnResult;
			} catch (org.openqa.selenium.WebDriverException e) {
				returnResult.setReturnMsg("加载出现问题, 将重试");
				return returnResult;
			}
		}
		submitWebElement.click();
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		log.info("提交订单花费时间：" + excTime + "s");
		int retryCount = 0;
		do {
			try {
				wait.until(ExpectedConditions.titleContains("支付收银台"));
				wait.until(isPageLoaded());
			} catch (org.openqa.selenium.TimeoutException e) {
				if (driver.getCurrentUrl().startsWith("https://shopping.suning.com/order.do") && retryCount == 0) {
					submitWebElement.click();
					retryCount = 1;
					continue;
				}
				if (!driver.getCurrentUrl().startsWith("https://payment.suning.com")) {
					try {
						webElement = driver.findElement(By.cssSelector(".container"));
						if (webElement != null && webElement.getCssValue("display").equals("block")) {
							webElement = webElement.findElement(By.cssSelector(".content>div>p"));
							if (webElement != null) {
								returnResult.setReturnMsg("凉凉: " + webElement.getText());
								return returnResult;
							}
						}
					} catch (org.openqa.selenium.NoSuchElementException e1) {
						returnResult.setReturnMsg("好像没下单成功");
						return returnResult;
					}
					returnResult.setReturnMsg("好像没下单成功");
					return returnResult;
				}
			}
		} while (retryCount == 1);
		returnResult.setResultCode(0);
		returnResult.setReturnMsg("好像下单成功了");
		return returnResult;
	}

}
