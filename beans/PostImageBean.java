package com.example.hufeng.com.example.hufeng.beans;

/**
 * Created by hufeng on 2016/4/18.
 */
public class PostImageBean {
    public enum Status{
        INIT,DONE,FAIL
    }

    private String localPath;
    private String serverPath;
    private Status status;
    private boolean isEmpty;

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

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

}
