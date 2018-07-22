package com.cjh.suning.test;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
			WebDriverWait wait = null;
			WebElement webElement = null;
			driver = new ChromeDriver(chromeOptions);
			do {
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
							break;
						}
					}
					System.out.println("用户名密码方式输入登录失败了");
					break;
				}
				driver.get(
						"https://product.suning.com/0000000000/10224351655.html#?src=item_407226560_recxjtj01n_1-8_p_0000000000_10224351654_01A_1-2_1A_A");
				wait.until(isPageLoaded());
				try {
					webElement = driver.findElement(By.cssSelector(".clr-item[title='魅焰红']"));
					if (webElement != null) {
						if (!webElement.getAttribute("class").contains("selected")) {
							webElement.findElement(By.cssSelector("a")).click();
						}
					} else {
						System.out.println("没发现\"魅焰红\", 现在还不能购买");
						break;
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"魅焰红\", 现在还不能购买");
					break;
				}
				wait.until(isPageLoaded());
				try {
					webElement = driver.findElement(By.cssSelector("#versionItemList>dd>ul>li[title='标配版']"));
					if (webElement != null) {
						if (webElement.getAttribute("class").contains("disabled")) {
							System.out.println("没发现\"标配版\", 现在还不能购买");
							break;
						}
						if (!webElement.getAttribute("class").contains("selected")) {
							webElement.findElement(By.cssSelector("a")).click();
						}
					} else {
						System.out.println("没发现\"标配版\", 现在还不能购买");
						break;
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"标配版\", 现在还不能购买");
					break;
				}
				wait.until(isPageLoaded());
				try {
					webElement = driver.findElement(By.cssSelector("#phonedl"));
					if (webElement == null) {
						System.out.println("没发现\"裸机版\", 现在还不能购买");
						break;
					} else {
						if (webElement.getCssValue("display").equals("none")) {
							System.out.println("没发现\"裸机版\", 现在还买不不能购买");
							break;
						}
					}
					webElement = webElement.findElement(By.cssSelector("dd>ul>li[title='裸机版']"));
					if (webElement == null) {
						System.out.println("没发现\"裸机版\", 现在还买不不能购买");
						break;
					} else {
						if (!webElement.getAttribute("class").contains("selected")) {
							webElement.findElement(By.cssSelector("a")).click();
						}
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"裸机版\", 现在还不能购买");
					break;
				}
				try {
					webElement = driver.findElement(By.cssSelector("#buyNum"));
					if (webElement == null) {
						System.out.println("没发现\"数量\", 现在还不能购买");
						break;
					} else {
						String max = webElement.getAttribute("max");
						webElement.sendKeys(Keys.CONTROL + "a"); // 清空
						webElement.sendKeys(new String[] { "2" });
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"数量\", 现在还不能购买");
					break;
				}
				wait.until(isPageLoaded());
				try {
					webElement = driver.findElement(By.cssSelector("#buyNowAddCart"));
					if (webElement == null) {
						System.out.println("没发现\"立即购买\", 现在还买不不能购买");
						break;
					} else {
						if (webElement.getCssValue("display").equals("none")) {
							System.out.println("没发现\"立即购买\", 现在还买不不能购买");
							break;
						}
						webElement.click();
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"立即购买\", 现在还买不不能购买");
					break;
				}
				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector("#submit-btn[name='new_icart2_account_submit']")));
				} catch (org.openqa.selenium.TimeoutException e) {
					System.out.println("没发现\"提交订单\", 无法购买");
				}
				wait.until(isPageLoaded());
				WebElement submitWebElement = driver
						.findElement(By.cssSelector("#submit-btn[name='new_icart2_account_submit']"));
				try {
					wait.until(ExpectedConditions.visibilityOf(submitWebElement));
				} catch (org.openqa.selenium.TimeoutException e) {
					System.out.println("\"提交订单\"不可见, 无法购买");
				}
				try {
					webElement = driver.findElement(By.cssSelector("#payAmountID"));
					if (webElement == null) {
						System.out.println("没发现\"应付金额\", 无法校验");
						break;
					} else {
						String amount = webElement.getText();
						System.out.println("应付金额 = " + amount);
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					System.out.println("没发现\"应付金额\", 无法校验");
					break;
				}
				submitWebElement.click();
				System.out.println("clicked");
				try {
					wait.until(ExpectedConditions.titleContains("支付收银台"));
					wait.until(isPageLoaded());
				} catch (org.openqa.selenium.TimeoutException e) {
					if (!driver.getCurrentUrl().startsWith("https://payment.suning.com")) {
						try {
							webElement = driver.findElement(By.cssSelector(".container"));
							if (webElement != null && webElement.getCssValue("display").equals("block")) {
								webElement = webElement.findElement(By.cssSelector(".content>div>p"));
								if (webElement != null) {
									System.out.println("凉凉: " + webElement.getText());
									break;
								}
							}
						} catch (org.openqa.selenium.NoSuchElementException e1) {
							e1.printStackTrace();
							System.out.println("好像没下单成功");
							break;
						}
						System.out.println("好像没下单成功");
					}
					e.printStackTrace();
					break;
				}
				System.out.println("好像下单成功了");
			} while (false);
			long endTime = System.currentTimeMillis();
			float excTime = (float) (endTime - startTime) / 1000;
			System.out.println("执行时间：" + excTime + "s");
			Scanner input = new Scanner(System.in);
			String val = null;
			do {
				val = input.next();
			} while (!val.equals("q"));
			System.out.println("你输入了\"q\", 程序已经退出！");
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
				return ((JavascriptExecutor) driver).executeScript("return window.document.readyState")
						.equals("complete");
			}
		};
	}
}
