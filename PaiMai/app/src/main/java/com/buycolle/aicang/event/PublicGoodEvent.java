package com.buycolle.aicang.event;

/**
 * Created by joe on 16/5/7.
 */
public class PublicGoodEvent {

    private int status;

    public PublicGoodEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
