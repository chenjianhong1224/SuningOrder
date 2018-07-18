package com.cjh.suning.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.cjh.suning.bean.ReturnResultBean;
import com.cjh.suning.service.SuningService;
import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

	@Autowired
	private SuningService service;

	@Test
	public void testLoginService() {
		CookieStore cookieStore = null;
		try {
			cookieStore = readCookieStore("cookie");
		} catch (ClassNotFoundException e1) {

		} catch (IOException e1) {
			System.out.println("没找到本地cookie");
		}
		if (cookieStore == null) {
			cookieStore = new BasicCookieStore();
			ReturnResultBean bean = service.login("15677982400", "hb710chw");
			if (bean.getResultCode() == 0) {
				List<BasicClientCookie> cookies = (List<BasicClientCookie>) bean.getReturnObj();
				// 创建一个HttpContext对象，用来保存Cookie
				for (BasicClientCookie cookie : cookies) {
					cookieStore.addCookie(cookie);
				}
			} else {
				System.out.println(bean.getReturnMsg());
				return;
			}
			try {
				saveCookieStore(cookieStore, "cookie");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		String url = "http://shopping.suning.com/addCart.do";
		URI uri = null;
		try {
			List<NameValuePair> addToCar = Lists.newArrayList();
			addToCar.add(new BasicNameValuePair("cartVO",
					"{\"provinceCode\":\"210\",\"cityCode\":\"771\",\"districtCode\":\"77102\",\"cmmdtyVOList\":[{\"cmmdtyCode\":\"000000010525048635\",\"shopCode\":\"0070063411\",\"activityType\":\"01\",\"cmmdtyQty\":\"1\",\"activityId\":\"\"}],\"verifyCode\":\"\",\"uuid\":\"\",\"sceneId\":\"\"}"));
			uri = new URIBuilder(url).addParameters(addToCar).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		HttpClientContext httpClientContext = HttpClientContext.create();
		httpClientContext.setCookieStore(cookieStore);
		// 构造自定义Header信息
		List<Header> headerList = Lists.newArrayList();
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
		headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"));
		headerList.add(new BasicHeader(HttpHeaders.HOST, "shopping.suning.com"));
		headerList.add(new BasicHeader(HttpHeaders.REFERER, "https://product.suning.com/0070063411/10525048635.html"));
		headerList.add(new BasicHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"));
		// 构造自定义的HttpClient对象
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList)
				.setDefaultRequestConfig(requestConfig).build();

		// 构造请求对象
		HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
		// 执行请求，传入HttpContext，将会得到请求结果的信息
		try {
			HttpResponse response = httpClient.execute(httpUriRequest, httpClientContext);
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(JSON.toJSONString(httpClientContext.getCookieStore().getCookies()));
			saveCookieStore(httpClientContext.getCookieStore(), "cookie");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
