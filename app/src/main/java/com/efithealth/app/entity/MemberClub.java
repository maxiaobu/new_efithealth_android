package com.efithealth.app.entity;

/**
 * 判断用户关系
 * 
 * @author star
 *
 */
public class MemberClub {
	private String msgContent;// 判断用户关系
	private String msgFlag;// "1"
	private String isbind;// 1已绑定 0绑定 else不显示
	private String isfollow;// 1已关注 0关注
	private String isblacker;// 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)

	/**
	 * 判断用户关系
	 * 
	 * @return
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * 判断用户关系
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

	/**
	 * 1已绑定 0绑定 else不显示
	 * 
	 * @return
	 */
	public String getIsbind() {
		return isbind;
	}

	/**
	 * 1已绑定 0绑定 else不显示
	 * 
	 * @param isbind
	 */
	public void setIsbind(String isbind) {
		this.isbind = isbind;
	}

	/**
	 * 1已关注 0关注
	 * 
	 * @return
	 */
	public String getIsfollow() {
		return isfollow;
	}

	/**
	 * 1已关注 0关注
	 * 
	 * @param isfollow
	 */
	public void setIsfollow(String isfollow) {
		this.isfollow = isfollow;
	}

	/**
	 * 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)
	 * 
	 * @return
	 */
	public String getIsblacker() {
		return isblacker;
	}

	/**
	 * 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)
	 * 
	 * @param isblacker
	 */
	public void setIsblacker(String isblacker) {
		this.isblacker = isblacker;
	}
}
