package com.buycolle.aicang.ui.view.recyclertablayout;

public class RecyclerTabEntity {

    private String mTitle;
    private int    mIcon;

    public RecyclerTabEntity(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }
}
