package com.cjh.suning.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChromeDriverTest {

	@Test
	public void testLogin() {
		System.setProperty("webdriver.chrome.driver", "D:\\msysgit\\git\\SuningOrder\\SuningOrder\\src\\main\\resources\\chromedriver.exe"); //指定驱动路径
		WebDriver driver = new ChromeDriver();
	}
}
