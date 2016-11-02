package com.example.hufeng.com.example.hufeng.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * json数据解析的相关工具类
 * Created by hufeng on 2016/4/5.
 */
public class JSONUtil {
    //服务器正常返回码
    private static final String SUCCESS_RETURN_CODE = "1";
    /**
     * 服务器状态是否正确
     * @param jsonObject
     * @return boolean类型:true:表示正常，false：表示不正常
     */
    public static boolean isOK(JSONObject jsonObject){
        try {
            if (jsonObject.getString("status").equals(SUCCESS_RETURN_CODE)){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析的数据是否为空
     * @param jsonObject
     * @return boolean类型：ture：表示解析的数据为空
     *                       false:表示解析的数据为空
     */
    public static boolean isHasData(JSONObject jsonObject){
        try {
            if (jsonObject.getString("total").equals("0")){
                return false;
            }else {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否加载更多的页
     * @param jsonObject
     * @return boolean 类型：true：表示可以加载更多，
     *                        false：表示不可以加载
     */
    public static boolean isCanLoadMore(JSONObject jsonObject){
        try {
            if (Integer.valueOf(jsonObject.getString("remain_page"))>0){
                return true;
            }else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取服务器返回的信息
     * @param jsonObject
     * @return String:服务器的信息
     */
    public static String getServerMessage(JSONObject jsonObject){
        try {
            return jsonObject.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


}
