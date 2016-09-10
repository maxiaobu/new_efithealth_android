package com.efithealth.app.javabean;

import java.util.List;

public class HotDynamic {
/*
 *  "hotDynamicsList": [
        {
            "clubid": "",
            "content": "",
            "createtime": {
                "date": 15,
                "day": 3,
                "hours": 17,
                "minutes": 48,
                "month": 5,
                "nanos": 0,
                "seconds": 28,
                "time": 1465984108000,
                "timezoneOffset": -480,
                "year": 116
            },
            "createtimes": "2016-06-15 17:48:28",
            "createuser": "",
            "dynid": "D000225",
            "imagefile": [],
            "imgList": [
                {
                    "createtime": {
                        "date": 15,
                        "day": 3,
                        "hours": 17,
                        "minutes": 48,
                        "month": 5,
                        "nanos": 0,
                        "seconds": 31,
                        "time": 1465984111000,
                        "timezoneOffset": -480,
                        "year": 116
                    },
                    "createuser": "M000421",
                    "dynid": "D000225",
                    "imgid": "I000322",
                    "imgpfile": "/image/bdynamicimg/I000322_1465984108293_p.png",
                    "imgpfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bdynamicimg/I000322_1465984108293_p.png",
                    "imgsfile": "/image/bdynamicimg/I000322_1465984108293_s.png",
                    "imgsfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bdynamicimg/I000322_1465984108293_s.png",
                    "modifytime": null,
                    "modifyuser": "",
                    "pkeyListStr": "",
                    "remark": ""
                }
            ],
            "imgpfile": "",
            "imgpfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000421_1465354891210_s.png",
            "imgsfile": "/image/bmember/M000421_1465354891210_s.png",
            "imgsfilename": "http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000421_1465354891210_s.png",
            "isPoint": "1",
            "isprivate": "0",
            "istop": "0",
            "memid": "M000421",
            "modifytime": null,
            "modifyuser": "",
            "nickname": "",
            "pkeyListStr": "",
            "pointnum": 1,
            "remark": "",
            "reviewnum": 0,
            "role": "coach",
            "signature": ""
        }
    ]
 */
	
	private String clubid;
	private String content;
	private String createtimes;
	private String createuser;
	private String dynid;
	private String imgpfile;
	private String imgpfilename;
	private String imgsfile;
	private String imgsfilename;
	private String isPoint;
	private String isprivate;
	private String istop;
	private String memid;
	private String modifyuser;
	private String nickname;
	private String pkeyListStr;
	private String pointnum;
	private String remark;
	private String reviewnum;
	private String role;
	private String signature;
	private CreateTime createtime;
	public List<ImgList> imgList;
	
	
	
	public List<ImgList> getImgList() {
		return imgList;
	}

	public void setImgList(List<ImgList> imgList) {
		this.imgList = imgList;
	}

	public String getPointnum() {
		return pointnum;
	}

	public void setPointnum(String pointnum) {
		this.pointnum = pointnum;
	}

	public String getReviewnum() {
		return reviewnum;
	}

	public void setReviewnum(String reviewnum) {
		this.reviewnum = reviewnum;
	}

	public String getClubid() {
		return clubid;
	}

	public void setClubid(String clubid) {
		this.clubid = clubid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatetimes() {
		return createtimes;
	}

	public void setCreatetimes(String createtimes) {
		this.createtimes = createtimes;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getDynid() {
		return dynid;
	}

	public void setDynid(String dynid) {
		this.dynid = dynid;
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

	public String getIsPoint() {
		return isPoint;
	}

	public void setIsPoint(String isPoint) {
		this.isPoint = isPoint;
	}

	public String getIsprivate() {
		return isprivate;
	}

	public void setIsprivate(String isprivate) {
		this.isprivate = isprivate;
	}

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getMemid() {
		return memid;
	}

	public void setMemid(String memid) {
		this.memid = memid;
	}

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public CreateTime getCreatetime() {
		return createtime;
	}

	public void setCreatetime(CreateTime createtime) {
		this.createtime = createtime;
	}

	public class ImgList {
		public String createuser;
		public String dynid;
		public String imgid;
		public String imgpfile;
		public String imgpfilename;
		public String imgsfile;
		public String imgsfilename;
		public String modifyuser;
		public String pkeyListStr;
		public String remark;
		public Createtime createtime;
		public String getCreateuser() {
			return createuser;
		}
		public void setCreateuser(String createuser) {
			this.createuser = createuser;
		}
		public String getDynid() {
			return dynid;
		}
		public void setDynid(String dynid) {
			this.dynid = dynid;
		}
		public String getImgid() {
			return imgid;
		}
		public void setImgid(String imgid) {
			this.imgid = imgid;
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
		public Createtime getCreatetime() {
			return createtime;
		}
		public void setCreatetime(Createtime createtime) {
			this.createtime = createtime;
		}
		
	}
	
	public class Createtime{
		public String date;
		public String day;
		public String hours;
		public String minutes;
		public String month;
		public String nanos;
		public String seconds;
		public String time;
		public String timezoneOffset;
		public String year;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
		public String getHours() {
			return hours;
		}
		public void setHours(String hours) {
			this.hours = hours;
		}
		public String getMinutes() {
			return minutes;
		}
		public void setMinutes(String minutes) {
			this.minutes = minutes;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public String getNanos() {
			return nanos;
		}
		public void setNanos(String nanos) {
			this.nanos = nanos;
		}
		public String getSeconds() {
			return seconds;
		}
		public void setSeconds(String seconds) {
			this.seconds = seconds;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTimezoneOffset() {
			return timezoneOffset;
		}
		public void setTimezoneOffset(String timezoneOffset) {
			this.timezoneOffset = timezoneOffset;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		
	}
}
