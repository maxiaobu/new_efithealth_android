package com.efithealth.app.entity;

/**
 * 分类标题
 * @author star
 *
 */
public class Course {
	private String etid;// 标题id
	private String etname;// 标题名

	/**
	 * 标题id
	 * @return
	 */
	public String getEtid() {
		return etid;
	}

	/**
	 * 标题id
	 * @param etid
	 */
	public void setEtid(String etid) {
		this.etid = etid;
	}

	/**
	 * 标题名
	 * @return
	 */
	public String getEtname() {
		return etname;
	}

	/**
	 * 标题名
	 * @param etname
	 */
	public void setEtname(String etname) {
		this.etname = etname;
	}
}
