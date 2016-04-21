package com.example.hufeng.com.example.hufeng.beans;

import java.util.List;

/**
 * Created by hufeng on 2016/4/11.
 */
public class CityBean {

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    private String area_name;

    public List<Dists> getDistincList() {
        return distincList;
    }

    public void setDistincList(List<Dists> distincList) {
        this.distincList = distincList;
    }

    private List<Dists> distincList;




}
