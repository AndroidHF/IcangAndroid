package com.buycolle.aicang.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by joe on 16/3/10.
 */
public class PaiPinDetailBean implements Serializable{
    private boolean isExpand = false;
    private String content;
    private long time;
    private boolean isFinish =false;

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

    /**
     * begin_auct_price : 测试内容sz13
     * but_it_price : 测试内容h247
     * cate_id : 26580
     * cate_name : 测试内容tcc9
     * cover_pic : 测试内容31nk
     * express_out_type : 16885
     * fahou_city : 测试内容4x3i
     * fahou_province : 测试内容23t6
     * fahuo_time_type : 测试内容2flm
     * faq : [{"answer":"测试内容8884","question":"测试内容j2nl"}]
     * jp_count : 61815
     * max_pric : 测试内容7y37
     * open_but_it : 40216
     * pm_end_time : 测试内容b94u
     * pm_start_time : 测试内容1637
     * product_desc : 测试内容5742
     * product_id : 22018
     * product_title : 测试内容1ow1
     * raretag_icon : 测试内容8l99
     * raretag_level : 53710
     * seller_user_id : 50748
     * st_id : 55088
     * st_name : 测试内容q825
     * user_avatar : 测试内容0w4u
     * user_name : 测试内容3358
     */

    private String begin_auct_price;
    private String but_it_price;
    private int cate_id;
    private int ent_id;
    private String cate_name;
    private String cover_pic;
    private String all_cate_name;
    private String p_cate_name;
    private int col_id = 0;
    private int p_cate_id;

    private int express_out_type;
    private String fahou_city;
    private String fahou_province;
    private String fahuo_time_type;
    private int jp_count;
    private String max_pric;
    private int open_but_it;
    private String pm_end_time;
    private String pm_start_time;
    private String product_desc;
    private int product_id;
    private String product_title;
    private String raretag_icon;
    private int raretag_level;
    private int seller_user_id;
    private int st_id;
    private String st_name;
    private String user_avatar;
    private String user_name;
    private String cycle_pic;
    private int total_comment;
    private int good_comment;
    private int bad_comment;

    private long pm_end_remain_second;

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public long getPm_end_remain_second() {
        return pm_end_remain_second;
    }

    public void setPm_end_remain_second(long pm_end_remain_second) {
        this.pm_end_remain_second = pm_end_remain_second;
    }

    public int getCol_id() {
        return col_id;
    }

    public void setCol_id(int col_id) {
        this.col_id = col_id;
    }

    public String getAll_cate_name() {
        return all_cate_name;
    }

    public void setAll_cate_name(String all_cate_name) {
        this.all_cate_name = all_cate_name;
    }

    public String getP_cate_name() {
        return p_cate_name;
    }

    public void setP_cate_name(String p_cate_name) {
        this.p_cate_name = p_cate_name;
    }

    public int getP_cate_id() {
        return p_cate_id;
    }

    public void setP_cate_id(int p_cate_id) {
        this.p_cate_id = p_cate_id;
    }


    public int getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(int total_comment) {
        this.total_comment = total_comment;
    }

    public int getGood_comment() {
        return good_comment;
    }

    public void setGood_comment(int good_comment) {
        this.good_comment = good_comment;
    }

    public int getBad_comment() {
        return bad_comment;
    }

    public void setBad_comment(int bad_comment) {
        this.bad_comment = bad_comment;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getCycle_pic() {
        return cycle_pic;
    }

    public void setCycle_pic(String cycle_pic) {
        this.cycle_pic = cycle_pic;
    }

    /**
     * answer : 测试内容8884
     * question : 测试内容j2nl
     */


    private ArrayList<FaqEntity> faq;


    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
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

    public String getFahuo_time_type() {
        return fahuo_time_type;
    }

    public void setFahuo_time_type(String fahuo_time_type) {
        this.fahuo_time_type = fahuo_time_type;
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

    public int getSeller_user_id() {
        return seller_user_id;
    }

    public void setSeller_user_id(int seller_user_id) {
        this.seller_user_id = seller_user_id;
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

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<FaqEntity> getFaq() {
        return faq;
    }

    public void setFaq(ArrayList<FaqEntity> faq) {
        this.faq = faq;
    }

    public static class FaqEntity implements Serializable{
        private String answer;
        private String question;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
