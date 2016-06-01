package com.buycolle.aicang.bean;

import java.util.List;

/**
 * Created by joe on 16/3/25.
 */
public class AddressBean {

    private String desc;
    private int status;

    private List<RowsEntity> rows;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity {
        private String city;
        private int city_id;
        private String district;
        private int district_id;
        private int id;
        private int pro_id;
        private String province;
        private String receipt_address;
        private String receipt_name;
        private String receipt_tel;
        private String zip_code;
        private int user_id;

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(int district_id) {
            this.district_id = district_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPro_id() {
            return pro_id;
        }

        public void setPro_id(int pro_id) {
            this.pro_id = pro_id;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReceipt_address() {
            return receipt_address;
        }

        public void setReceipt_address(String receipt_address) {
            this.receipt_address = receipt_address;
        }

        public String getReceipt_name() {
            return receipt_name;
        }

        public void setReceipt_name(String receipt_name) {
            this.receipt_name = receipt_name;
        }

        public String getReceipt_tel() {
            return receipt_tel;
        }

        public void setReceipt_tel(String receipt_tel) {
            this.receipt_tel = receipt_tel;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
