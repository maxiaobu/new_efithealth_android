package com.efithealth.app.javabean;

import java.util.List;

public class ForderList {
/*
 * "forderList": [
        {
            "clubid": "0",
            "clubname": "羿健康业务平台",
            "disttype": "1",
            "expressbillno": "",
            "expresstype": "0",
            "imagesfilename": "",
            "mbfordermerlist": [
                {
                    "buynum": 1,
                    "merid": "M000008",
                    "mername": "凉拌白菜心",
                    "ordno": "FO-20160612-184",
                    "pkeyListStr": ""
                }
            ],
            "ordamt": 15,
            "ordno": "FO-20160612-184",
            "ordstatus": "0",
            "payamt": 0,
            "paystatus": "0",
            "pkeyListStr": "",
            "recaddress": "市府广场市府大路",
            "recname": "就不告诉你",
            "recphone": "18040035083",
            "sendaddress": "",
            "sendstatus": "0",
            "sendtime": null,
            "senduserid": "",
            "sendusername": ""
        }
 */
	
	private String clubid;
	private String clubname;
	private String disttype;
	private String expressbillno;
	private String expresstype;
	private String imagesfilename;
	private String ordamt;
	private String ordno;
	private String ordstatus;
	private String payamt;
	private String paystatus;
	private String pkeyListStr;
	private String recaddress;
	private String recname;
	private String recphone;
	private String sendaddress;
	private String sendstatus;
	private String sendtime;
	private String senduserid;
	private String sendusername;
	private List<MbFordermerList> mbfordermerlist;
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
	public String getDisttype() {
		return disttype;
	}
	public void setDisttype(String disttype) {
		this.disttype = disttype;
	}
	public String getExpressbillno() {
		return expressbillno;
	}
	public void setExpressbillno(String expressbillno) {
		this.expressbillno = expressbillno;
	}
	public String getExpresstype() {
		return expresstype;
	}
	public void setExpresstype(String expresstype) {
		this.expresstype = expresstype;
	}
	public String getImagesfilename() {
		return imagesfilename;
	}
	public void setImagesfilename(String imagesfilename) {
		this.imagesfilename = imagesfilename;
	}
	public String getOrdamt() {
		return ordamt;
	}
	public void setOrdamt(String ordamt) {
		this.ordamt = ordamt;
	}
	public String getOrdno() {
		return ordno;
	}
	public void setOrdno(String ordno) {
		this.ordno = ordno;
	}
	public String getOrdstatus() {
		return ordstatus;
	}
	public void setOrdstatus(String ordstatus) {
		this.ordstatus = ordstatus;
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
	public String getPkeyListStr() {
		return pkeyListStr;
	}
	public void setPkeyListStr(String pkeyListStr) {
		this.pkeyListStr = pkeyListStr;
	}
	public String getRecaddress() {
		return recaddress;
	}
	public void setRecaddress(String recaddress) {
		this.recaddress = recaddress;
	}
	public String getRecname() {
		return recname;
	}
	public void setRecname(String recname) {
		this.recname = recname;
	}
	public String getRecphone() {
		return recphone;
	}
	public void setRecphone(String recphone) {
		this.recphone = recphone;
	}
	public String getSendaddress() {
		return sendaddress;
	}
	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}
	public String getSendstatus() {
		return sendstatus;
	}
	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getSenduserid() {
		return senduserid;
	}
	public void setSenduserid(String senduserid) {
		this.senduserid = senduserid;
	}
	public String getSendusername() {
		return sendusername;
	}
	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}
	public List<MbFordermerList> getMbfordermerlist() {
		return mbfordermerlist;
	}
	public void setMbfordermerlists(List<MbFordermerList> mbfordermerlist) {
		this.mbfordermerlist = mbfordermerlist;
	}
	
}





























