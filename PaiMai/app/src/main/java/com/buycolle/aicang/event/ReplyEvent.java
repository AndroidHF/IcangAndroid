package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/6.
 */
public class ReplyEvent {

    private int status;

    public ReplyEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
