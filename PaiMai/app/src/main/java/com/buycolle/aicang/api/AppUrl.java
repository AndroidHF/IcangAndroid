package com.buycolle.aicang.api;

/**
 * Created by joe on 15/12/30.
 */
public class AppUrl {
    //public static final String BASE_URL = "http://139.196.229.110/paimai/";//本帮环境
    public static final String BASE_URL = "http://139.196.195.196/paimaitest/";//测试url
    public static final String FILEUPLAOD = BASE_URL + "fileuplaod.up";


    //获取短信验证码
    public static final String LOGIN_GETPHONECHECKCODEBYORDINARYUSER = BASE_URL + "login_getPhoneCheckCodeByOrdinaryUser.action";
    //app注册
    public static final String LOGIN_REGISTER2ORDINARYUSER = BASE_URL + "login_register2OrdinaryUser.action";
    //app登录
    public static final String LOGIN_BYAPPORDINARYUSER = BASE_URL + "login_byAppOrdinaryUser.action";
    //忘记密码
    public static final String LOGIN_FORGETLOGINPWD = BASE_URL + "login_forgetLoginPwd.action";
    //拍品商品状态
    public static final String DIRTIONARY_GETPRODUCTSTATUSLISTBYAPP = BASE_URL + "dirtionary_getProductStatusListByAPP.action";
    //拍品违反类型
    public static final String DIRTIONARY_GETPRODUCTWFTYPELISTBYAPP = BASE_URL + "dirtionary_getProductWfTypeListByApp.action";
    //热门搜索
    public static final String DIRTIONARY_GETHOTSEARCHLISTBYAPP = BASE_URL + "dirtionary_getHotSearchListByApp.action";
    //获取拍品一级分类
    public static final String PRODUCTCATEGORY_GETTOPLISTBYAPP = BASE_URL + "productCategory_getTopListByApp.action";
    //获取拍品下级分类
    public static final String PRODUCTCATEGORY_GETCHILDLISTBYAPP = BASE_URL + "productCategory_getChildListByApp.action";
    // 获取个人信息
    public static final String APPUSER_GETINFOSBYAPP = BASE_URL + "appUser_getInfosByApp.action";
    //  修改个人信息
    public static final String APPUSER_UPDATEINFOSBYAPP = BASE_URL + "appUser_updateInfosByApp.action";
    //  申请成为卖家
    public static final String APPUSER_SUBMITSUPPLYSELLERBYAPP = BASE_URL + "appUser_submitSupplySellerByApp.action";
    //   退出
    public static final String LOGIN_LOGOUTPLAT = BASE_URL + "login_logOutPlat.action";
    //    发布拍品
    public static final String PRODUCT_SUBMITBYAPP = BASE_URL + "product_submitByApp.action";


    //**********我卖出去的 begin***************

    //     审核状态
    public static final String PRODUCT_GETCENTERCHECKLISTBYAPP = BASE_URL + "product_getCenterCheckListByApp.action";
    //     拍卖中
    public static final String PRODUCT_GETCENTERINGLISTBYAPP = BASE_URL + "product_getCenterIngListByApp.action";
    //      已卖出
    public static final String PRODUCT_GETCENTERENDLISTBYAPP = BASE_URL + "product_getCenterEndListByApp.action";
    //      流拍
    public static final String PRODUCT_GETCENTERENDFAILLISTBYAPP = BASE_URL + "product_getCenterEndFailListByApp.action";
    //     取消拍卖
    public static final String PRODUCT_UNDO = BASE_URL + "product_undo.action";
    //      获取快递公司列表
    public static final String EXPRESS_GETLISTBYAPP = BASE_URL + "express_getListByApp.action";
    //       提交发货信息
    public static final String WINPRODUCTORDER_SUBMITEXPRESSINFOSBYSELLERAPP = BASE_URL + "winProductOrder_submitExpressInfosBySellerApp.action";


    //**********我卖出去的 end***************


    //**********我买到的 begin***************

    //     我买到的--拍卖中
    public static final String PRODUCT_GETSJOININGLISTBYAPP = BASE_URL + "product_getSJoinIngListByApp.action";
    //     我买到的-- 已落拍
    public static final String PRODUCT_GETSJOINSUCCLISTBYAPP = BASE_URL + "product_getSJoinSuccListByApp.action";
    //     我买到的--  未拍到
    public static final String PRODUCT_GETSJOINFAILLISTBYAPP = BASE_URL + "product_getSJoinFailListByApp.action";


    //**********我卖出去的 end***************


    //**********首页的 begin ***************

    // 顶部广告--已经放弃
    public static final String TOPRECOM_GETRECOMLIST = BASE_URL + "topRecom_getRecomList.action";
    // 顶部广告
    public static final String APPBANNER_GETLISTBYAPP = BASE_URL + "appBanner_getListByApp.action";
    // 拍卖会顶部广告
    public static final String APPBANNER_GETCAROUSELPHOTOBYAPP = BASE_URL + "appBanner_getCarouselPhotoByApp.action";
    // 首页拍品列表
    public static final String PRODUCT_GETNORMALLISTBYAPP = BASE_URL + "product_getNormalListByAPP.action";
    //**********首页的 end ***************

    //  拍品详情
    public static final String PRODUCT_GETPREAUCTDETAILIDBYAPP = BASE_URL + "product_getPreAuctDetailIdByApp.action";
    //  竞拍
    public static final String PRODUCTJPFOLLOW_SUBMITBYAPP = BASE_URL + "productJPFollow_submitByApp.action";

    //**********Event 的 begin ***************

    //竞拍--正在拍卖
    public static final String PRODUCT_GETAUCTINGLISTBYAPP = BASE_URL + "product_getAuctIngListByApp.action";
    //竞拍--即将开始
    public static final String PRODUCT_GETAUCTREADYLISTBYAPP = BASE_URL + "product_getAuctReadyListByApp.action";
    //竞拍--已结束
    public static final String PRODUCT_GETAUCTENDLISTBYAPP = BASE_URL + "product_getAuctEndListByApp.action";
    //**********Event 的 end ***************

    //举报拍品
    public static final String PROUDCTREPORT_SUBMITBYAPP = BASE_URL + "productReport_submitByApp.action";
    // 获取个人收货地址信息
    public static final String USERRECEIPT_GETLISTBYAPP = BASE_URL + "userReceipt_getListByApp.action";
    //  修改收货地址
    public static final String USERRECEIPT_SAVEORUPDATE = BASE_URL + "userReceipt_saveOrUpdate.action";

    //**********文档类 的 begin ***************

    //个人中心--公告
    public static final String PUBLICNOTICE_GETLISTBYAPP = BASE_URL + "publicNotice_getListByApp.action";
    // 交易流程文章
    public static final String COPYWRITER_GETJIAOYILCBYAPP = BASE_URL + "copyWriter_getJiaoyilcByApp.action";
    //  使用帮助说明
    public static final String WRITER_GETUSEHELPBYAPP = BASE_URL + "copyWriter_getUsehelpByApp.action";
    //   关于我们
    public static final String COPYWRITER_GETABOUTUSBYAPP = BASE_URL + "copyWriter_getAboutusByApp.action";
    //    发货指导
    public static final String WRITER_GETFAHUOZDBYAPP = BASE_URL + "copyWriter_getFahuozdByApp.action";
    //    成为卖家--常见问题
    public static final String COMMONQUESTION_GETSELLERLISTBYAPP = BASE_URL + "commonQuestion_getSellerListByApp.action";
    //    拍卖协议（竞拍规则）
    public static final String WRITER_GETPAIMAIXYBYAPP = BASE_URL + "copyWriter_getPaimaixyByApp.action";
    //    拍卖详情--常见问题
    public static final String COMMONQUESTION_GETPAIPINGLISTBYAPP = BASE_URL + "commonQuestion_getPaiPingListByApp.action";
    //     注册协议
    public static final String WRITER_GETREGISTERBYAPP = BASE_URL + "copyWriter_getRegisterByApp.action";
    // 用户协议
    public static final String WRITER_GETUSERAGREERBYAPP = BASE_URL + "copyWriter_getUseragreerByApp.action";
    //  身份证示例图像
    public static final String WRITER_GETIDENTITYCARDMODELBYAPP = BASE_URL + "copyWriter_getIdentitycardmodelByApp.action";

    //**********文档类 的 end ***************


    // 保存为草稿提交晒物
    public static final String SHOW_SUBMITTOREADYBYAPP = BASE_URL + "show_submitToReadyByApp.action";

    //  发布晒物
    public static final String SHOW_SUBMITBYAPP = BASE_URL + "show_submitByApp.action";
    //  重新发布晒物
    public static final String SHOW_RESUBMITBYAPP = BASE_URL + "show_reSubmitByApp.action";
    //  我的晒物--通过列表
    public static final String SHOW_GETOWNERCENTERSUCCLISTBYAPP = BASE_URL + "show_getOwnerCenterSuccListByApp.action";
    //  我的晒物--未通过列表
    public static final String SHOW_GETOWNERCENTERFAILLISTBYAPP = BASE_URL + "show_getOwnerCenterFailListByApp.action";
    //  我的晒物--草稿列表
    public static final String SHOW_GETOWNERCENTERREADYLISTBYAPP = BASE_URL + "show_getOwnerCenterReadyListByApp.action";
    //  首页晒物列表
    public static final String SHOW_GETLISTBYAPP = BASE_URL + "show_getListByApp.action";
    //  点赞/取消晒物
    public static final String COMMONNOTE_CLICKZSHARE = BASE_URL + "commonNote_clickZShare.action";
    //    获取该拍品的问与答
    public static final String PRODUCT_GETCURANDTOTALCOMMENTINFOSBYAPP = BASE_URL + "product_getCurAndTotalCommentInfosByApp.action";
    //     提交问题或回答
    public static final String COMMONCOMMENT_SUBMITFORPRODUCTBYAPP = BASE_URL + "commonComment_submitForProductByApp.action";
    //      我的问询
    public static final String COMMONCOMMENT_GETSELFSENDLISTBYAPP = BASE_URL + "commonComment_getSelfSendListByApp.action";
    //       我收到的问询
    public static final String COMMONCOMMENT_GETSELFRECEIVELISTBYAPP = BASE_URL + "commonComment_getSelfReceiveListByApp.action";
    //       根据ID获取详情
    public static final String SHOW_GETDETAILIDBYAPP = BASE_URL + "show_getDetailIdByApp.action";
    //        提交问题或回答
    public static final String COMMONCOMMENT_SUBMITFORSHOWBYAPP = BASE_URL + "commonComment_submitForShowByApp.action";
    //         提交意见反馈
    public static final String OPINION_SUBMITBYAPP = BASE_URL + "opinion_submitByApp.action";
    //          获取推送设置
    public static final String USERPUSHCFG_GETBYAPP = BASE_URL + "userPushCfg_getByApp.action";
    //           设置推送配置
    public static final String USERPUSHCFG_SETBYAPP = BASE_URL + "userPushCfg_setByApp.action";

    //收藏/关注拍品
    public static final String COMMONNOTE_COLPRODUCT = BASE_URL + "commonNote_colProduct.action";
    //收藏/关注拍品 --拍卖会的
    public static final String COMMONNOTE_PRPRODUCT = BASE_URL + "commonNote_prProduct.action";

    //收藏/关注取消卖家
    public static final String COMMONNOTE_COLSELLER = BASE_URL + "commonNote_colSeller.action";

    //收藏/关注取消晒物
    public static final String COMMONNOTE_COLSHARE = BASE_URL + "commonNote_colShare.action";

    //用户关注拍品的列表
    public static final String PRODUCT_GETCOLLISTBYAPP = BASE_URL + "product_getColListByApp.action";
    //我关注的晒物列表
    public static final String SHOW_GETCOLLISTBYAPP = BASE_URL + "show_getColListByApp.action";
    //获取关注收藏卖家列表
    public static final String APPUSER_GETCOLLISTBYAPP = BASE_URL + "appUser_getColListByApp.action";
    //更新极光推送ID
    public static final String APPUSER_UPDATEPUSHTOKEN = BASE_URL + "appUser_updatePushToken.action";

    // 去支付
    public static final String WINPRODUCTORDER_GETPREPAYINFOSBYAPP = BASE_URL + "winProductOrder_getPrePayInfosByApp.action";
    // 获取支付宝订单信息
    public static final String PAYRECORD_BUILDALIPREPAY = BASE_URL + "payRecord_buildAliPrePay.action";
    // 顶--晒物评论
    public static final String COMMONCOMMENT_TOTOPSHOWCOMMENTBYAPP = BASE_URL + "commonComment_toTopShowCommentByApp.action";
    //  获取出品人全部评价
    public static final String PRODUCTEVALUATE_GETLISTALLEVALUATE = BASE_URL + "productEvaluate_getListAllEvaluate.action";
    //   获取出品人好评评价
    public static final String PRODUCTEVALUATE_GETLISTGOODEVALUATE = BASE_URL + "productEvaluate_getListGoodEvaluate.action";
    //    获取出品人差评评价
    public static final String PRODUCTEVALUATE_GETLISTBADEVALUATE = BASE_URL + "productEvaluate_getListBadEvaluate.action";
    //     确认收货
    public static final String WINPRODUCTORDER_SURERECEIPT = BASE_URL + "winProductOrder_sureReceipt.action";
    //      提交发表评价
    public static final String PRODUCTEVALUATE_SUBMITWINORDER = BASE_URL + "productEvaluate_submitWinOrder.action";
    //       卖家发布的拍品
    public static final String PRODUCT_GETCURSELLERPUSHLISTBYAPP = BASE_URL + "product_getCurSellerPushListByApp.action";
    //        拍品竞拍记录列表
        public static final String PRODUCTJPFOLLOW_GETCURPRODUCTHISLIST = BASE_URL + "productJPFollow_getCurProductHisList.action";
    //         微信支付统一下单
        public static final String PAYRECORD_BUILDWXPREPAY = BASE_URL + "payRecord_buildWxPrePay.action";
    //         根据ID获取拍品发布信息
        public static final String PRODUCT_GETHISSUBMITINFOSBYAPP = BASE_URL + "product_getHisSubmitInfosByApp.action";
    //         获取用户的个人消费和营业额信息
        public static final String APPUSER_GETUSERCOSTINFOSBYAPP = BASE_URL + "appUser_getUserCostInfosByApp.action";
    //         根据ID获取收货物流信息
        public static final String WINPRODUCTORDER_GETRECEIPTINFOSBYAPP = BASE_URL + "winProductOrder_getReceiptInfosByApp.action";
    //          获取成为卖家banner列表
        public static final String APPBANNER_GETSELLERLISTBYAPP = BASE_URL + "appBanner_getSellerListByApp.action";
    //          获取感叹号信息
        public static final String JPUSHRECORD_GETTIPBYAPP = BASE_URL + "jPushRecord_getTipByApp.action";
    //          更新叹号信息（设置已读）
        public static final String JPUSHRECORD_UPDATETIPBYAPP = BASE_URL + "jPushRecord_updateTipByApp.action";
    //          个人中心更新感叹号（设置已读）
        public static final String JPUSHRECORD_DELETEREDTIPBYAPP = BASE_URL+"jPushRecord_deleteRedTipByApp.action";
    //          推送更新小红点
        public static final String JPUSHRECORD_UPDATEREDTIPBYAPP  =BASE_URL+"jPushRecord_updateRedTipByApp.action";
    //           获取设置了下次同样物流的物流
        public static final String SELLERFAHUO_GETSAMEFAHUO = BASE_URL + "sellerFahuo_getSameFaHuo.action";
    //            重新上架
        public static final String PRODUCT_REUPSTOREBYAPP = BASE_URL + "product_reUpStoreByApp.action";
    //             拍品拍卖结果查询
        public static final String PRODUCT_GETCURPMRESULTBYAPP = BASE_URL + "product_getCurPMResultByApp.action";
    //              修改密码
        public static final String LOGIN_UPDATELOGINPWD = BASE_URL + "login_updateLoginPwd.action";

    //add by hufeng :解锁优惠码传递数据
    //public static final String COUPON_FAILNOTIFYBYAPP = BASE_URL + "coupon_failNotifyByApp.action";

    //首页筛选排序具体筛选和排序的接口
    public static final  String DIRTIONARY_GETFILTERANDSORTLISTBYAPP = BASE_URL+"dirtionary_getFilterAndSortListByApp.action";

    public static final  String SPLASH_SHOUFA_IMAGE = "http://download.buycolle.com/android_icon/shoufa.png";
    public static final String SPLASH_360_IAMGE = "http://download.buycolle.com/android_icon/360.png";

    //获取活动界面的bannner图和底纹的图片
    public static final String APPEVENT_GETEVENTBANNERBYAPP = BASE_URL+"appEvent_getEventBannerByApp.action";

    //获取专题活动界面的商品列表

    public static final String APPEVENT_GETEVENTPRODUCTLISTBYAPP = BASE_URL+"appEvent_getEventProductListByApp.action";

    // 首页商品筛选
    public static final String GET_FILTER_AND_SORT_LIST_BY_APP = BASE_URL + "product_getMainPageListByAPP.action";

    //买卖家交互
    public static final String COMMONAFTERSALES_GETLISTBYAPP = BASE_URL + "commonAftersales_getListByApp.action";

    //提交买卖家交流信息
    public static final String COMMONAFTERSALES_SUBMITCOMMONBYAPP =BASE_URL+"commonAftersales_submitCommonByApp.action";
}
