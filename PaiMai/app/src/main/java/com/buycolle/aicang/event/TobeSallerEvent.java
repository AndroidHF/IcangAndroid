package com.buycolle.aicang.event;

/**
 * Created by joe on 16/5/20.
 */
public class TobeSallerEvent {
    private int status;

    public TobeSallerEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
