package com.efithealth.app.entity;

import java.util.List;

public class ClubList {
	
	private String msgContent;//教练俱乐部绑定列表!",
	private String msgFlag;// "1"
	
	private List<ClubList_BindList> bindList;
	
	
	
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getMsgFlag() {
		return msgFlag;
	}
	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}
	public List<ClubList_BindList> getBindList() {
		return bindList;
	}
	public void setBindList(List<ClubList_BindList> bindList) {
		this.bindList = bindList;
	}
	
	
	
	
}
