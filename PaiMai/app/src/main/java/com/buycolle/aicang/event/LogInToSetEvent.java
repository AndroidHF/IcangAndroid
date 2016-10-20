package com.buycolle.aicang.event;

/**
 * Created by joe on 16/3/3.
 */
public class LogInToSetEvent {
    private int status;

    public LogInToSetEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
