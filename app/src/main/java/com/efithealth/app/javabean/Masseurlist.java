package com.efithealth.app.javabean;

public class Masseurlist {
	/*
 * "createuser":"platAdmin",
 * "datomworks":"",
 * "gender":"1",
 * "imgfile":null,
 * "imgpfile":"/image/bmasseur/M000007_1465203642539_p.png",
 * "imgpfilename":"",
 * "imgsfile":"/image/bmasseur/M000007_1465203642539_s.png",
 * "imgsfilename":"",
 * "masseurid":"M000007",
 * "mname":"撒啊啊",
 * "modifytime":null,
 * "modifyuser":"",
 * "remark":"",
 * "status":"1",
 * "statusname":"",
 * "summary":"按时发大厦等发达水电费",
 * "todworks":"2016060711|2016060712|2016060713",
 * "tomworks":"2016060810|2016060811|2016060812"}]}
	 */
	
	private String createuser;
	private String datomworks;
	private String gender;
	private String imgfile;
	private String imgpfile;
	private String imgsfile;
	private String imgpfilename;
	private String imgsfilename;
	private String masseurid;
	private String mname;
//	private String modifytime;
	private String modifyuser;
	private String remark;
	private String status;
	private String statusname;
	private String summary;
	private String todworks;
	private String tomworks;
	private CreateTime createtime;
	private boolean flag_mpa=false;
	
	
	public boolean getFlag_mpa() {
		return flag_mpa;
	}
	public void setFlag_mpa(boolean flag_mpa) {
		this.flag_mpa = flag_mpa;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getDatomworks() {
		return datomworks;
	}
	public void setDatomworks(String datomworks) {
		this.datomworks = datomworks;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getImgfile() {
		return imgfile;
	}
	public void setImgfile(String imgfile) {
		this.imgfile = imgfile;
	}
	public String getImgpfile() {
		return imgpfile;
	}
	public void setImgpfile(String imgpfile) {
		this.imgpfile = imgpfile;
	}
	public String getImgsfile() {
		return imgsfile;
	}
	public void setImgsfile(String imgsfile) {
		this.imgsfile = imgsfile;
	}
	public String getImgpfilename() {
		return imgpfilename;
	}
	public void setImgpfilename(String imgpfilename) {
		this.imgpfilename = imgpfilename;
	}
	public String getImgsfilename() {
		return imgsfilename;
	}
	public void setImgsfilename(String imgsfilename) {
		this.imgsfilename = imgsfilename;
	}
	public String getMasseurid() {
		return masseurid;
	}
	public void setMasseurid(String masseurid) {
		this.masseurid = masseurid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTodworks() {
		return todworks;
	}
	public void setTodworks(String todworks) {
		this.todworks = todworks;
	}
	public String getTomworks() {
		return tomworks;
	}
	public void setTomworks(String tomworks) {
		this.tomworks = tomworks;
	}
	public CreateTime getCreatetime() {
		return createtime;
	}
	public void setCreatetime(CreateTime createtime) {
		this.createtime = createtime;
	}
	
	
	
}
