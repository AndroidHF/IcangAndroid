package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/6.
 */
public class MyBuyPaiMainFinishBean {
    private int status;//1 未付款，2 未发货 3 已发货 4 确认收货 5 失败了
    private long time;
    private boolean isFinish;
    private String image;
    private String begin_auct_price;
    private String but_it_price;
    private String cover_pic;
    private String express_bill_num;
    private String express_name;
    private int is_eval;
    private int is_sd;
    private int jp_count;
    private String last_pay_time;
    private String max_pric;
    private int open_but_it;
    private String order_no;
    private int order_status;
    private int pay_status;
    private String pm_end_time;
    private int product_id;
    private String product_title;
    private String raretag_icon;
    private String self_max_price;
    private int st_id;
    private String st_name;
    private long pm_end_remain_second;
    private long last_pay_remain_second;

    public long getPm_end_remain_second() {
        return pm_end_remain_second;
    }

    public void setPm_end_remain_second(long pm_end_remain_second) {
        this.pm_end_remain_second = pm_end_remain_second;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public long getLast_pay_remain_second() {
        return last_pay_remain_second;
    }

    public void setLast_pay_remain_second(long last_pay_remain_second) {
        this.last_pay_remain_second = last_pay_remain_second;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBegin_auct_price() {
        return begin_auct_price;
    }

    public void setBegin_auct_price(String begin_auct_price) {
        this.begin_auct_price = begin_auct_price;
    }

    public String getBut_it_price() {
        return but_it_price;
    }

    public void setBut_it_price(String but_it_price) {
        this.but_it_price = but_it_price;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getExpress_bill_num() {
        return express_bill_num;
    }

    public void setExpress_bill_num(String express_bill_num) {
        this.express_bill_num = express_bill_num;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public int getIs_eval() {
        return is_eval;
    }

    public void setIs_eval(int is_eval) {
        this.is_eval = is_eval;
    }

    public int getIs_sd() {
        return is_sd;
    }

    public void setIs_sd(int is_sd) {
        this.is_sd = is_sd;
    }

    public int getJp_count() {
        return jp_count;
    }

    public void setJp_count(int jp_count) {
        this.jp_count = jp_count;
    }

    public String getLast_pay_time() {
        return last_pay_time;
    }

    public void setLast_pay_time(String last_pay_time) {
        this.last_pay_time = last_pay_time;
    }

    public String getMax_pric() {
        return max_pric;
    }

    public void setMax_pric(String max_pric) {
        this.max_pric = max_pric;
    }

    public int getOpen_but_it() {
        return open_but_it;
    }

    public void setOpen_but_it(int open_but_it) {
        this.open_but_it = open_but_it;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public String getPm_end_time() {
        return pm_end_time;
    }

    public void setPm_end_time(String pm_end_time) {
        this.pm_end_time = pm_end_time;
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

    public String getRaretag_icon() {
        return raretag_icon;
    }

    public void setRaretag_icon(String raretag_icon) {
        this.raretag_icon = raretag_icon;
    }

    public String getSelf_max_price() {
        return self_max_price;
    }

    public void setSelf_max_price(String self_max_price) {
        this.self_max_price = self_max_price;
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
}
