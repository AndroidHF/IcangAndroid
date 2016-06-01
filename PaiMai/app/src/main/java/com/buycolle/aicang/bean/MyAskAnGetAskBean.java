package com.buycolle.aicang.bean;

import java.util.ArrayList;

/**
 * Created by joe on 16/3/30.
 */
public class MyAskAnGetAskBean {


    /**
     * c_user_avatar : 测试内容vue8
     * c_user_nick : 测试内容ql1d
     * context : 测试内容26dh
     * create_date : 测试内容7ps2
     * create_user_id : 20177
     * id : 48260
     * pid : 81887
     * product_id : 74702
     * product_title : 测试内容i5x1
     * root_id : 85143
     * st_name : 测试内容u78c
     * subVec : [{"c_user_avatar":"测试内容7p91","c_user_nick":"测试内容3v8n","context":"测试内容i621","create_date":"测试内容n8yy","create_user_id":43870,"id":85177,"pid":81625,"root_id":17887}]
     */

    private String c_user_avatar;
    private String c_user_nick;
    private String context;
    private String create_date;
    private int create_user_id;
    private int id;
    private int pid;
    private int product_id;
    private String product_title;
    private int root_id;
    private String st_name;
    /**
     * c_user_avatar : 测试内容7p91
     * c_user_nick : 测试内容3v8n
     * context : 测试内容i621
     * create_date : 测试内容n8yy
     * create_user_id : 43870
     * id : 85177
     * pid : 81625
     * root_id : 17887
     */

    private ArrayList<SubVecEntity> subVec;

    public String getC_user_avatar() {
        return c_user_avatar;
    }

    public void setC_user_avatar(String c_user_avatar) {
        this.c_user_avatar = c_user_avatar;
    }

    public String getC_user_nick() {
        return c_user_nick;
    }

    public void setC_user_nick(String c_user_nick) {
        this.c_user_nick = c_user_nick;
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

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public ArrayList<SubVecEntity> getSubVec() {
        return subVec;
    }

    public void setSubVec(ArrayList<SubVecEntity> subVec) {
        this.subVec = subVec;
    }

    public static class SubVecEntity {
        private String c_user_avatar;
        private String c_user_nick;
        private String context;
        private String create_date;
        private int create_user_id;
        private int id;
        private int pid;
        private int root_id;

        public String getC_user_avatar() {
            return c_user_avatar;
        }

        public void setC_user_avatar(String c_user_avatar) {
            this.c_user_avatar = c_user_avatar;
        }

        public String getC_user_nick() {
            return c_user_nick;
        }

        public void setC_user_nick(String c_user_nick) {
            this.c_user_nick = c_user_nick;
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

        public int getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(int create_user_id) {
            this.create_user_id = create_user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getRoot_id() {
            return root_id;
        }

        public void setRoot_id(int root_id) {
            this.root_id = root_id;
        }
    }
}
