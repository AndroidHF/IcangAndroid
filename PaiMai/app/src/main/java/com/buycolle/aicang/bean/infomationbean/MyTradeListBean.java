package com.buycolle.aicang.bean.infomationbean;

/**
 * Created by hufeng on 2016/10/11.
 */
public class MyTradeListBean {
    private int id;//代表信息id，例如我的交易id：business_id，我的私信id：common_id，系统消息和活动信息id：other_id
    private int key_id;//跳转相关id
    private int type;//跳转类型
    private String title;//标题
    private String content;//内容
    private String create_date;//创建时间
    private String cover_pic;//商品封面url
    private int is_read;//是否已读，0：未读，显示NEW图标，1：表示已读，隐藏NEW图标

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    @Override
    public String toString() {
        return "MyTradeListBean{" +
                "id=" + id +
                ", key_id=" + key_id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", create_date='" + create_date + '\'' +
                ", cover_pic='" + cover_pic + '\'' +
                ", is_read=" + is_read +
                '}';
    }
}
