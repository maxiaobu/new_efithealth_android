package com.efithealth.app.javabean;

import java.util.List;

public class PublicGroupModel {
	private String msgFlag;
	private String msgContent;
	private List<PublicGroup> groupList;
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
	public List<PublicGroup> getGrouplist() {
		return groupList;
	}
	public void setGrouplist(List<PublicGroup> groupList) {
		this.groupList = groupList;
	}
	

}
