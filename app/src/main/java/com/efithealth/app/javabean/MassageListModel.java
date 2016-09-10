package com.efithealth.app.javabean;

import java.util.List;

public class MassageListModel {
	private String msgFlag;
	private String msgContent;
	private List<MassageList> massagelist;
	
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
	public List<MassageList> getMassageList() {
		return massagelist;
	}
	public void setMassageList(List<MassageList> massageList) {
		this.massagelist = massageList;
	}
	
}
