package com.cjh.suning.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhantomjsTest {

	@Test
	public void testLogin() {
		Runtime rt = Runtime.getRuntime();
		Process p;
		long startTime = System.currentTimeMillis();
		try {
			String userName = "15677982400";
			String password = "hb710chw";
			File file;
			String phantomjsPath = "I:\\Program Files (x86)\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
			String myLoginjsPath = "E:\\code\\eclipse-workspace\\SuningOrder\\src\\main\\resources\\SuningJs\\myLogin.js";
			try {
				file = ResourceUtils.getFile("classpath:phantomjs.exe");
				if (file.exists()) {
					phantomjsPath = file.getPath();
				}
				file = ResourceUtils.getFile("classpath:SuningJs/myLogin.js");
				if (file.exists()) {
					myLoginjsPath = file.getPath();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			p = rt.exec(phantomjsPath + " " + myLoginjsPath + " " + userName + " " + password);
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tmp = "";
			int line = 1;
			while ((tmp = br.readLine()) != null) {
				line++;
				System.out.println(tmp);
			}
			long endTime = System.currentTimeMillis();
			float excTime = (float) (endTime - startTime) / 1000;
			System.out.println("执行时间：" + excTime + "s");
			//12.188s
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
