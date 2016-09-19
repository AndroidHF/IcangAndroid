package com.buycolle.aicang.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.bean.PushMessageEntity;
import com.buycolle.aicang.ui.activity.SplashActivity;
import com.buycolle.aicang.ui.activity.SubjectActivity;
import com.buycolle.aicang.util.ForegroundUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by joe on 16/1/3.
 */
public class MyPushReceiver extends BroadcastReceiver {

    private static final String TAG = "极光推送";
    private String taskId = "";
    private String type = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//            if(!AppConfig.getAppConfig(context).getString(AppConfig.JPUSH_CLIENT_ID_KEY).equals(regId)){//防止token变化提交
//                AppConfig.getAppConfig(context).set(AppConfig.UPDATE_PHONE_INFO_TOSERVER_KEY,false);
//                AppConfig.getAppConfig(context).set(AppConfig.JPUSH_CLIENT_ID_KEY,regId);
//            }
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            Bundle _bundle = intent.getExtras();
            String s = _bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject obj = new JSONObject(s);
                taskId = obj.getString("key_id");
                Log.i("推送taskID",taskId);
                type = obj.getString("type");
                Log.i("推送type ---",type);
            }catch(JSONException e){

            }
            // 在这里可以自己写代码去定义用户点击后的行为
            Bundle bundle2 = new Bundle();
            bundle2.putString("taskId", taskId);
            bundle2.putString("type",type);
            if("1".equals(type)){
                Intent i = new Intent(context, SubjectActivity.class);  //自定义打开的界面
                i.putExtras(bundle2);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else {
                Intent intent1 = new Intent(context, SplashActivity.class);  //自定义打开的界面
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            Log.i("推送type--------",bundle.getInt("type")+"");
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void processCustomMessage(String content) {
        PushMessageEntity pushMessageEntity = new Gson().fromJson(content,PushMessageEntity.class);
        MainApplication.getInstance().pushNotificationHelper.sendNotification(pushMessageEntity, ForegroundUtil.get(MainApplication.getInstance()).isForeground());
    }
}
