package com.buycolle.aicang.bean;

/**
 * Created by hufeng on 2016/8/15.
 */
public class SubactivityBean {
    private String banner_url;//活动的bannner图
    private String bg_url;//底纹
    private String event_title;//专题活动名称

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    @Override
    public String toString() {
        return "SubactivityBean{" +
                "banner_url='" + banner_url + '\'' +
                ", bg_url='" + bg_url + '\'' +
                ", event_title='" + event_title + '\'' +
                '}';
    }
}
