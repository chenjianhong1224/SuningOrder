package com.cjh.suning.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.cjh.suning.bean.ReturnResultBean;
import com.cjh.suning.service.SuningService;
import com.google.common.collect.Lists;

@Scope("prototype")
@Service
public class SuningServicePhantomJsImpl implements SuningService {

	@Override
	public ReturnResultBean login(String userName, String password) {
		ReturnResultBean returnBean = new ReturnResultBean();
		returnBean.setResultCode(0);
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		File file;
		String phantomjsPath = "";
		String myLoginjsPath = "";
		try {
			file = ResourceUtils.getFile("classpath:phantomjs.exe");
			if (file.exists()) {
				phantomjsPath = file.getPath();
			}
			file = ResourceUtils.getFile("classpath:SuningJs/myLogin.js");
			if (file.exists()) {
				myLoginjsPath = file.getPath();
			}
			p = rt.exec(phantomjsPath + " " + myLoginjsPath + " " + userName + " " + password);
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tmp = "";
			int line = 0;
			List<BasicClientCookie> cookies = Lists.newArrayList();
			while ((tmp = br.readLine()) != null) {
				line++;
				if (line == 1) {
					if (tmp.equals("success")) {
						continue;
					} else {
						returnBean.setResultCode(-1);
						returnBean.setReturnMsg(tmp);
						return returnBean;
					}
				}
				String[] kv = tmp.split("=");
				BasicClientCookie cookie = new BasicClientCookie(kv[0], kv[1]);
				cookie.setDomain(kv[2]);
				cookie.setPath(kv[3]);
				cookie.setAttribute(ClientCookie.DOMAIN_ATTR, kv[2]);
				cookie.setAttribute(ClientCookie.PATH_ATTR, kv[3]);
				cookies.add(cookie);
			}
			is.close();
			returnBean.setReturnObj(cookies);
		} catch (Exception e) {
			e.printStackTrace();
			returnBean.setResultCode(-1);
			returnBean.setReturnMsg(e.getMessage());
			return returnBean;
		}
		return returnBean;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnResultBean orderPhone(String skuUrl, String skuColor, String skuVersion, String skuPhonel,
			String skuBuyNum, String checkPayAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnResultBean orderMaotai(String skuUrl, String skuSerial, String skuSpec, String skuBuyNum,
			String checkPayAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaitTime(String time) {
		// TODO Auto-generated method stub

	}

}
