package com.buycolle.aicang.bean;

/**
 * Created by hufeng on 2016/7/26.
 */
public class UpdateInfo {
    private String version;

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", context='" + context +
                '}';
    }

    private String url;
    private String context;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
