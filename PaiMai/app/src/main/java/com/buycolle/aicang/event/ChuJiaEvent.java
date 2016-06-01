package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/8.
 */
public class ChuJiaEvent {

    private  int status;//可以当做id 使用

    public ChuJiaEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
