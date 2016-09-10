package com.efithealth.app.entity;

/**
 * 状态标识
 * 
 * @author star
 *
 */
public class RequestFlag {
	private String msgContent;// 操作返回说明
	private String msgFlag;// "1"
	/**
	 * 操作返回说明
	 * 
	 * @return
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * 操作返回说明
	 * 
	 * @param msgContent
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}	
}
