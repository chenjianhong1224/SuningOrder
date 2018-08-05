package com.cjh.suning.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjh.suning.test.bean.MaotaiOrderBase;
import com.cjh.suning.test.bean.MaotaiOrderBean;
import com.cjh.suning.test.bean.MaotaiOrderItem;
import com.cjh.suning.test.bean.MaotaiOrderSend;
import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
public class MaotaiTest {

	@Test
	public void timeTest() {
		long time = 1533429596360L;
		Date dateTime = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(dateTime));
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
		maotaiOrderBean.setPurchaseWay(0);
		maotaiOrderBean.setVoucherId(0);
		String orderBean = JSON.toJSONString(maotaiOrderBeans);
		System.out.println(orderBean);
		int count = 2;
		while (count > 0) {
			Date now = new Date();
			String begin = "2018-08-05 15:00:00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if (now.getTime() < sdf.parse(begin).getTime()) {
					Thread.sleep(1);
					continue;
				}
			} catch (ParseException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			count--;
			// 构造自定义Header信息
			List<Header> headerList = Lists.newArrayList();
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01"));
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
			headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8"));
			headerList.add(new BasicHeader("appId", "1"));
			String auth = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI4OWQ5ZGIyZi1iY2YwLTRhNTgtOWVmYi1lNjA2NGMyMzA2NWUiLCJzdWIiOiJrZXkmX18mWElBTkdMT05HJl9fJjI5Nzg2OTQmX18mY2h3X2sxJl9fJjAmX18mYjJjbWVtYmVyJl9fJnd4Jl9fJjEmX18mMCJ9.VQOn0TaqwR8mqEZZRgmQlh2YbaeOb181dhmpR7xP-XiB4wuTzstD4SYOdnJLAYwX21BPPwouJ0INGQKMY4j8Sw";
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
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String content = EntityUtils.toString(entity);
					JSONObject jsonObject = JSON.parseObject(content);
					System.out.println(content);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
