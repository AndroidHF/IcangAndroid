package com.buycolle.aicang.event;

/**
 * Created by joe on 16/5/3.
 */
public class UpdateTanNoticeEvent {

    private int status;

    public UpdateTanNoticeEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
