package com.buycolle.aicang.bean.infomationbean;

/**
 * Created by hufeng on 2016/10/10.
 */
public class InfoNoticeBean {
    private String type;//公告主标题
    private String title;//公告内容
    private String create_date;//公告创建时间
    private int remark;//置顶标识位（大于0：置顶图标显示，小于等于0：没有值）
    private String context;//公告Webview内容

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRemark() {
        return remark;
    }

    public void setRemark(int remark) {
        this.remark = remark;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "InfoNoticeBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", create_date='" + create_date + '\'' +
                ", remark=" + remark +
                ", context='" + context + '\'' +
                '}';
    }
}
