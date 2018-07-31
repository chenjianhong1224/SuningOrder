package com.cjh.suning.service;

import com.cjh.suning.bean.ReturnResultBean;

public interface I360Service {

	public ReturnResultBean login(String userName, String password);

	public ReturnResultBean order(String itemId, String num);

	public void destroy();

	public void refresh(String url);

	public void setWaitTime(String time);
}
