package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/3.
 */
public class NoticeBean {


    private boolean isExpand = false;
    /**
     * context : 测试内容2xn4
     * create_date : 测试内容u50n
     * id : 81763
     */

    private String context;
    private String create_date;
    private int id;


    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
