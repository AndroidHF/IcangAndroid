package com.buycolle.aicang.bean;

import java.util.ArrayList;

/**
 * Created by joe on 16/3/23.
 */
public class ProvinceBean {

    private String area_name;
    private String isleaf;
    private String area_type;
    private String parent_id;
    private String levels;
    private String id;
    private ArrayList<CityBean> cityList;

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public String getArea_type() {
        return area_type;
    }

    public void setArea_type(String area_type) {
        this.area_type = area_type;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
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

    public ArrayList<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityBean {
        private String area_name;
        private String isleaf;
        private String area_type;
        private String parent_id;
        private String levels;
        private String id;
        private ArrayList<DistinctBean> distinctList;

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getIsleaf() {
            return isleaf;
        }

        public void setIsleaf(String isleaf) {
            this.isleaf = isleaf;
        }

        public String getArea_type() {
            return area_type;
        }

        public void setArea_type(String area_type) {
            this.area_type = area_type;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
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

        public ArrayList<DistinctBean> getDistinctList() {
            return distinctList;
        }

        public void setDistinctList(ArrayList<DistinctBean> distinctList) {
            this.distinctList = distinctList;
        }

        public static class DistinctBean {
            private String area_name;
            private String isleaf;
            private String area_type;
            private String parent_id;
            private String levels;
            private String id;

            public String getArea_name() {
                return area_name;
            }

            public void setArea_name(String area_name) {
                this.area_name = area_name;
            }

            public String getIsleaf() {
                return isleaf;
            }

            public void setIsleaf(String isleaf) {
                this.isleaf = isleaf;
            }

            public String getArea_type() {
                return area_type;
            }

            public void setArea_type(String area_type) {
                this.area_type = area_type;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
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
        }

    }
}
