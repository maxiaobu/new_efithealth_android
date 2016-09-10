package com.efithealth.app.javabean;

import java.util.List;

public class MassagePersonModel {
	
	private String msgFlag;
	private String msgContent;
	private List<Masseurlist> masseurlist;
	
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
	public List<Masseurlist> getMasseurlist() {
		return masseurlist;
	}
	public void setMasseurlist(List<Masseurlist> masseurlist) {
		this.masseurlist = masseurlist;
	}
	
}
