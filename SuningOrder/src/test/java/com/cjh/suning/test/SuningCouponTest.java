package com.cjh.suning.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuningCouponTest {

	@Test
	public void getCouponTest() throws FileNotFoundException, InterruptedException {
		List<Thread> threads = Lists.newArrayList();
		for (int i = 0; i < 2; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					String buttonPath = "/html/body/div[6]/div[1]/div[1]/div[3]/a";
					String url = "https://quan.suning.com/couponcenter_1__201807240002925432_qGTgXPSr06eevUpmKjhXp7uV.htm";
					File file;
					try {
						file = ResourceUtils.getFile("classpath:chromedriver.exe");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						return;
					}
					String driverPath = "";
					if (file.exists()) {
						driverPath = file.getPath();
					}
					System.setProperty("webdriver.chrome.driver", driverPath); // 指定驱动路径
					ChromeOptions chromeOptions = new ChromeOptions();
					// 无头模式
					chromeOptions.addArguments("--headless");
					chromeOptions.addArguments("--no-sandbox");
					/** begin 禁止加载图片 **/
					Map<String, Object> contentSettings = Maps.newHashMap();
					contentSettings.put("images", 2);
					Map<String, Object> preferences = Maps.newHashMap();
					preferences.put("profile.default_content_setting_values", contentSettings);
					chromeOptions.setExperimentalOption("prefs", preferences);
					/** end **/
					WebDriverWait wait = null;
					WebElement webElement = null;
					WebDriver driver = new ChromeDriver(chromeOptions);
					try {
						driver.get("https://passport.suning.com/ids/login");
						wait = new WebDriverWait(driver, 10);
						JavascriptExecutor j = (JavascriptExecutor) driver;
						j.executeScript("document.getElementsByClassName('pc-login')[0].style.display='block';");
						driver.findElement(By.id("userName")).sendKeys(new String[] { "18577787720" });
						driver.findElement(By.id("password")).sendKeys(new String[] { "hb710chw" });
						driver.findElement(By.id("submit")).click();
						wait.until(ExpectedConditions.titleContains("苏宁易购(Suning.com)-送货更准时、价格更超值、上新货更快"));
						wait.until(isPageLoaded());
					} catch (org.openqa.selenium.TimeoutException e) {
						webElement = driver.findElement(By.cssSelector(".login-error"));
						if (webElement != null && webElement.getCssValue("display").equals("block")) {
							webElement = webElement.findElement(By.cssSelector("span"));
							if (webElement != null) {
								System.out.println("登录失败: " + webElement.getText());
								return;
							}
						}
						System.out.println("用户名密码方式输入登录失败了");
						return;
					}
					System.out.println("登陆成功");
					String begin = "2018-07-25 23:59:59";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date beginTime = null;
					try {
						beginTime = sdf.parse(begin);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					while (true) {
						Date now = new Date();
						if (now.getTime() > beginTime.getTime()) {
							break;
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							return;
						}
					}
					while (true) {
						try {
							System.out.println("抢呀");
							driver.get(url);
							wait.until(ExpectedConditions.titleContains("苏宁易购优惠券|代金券||满减券|折扣券|直减券|免息券免费领取_苏宁易购领券中心"));
							webElement = driver.findElement(By.xpath(buttonPath));
							webElement.click();
							webElement = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[1]/div[1]/div[4]"));
							if (webElement.getAttribute("class").contains("has")) {
								System.out.println("抢到了");
								break;
							}
						} catch (Exception e) {

						}
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							return;
						}
					}

				}
			});
			thread.start();
			threads.add(thread);
		}
		Scanner input = new Scanner(System.in);
		String val = null;
		do {
			val = input.next();
			Thread.sleep(10);
		} while (!val.equals("q"));
		System.out.println("你输入了\"q\", 程序正在退出，请勿关闭！");
		for (Thread thread : threads) {
			thread.interrupt();
			thread.join();
		}
	}

	protected Function<WebDriver, Boolean> isPageLoaded() {
		return new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return window.document.readyState")
						.equals("complete");
			}
		};
	}
}
