package com.buycolle.aicang.event;

/**
 * Created by joe on 16/3/3.
 */
public class LogOutToMainEvent {
    private int status;

    public LogOutToMainEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
