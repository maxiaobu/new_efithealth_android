package com.efithealth.app.javabean;

public class Member {

	/*
	 * "member": {
        "applydate": null,
        "applydatestr": "",
        "applydescr": "",
        "birth": "2016/04/23",
        "
        "checkopinion": "",
        "clubid": "",
        "clubname": "",
        "coachadept": "",
        "coachcert": "0",
        "coachcertname": "无",
        "coachprice": 0,
        "concernnum": 2,
        "courseprice": 0,
        "coursetimes": 0,
        
        "createuser": "M000003",
        "curMemrole": "mem",
        "dayadd": "",
        "distance": 0,
        "distancestr": "",
        "dynamicnum": 0,
        "evascore": 0,
        "evascoretotal": 0,
        "evatimes": 0,
        "follownum": 2,
        "gender": "0",
        "gendername": "女",
        "headpage": "1",
        "headpagename": "天气预报",
        "identcode": "",
        "identity": "",
        "imagefile": [],
        "img": null,
        "imgfile": null,
        "imgfileFileName": "",
        "imgpfile": "/image/bmember/M000003_1466067256607_p.png",
        "imgpfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000003_1466067256607_p.png",
        "imgsfile": "/image/bmember/M000003_1466067256607_s.png",
        "imgsfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000003_1466067256607_s.png",
        "isclubadmin": "0",
        "isclubadminname": "否",
        "ispush": "1",
        "ispushname": "开启",
        "isstealth": "0",
        "istopfile": [],
        "istrans": "1",
        "latitude": 41.811118,
        "lessonresent": 0,
        "lessontotal": 0,
        "linkurl": "",
        "longitude": 123.433088,
        "massagetimes": 7,
        "memid": "M000003",
        "memname": "",
        "mempass": "e10adc3949ba59abbe56e057f20f883e",
        "memrole": "mem",
        "mobphone": "15311420803",
        
        "modifyuser": "M000003",
        "nickname": "阳仔",
        "nowTime": 0,
        "nowtimestr": "",
        "phonedeviceno": "864394029394170",
        "pkeyListStr": "",
        
        "recaddress": "还可以",
        "recname": "教教",
        "recphone": "15311428803",
        "remark": "",
        "resinform": "",
        "signature": "回来了",
        "sorttype": "",
        "status": "1",
        "statusname": "有效",
        "transtime": null,
        
        "ycoincashnum": 792600,
        "ycoinnum": 0
    },
    "shopcount": 0,
    "ordercount": 10,
    "lessoncount": 0,
    "ycoinnum": 792600
	 */
	
	private String imgsfilename;
	private String imgsfile;
	private String nickname;
	private String memrole;
	private String memid;
	
	
	
	public String getMemid() {
		return memid;
	}
	public void setMemid(String memid) {
		this.memid = memid;
	}
	public String getImgsfilename() {
		return imgsfilename;
	}
	public void setImgsfilename(String imgsfilename) {
		this.imgsfilename = imgsfilename;
	}
	public String getImgsfile() {
		return imgsfile;
	}
	public void setImgsfile(String imgsfile) {
		this.imgsfile = imgsfile;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMemrole() {
		return memrole;
	}
	public void setMemrole(String memrole) {
		this.memrole = memrole;
	}
	
}
