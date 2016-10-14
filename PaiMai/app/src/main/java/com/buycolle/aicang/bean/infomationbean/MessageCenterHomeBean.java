package com.buycolle.aicang.bean.infomationbean;

/**
 * Created by hufeng on 2016/10/10.
 */
public class MessageCenterHomeBean {

    private int count;//消息数
    private String create_date;//创建日期
    private String title;//标题内容
    private String logo_url;//活动的logo图的url
    private int type;//消息类型,1:我的交易，2：我的私信，3：活动信息，4：系统消息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    @Override
    public String toString() {
        return "MessageCenterHomeBean{" +
                "count=" + count +
                ", create_date='" + create_date + '\'' +
                ", title='" + title + '\'' +
                ", logo_url='" + logo_url + '\'' +
                ", type=" + type +
                '}';
    }
}
