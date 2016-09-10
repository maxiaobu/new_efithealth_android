package com.efithealth.app.javabean;

public class MassageOrderList {
/*
 * {
            "clubid": "C000004",
            "clubname": "",
            "clubsplitamt": 0,
            "coachsplitamt": 0,
            "createtime": null,
            "createuser": "",
            "evascore": 0,
            "evastatus": "0",
            "evatime": null,
            "gmsplitamt": 0,
            "isplateassign": "0",
            "isplateassignname": "否",
            "massageid": "M000008",
            "massagename": "阿瑟撒",
            "masseurid": "M000007",
            "memid": "M000003",
            "memname": "",
            "mname": "",
            "mobphone": "",
            "modifytime": null,
            "modifyuser": "",
            "nickname": "",
            "ordamt": 100.3,
            "orderid":"MASS20160612094857852M000003",
            "orderstatusname": "未完成",
            "ordstatus": "0",
            
            "payaccount": "",
            "payamt": 0,
            "paystatus": "0",
            "paystatusname": "",
            "paytime": null,
            "paytype": "",
            "platsplitamt": 0,
            "remark": "",
            
            "timeconsuming": "",
            "ycoinamt": 0,
            "ycoincashnum": 0,
            "ycoinnum": 0
        }
 */
	
	private String clubid;
	private String clubname;
	private String clubsplitamt;
	private String coachsplitamt;
//	private String createtime;
	private String createuser;
	private String evascore;
	private String evastatus;
//	private String evatime;
	private String gmsplitamt;
	private String isplateassign;
	private String isplateassignname;
	private String massageid;
	private String massagename;
	private String masseurid;
	private String memid;
	private String memname;
	private String mname;
	private String mobphone;
//	private String modifytime;
	private String modifyuser;
	private String nickname;
	private String ordamt;
	private String orderid;
	private String orderstatusname;
	private String ordstatus;
	private String payaccount;
	private String payamt;
	private String paystatus;
	private String paystatusname;
	private String paytime;
	private String paytype;
	private String platsplitamt;
	private String remark;
	private String timeconsuming;
	private String ycoinamt;
	private String ycoincashnum;
	private String ycoinnum;
	private OrdendDate ordtime;
	private ReserveTime reservetime;
	private String imgsfilename;
	
	public String getImgsfilename() {
		return imgsfilename;
	}
	public void setImgsfilename(String imgsfilename) {
		this.imgsfilename = imgsfilename;
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
	public String getClubsplitamt() {
		return clubsplitamt;
	}
	public void setClubsplitamt(String clubsplitamt) {
		this.clubsplitamt = clubsplitamt;
	}
	public String getCoachsplitamt() {
		return coachsplitamt;
	}
	public void setCoachsplitamt(String coachsplitamt) {
		this.coachsplitamt = coachsplitamt;
	}
//	public String getCreatetime() {
//		return createtime;
//	}
//	public void setCreatetime(String createtime) {
//		this.createtime = createtime;
//	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getEvascore() {
		return evascore;
	}
	public void setEvascore(String evascore) {
		this.evascore = evascore;
	}
	public String getEvastatus() {
		return evastatus;
	}
	public void setEvastatus(String evastatus) {
		this.evastatus = evastatus;
	}
//	public String getEvatime() {
//		return evatime;
//	}
//	public void setEvatime(String evatime) {
//		this.evatime = evatime;
//	}
	public String getGmsplitamt() {
		return gmsplitamt;
	}
	public void setGmsplitamt(String gmsplitamt) {
		this.gmsplitamt = gmsplitamt;
	}
	public String getIsplateassign() {
		return isplateassign;
	}
	public void setIsplateassign(String isplateassign) {
		this.isplateassign = isplateassign;
	}
	public String getIsplateassignname() {
		return isplateassignname;
	}
	public void setIsplateassignname(String isplateassignname) {
		this.isplateassignname = isplateassignname;
	}
	public String getMassageid() {
		return massageid;
	}
	public void setMassageid(String massageid) {
		this.massageid = massageid;
	}
	public String getMassagename() {
		return massagename;
	}
	public void setMassagename(String massagename) {
		this.massagename = massagename;
	}
	public String getMasseurid() {
		return masseurid;
	}
	public void setMasseurid(String masseurid) {
		this.masseurid = masseurid;
	}
	public String getMemid() {
		return memid;
	}
	public void setMemid(String memid) {
		this.memid = memid;
	}
	public String getMemname() {
		return memname;
	}
	public void setMemname(String memname) {
		this.memname = memname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMobphone() {
		return mobphone;
	}
	public void setMobphone(String mobphone) {
		this.mobphone = mobphone;
	}
//	public String getModifytime() {
//		return modifytime;
//	}
//	public void setModifytime(String modifytime) {
//		this.modifytime = modifytime;
//	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getOrdamt() {
		return ordamt;
	}
	public void setOrdamt(String ordamt) {
		this.ordamt = ordamt;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrderstatusname() {
		return orderstatusname;
	}
	public void setOrderstatusname(String orderstatusname) {
		this.orderstatusname = orderstatusname;
	}
	public String getOrdstatus() {
		return ordstatus;
	}
	public void setOrdstatus(String ordstatus) {
		this.ordstatus = ordstatus;
	}
	public String getPayaccount() {
		return payaccount;
	}
	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}
	public String getPayamt() {
		return payamt;
	}
	public void setPayamt(String payamt) {
		this.payamt = payamt;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public String getPaystatusname() {
		return paystatusname;
	}
	public void setPaystatusname(String paystatusname) {
		this.paystatusname = paystatusname;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPlatsplitamt() {
		return platsplitamt;
	}
	public void setPlatsplitamt(String platsplitamt) {
		this.platsplitamt = platsplitamt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTimeconsuming() {
		return timeconsuming;
	}
	public void setTimeconsuming(String timeconsuming) {
		this.timeconsuming = timeconsuming;
	}
	public String getYcoinamt() {
		return ycoinamt;
	}
	public void setYcoinamt(String ycoinamt) {
		this.ycoinamt = ycoinamt;
	}
	public String getYcoincashnum() {
		return ycoincashnum;
	}
	public void setYcoincashnum(String ycoincashnum) {
		this.ycoincashnum = ycoincashnum;
	}
	public String getYcoinnum() {
		return ycoinnum;
	}
	public void setYcoinnum(String ycoinnum) {
		this.ycoinnum = ycoinnum;
	}
	public OrdendDate getOrdtime() {
		return ordtime;
	}
	public void setOrdtime(OrdendDate ordtime) {
		this.ordtime = ordtime;
	}
	public ReserveTime getReservetime() {
		return reservetime;
	}
	public void setReservetime(ReserveTime reservetime) {
		this.reservetime = reservetime;
	}
	
}
