package com.buycolle.aicang.bean.mysale;

/**
 * Created by joe on 16/3/23.
 */
public class MySaleShenHeBean {

    private  String check_reason;//审核理由
    private  String begin_auct_price;//起拍价
    private  String cover_pic;//封面
    private  String st_id;//拍品商品状态ID
    private  String st_name;
    private  String product_id;
    private  String jp_count;
    private  String but_it_price;//一口价价格
    private  String raretag_icon;
    private int open_but_it;
    private  String product_title;
    private  String check_status;//审核状态 0：未审核 1：失败 2：成功

    public int getOpen_but_it() {
        return open_but_it;
    }

    public void setOpen_but_it(int open_but_it) {
        this.open_but_it = open_but_it;
    }

    public String getCheck_reason() {
        return check_reason;
    }

    public void setCheck_reason(String check_reason) {
        this.check_reason = check_reason;
    }

    public String getBegin_auct_price() {
        return begin_auct_price;
    }

    public void setBegin_auct_price(String begin_auct_price) {
        this.begin_auct_price = begin_auct_price;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getSt_id() {
        return st_id;
    }

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getJp_count() {
        return jp_count;
    }

    public void setJp_count(String jp_count) {
        this.jp_count = jp_count;
    }

    public String getBut_it_price() {
        return but_it_price;
    }

    public void setBut_it_price(String but_it_price) {
        this.but_it_price = but_it_price;
    }

    public String getRaretag_icon() {
        return raretag_icon;
    }

    public void setRaretag_icon(String raretag_icon) {
        this.raretag_icon = raretag_icon;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }
}
