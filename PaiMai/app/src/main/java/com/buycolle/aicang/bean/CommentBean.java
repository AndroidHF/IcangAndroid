package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/4/8.
 */
public class CommentBean {


    /**
     * create_date : 测试内容324o
     * eval_content : 测试内容lw28
     * product_title : 测试内容5a88
     * user_nick : 测试内容5414
     */

    private int is_good;
    private String create_date;
    private String eval_content;
    private String product_title;
    private String user_nick;

    public int getIs_good() {
        return is_good;
    }

    public void setIs_good(int is_good) {
        this.is_good = is_good;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getEval_content() {
        return eval_content;
    }

    public void setEval_content(String eval_content) {
        this.eval_content = eval_content;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }
}
