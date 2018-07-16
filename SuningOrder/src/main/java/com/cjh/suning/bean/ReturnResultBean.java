package com.cjh.suning.bean;

import java.io.Serializable;

public class ReturnResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5172597012141921377L;

	private int resultCode;
	
	private String returnMsg;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

}
