package com.efithealth.app.entity;

import java.util.List;

public class MCourseList {

	private List<CourseList> courseList;//

	private String msgContent;// ": "课程管理列表",
	private String msgFlag;// ": "1"

	public List<CourseList> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<CourseList> courseList) {
		this.courseList = courseList;
	}

	public String getMsgContent() {
		return msgContent;
	}

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
