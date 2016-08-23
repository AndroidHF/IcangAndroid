package com.buycolle.aicang;

import android.content.Context;
import android.content.SharedPreferences;

import com.buycolle.aicang.bean.UserBean;

/**
 * Created by joe on 16/3/18.
 */
public class LoginConfig {

    public final static String APP_MAIN_CONFIG = "appconfig";

    public final static String KEY_APP_MODLE = "key_app_modle";//首页选中的模块


    public final static String APP_USER_CONFIG = "userconfig";

    public static final String KEY_USER_ENT_ID = "key_user_ent_id";
    public static final String KEY_USER_USER_ID = "key_user_user_id";
    public static final String KEY_USER_USER_LEVEL = "key_user_user_level";
    public static final String KEY_USER_USER_LOGIN_NAME = "key_user_user_login_name";
    public static final String KEY_USER_USER_USER_NAME = "key_user_user_user_name";
    public static final String KEY_USER_USER_USER_NICK = "key_user_user_user_nick";
    public static final String KEY_USER_USER_USER_TYPE = "key_user_user_user_type";
    public static final String KEY_USER_USER_USER_PHONE = "key_user_user_user_phone";
    public static final String KEY_USER_USER_SESSIONID = "key_user_user_sessionid";

    public static final String KEY_USER_USER_EMAIL = "key_user_user_email";
    public static final String KEY_USER_USER_AVATAR = "key_user_user_avatar";
    public static final String KEY_USER_PERSON_TIP = "key_user_person_tip";
    public static final String KEY_USER_MAX_DEEP = "key_user_max_deep";
    public static final String KEY_USER_USER_CODE = "key_user_user_code";
    public static final String KEY_USER_ROLE_NAME = "key_user_role_name";
    public static final String KEY_USER_ROLE_ID = "key_user_role_id";
    public static final String KEY_USER_ROLE_CODE = "key_user_role_code";

    public static final String KEY_USER_ALIPAY_CARD = "key_user_alipay_card";
    public static final String KEY_USER_IDENTITY_CARD = "key_user_identity_card";
    public static final String KEY_USER_SEX = "key_user_sex";


    public static final String KEY_USER_IS_LOGIN = "key_user_is_login";
    public static final String KEY_USER_PSW = "key_user_psw";


    public static void setUserInfo(Context context, UserBean userInfo) {
        if (userInfo == null)
            return;
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();

        editor.putInt(KEY_USER_ENT_ID, userInfo.getEnt_id());
        editor.putInt(KEY_USER_USER_ID, userInfo.getUser_id());
        editor.putInt(KEY_USER_USER_LEVEL, userInfo.getUser_level());
        editor.putInt(KEY_USER_USER_USER_TYPE, userInfo.getUser_type());
        editor.putString(KEY_USER_USER_LOGIN_NAME, userInfo.getUser_login_name());
        editor.putString(KEY_USER_USER_USER_NAME, userInfo.getUser_name());
        editor.putString(KEY_USER_USER_USER_NICK, userInfo.getUser_nick());
        editor.putString(KEY_USER_USER_USER_PHONE, userInfo.getUser_phone());
        editor.putString(KEY_USER_USER_SESSIONID, userInfo.getSessionid());

        editor.putString(KEY_USER_USER_EMAIL, userInfo.getEmail());
        editor.putString(KEY_USER_USER_AVATAR, userInfo.getUser_avatar());
        editor.putString(KEY_USER_PERSON_TIP, userInfo.getPerson_tip());
        editor.putString(KEY_USER_MAX_DEEP, userInfo.getMax_deep());
        editor.putString(KEY_USER_USER_CODE, userInfo.getUser_code());
        editor.putString(KEY_USER_ROLE_NAME, userInfo.getRole_name());
        editor.putString(KEY_USER_ROLE_ID, userInfo.getRole_id());
        editor.putString(KEY_USER_ROLE_CODE, userInfo.getRole_code());

        editor.putBoolean(KEY_USER_IS_LOGIN, true);
        editor.commit();
    }

    public static void updateUserInfoByUserCenter(Context context, UserBean userInfo) {
        if (userInfo == null)
            return;
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_ALIPAY_CARD, userInfo.getAlipay_card());
        editor.putString(KEY_USER_USER_AVATAR, userInfo.getUser_avatar());
        editor.putString(KEY_USER_IDENTITY_CARD, userInfo.getIdentity_card());
        editor.putInt(KEY_USER_SEX, userInfo.getSex());
        editor.commit();
    }

    public static void updateUserInfoSex(Context context, int sex) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(KEY_USER_SEX, sex);
        editor.commit();
    }

    public static void updateHomeMenu(Context context, String sex) {
        SharedPreferences userSp = context.getSharedPreferences(APP_MAIN_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_APP_MODLE, sex);
        editor.commit();
    }

    public static String getHomeMenu(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_MAIN_CONFIG, Context.MODE_PRIVATE);
        //return userSp.getString(KEY_APP_MODLE, "手办、模型,全部,周边,漫画,书籍,BD、DVD,游戏,音乐、演出,服装、COS,其他");
        return userSp.getString(KEY_APP_MODLE, "个性化,全部");
    }


    /**
     * 用户的登录状态
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getBoolean(KEY_USER_IS_LOGIN, false);
    }

    /**
     * 获取本地保存的用户信息
     *
     * @param context
     * @return
     */
    public static UserBean getUserInfo(Context context) {
        UserBean userBean = new UserBean();
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        userBean.setEnt_id(userSp.getInt(KEY_USER_ENT_ID, 0));
        userBean.setUser_id(userSp.getInt(KEY_USER_USER_ID, 0));
        userBean.setUser_level(userSp.getInt(KEY_USER_USER_LEVEL, 0));
        userBean.setUser_type(userSp.getInt(KEY_USER_USER_USER_TYPE, 0));
        userBean.setUser_login_name(userSp.getString(KEY_USER_USER_LOGIN_NAME, ""));
        userBean.setUser_name(userSp.getString(KEY_USER_USER_USER_NAME, ""));
        userBean.setUser_nick(userSp.getString(KEY_USER_USER_USER_NICK, ""));
        userBean.setUser_phone(userSp.getString(KEY_USER_USER_USER_PHONE, ""));
        userBean.setSessionid(userSp.getString(KEY_USER_USER_SESSIONID, ""));

        userBean.setEmail(userSp.getString(KEY_USER_USER_EMAIL, ""));
        userBean.setUser_avatar(userSp.getString(KEY_USER_USER_AVATAR, ""));
        userBean.setPerson_tip(userSp.getString(KEY_USER_PERSON_TIP, ""));
        userBean.setMax_deep(userSp.getString(KEY_USER_MAX_DEEP, ""));
        userBean.setUser_code(userSp.getString(KEY_USER_USER_CODE, ""));
        userBean.setRole_name(userSp.getString(KEY_USER_ROLE_NAME, ""));
        userBean.setRole_id(userSp.getString(KEY_USER_ROLE_ID, ""));
        userBean.setRole_code(userSp.getString(KEY_USER_ROLE_CODE, ""));

        userBean.setSex(userSp.getInt(KEY_USER_SEX, 0));
        userBean.setAlipay_card(userSp.getString(KEY_USER_ALIPAY_CARD, ""));
        userBean.setIdentity_card(userSp.getString(KEY_USER_IDENTITY_CARD, ""));

        return userBean;
    }


    public static void updateUserInfoGeXing(Context context, String gexing) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_PERSON_TIP, gexing);
        editor.commit();
    }

    public static String getUserInfoGeXing(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_PERSON_TIP, "");
    }

    public static void updateUserInfoEmail(Context context, String email) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_USER_EMAIL, email);
        editor.commit();
    }

    public static String getUserInfoEmail(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_USER_EMAIL, "");
    }

    public static void updateUserInfoZhifuBao(Context context, String zhifubao) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_ALIPAY_CARD, zhifubao);
        editor.commit();
    }
    public static void updateUserType(Context context, int type) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(KEY_USER_USER_USER_TYPE, type);
        editor.commit();
    }

    public static String getUserInfoZhifuBao(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_ALIPAY_CARD, "");
    }

    public static void updateUserInfoImage(Context context, String image) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_USER_AVATAR, image);
        editor.commit();
    }

    public static String getUserInfoImage(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_USER_AVATAR, "");
    }

    public static void updateUserInfoPassWord(Context context, String image) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KEY_USER_PSW, image);
        editor.commit();
    }

    public static String getUserInfoPassWord(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        return userSp.getString(KEY_USER_PSW, "");
    }


    public static void clear(Context context) {
        SharedPreferences userSp = context.getSharedPreferences(APP_USER_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.clear();
        editor.commit();
    }
}
