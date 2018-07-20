package com.cjh.suning.test;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChromeDriverTest {

	@Test
	public void testLogin() {
		long startTime = System.currentTimeMillis();
		System.setProperty("webdriver.chrome.driver", "E:\\git\\SuningOrder\\src\\main\\resources\\chromedriver.exe"); // 指定驱动路径
		ChromeOptions chromeOptions = new ChromeOptions();
		// 无头模式
		// chromeOptions.addArguments("--headless");
		/** begin 禁止加载图片 **/
		Map<String, Object> contentSettings = Maps.newHashMap();
		contentSettings.put("images", 2);
		Map<String, Object> preferences = Maps.newHashMap();
		preferences.put("profile.default_content_setting_values", contentSettings);
		chromeOptions.setExperimentalOption("prefs", preferences);
		/** end **/

		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get("https://order.suning.com/onlineOrder/orderList.do");
		// wait.until(ExpectedConditions.titleContains("用户登录"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(isPageLoaded());
		JavascriptExecutor j = (JavascriptExecutor) driver;
		j.executeScript("document.getElementsByClassName('pc-login')[0].style.display='block';");
		driver.findElement(By.id("userName")).sendKeys(new String[] { "185777077201" });
		driver.findElement(By.id("password")).sendKeys(new String[] { "2hb7110chw3" });
		driver.findElement(By.id("submit")).click();
		wait.until(isPageLoaded());
		System.out.println(driver.getCurrentUrl());

		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		System.out.println("执行时间：" + excTime + "s");
		Scanner input = new Scanner(System.in);
		String val = null; // 记录输入的字符串
		do {
			val = input.next(); // 等待输入值
		} while (!val.equals("q")); // 如果输入的值不是#就继续输入
		System.out.println("你输入了\"q\"，程序已经退出！");
		input.close(); // 关闭
		driver.quit();
	}

	protected Function<WebDriver, Boolean> isPageLoaded() {
		return new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
	}
}
