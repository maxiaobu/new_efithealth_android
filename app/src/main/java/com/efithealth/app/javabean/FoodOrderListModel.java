package com.efithealth.app.javabean;

import java.util.List;

public class FoodOrderListModel {

	private String msgFlag;
	private String msgContent;
	private List<ForderList> forderList;
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
	public List<ForderList> getForderList() {
		return forderList;
	}
	public void setForderLists(List<ForderList> forderList) {
		this.forderList = forderList;
	}
}
