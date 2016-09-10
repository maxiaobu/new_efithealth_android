package com.efithealth.app.javabean;

import java.util.List;


public class OrderListModel {
	
	private String msgFlag;
	private String msgContent;
	private List<CorderList> corderLists;
	
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
	public List<CorderList> getCorderLists() {
		return corderLists;
	}
	public void setCorderLists(List<CorderList> corderLists) {
		this.corderLists = corderLists;
	}
}
