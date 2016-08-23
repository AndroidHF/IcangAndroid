package com.buycolle.aicang;

/**
 * Created by joe on 16/3/5.
 */
public class Constans {

    public static final String zhilingPath    = "http://b.hiphotos.baidu" +
            ".com/zhidao/wh%3D450%2C600/sign=351e66ea03e939015657853a4edc78d1" +
            "/58ee3d6d55fbb2fb0ad186d84d4a20a44723dc06.jpg";
    public static final String liuyanPath     = "http://img4.duitang.com/uploads/item/201304/19/20130419230512_88hYu" +
            ".jpeg";
    public static final String canglaoshiPath = "http://img3.imgtn.bdimg.com/it/u=1452953139,3502021255&fm=21&gp=0.jpg";

    public static final int MAX_SHOW_FLOAT_BTN = 1;

    // 获取拍品的商品状态
    public static final String TAG_GOOD_STATUS = "good_status";

    // 商品筛选
    public static final String TAG_GOOD_FILTER = "good_filter";

    // 获取首页顶部广告的缓存
    public static final String TAG_HOME_TOP_ADS        = "tag_home_top_ads";
    // 获取拍卖会顶部广告的缓存
    public static final String TAG_EVENT_TOP_ADS       = "tag_event_top_ads";
    // 成为卖家顶部广告列表的缓存
    public static final String TAG_TOBE_SALLER_TOP_ADS = "tag_tobe_saller_top_ads";

    // 热门搜索 ---- 拍品
    public static final String TAG_HOT_SEARCH_GOOD  = "tag_hot_search_good";
    // 热门搜索 ---- 拍卖会
    public static final String TAG_HOT_SEARCH_EVENT = "tag_hot_search_event";
    // 热门搜索 ---- 晒物
    public static final String TAG_HOT_SEARCH_SHOW  = "tag_hot_search_show";

    // 举报 ---- 类型
    public static final String TAG_REPORT_TYPE = "tag_report_type";

    //微信支付和分享的id
    public static String APP_ID = "wx827bab983b19b81f";

    /**
     * 新浪分享需要
     */
    public static final String APP_KEY = "1684917016";

    /**
     * QQ分享需要
     */
    //"会玩"的QQ分享id
    //public static final String APP_TX_KEY = "1105173111";
    //"荟玩"的新申请的QQ分享id
    public static final String APP_TX_KEY = "1105431053";

    //public static String SHARE_URL = "http://www.baidu.com";
    /**
     * change by :胡峰
     * 分享的URL
     */
    public static String SHARE_URL = "http://www.buycolle.com";

    //分享我们的APP：
    public static String shareTitle_Type_1    = "来这里买卖手办，每月盈余还有赚，老婆老妈再也不喷我买小人了！";
    public static String shareSubTitle_Type_1 = "一块钱买手办，两块钱买模型，三块钱到手声优签名！";

    //拍品及拍卖会分享：
    public static String shareTitle_Type_2    = "手办模型一元购，会玩也会买，真品百分百！";
    public static String shareSubTitle_Type_2 = "调用拍品标题（字数限制按各家的接口来，超出用⋯表示）";

    //晒单分享：
    public static String shareTitle_Type_3    = "怎么给钱包君止血？来会玩看大大科学地剁手吧！";
    public static String shareSubTitle_Type_3 = "调用晒单标题（字数限制按各家的接口来，超出用⋯表示）";

}
