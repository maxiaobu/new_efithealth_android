package com.efithealth.app.javabean;

import java.util.List;

public class CoachListModel {

	private String msgFlag;
	private String msgContent;
	private List<CoachList> coachList;
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
	public List<CoachList> getCoachList() {
		return coachList;
	}
	public void setCoachList(List<CoachList> coachList) {
		this.coachList = coachList;
	}
	@Override
	public String toString() {
		return "CoachListModel [msgFlag=" + msgFlag + ", msgContent="
				+ msgContent + ", coachList=" + coachList + "]";
	} 
	
}
