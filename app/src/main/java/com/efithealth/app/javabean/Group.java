package com.efithealth.app.javabean;

public class Group {
/*
 * "address": "",
        "bulletin": "阿瑟发色",
        "checkstatus": "1",
        "checkstatusname": "审核通过",
        "clubid": "C000004",
        "clubname": "大伟健身俱乐部",
        "clubperson": "大伟健身俱乐部管理员",
        "clubphone": "11122223333",
        "createuser": "C000004",
        "currpeoples": 0,
        "distance": 0,
        "distancestr": "",
        "gname": "ddd, 100",
        "groupid": "GC000261",
        "gsort": "S000003",
        "gsortname": "",
        "gtype": "1",
        "gtypename": "主题群",
        "imgpfile": "/image/bfoodmer/GC000261_1466399753303_p.jpg",
        "imgpfilename": "",
        "imgsfile": "/image/bfoodmer/GC000261_1466399753303_s.jpg",
        "imgsfilename": "",
        "imid": "",
        "latitude": 41.81134,
        "longitude": 123,
        "managerid": "M000002",
        "manname": "coco",
        "mannick": "adfsafsd",
        "manphone": "18304025559",
        "memberuplimit": 100,
        "modifyuser": "platAdmin",
        "pkeyListStr": "",
        "remark": "",
        "summary": "肤色暗色"
    }
 */
	
	private String address;
	private String bulletin;
	private String checkstatus;
	private String checkstatusname;
	private String clubid;
	private String clubname;
	private String clubperson;
	private String clubphone;
	private String createuser;
	private String currpeoples;
	private String distance;
	private String distancestr;
	private String gname;
	private String groupid;
	private String gsort;
	private String gsortname;
	private String gtype;
	private String gtypename;
	private String imgpfile;
	private String imgpfilename;
	private String imgsfile;
	private String imgsfilename;
	private String imid;
	private String latitude;
	private String longitude;
	private String managerid;
	private String manname;
	private String mannick;
	private String manphone;
	private String memberuplimit;
	private String modifyuser;
	private String pkeyListStr;
	private String remark;
	private String summary;
	private String memcheckstatus;//0,shenhe  1  chenggong 2 bohui
	public String getMemcheckstatus() {
		return memcheckstatus;
	}
	public void setMemcheckstatus(String memcheckstatus) {
		this.memcheckstatus = memcheckstatus;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBulletin() {
		return bulletin;
	}
	public void setBulletin(String bulletin) {
		this.bulletin = bulletin;
	}
	public String getCheckstatus() {
		return checkstatus;
	}
	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}
	public String getCheckstatusname() {
		return checkstatusname;
	}
	public void setCheckstatusname(String checkstatusname) {
		this.checkstatusname = checkstatusname;
	}
	public String getClubid() {
		return clubid;
	}
	public void setClubid(String clubid) {
		this.clubid = clubid;
	}
	public String getClubname() {
		return clubname;
	}
	public void setClubname(String clubname) {
		this.clubname = clubname;
	}
	public String getClubperson() {
		return clubperson;
	}
	public void setClubperson(String clubperson) {
		this.clubperson = clubperson;
	}
	public String getClubphone() {
		return clubphone;
	}
	public void setClubphone(String clubphone) {
		this.clubphone = clubphone;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getCurrpeoples() {
		return currpeoples;
	}
	public void setCurrpeoples(String currpeoples) {
		this.currpeoples = currpeoples;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDistancestr() {
		return distancestr;
	}
	public void setDistancestr(String distancestr) {
		this.distancestr = distancestr;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGsort() {
		return gsort;
	}
	public void setGsort(String gsort) {
		this.gsort = gsort;
	}
	public String getGsortname() {
		return gsortname;
	}
	public void setGsortname(String gsortname) {
		this.gsortname = gsortname;
	}
	public String getGtype() {
		return gtype;
	}
	public void setGtype(String gtype) {
		this.gtype = gtype;
	}
	public String getGtypename() {
		return gtypename;
	}
	public void setGtypename(String gtypename) {
		this.gtypename = gtypename;
	}
	public String getImgpfile() {
		return imgpfile;
	}
	public void setImgpfile(String imgpfile) {
		this.imgpfile = imgpfile;
	}
	public String getImgpfilename() {
		return imgpfilename;
	}
	public void setImgpfilename(String imgpfilename) {
		this.imgpfilename = imgpfilename;
	}
	public String getImgsfile() {
		return imgsfile;
	}
	public void setImgsfile(String imgsfile) {
		this.imgsfile = imgsfile;
	}
	public String getImgsfilename() {
		return imgsfilename;
	}
	public void setImgsfilename(String imgsfilename) {
		this.imgsfilename = imgsfilename;
	}
	public String getImid() {
		return imid;
	}
	public void setImid(String imid) {
		this.imid = imid;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getManagerid() {
		return managerid;
	}
	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}
	public String getManname() {
		return manname;
	}
	public void setManname(String manname) {
		this.manname = manname;
	}
	public String getMannick() {
		return mannick;
	}
	public void setMannick(String mannick) {
		this.mannick = mannick;
	}
	public String getManphone() {
		return manphone;
	}
	public void setManphone(String manphone) {
		this.manphone = manphone;
	}
	public String getMemberuplimit() {
		return memberuplimit;
	}
	public void setMemberuplimit(String memberuplimit) {
		this.memberuplimit = memberuplimit;
	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public String getPkeyListStr() {
		return pkeyListStr;
	}
	public void setPkeyListStr(String pkeyListStr) {
		this.pkeyListStr = pkeyListStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
