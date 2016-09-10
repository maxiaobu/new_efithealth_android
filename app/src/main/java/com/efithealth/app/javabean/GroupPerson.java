package com.efithealth.app.javabean;

import java.util.List;

public class GroupPerson {

	private String msgFlag;
	private String msgContent;
	private List<Memberlist> memberlist;
	
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
	public List<Memberlist> getMemberlist() {
		return memberlist;
	}
	public void setMemberlist(List<Memberlist> memberlist) {
		this.memberlist = memberlist;
	}
	
} 
