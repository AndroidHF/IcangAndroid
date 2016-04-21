package com.example.hufeng.com.example.hufeng.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hufeng.com.example.hufeng.beans.UserBean;

/**
 * Created by hufeng on 2016/4/18.
 */
public class LoginConfig {
    public final static String APP_MAIN_CONFIG = "appconfig";
    //首页选中的模块
    public final static String KEY_APP_MODLE = "key_app_modle";
    public final static String APP_USER_CONFIG = "userconfig";
    public final static String KEY_USER_ENT_ID = "key_user_ent_id";
    public final static String KEY_USER_USER_ID = "key_user_user_id";
    public final static String KEY_USER_USER_LEVEL = "key_user_user_level";
    public final static String KEY_USER_USER_LOGIN_NAME = "key_user_user_login_name";
    public final static String KEY_USER_USER_USER_NAME = "key_user_user_user_name";
    public final static String KEY_USER_USER_USER_NICK = "key_user_user_user_nick";
    public final static String KEY_USER_USER_USER_TYPE = "key_user_user_user_type";
    public final static String KEY_USER_USER_USER_PHONE = "key_user_user_user_phone";
    public final static String KEY_USER_USER_SESSIONID = "key_user_user_sessionid";
    public final static String KEY_USER_USER_EMAIL = "key_user_user_email";
    public final static String KEY_USER_USER_AVATAR = "key_user_user_avatar";
    public final static String KEY_USER_PERSON_TIP = "key_user_person_tip";
    public final static String KEY_USER_MAX_DEEP = "key_user_max_deep";
    public final static String KEY_USER_USER_CODE = "key_user_user_code";
    public final static String KEY_USER_ROLE_NAME = "key_user_role_name";
    public final static String KEY_USER_ROLE_ID = "key_user_role_id";
    public final static String KEY_USER_ROLE_CODE = "key_user_role_code";
    public final static String KEY_USER_ALIPAY_CARD = "key_user_alipay_card";
    public final static String KEY_USER_IDENTITY_CARD = "key_user_identity_card";
    public final static String KEY_USER_SEX = "key_user_sex";
    public final static String KEY_USER_IS_LOGIN = "key_user_is_login";
    public final static String KEY_USER_PSW = "key_user_psw";

    /**
     * 设置个人信息
     * @param context
     * @param userinfo
     */
    public static void setUserInfo(Context context,UserBean userinfo){
        if (userinfo == null)
            return;

        SharedPreferences userSP = context.getSharedPreferences(APP_USER_CONFIG,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();

        editor.putInt(KEY_USER_ENT_ID,userinfo.getEnt_id());
        editor.putInt(KEY_USER_USER_ID,userinfo.getUser_id());
        editor.putInt(KEY_USER_USER_LEVEL,userinfo.getUser_level());
        editor.putInt(KEY_USER_USER_USER_TYPE,userinfo.getUser_type());
        editor.putString(KEY_USER_USER_LOGIN_NAME, userinfo.getUser_login_name());
        editor.putString(KEY_USER_USER_USER_NAME, userinfo.getUser_name());
        editor.putString(KEY_USER_USER_USER_NICK, userinfo.getUser_nick());
        editor.putString(KEY_USER_USER_USER_PHONE, userinfo.getUser_phone());
        editor.putString(KEY_USER_USER_SESSIONID,userinfo.getSessionid());
        editor.putString(KEY_USER_USER_EMAIL,userinfo.getEmail());
        editor.putString(KEY_USER_USER_AVATAR, userinfo.getUser_avatar());
        editor.putString(KEY_USER_PERSON_TIP, userinfo.getPerson_tip());
        editor.putString(KEY_USER_MAX_DEEP,userinfo.getMax_deep());
        editor.putString(KEY_USER_USER_CODE,userinfo.getUser_code());
        editor.putString(KEY_USER_ROLE_NAME,userinfo.getRole_name());
        editor.putString(KEY_USER_ROLE_ID,userinfo.getRole_id());
        editor.putString(KEY_USER_ROLE_CODE,userinfo.getRole_code());

        editor.putBoolean(KEY_USER_IS_LOGIN,true);

        editor.commit();
    }

    /**
     * 更新用户信息中的个人中心
     * @param context
     * @param userinfo
     */
    public static void updateUserInfoByUserCenter(Context context,UserBean userinfo){
        if (userinfo == null)
            return;
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();

        editor.putString(KEY_USER_ALIPAY_CARD,userinfo.getAlipay_card());
        editor.putString(KEY_USER_USER_AVATAR,userinfo.getUser_avatar());
        editor.putString(KEY_USER_IDENTITY_CARD,userinfo.getIdentity_card());
        editor.putInt(KEY_USER_SEX,userinfo.getSex());

        editor.commit();
    }

    /**
     * 更新用户信息中的性别
     * @param context
     * @param sex
     */
    public static void updateUserInfoSex(Context context,int sex){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(KEY_USER_SEX,sex);
        editor.commit();
    }

    /**
     * 更新主界面按钮
     * @param context
     * @param sex
     */
    public static void updateHomeMenu(Context context,String sex){
        SharedPreferences userSp = context.getSharedPreferences(APP_MAIN_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_SEX, sex);
        editor.commit();
    }


    public static String getHomeMenu(Context context){
        SharedPreferences userSp = context.getSharedPreferences(APP_MAIN_CONFIG,Context.MODE_PRIVATE);
        return userSp.getString(KEY_APP_MODLE,"个性化，全部");
    }

    /**
     * 用户的登录状态
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        return userSp.getBoolean(KEY_USER_IS_LOGIN, false);
    }


    /**
     * 获取本地保存的用户的信息
     * @param context
     * @return
     */
    public static UserBean getUserInfo(Context context){
        UserBean userBean = new UserBean();
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        userBean.setEnt_id(userSp.getInt(KEY_USER_ENT_ID,0));
        userBean.setUser_id(userSp.getInt(KEY_USER_USER_ID, 0));
        userBean.setUser_level(userSp.getInt(KEY_USER_USER_LEVEL, 0));
        userBean.setUser_type(userSp.getInt(KEY_USER_USER_USER_TYPE, 0));
        userBean.setUser_login_name(userSp.getString(KEY_USER_USER_LOGIN_NAME, ""));
        userBean.setUser_name(userSp.getString(KEY_USER_USER_USER_NAME, ""));
        userBean.setUser_nick(userSp.getString(KEY_USER_USER_USER_NICK, ""));
        userBean.setUser_phone(userSp.getString(KEY_USER_USER_USER_PHONE, ""));
        userBean.setSessionid(userSp.getString(KEY_USER_USER_SESSIONID,""));

        return userBean;
    }

    /**
     * 更新个性化信息
     * @param context
     * @param gexing
     */
    public static void updateUserInfoGeXing(Context context,String gexing){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_PERSON_TIP, gexing);
        editor.commit();
    }

    /**
     * 获取个性化信息
     * @param context
     * @return
     */
    public static String getUserInfoGeXing(Context context){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_PERSON_TIP,"");
    }

    /**
     * 更新Email信息
     * @param context
     * @param gexing
     */
    public static void updateUserInfoEmail(Context context,String email){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_USER_EMAIL, email);
        editor.commit();
    }

    /**
     * 获取Email信息
     * @param context
     * @return
     */
    public static String getUserInfoEmail(Context context){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_USER_EMAIL,"");
    }


    /**
     * 更新支付宝信息
     * @param context
     * @param gexing
     */
    public static void updateUserInfoZhiFuBao(Context context,String zhifubao){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_ALIPAY_CARD, zhifubao);
        editor.commit();
    }

    /**
     * 获取支付宝信息
     * @param context
     * @return
     */
    public static String getUserInfoZhiFuBao(Context context){
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG,Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_ALIPAY_CARD,"");
    }

}
