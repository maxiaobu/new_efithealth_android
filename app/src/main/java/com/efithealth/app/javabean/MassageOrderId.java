package com.efithealth.app.javabean;

public class MassageOrderId {

	/*
	 * {"msgFlag":"1","msgContent":"订单确认成功!","orderid":"MASS20160612100903910M000003"}
	 */
	private String msgFlag;
	private String msgContent;
	private String orderid;
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
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
}
