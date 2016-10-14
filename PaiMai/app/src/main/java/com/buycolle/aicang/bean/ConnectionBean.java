package com.buycolle.aicang.bean;

/**
 * Created by hufeng on 2016/8/25.
 */
public class ConnectionBean {
    private int user_id;//用户ID
    private String user_avatar;//用户头像地址
    private String create_date;//创建时间
    private String context;//交流的内容

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ConnectionBean{" +
                ", user_id=" + user_id +
                ", user_avatar='" + user_avatar + '\'' +
                ", create_date='" + create_date + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

}
