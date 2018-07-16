package com.cjh.suning.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsTest {

	@Test
	public void testWebDriver() {

	}

	@Test
	public void testPwdEncrypt() {

	}

	@Test
	public void testPhantomjs() {
		Runtime rt = Runtime.getRuntime();
		Process p;
		try {
			String userName = "18577787720";
			String password = "hb710chw";
			p = rt.exec("I:\\Program Files (x86)\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe "
					+ "E:\\code\\eclipse-workspace\\SuningOrder\\src\\main\\resources\\SuningJs\\myLogin.js " + userName
					+ " " + password);
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tmp = "";
			int line = 1;
			while ((tmp = br.readLine()) != null) {
				// if (line == 1) {
				// if (tmp != "success") {
				// System.out.println("登录失败");
				// break;
				// }
				// continue;
				// }
				line++;
				System.out.println(tmp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
