package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/9.
 */
public class MyBuyCommentEvent {

    private int status;

    public MyBuyCommentEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
