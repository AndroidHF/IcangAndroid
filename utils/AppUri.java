package com.example.hufeng.com.example.hufeng.utils;

/**
 * Created by hufeng on 2016/4/20.
 */
public class AppUri {
    public static final String BASE_URL = "http://120.24.70.253:6080/paimais/";
    public static final String FILEUPLAOD = BASE_URL+"fileuplaod.up";

    //获取短信验证码
    public static final String LOGIN_GETPHONECHECKCODEBYORDINARYUSER = BASE_URL+"login_getPhoneCheckCodeByOrdinaryUser.action";
    //app注册
    public static final String LOGIN_REGISTER2ORDINARYUSER = BASE_URL+"login_register2OrdinaryUser.action";
    //app登录
    public static final String LOGIN_BYAPPORDINARYUSER = BASE_URL+"login_byAppOrdinaryUser.action";
    //忘记密码
    private static final String LOGIN_FORGETLOGINPWD = BASE_URL+"login_forgetLoginPwd.action";
    //拍品商品的状态
    private static final String DIRTIONARY_GETPRODUCTSTATUSLISTBYAPP = BASE_URL+"dirtionary_getProductStatusListByApp.action";
    //拍品违反类型
    public static final String DIRTIONARY_GETPRODUCTWFTYPELISTBYAPP = BASE_URL+"dirtionary_getProductWfTypeListByApp.action";
    //热门搜索
    public static final String DIRTIONARY_GETHOTSEARCHLISTBYAPP = BASE_URL+"dirtionary_getHotSearchListByApp.action";
    //获取拍品一级分类
    public static final String PRODUCTCATEGORY_GETTOPLISTBYAPP = BASE_URL+"productCategory_getTopListByApp.action";
    //获取拍品下级分类
    public static final String PRODUCTCATEGORY_GETCHILDLISTBYAPP = BASE_URL+"productCategory_getChildListByApp.action";
    //获取个人信息
    public static final String APPUSER_GETINFOSBYAPP = BASE_URL+"appUser_getInfosByApp.action";
    //修改个人信息
    public static final String APPUSER_GETUPDATEINFOSBYAPP = BASE_URL+"appUser_getUpdateInfosByApp.action";
    //申请成为卖家
}
