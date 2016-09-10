package com.efithealth.app.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class MtoupdateMember implements Serializable {


    private String msgFlag;
    private String msgContent;

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
}
