/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buycolle.aicang.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.sina.weibo.sdk.utils.LogUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	private static final String TAG = "CommonUtils";


    /**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

    /**
     * 开启activity(带参数)
     */
    public static void launchActivity(Context context, Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }


    /** 通过Uri获取图片路径 */
    public static String query(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
        cursor.moveToNext();
        return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
    }


    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
	
	
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

    public static int dp2Px(Context context,int dp){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        int px=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,dm);
        return px;
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        }catch (Exception e){
            e.printStackTrace();

        }
        return 0;
    }
    public static int getScreenHeight(Context context){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.heightPixels;
        }catch (Exception e){
            e.printStackTrace();

        }
        return 0;
    }


    /**
     * 获取设备信息
     */
    public static String getDeviceInfo(){
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        if(myDevice!=null){
            String deviceName = myDevice.getName();
            LogUtil.i(TAG, "device:" + myDevice.getName());
            return deviceName;
        }else{
            return android.os.Build.MODEL;
        }
    }

    /**
     * 获取手机的IMEI号，需要权限android:name="android.permission.READ_PHONE_STATE"
     * @param context
     * @return
     */
    public String getImei(Context context){
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();
        return  imei;
    }

    /**
     * 检查密码是否符合要求
     *
     * @param pw 输入的密码
     * @return 符合要求则返回true
     */
    public static boolean checkPw(String pw) {
        if (pw.length() < 6) {
            return false;
        }
        String[] rex = new String[]{"[A-Za-z]+$", "[0-9]+$"};
        for (int i = 0; i < rex.length; i++) {
            Pattern pattern = Pattern.compile(rex[i]);
            Matcher matcher = pattern.matcher(pw);
            if (matcher.matches()) {
                return false;
            }
        }

        return true;
    }
}
