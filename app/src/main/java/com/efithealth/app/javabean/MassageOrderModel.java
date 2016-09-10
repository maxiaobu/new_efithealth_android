package com.efithealth.app.javabean;

import java.util.List;


public class MassageOrderModel {

	/*
	 *   "msgFlag": "1",
    "msgContent": "",
    "massageorderList": 
        
	 */
	private String msgFlag;
	private String msgContent;
	private List<MassageOrderList> massageorderList;
	public String getMsgFlag() {
		return msgFlag;
	}
	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public List<MassageOrderList> getMassageorderList() {
		return massageorderList;
	}
	public void setMassageorderList(List<MassageOrderList> massageorderList) {
		this.massageorderList = massageorderList;
	}
	
	
}
