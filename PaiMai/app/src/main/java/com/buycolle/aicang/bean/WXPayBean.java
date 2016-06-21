package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/4/12.
 */
public class WXPayBean {


    /**
     * appid : 测试内容gz3t
     * noncestr : 测试内容4kl1
     * package : 测试内容5uoh
     * partnerid : 测试内容1c5j
     * paySign : 测试内容k748
     * prepayid : 测试内容njs6
     * timestamp : 测试内容o473
     */

    private String appid;
    private String noncestr;
    private String partnerid;
    private String paySign;
    private String prepayid;
    private String timestamp;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }


    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
