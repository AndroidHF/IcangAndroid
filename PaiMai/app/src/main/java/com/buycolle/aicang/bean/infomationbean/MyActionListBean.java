package com.buycolle.aicang.bean.infomationbean;

/**
 * Created by hufeng on 2016/10/12.
 */
public class MyActionListBean {
    private String banner_icon;//banner图的url
    private String banner_link;//外链地址
    private String context;//Webview内容
    private int action_type;//跳转类型
    private int target_id;//跳转id
    private String create_date;//创建时间

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public String getBanner_icon() {
        return banner_icon;
    }

    public void setBanner_icon(String banner_icon) {
        this.banner_icon = banner_icon;
    }

    public String getBanner_link() {
        return banner_link;
    }

    public void setBanner_link(String banner_link) {
        this.banner_link = banner_link;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "MyActionListBean{" +
                "banner_icon='" + banner_icon + '\'' +
                ", banner_link='" + banner_link + '\'' +
                ", context='" + context + '\'' +
                ", action_type=" + action_type +
                ", target_id=" + target_id +
                ", create_date='" + create_date + '\'' +
                '}';
    }
}
