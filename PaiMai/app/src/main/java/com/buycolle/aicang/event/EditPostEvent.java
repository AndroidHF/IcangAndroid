package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/18.
 */
public class EditPostEvent {

    private int status;

    public EditPostEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
