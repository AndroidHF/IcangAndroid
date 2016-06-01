package com.buycolle.aicang.event;

/**
 * Created by joe on 16/4/28.
 */
public class LikeShowEvent {

    private int id;
    private boolean isStore;

    public LikeShowEvent(int id, boolean isStore) {
        this.id = id;
        this.isStore = isStore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStore() {
        return isStore;
    }

    public void setStore(boolean store) {
        isStore = store;
    }
}
