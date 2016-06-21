package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/4/8.
 */
public class PayBean {


    /**
     * city : 测试内容25ol
     * cj_type : 36248
     * cover_pic : 测试内容ixjl
     * district : 测试内容2w13
     * express_out_type : 50322
     * fahou_city : 测试内容sv78
     * fahou_province : 测试内容mu27
     * fahuo_time_type : 65728
     * order_price : 测试内容m684
     * product_desc : 测试内容1y03
     * product_id : 82767
     * product_title : 测试内容i1o5
     * province : 测试内容ex6f
     * receipt_address : 测试内容36iu
     * receipt_name : 测试内容damk
     * receipt_tel : 测试内容q8c9
     * st_id : 21630
     * st_name : 测试内容4455
     * zip_code : 测试内容g607
     */

    private String city;
    private int cj_type;
    private String cover_pic;
    private String district;
    private String order_no;
    private int express_out_type;
    private String fahou_city;
    private String fahou_province;
    private int fahuo_time_type;
    private String order_price;
    private String product_desc;
    private int product_id;
    private String product_title;
    private String province;
    private String receipt_address;
    private String receipt_name;
    private String receipt_tel;
    private int st_id;
    private String st_name;
    private String zip_code;



    //add by:胡峰
    private String real_price;//使用小卡片的实际金额
    private String cou_flag;//是否使用过小卡片的标识，0：表示没有使用过，1：表示已经使用过

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCj_type() {
        return cj_type;
    }

    public void setCj_type(int cj_type) {
        this.cj_type = cj_type;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getExpress_out_type() {
        return express_out_type;
    }

    public void setExpress_out_type(int express_out_type) {
        this.express_out_type = express_out_type;
    }

    public String getFahou_city() {
        return fahou_city;
    }

    public void setFahou_city(String fahou_city) {
        this.fahou_city = fahou_city;
    }

    public String getFahou_province() {
        return fahou_province;
    }

    public void setFahou_province(String fahou_province) {
        this.fahou_province = fahou_province;
    }

    public int getFahuo_time_type() {
        return fahuo_time_type;
    }

    public void setFahuo_time_type(int fahuo_time_type) {
        this.fahuo_time_type = fahuo_time_type;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
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

    public int getSt_id() {
        return st_id;
    }

    public void setSt_id(int st_id) {
        this.st_id = st_id;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getReal_price() {
        return real_price;
    }

    public void setReal_price(String real_price) {
        this.real_price = real_price;
    }

    public String getCou_flag() {
        return cou_flag;
    }

    public void setCou_flag(String cou_flag) {
        this.cou_flag = cou_flag;
    }
}
