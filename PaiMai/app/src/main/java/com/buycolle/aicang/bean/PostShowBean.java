package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/12.
 */
public class PostShowBean {

    public enum Status {
        INIT, DONE, FAIL
    }

    private int type;//1 表示文字，2 表示图  3表示链接
    private String content;
    private String imageLocal = "";
    private Status status;
    private boolean isServer = false;

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public String getImageLocal() {
        return imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
