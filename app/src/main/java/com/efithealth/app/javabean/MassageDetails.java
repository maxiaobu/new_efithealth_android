package com.efithealth.app.javabean;

import java.util.List;

public class MassageDetails {
 /*
  * {"msgFlag":"1",
  * "msgContent":"",
  * "massage":
  * {"clubsplitamt":5.02,
  * "coachsplitamt":5.02,
  * "createtime":
  * {"date":6,
  * "day":1,
  * "hours":15,
  * "minutes":42,
  * "month":5,
  * "nanos":0,
  * "seconds":3,
  * "time":1465198923000,
  * "timezoneOffset":-480,
  * "year":116},
  * "createuser":"platAdmin",
  * "disclaimer":"阿斯顿发生的发生",
  * "evascore":0,
  * "gmsplitamt":5.02,
  * "imgfile":null,
  * "imgpfile":"/image/bmassage/M000008_1465198918204_p.png",
  * "imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmassage/M000008_1465198918204_p.png",
  * "imgsfile":"/image/bmassage/M000008_1465198918204_s.png",
  * "imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmassage/M000008_1465198918204_s.png",
  * "intro":"阿瑟发违法",
  * "massageid":"M000008",
  * "mname":"阿瑟撒",
  * "modifytime":null,
  * "modifyuser":"",
  * "platsplitamt":0,
  * "price":100.3,
  * "remark":"",
  * "salenum":0,
  * "status":"1",
  * "statusname":"有效",
  * "subtitle":"吃水不忘打井人",
  * "timeconsuming":120}}



  */
	
	private String msgFlag;
	private String msgContent;
	private MassageDetailsModel massage;
	private List<ClubList> clublist;
	
	public List<ClubList> getClublist() {
		return clublist;
	}
	public void setClublist(List<ClubList> clublist) {
		this.clublist = clublist;
	}
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
	public MassageDetailsModel getMassage() {
		return massage;
	}
	public void setMassage(MassageDetailsModel massage) {
		this.massage = massage;
	}
	
	
	
}
