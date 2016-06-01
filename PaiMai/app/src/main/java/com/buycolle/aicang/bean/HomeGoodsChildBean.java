package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/3/24.
 */
public class HomeGoodsChildBean {

    /**
     * begin_auct_price : 测试内容7bx9
     * but_it_price : 测试内容8ji4
     * cover_pic : 测试内容7nf2
     * express_out_type : 12241
     * jp_count : 77128
     * max_pric : 测试内容ifg6
     * open_but_it : 54858
     * pm_end_time : 测试内容lrkp
     * pm_start_time : 测试内容d481
     * product_id : 30756
     * product_title : 测试内容416o
     * raretag_icon : 测试内容65r5
     * raretag_level : 43386
     */

    private String begin_auct_price;
    private String but_it_price;
    private String cover_pic;
    private int express_out_type;
    private int jp_count;
    private String max_pric;
    private int open_but_it;
    private String pm_end_time;
    private String pm_start_time;
    private int product_id;
    private String product_title;
    private String raretag_icon;
    private int raretag_level;
    private long pm_end_remain_second;
    private long time;
    private boolean isFinish =false;

    public long getPm_end_remain_second() {
        return pm_end_remain_second;
    }

    public void setPm_end_remain_second(long pm_end_remain_second) {
        this.pm_end_remain_second = pm_end_remain_second;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
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

    public int getExpress_out_type() {
        return express_out_type;
    }

    public void setExpress_out_type(int express_out_type) {
        this.express_out_type = express_out_type;
    }

    public int getJp_count() {
        return jp_count;
    }

    public void setJp_count(int jp_count) {
        this.jp_count = jp_count;
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

    public String getPm_end_time() {
        return pm_end_time;
    }

    public void setPm_end_time(String pm_end_time) {
        this.pm_end_time = pm_end_time;
    }

    public String getPm_start_time() {
        return pm_start_time;
    }

    public void setPm_start_time(String pm_start_time) {
        this.pm_start_time = pm_start_time;
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

    public int getRaretag_level() {
        return raretag_level;
    }

    public void setRaretag_level(int raretag_level) {
        this.raretag_level = raretag_level;
    }
}
