package com.efithealth.app.javabean;

import java.util.List;

public class HotDynamicModel {

	private String msgFlag;
	private String msgContent;
	private List<HotDynamic> hotDynamicsList;
	public String getMsgFlag() {
		return msgFlag+"";
	}
	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag+"";
	}
	public String getMsgContent() {
		return msgContent+"";
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent+"";
	}
	public List<HotDynamic> getHotDynamicsList() {
		return hotDynamicsList;
	}
	public void setHotDynamicsList(List<HotDynamic> hotDynamicsList) {
		this.hotDynamicsList = hotDynamicsList;
	}
	
}
