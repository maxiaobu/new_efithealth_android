package com.efithealth.app.javabean;

import java.util.List;

public class ClassOrderListModel {
	
	private String msgFlag;
	private String msgContent;
	private List<CorderList> corderList;
	
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
	public List<CorderList> getCorderList() {
		return corderList;
	}
	public void setCorderLists(List<CorderList> corderList) {
		this.corderList = corderList;
	}
}
