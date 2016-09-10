package com.efithealth.app.javabean;

public class GroupDetailsModle {
	/*
	 *  "msgFlag": "1",
    "msgContent": "",
    "group": {
        
}
	 */

	private String msgFlag;
	private String msgContent;
	private Group group;
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
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
}
