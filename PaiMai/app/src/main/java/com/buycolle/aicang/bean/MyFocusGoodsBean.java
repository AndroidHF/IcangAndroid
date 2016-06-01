package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/4/5.
 */
public class MyFocusGoodsBean {

    private int product_id;
    private int raretag_level;
    private int st_id;
    private int col_id; //大于1表示有收藏
    private String raretag_icon;
    private String st_name;
    private String product_title;
    private String product_intro;
    private String cover_pic;
    private String pm_start_time;

    public String getPm_start_time() {
        return pm_start_time;
    }

    public void setPm_start_time(String pm_start_time) {
        this.pm_start_time = pm_start_time;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getRaretag_level() {
        return raretag_level;
    }

    public void setRaretag_level(int raretag_level) {
        this.raretag_level = raretag_level;
    }

    public int getSt_id() {
        return st_id;
    }

    public void setSt_id(int st_id) {
        this.st_id = st_id;
    }

    public int getCol_id() {
        return col_id;
    }

    public void setCol_id(int col_id) {
        this.col_id = col_id;
    }

    public String getRaretag_icon() {
        return raretag_icon;
    }

    public void setRaretag_icon(String raretag_icon) {
        this.raretag_icon = raretag_icon;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_intro() {
        return product_intro;
    }

    public void setProduct_intro(String product_intro) {
        this.product_intro = product_intro;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }
}
