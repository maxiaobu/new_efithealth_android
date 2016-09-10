package com.efithealth.app.javabean;

import java.util.List;

public class MBDynamicModel {

	private String msgFlag;
	private String msgContent;
	private List<MBDynamicList> mBDynamicList;
	
	public List<MBDynamicList> getmBDynamicList() {
		return mBDynamicList;
	}
	public void setmBDynamicList(List<MBDynamicList> mBDynamicList) {
		this.mBDynamicList = mBDynamicList;
	}
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
	
	
}
