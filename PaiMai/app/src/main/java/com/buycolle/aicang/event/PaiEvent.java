package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/14.
 */
public class PaiEvent {

    private int status;

    public PaiEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
