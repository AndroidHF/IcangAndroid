package com.example.hufeng.com.example.hufeng.beans;

import java.util.List;

/**
 * Created by hufeng on 2016/4/11.
 */
public class ProvinceBean {
    private String area_name;
    private String isleaf;
    private String area_type;
    private String levels;
    private String id;
    private List<CityBean> cityList;

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_type() {
        return area_type;
    }

    public void setArea_type(String area_type) {
        this.area_type = area_type;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityBean> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceBean{" +
                "area_name='" + area_name + '\'' +
                ", isleaf='" + isleaf + '\'' +
                ", area_type='" + area_type + '\'' +
                ", levels='" + levels + '\'' +
                ", id='" + id + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}
