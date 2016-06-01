package com.buycolle.aicang.util.superlog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joe on 16/1/15.
 */
public class JSONUtil {

    private static final String SUCCESS_RETURN_CODE = "1";

    /**
     * 服务器状态是否正确
     *
     * @param jsonObject
     * @return
     */
    public static boolean isOK(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("status").equals(SUCCESS_RETURN_CODE)) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isHasData(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("total").equals("0")) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isCanLoadMore(JSONObject jsonObject) {
        try {
            if (Integer.valueOf(jsonObject.getString("remain_page")) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 服务器返回的信息
     *
     * @param jsonObject
     * @return
     */
    public static String getServerMessage(JSONObject jsonObject) {
        try {
            return jsonObject.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
