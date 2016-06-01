package com.buycolle.aicang.event;

/**
 * Created by joe on 16/3/20.
 */
public class EditUserInfoEvent {

    private int status;// 0 更新头像

    public EditUserInfoEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
