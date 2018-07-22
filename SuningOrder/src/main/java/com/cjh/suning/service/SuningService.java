package com.cjh.suning.service;

import com.cjh.suning.bean.ReturnResultBean;

public interface SuningService {

	public ReturnResultBean login(String userName, String password);

	public ReturnResultBean order(String skuUrl, String skuColor, String skuVersion, String skuPhonel, String skuBuyNum,
			String checkPayAmount);
	
	public ReturnResultBean testOrder(String skuUrl, String skuColor, String skuVersion, String skuPhonel);

	public void destroy();
	
	public void refresh(String url);

}
