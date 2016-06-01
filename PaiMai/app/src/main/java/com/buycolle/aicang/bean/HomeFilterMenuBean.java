package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/4/26.
 */
public class HomeFilterMenuBean {

    private String name;
    private int resId;
    private boolean isEmpty;

    public HomeFilterMenuBean(String name, int resId, boolean isEmpty) {
        this.name = name;
        this.resId = resId;
        this.isEmpty = isEmpty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
