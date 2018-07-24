package com.cjh.suning.service;

import com.cjh.suning.bean.ReturnResultBean;

public interface SuningService {

	public ReturnResultBean login(String userName, String password);

	public ReturnResultBean orderPhone(String skuUrl, String skuColor, String skuVersion, String skuPhonel,
			String skuBuyNum, String checkPayAmount);

	public ReturnResultBean orderMaotai(String skuUrl, String skuSerial, String skuSpec, String skuBuyNum,
			String checkPayAmount);

	public void destroy();

	public void refresh(String url);

	public void setWaitTime(String time);

}
