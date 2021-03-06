package com.cjh.suning.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest
public class I360Test {

	@Test
	public void testLogin() throws FileNotFoundException, InterruptedException {
		CookieStore cookieStore = null;
		Scanner input = new Scanner(System.in);
		try {
			cookieStore = readCookieStore("cookie");
		} catch (ClassNotFoundException e1) {

		} catch (IOException e1) {

		}
		if (cookieStore == null) {
			cookieStore = new BasicCookieStore();
			WebDriver driver = null;
			File file = ResourceUtils.getFile("classpath:chromedriver.exe");
			String driverPath = "";
			if (file.exists()) {
				driverPath = file.getPath();
			}
			System.setProperty("webdriver.chrome.driver", driverPath); // 指定驱动路径
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--no-sandbox");
			// 无头模式
			// chromeOptions.addArguments("--headless");
			/** begin 禁止加载图片 **/
			// Map<String, Object> contentSettings = Maps.newHashMap();
			// contentSettings.put("images", 2);
			// Map<String, Object> preferences = Maps.newHashMap();
			// preferences.put("profile.default_content_setting_values", contentSettings);
			// chromeOptions.setExperimentalOption("prefs", preferences);
			/** end **/
			WebDriverWait wait = null;
			driver = new ChromeDriver(chromeOptions);
			driver.get("http://i360mall.com/");
			wait = new WebDriverWait(driver, 10);
			driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/ul/li[5]/div[1]/a[1]")).click();
			wait.until(ExpectedConditions.titleContains("登录-360帐号中心"));
			wait.until(isPageLoaded());
			boolean firstSucc = false;
			try {
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[3]/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[1]/div/div/input"))
						.sendKeys(new String[] { "18577787720" });
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[3]/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[2]/div/div/input"))
						.sendKeys(new String[] { "824682k1" });
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[3]/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[5]/input"))
						.click();
				wait.until(ExpectedConditions.titleContains("360商城"));
				wait.until(isPageLoaded());
				firstSucc = true;
			} catch (Exception e) {
				System.out.println("用户名密码模式登录失败");
			}
			if (!firstSucc) {
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[1]/div/div/div/div[2]/a"))
						.click();
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[1]/div/div/input"))
						.sendKeys(new String[] { "18577787720" });
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[3]/div/div/div/span"))
						.click();
				if (driver.findElement(By.xpath(
						"//*[@id=\"doc\"]/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[2]"))
						.isDisplayed()) {
					System.out.println("请输入验证码，回车结束");
					String val = input.next();
					driver.findElement(By.cssSelector("input[name='captcha']")).sendKeys(new String[] { val });
					driver.findElement(By.xpath(
							"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[3]/div/div/div/span"))
							.click();
				}
				System.out.println("请输入短信验证码，回车结束");
				String val = input.next();
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[3]/div/div/input"))
						.sendKeys(new String[] { val });
				driver.findElement(By.xpath(
						"/html/body/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div/div/div[2]/div/div[1]/div/div[2]/form/div[5]/input"))
						.click();
				wait.until(ExpectedConditions.titleContains("360商城"));
				wait.until(isPageLoaded());
			}
			Set<Cookie> driverCookies = driver.manage().getCookies();
			List<BasicClientCookie> cookies = Lists.newArrayList();
			for (Cookie driverCookie : driverCookies) {
				BasicClientCookie cookie = new BasicClientCookie(driverCookie.getName(), driverCookie.getValue());
				cookie.setDomain(driverCookie.getDomain());
				cookie.setPath(driverCookie.getPath());
				cookie.setExpiryDate(driverCookie.getExpiry());
				cookie.setSecure(driverCookie.isSecure());
				cookie.setAttribute(ClientCookie.DOMAIN_ATTR, driverCookie.getDomain());
				cookie.setAttribute(ClientCookie.PATH_ATTR, driverCookie.getPath());
				cookies.add(cookie);
			}
			for (BasicClientCookie cookie : cookies) {
				cookieStore.addCookie(cookie);
			}
			driver.quit();
			try {
				saveCookieStore(cookieStore, "cookie");
			} catch (IOException e) {
				e.printStackTrace();
				input.close();
				return;
			}
		}
		String url = "http://i360mall.com/cart/addToCart";
		URI uri = null;
		try {
			uri = new URIBuilder(url).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			input.close();
			return;
		}
		HttpClientContext httpClientContext = HttpClientContext.create();

		BasicClientCookie cookie1 = new BasicClientCookie("_area_id_", "20-1715-43117-0"); // 20-1715-43117-0 青秀 四级
																							// 43151青秀山， 20-1715-43117-0
																							// 上林
		cookie1.setDomain(".i360mall.com");
		cookie1.setPath("/");
		cookie1.setAttribute(ClientCookie.DOMAIN_ATTR, ".i360mall.com");
		cookie1.setAttribute(ClientCookie.PATH_ATTR, "/");
		cookieStore.addCookie(cookie1);

		httpClientContext.setCookieStore(cookieStore);
		for (org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()) {
			System.out.println(cookie.getName() + "=" + cookie.getValue() + " domain=" + cookie.getDomain() + ", path="
					+ cookie.getPath());
		}
		// 构造自定义Header信息
		List<Header> headerList = Lists.newArrayList();
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
		headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
		headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		headerList.add(new BasicHeader("Origin", "http://i360mall.com"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"));
		headerList.add(new BasicHeader(HttpHeaders.HOST, "i360mall.com"));
		headerList.add(new BasicHeader(HttpHeaders.REFERER, "http://i360mall.com/shop/item?itemId=3430987"));
		headerList.add(new BasicHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"));
		// 构造自定义的HttpClient对象
		HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();

		// 构造请求对象
		List<NameValuePair> addToCar = Lists.newArrayList();
		addToCar.add(new BasicNameValuePair("itemId", "3430987"));
		addToCar.add(new BasicNameValuePair("num", "1"));
		HttpUriRequest httpUriRequest = RequestBuilder.post().addParameters(new BasicNameValuePair("itemId", "3430987"))
				.addParameters(new BasicNameValuePair("num", "1")).setUri(uri).build();
		// 执行请求，传入HttpContext，将会得到请求结果的信息
		try {
			HttpResponse response = httpClient.execute(httpUriRequest, httpClientContext);
			System.out.println(response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String content = EntityUtils.toString(entity);
				JSONObject jsonObject = JSON.parseObject(content);
				System.out.println(content);

				if (jsonObject.getBoolean("isSuccess")) {
					BasicCookieStore cookieStore2 = new BasicCookieStore();
					for (org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()) {
						if (cookie.getName().equals("_area_id_")) {
							BasicClientCookie cookie2 = new BasicClientCookie("_area_id_", "20-1715-43117-43151");
							cookie2.setDomain(cookie.getDomain());
							cookie2.setPath(cookie.getPath());
							cookie2.setAttribute(ClientCookie.DOMAIN_ATTR, ".i360mall.com");
							cookie2.setAttribute(ClientCookie.PATH_ATTR, "/");
							cookieStore2.addCookie(cookie2);
						} else {
							cookieStore2.addCookie(cookie);
						}
					}
					HttpClientContext httpClientContext2 = HttpClientContext.create();
					httpClientContext2.setCookieStore(cookieStore2);
					// 构造自定义Header信息
					List<Header> headerList2 = Lists.newArrayList();
					headerList2.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
					headerList2.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
					headerList2.add(new BasicHeader("Accept-Type", "application/json"));
					headerList2.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
					headerList2.add(new BasicHeader(HttpHeaders.HOST, "trade.i360mall.com"));
					headerList2.add(new BasicHeader("Origin", "http://trade.i360mall.com"));
					headerList2
							.add(new BasicHeader(HttpHeaders.REFERER, "http://trade.i360mall.com/order/currentOrder"));
					headerList2.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
					headerList2.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"));
					headerList2.add(new BasicHeader(HttpHeaders.USER_AGENT,
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"));
					// 构造自定义的HttpClient对象
					HttpClient httpClient2 = HttpClients.custom().setDefaultHeaders(headerList2).build();
					String url2 = "http://trade.i360mall.com/order/submitOrder";
					URI uri2 = null;
					try {
						uri2 = new URIBuilder(url2).build();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					List<? extends NameValuePair> params = Lists.newArrayList();
					HttpUriRequest httpUriRequest2 = RequestBuilder.post()
							.setEntity(new UrlEncodedFormEntity(params , "UTF-8")).setUri(uri2).build();
					HttpResponse response2 = httpClient2.execute(httpUriRequest2, httpClientContext2);
					System.out.println(response2.getStatusLine().getStatusCode());
					HttpEntity entity2 = response2.getEntity();
					if (entity2 != null) {
						String content2 = EntityUtils.toString(entity2);
						JSONObject jsonObject2 = JSON.parseObject(content2);
						System.out.println(content2);
					}
				}
			}
			// System.out.println(JSON.toJSONString(httpClientContext.getCookieStore().getCookies()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String val = "";
		do {
			val = input.next();
			Thread.sleep(10);
		} while (!val.equals("q"));
		System.out.println("你输入了\"q\", 程序正在退出，请勿关闭！");
		input.close();
		System.out.println("程序正常退出");
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

	private static void saveCookieStore(CookieStore cookieStore, String savePath) throws IOException {
		FileOutputStream fs = new FileOutputStream(savePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(cookieStore);
		os.close();
	}

	// 读取Cookie的序列化文件，读取后可以直接使用
	private static CookieStore readCookieStore(String savePath) throws IOException, ClassNotFoundException {
		FileInputStream fs = new FileInputStream(savePath);
		ObjectInputStream ois = new ObjectInputStream(fs);
		CookieStore cookieStore = (CookieStore) ois.readObject();
		ois.close();
		return cookieStore;
	}
}
