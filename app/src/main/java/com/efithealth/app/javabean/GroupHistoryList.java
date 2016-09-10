package com.efithealth.app.javabean;

import java.util.List;

public class GroupHistoryList {

	private String msgFlag;
	private String msgContent;
	private List<GroupList> groupList;
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
	public List<GroupList> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<GroupList> groupList) {
		this.groupList = groupList;
	}
	
}
