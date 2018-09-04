package com.cjh.suning.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjh.suning.test.bean.MaotaiOrderBase;
import com.cjh.suning.test.bean.MaotaiOrderBean;
import com.cjh.suning.test.bean.MaotaiOrderItem;
import com.cjh.suning.test.bean.MaotaiOrderSend;
import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
public class MaotaiTest {

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0";

	@Test
	public void jsTest() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			// 获取一个指定的名称的脚本引擎
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			Compilable compilable = (Compilable) engine;
			Document doc = Jsoup.parse(new URL("https://www.emaotai.cn/smartsales-b2c-web-pc/login"), 3000);
			Elements eles = doc.getElementsByTag("script");
			for (Element ele : eles) {
				Attributes attributes = ele.attributes();
				if (attributes.size() > 0) {
					String src = attributes.get("src");
					String type = attributes.get("type");
					if (!StringUtils.isEmpty(src)) {
						System.out.println("src=" + src);
						try {
							URL url = new URL(src);
							compilable.compile(new jdk.nashorn.api.scripting.URLReader(url));
						} catch (ScriptException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return;
						}
					} else if (!StringUtils.isEmpty(type)) {
						String script = ele.childNode(0).toString();
						System.out.println("script=" + script);
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		// try {
		// // compilable.compile(new
		// //
		// FileReader(ResourceUtils.getFile("classpath:EmaotaiJs/common.js")));
		// // compilable.compile(new
		// //
		// FileReader(ResourceUtils.getFile("classpath:EmaotaiJs/login.js")));
		// engine.eval(new
		// FileReader(ResourceUtils.getFile("classpath:EmaotaiJs/common.js")));
		// } catch (FileNotFoundException | ScriptException e) {
		// e.printStackTrace();
		// return;
		// }
		// if (engine instanceof Invocable) {
		// Object obj = engine.get("e");
		// Invocable invocable = (Invocable) engine;
		// try {
		// invocable.invokeMethod(obj, "init");
		// } catch (NoSuchMethodException | ScriptException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	@Test
	public void timeTest() {
		long time = 1533429596360L;
		Date dateTime = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(dateTime));
	}

	@Test
	public void loginTest() {
		List<Header> headerList = Lists.newArrayList();
		headerList.add(
				new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE,
				"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2"));
		headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
		headerList.add(new BasicHeader(HttpHeaders.HOST, "www.emaotai.cn"));
		headerList.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
		headerList.add(new BasicHeader(HttpHeaders.USER_AGENT, userAgent));
		// 构造自定义的HttpClient对象
		CookieStore cookieStore = new BasicCookieStore();
		HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).setDefaultCookieStore(cookieStore)
				.build();
		String url = "https://www.emaotai.cn/smartsales-b2c-web-pc/login";
		URI uri = null;
		try {
			uri = new URIBuilder(url).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
		// 执行请求，传入HttpContext，将会得到请求结果的信息
		try {
			HttpClientContext httpClientContext = HttpClientContext.create();
			HttpResponse response = httpClient.execute(httpUriRequest, httpClientContext);
			if (response.getStatusLine().getStatusCode() == 200) {
				List<Cookie> cookies = cookieStore.getCookies();
				for (Cookie cookie : cookies) {
					System.out.println(String.format("domain={%s}, path={%s}, name={%s}, value={%s}",
							cookie.getDomain(), cookie.getPath(), cookie.getName(), cookie.getValue()));
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void orderTest() {
		List<MaotaiOrderBean> maotaiOrderBeans = Lists.newArrayList();
		MaotaiOrderBean maotaiOrderBean = new MaotaiOrderBean();
		MaotaiOrderBase maotaiOrderBase = new MaotaiOrderBase();
		maotaiOrderBase.setRemark("");
		maotaiOrderBase.setShopId("1173773178264259584");
		maotaiOrderBean.setMaotaiOrderBase(maotaiOrderBase);
		MaotaiOrderSend maotaiOrderSend = new MaotaiOrderSend();
		maotaiOrderSend.setDeliveryId("4");
		maotaiOrderSend.setAddressId("1182137339488753664");
		maotaiOrderBean.setMaotaiOrderSend(maotaiOrderSend);
		MaotaiOrderItem maotaiOrderItem = new MaotaiOrderItem();
		maotaiOrderItem.setIsPartialShipment("0");
		maotaiOrderItem.setItemId("1180731799924468740");
		maotaiOrderItem.setItemNum("1");
		maotaiOrderItem.setItemPrice("1499.00");
		maotaiOrderItem.setShopId("1173773178264259584");
		maotaiOrderItem.setSkuId("1180731799931808771");
		List<MaotaiOrderItem> maotaiOrderItems = Lists.newArrayList();
		maotaiOrderItems.add(maotaiOrderItem);
		maotaiOrderBean.setMaotaiOrderItems(maotaiOrderItems);
		maotaiOrderBeans.add(maotaiOrderBean);
		maotaiOrderBean.setPurchaseWay(1);
		maotaiOrderBean.setVoucherId(0);
		String orderBean = JSON.toJSONString(maotaiOrderBeans);
		System.out.println(orderBean);
		while (true) {

			Date now = new Date();
			String begin = "2018-08-12 14:59:55";
			String end = "2018-08-12 15:10:00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if (now.getTime() > sdf.parse(end).getTime()) {
					return;
				}
				Thread.sleep(1000);
				if (now.getTime() < sdf.parse(begin).getTime()) {
					Thread.sleep(1);
					continue;
				}
			} catch (ParseException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 构造自定义Header信息
			List<Header> headerList = Lists.newArrayList();
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01"));
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"));
			headerList.add(new BasicHeader("appId", "1"));
			String auth = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkNDg3ZjY3YS1jYTMwLTRhMGYtOGJlZi0xYjBkYWIxMWExNTUiLCJzdWIiOiJrZXkmX18mWElBTkdMT05HJl9fJjI5Nzg2OTQmX18mY2h3X2sxJl9fJjAmX18mYjJjbWVtYmVyJl9fJnd4Jl9fJjEmX18mMCJ9.tuXBBVjGyQjSE2NJ7a_QdzIMyAkS8XEIzYeBNn1KHEcgVaXixhck-yAvbtAzEjlretRcR3e9OvtuyuCey1LIsQ";
			headerList.add(new BasicHeader("auth", auth));
			headerList.add(new BasicHeader("channelCode", "01"));
			headerList.add(new BasicHeader("channelId", "01"));
			headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
			headerList.add(new BasicHeader("Flag", "1"));
			headerList.add(new BasicHeader(HttpHeaders.HOST, "i.emaotai.cn"));
			headerList.add(new BasicHeader(HttpHeaders.REFERER, "https://www.emaotai.cn/smartsales-b2c-web-pc/deal"));
			String encodeStr = DigestUtils.md5Hex(auth + now.getTime() + "");
			System.out.println(encodeStr);
			headerList.add(new BasicHeader("Sign", encodeStr));
			headerList.add(new BasicHeader("tenantId", "1"));
			headerList.add(new BasicHeader("terminalType", "a1"));
			headerList.add(new BasicHeader("Timestamp", now.getTime() + ""));
			headerList.add(new BasicHeader(HttpHeaders.USER_AGENT,
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:61.0) Gecko/20100101 Firefox/61.0"));
			// 构造自定义的HttpClient对象
			HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();
			String url = "https://i.emaotai.cn/yundt-application-trade-core/api/v1/yundt/trade/order/submit?appCode=1&_t="
					+ now.getTime();
			URI uri = null;
			try {
				uri = new URIBuilder(url).build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			List<NameValuePair> params = Lists.newArrayList();
			params.add(new BasicNameValuePair("orders", orderBean));
			params.add(new BasicNameValuePair("consumePoint", "0"));
			HttpUriRequest httpUriRequest2;
			try {
				httpUriRequest2 = RequestBuilder.post().setEntity(new UrlEncodedFormEntity(params, "UTF-8")).setUri(uri)
						.build();
				HttpClientContext httpClientContext = HttpClientContext.create();
				HttpResponse response = httpClient.execute(httpUriRequest2, httpClientContext);
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String content = EntityUtils.toString(entity);
						JSONObject jsonObject = JSON.parseObject(content);
						System.out.println(content);
						if ((jsonObject.getInteger("resultCode") == 0)
								&& jsonObject.getString("resultMsg").equals("success")) {
							System.out.println("======================抢到啦=====================");
							return;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
