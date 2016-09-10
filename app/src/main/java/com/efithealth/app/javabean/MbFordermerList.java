package com.efithealth.app.javabean;

public class MbFordermerList {
/*
 * mbfordermerlist": [
                {
                    "buynum": 1,
                    "merid": "M000008",
                    "mername": "凉拌白菜心",
                    "ordno": "FO-20160612-184",
                    "pkeyListStr": ""
                }
            ],
 */
	private String buynum;
	private String merid;
	private String mername;
	private String ordno;
	private String pkeyListStr;
	public String getBuynum() {
		return buynum;
	}
	public void setBuynum(String buynum) {
		this.buynum = buynum;
	}
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getMername() {
		return mername;
	}
	public void setMername(String mername) {
		this.mername = mername;
	}
	public String getOrdno() {
		return ordno;
	}
	public void setOrdno(String ordno) {
		this.ordno = ordno;
	}
	public String getPkeyListStr() {
		return pkeyListStr;
	}
	public void setPkeyListStr(String pkeyListStr) {
		this.pkeyListStr = pkeyListStr;
	}
	
}
