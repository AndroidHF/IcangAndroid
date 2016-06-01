package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/6.
 */
public class PostImageBean {

    public enum Status{
        INIT,DONE,FAIL
    }

    private String localPath;
    private String serverPath;
    private Status status;
    private boolean isEmpty;
    private boolean isServer = false;

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PostImageBean{" +
                "localPath='" + localPath + '\'' +
                ", serverPath='" + serverPath + '\'' +
                ", status=" + status +
                ", isEmpty=" + isEmpty +
                ", isServer=" + isServer +
                '}';
    }
}
