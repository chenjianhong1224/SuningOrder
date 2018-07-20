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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChromeDriverTest {

	@Test
	public void testLogin() {
		long startTime = System.currentTimeMillis();
		WebDriver driver = null;
		try {
			File file = ResourceUtils.getFile("classpath:chromedriver.exe");
			String driverPath = "";
			if (file.exists()) {
				driverPath = file.getPath();
			}
			System.setProperty("webdriver.chrome.driver", driverPath); // 指定驱动路径
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

			driver = new ChromeDriver(chromeOptions);
			driver.get("https://order.suning.com/onlineOrder/orderList.do");
			// wait.until(ExpectedConditions.titleContains("用户登录"));
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(isPageLoaded());
			JavascriptExecutor j = (JavascriptExecutor) driver;
			j.executeScript("document.getElementsByClassName('pc-login')[0].style.display='block';");
			driver.findElement(By.id("userName")).sendKeys(new String[] { "1857771287720" });
			driver.findElement(By.id("password")).sendKeys(new String[] { "hb2231710cchw" });
			driver.findElement(By.id("submit")).click();
			wait.until(isPageLoaded());
			System.out.println(driver.getCurrentUrl());
			do {
				driver.get("https://product.suning.com/0000000000/690105206.html");
				wait.until(isPageLoaded());

				WebElement webElement = driver.findElement(By.cssSelector(".clr-item[title='红色']"));
				if (webElement != null) {
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				}
				wait.until(isPageLoaded());
				webElement = driver.findElement(By.cssSelector("#versionItemList>dd>ul>li[title='64G公开版']"));
				if (webElement != null) {
					if (webElement.getAttribute("class").contains("disabled")) {
						System.out.println("现在还买不了64G公开版");
						break;
					}
					if (!webElement.getAttribute("class").contains("selected")) {
						webElement.findElement(By.cssSelector("a")).click();
					}
				}
				webElement = driver.findElement(By.cssSelector("#phoned1"));
				if (webElement == null) {
					System.out.println("现在还买不不能购买");
					break;
				}else{
					if (webElement.getCssValue("display").equals("none")) {
						System.out.println("现在还买不不能购买");
						break;
					}
				}
				webElement = webElement.findElement(By.cssSelector("dd>ul>li[title='裸机版']"));
				if (!webElement.getAttribute("class").contains("selected")) {
					webElement.findElement(By.cssSelector("a")).click();
				}
			} while (false);
			long endTime = System.currentTimeMillis();
			float excTime = (float) (endTime - startTime) / 1000;
			System.out.println("执行时间：" + excTime + "s");
			Scanner input = new Scanner(System.in);
			String val = null;
			do {
				val = input.next();
			} while (!val.equals("q"));
			System.out.println("你输入了\"q\"，程序已经退出！");
			input.close();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			if (driver != null) {
				driver.quit();
			}
		}
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
