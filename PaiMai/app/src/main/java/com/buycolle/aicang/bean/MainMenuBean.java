package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/9.
 */
public class MainMenuBean {

    private String title;
    private int res;
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public MainMenuBean(String title, int res) {
        this.title = title;
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
