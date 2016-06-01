package com.buycolle.aicang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 配置管理类
 */
public class AppConfig {

    /**
     * 默认配置文件名称
     */
    public final static String APP_CONFIG = "config_pm";
    public final static String APP_CONFIG_GUIDE = "app_config_guide";//是否第一次使用，显示导航 默认是false
    public final static String APP_CONFIG_KEYBORD_WH = "app_config_keybord_wh";//记录键盘高度


    private Context mContext;
    private static AppConfig appConfig;

    /**
     * 获得AppConfig实例
     *
     * @param context
     * @return
     */
    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
    }

    /**
     * 获得key对应配置信息
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return getSharedPreferences(mContext).getString(key, "");
    }

    /**
     * 获得key对应配置信息
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return getSharedPreferences(mContext).getBoolean(key, false);
    }

    public boolean getBooleanByTrueDefault(String key) {
        return getSharedPreferences(mContext).getBoolean(key, true);
    }

    /**
     * 获得key对应配置信息
     *
     * @param key
     * @return
     */
    public int getInteger(String key) {
        return getSharedPreferences(mContext).getInt(key, -1);
    }

    /**
     * 设置配置信息
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Editor editor = getSharedPreferences(mContext).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 设置配置信息
     *
     * @param key
     * @param value
     */
    public void set(String key, boolean value) {
        Editor editor = getSharedPreferences(mContext).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 设置配置信息
     *
     * @param key
     * @param value
     */
    public void set(String key, int value) {
        Editor editor = getSharedPreferences(mContext).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setPhoneKeyBordHeight(int value) {
        Editor editor = getSharedPreferences(mContext).edit();
        editor.putInt(APP_CONFIG_KEYBORD_WH, value);
        editor.commit();
    }

    public int getPhoneKeyBordHeight() {
        return getSharedPreferences(mContext).getInt(APP_CONFIG_KEYBORD_WH, -1);
    }

    public void set(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entry = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entry.iterator();
        Editor editor = getSharedPreferences(mContext).edit();
        for (; iterator.hasNext(); ) {
            Map.Entry<String, Object> nextEntry = iterator.next();
            Object obj = nextEntry.getValue();
            String key = nextEntry.getKey();
            if (obj instanceof Integer) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Float) {
                editor.putFloat(key, (float) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else if (obj instanceof String) {
                editor.putString(key, (String) obj);
            } else if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof Double) {
                editor.putString(key, String.valueOf(obj));
            }

        }
        editor.commit();
    }


    /**
     * 移除多个配置信息
     *
     * @param keys
     */
    public void remove(String... keys) {
        Editor editor = getSharedPreferences(mContext).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }
}
