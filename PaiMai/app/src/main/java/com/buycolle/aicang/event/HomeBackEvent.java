package com.buycolle.aicang.event;

/**
 * Created by joe on 16/3/18.
 */
public class HomeBackEvent {

    private int index;

    public HomeBackEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
