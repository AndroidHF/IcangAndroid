package com.buycolle.aicang.bean;

/**
 * Created by Ruily on 16/4/28.
 */
public class TabInfo {
    private int imgRes;
    private String text;

    private boolean hideText;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHideText() {
        return hideText;
    }

    public void setHideText(boolean hideText) {
        this.hideText = hideText;
    }
}
