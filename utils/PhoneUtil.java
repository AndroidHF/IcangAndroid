package com.example.hufeng.com.example.hufeng.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by hufeng on 2016/4/12.
 */
public class PhoneUtil {
    public static boolean isServiceRunning(Context context,String service){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info:infos){
            if (info.service.getClassName().equals(service)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网络是否可用
     * @param context 上下文环境
     * @return：true代表可用，网络正常，false代表网络异常
     */
    public static boolean isNetWorkAvailable(Context context){
       try {
           ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           if (connectivityManager != null){
               NetworkInfo info = connectivityManager.getActiveNetworkInfo();
               if (info != null && info.isConnected()){
                   if (info.getState()==NetworkInfo.State.CONNECTED){
                       return true;
                   }
               }
           }

       }catch (Exception e){
           return  false;
       }

        return false;
    }

    public static int dip2dx(Context context,float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context,float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    /**
     * 是否正常挂载
     * @return
     */
    public static boolean getSdCardStatus(){
        String sdcardState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(sdcardState)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getPhoneVersion(){
        return Build.MODEL + "";
    }

    /**
     *
     * 获取手机的品牌
     * @return
     */
    public static String getPhoneBrand(){
        return Build.BRAND + "";
    }

    /**
     * 获取电话的唯一id
     * @param context
     * @return
     */
    public static String getPhoneId(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 获取手机当前系统的版本
     * @return
     */
        public static String getPhoneSystemVersion(){
        return Build.VERSION.RELEASE;
    }
    /**
     * 获取手机当前sdk的版本
     * @return
     */
    public static String getPhoneSdkVersion(){
        return Build.VERSION.SDK_INT + "";
    }

    /**
     * 获取手机的分辨率
     * @param context
     * @return
     */
    public static String getPhonePx(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }

    /**
     * 获取当地的ip地址
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
                NetworkInterface inft =en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = inft.getInetAddresses();enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机的隐藏键盘
     * @param context
     * @param editText
     */
    public static void hideKeyBord(Context context,EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    /**
     * 跳转到打电话界面
     * @param activity
     * @param number
     */
    public static void editCallNumber(Activity activity,String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 加载数据
     * @param context
     * @param strOutFileName
     */
    public static void copyBigDataToSD(final Context context, final String strOutFileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream myInPut;
                OutputStream myOutPut = null;

                try {
                    myOutPut = new FileOutputStream(strOutFileName);
                    myInPut = context.getAssets().open("logo2.jpg");
                    byte[] buffer = new byte[1024];
                    int length = myInPut.read(buffer);
                    while (length > 0){
                        myOutPut.write(buffer,0,length);
                        length = myInPut.read(buffer);
                    }
                    myOutPut.flush();
                    myInPut.close();
                    myOutPut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 屏幕宽度的获取
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;//屏幕宽度像素
    }

    /**
     * 屏幕高度的获取
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;//屏幕高度像素

    }
}
