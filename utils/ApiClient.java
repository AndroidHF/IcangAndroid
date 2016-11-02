package com.example.hufeng.com.example.hufeng.utils;

import org.json.JSONObject;

/**
 * Created by hufeng on 2016/4/20.
 */
public class ApiClient {
    public void login_getPhoneCheckCodeByOrdinaryUser(JSONObject data,ApiCallback callback){
        post(callback,AppUri.LOGIN_GETPHONECHECKCODEBYORDINARYUSER,data,"获取短信验证码");
    }

    public interface ApiCallback{

    }

    private void post(final ApiCallback callback,String url,JSONObject jsonObject,final String tag){

    }
}
