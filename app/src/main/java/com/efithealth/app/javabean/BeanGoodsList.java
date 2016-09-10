package com.efithealth.app.javabean;

import java.util.List;

/**
 * Created by 马小布 on 2016/8/17.
 */
public class BeanGoodsList {


    /**
     * msgFlag : 1
     * msgContent : 配餐列表
     * list : [{"compodescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","detailsurl":"","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000019_1469589053322_p.png","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000019_1469589053322_p.png","imgsfile":"/image/bfoodmer/M000019_1469589053322_s.png","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000019_1469589053322_s.png","merdescr":"营养又健康","merid":"M000019","mername":"壹想食健身周双餐","merprice":300,"mertype":"1"},{"compodescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","detailsurl":"","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000024_1471051836026_p.jpg","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_p.jpg","imgsfile":"/image/bfoodmer/M000024_1471051836026_s.jpg","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_s.jpg","merdescr":"壹想食健身周单餐","merid":"M000024","mername":"壹想食健身周午餐","merprice":10,"mertype":"2"},{"compodescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","detailsurl":"","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000025_1471051878783_p.jpg","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000025_1471051878783_p.jpg","imgsfile":"/image/bfoodmer/M000025_1471051878783_s.jpg","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000025_1471051878783_s.jpg","merdescr":"壹想食健身周晚餐","merid":"M000025","mername":"壹想食健身周晚餐","merprice":15,"mertype":"3"},{"compodescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","detailsurl":"\\html\\bfoodmer\\M000027_1471225131947.html","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000027_1471225128501_p.png","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000027_1471225128501_p.png","imgsfile":"/image/bfoodmer/M000027_1471225128501_s.png","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000027_1471225128501_s.png","merdescr":"高原测试用例","merid":"M000027","mername":"高原测试用例","merprice":150,"mertype":"1"}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * compodescr : 热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg
     * detailsurl :
     * fplatConperson :
     * fplatConphone :
     * imgpfile : /image/bfoodmer/M000019_1469589053322_p.png
     * imgpfilename : http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000019_1469589053322_p.png
     * imgsfile : /image/bfoodmer/M000019_1469589053322_s.png
     * imgsfilename : http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000019_1469589053322_s.png
     * merdescr : 营养又健康
     * merid : M000019
     * mername : 壹想食健身周双餐
     * merprice : 300
     * mertype : 1
     */

    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String compodescr;
        private String detailsurl;
        private String fplatConperson;
        private String fplatConphone;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String merdescr;
        private String merid;
        private String mername;
        private int merprice;
        private String mertype;

        public String getCompodescr() {
            return compodescr;
        }

        public void setCompodescr(String compodescr) {
            this.compodescr = compodescr;
        }

        public String getDetailsurl() {
            return detailsurl;
        }

        public void setDetailsurl(String detailsurl) {
            this.detailsurl = detailsurl;
        }

        public String getFplatConperson() {
            return fplatConperson;
        }

        public void setFplatConperson(String fplatConperson) {
            this.fplatConperson = fplatConperson;
        }

        public String getFplatConphone() {
            return fplatConphone;
        }

        public void setFplatConphone(String fplatConphone) {
            this.fplatConphone = fplatConphone;
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

        public String getMerdescr() {
            return merdescr;
        }

        public void setMerdescr(String merdescr) {
            this.merdescr = merdescr;
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

        public int getMerprice() {
            return merprice;
        }

        public void setMerprice(int merprice) {
            this.merprice = merprice;
        }

        public String getMertype() {
            return mertype;
        }

        public void setMertype(String mertype) {
            this.mertype = mertype;
        }
    }
}
