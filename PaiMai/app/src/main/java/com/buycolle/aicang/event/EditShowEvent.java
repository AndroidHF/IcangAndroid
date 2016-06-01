package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/6.
 */
public class EditShowEvent {

    private int status ;

    public EditShowEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
