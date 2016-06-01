package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/12.
 */
public class WXZhifuEvent {

    private int status;

    public WXZhifuEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
