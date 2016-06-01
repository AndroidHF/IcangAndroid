package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/22.
 */
public class ShangPinSecondTypeBean {

    private String cate_name;
    private String cate_id;
    private String p_cate_name;
    private String pid;
    private int is_level;

    public int getIs_level() {
        return is_level;
    }

    public void setIs_level(int is_level) {
        this.is_level = is_level;
    }

    public String getP_cate_name() {
        return p_cate_name;
    }

    public void setP_cate_name(String p_cate_name) {
        this.p_cate_name = p_cate_name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }
}
