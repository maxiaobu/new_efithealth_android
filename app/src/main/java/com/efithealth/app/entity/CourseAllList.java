package com.efithealth.app.entity;

import java.util.List;

/**
 * 加载分类标题列表
 * @author star
 *
 */
public class CourseAllList {

	private String msgContent;// 教练俱乐部绑定列表!",
	private String msgFlag;// "1"

	private List<Course> tagList;

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

	public List<Course> getTagList() {
		return tagList;
	}

	public void setTagList(List<Course> tagList) {
		this.tagList = tagList;
	}
	//{"msgFlag":"1","msgContent":"获取文章分类集合","tagList":[{"createtime":{"date":8,"day":5,"hours":14,"minutes":14,"month":3,"nanos":0,"seconds":57,"time":1460096097000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000001","etname":"最新信息","modifytime":{"date":11,"day":1,"hours":10,"minutes":46,"month":3,"nanos":0,"seconds":49,"time":1460342809000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":8,"day":5,"hours":14,"minutes":16,"month":3,"nanos":0,"seconds":22,"time":1460096182000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000002","etname":"促销信息","modifytime":{"date":11,"day":1,"hours":10,"minutes":46,"month":3,"nanos":0,"seconds":15,"time":1460342775000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":8,"day":5,"hours":14,"minutes":18,"month":3,"nanos":0,"seconds":26,"time":1460096306000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000003","etname":"俱乐部信息","modifytime":null,"modifyuser":"","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":8,"day":5,"hours":14,"minutes":27,"month":3,"nanos":0,"seconds":14,"time":1460096834000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000006","etname":"每日信息","modifytime":{"date":12,"day":2,"hours":15,"minutes":21,"month":3,"nanos":0,"seconds":16,"time":1460445676000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":8,"day":5,"hours":14,"minutes":28,"month":3,"nanos":0,"seconds":3,"time":1460096883000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000007","etname":"3344","modifytime":{"date":11,"day":1,"hours":14,"minutes":2,"month":3,"nanos":0,"seconds":34,"time":1460354554000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"0","statusname":"无效"},{"createtime":{"date":8,"day":5,"hours":14,"minutes":31,"month":3,"nanos":0,"seconds":31,"time":1460097091000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000008","etname":"aaa","modifytime":{"date":11,"day":1,"hours":13,"minutes":59,"month":3,"nanos":0,"seconds":36,"time":1460354376000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"0","statusname":"无效"},{"createtime":{"date":9,"day":6,"hours":9,"minutes":10,"month":3,"nanos":0,"seconds":14,"time":1460164214000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000010","etname":"bbb","modifytime":{"date":11,"day":1,"hours":14,"minutes":1,"month":3,"nanos":0,"seconds":41,"time":1460354501000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"bbb","status":"0","statusname":"无效"},{"createtime":{"date":9,"day":6,"hours":11,"minutes":1,"month":3,"nanos":0,"seconds":55,"time":1460170915000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000011","etname":"教练信息","modifytime":null,"modifyuser":"","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":12,"day":2,"hours":17,"minutes":34,"month":3,"nanos":0,"seconds":31,"time":1460453671000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000012","etname":"美体信息","modifytime":{"date":12,"day":2,"hours":18,"minutes":38,"month":3,"nanos":0,"seconds":8,"time":1460457488000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"0","statusname":"无效"}]}
}
