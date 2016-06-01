package com.buycolle.aicang.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.buycolle.aicang.AppConfig;
import com.buycolle.aicang.util.superlog.KLog;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by joe on 16/3/2.
 */
public class PhoneUtil {

    public static boolean isServiceRunning(Context context, String service) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : infos) {
            if (info.service.getClassName().equals(service)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * sdcard 是否正常挂载
     *
     * @return
     */
    public static boolean getSdCardStatus() {
        String sdcardState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sdcardState))
            return true;
        else
            return false;
    }

    /**
     * 获取手机型号（nexus 5）
     *
     * @return
     */
    public static String getPhoneVersion() {
        return android.os.Build.MODEL + "";
    }

    public static String getPhoneBrand() {
        return Build.BRAND + "";
    }

    /**
     * 获取电话的唯一id  （IMEI）
     *
     * @param context
     * @return
     */
    public static String getPhoneId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机当前的系统版本
     *
     * @return
     */
    public static String getPhoneSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机当前的sdk版本
     *
     * @return
     */
    public static int getPhoneSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机的分辨率
     *
     * @param context
     * @return
     */
    public static String getPhonePx(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }


    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * md5加密
     *
     * @param plaintext
     * @return
     */
    public static String encryptByMD5(String plaintext) {

        if (plaintext != null) {
            try {
                plaintext = plaintext.toLowerCase();
                char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plaintext.getBytes());
                byte tmp[] = md.digest();
                char str[] = new char[16 * 2];
                int k = 0;
                for (int i = 0; i < 16; i++) {
                    byte byte0 = tmp[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // >>>(无符号右移运算符),高位补0
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void hideKeyBord(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void editCallNumber(Activity activity, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void copyBigDataToSD(final Context context, final String strOutFileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream myInput;
                OutputStream myOutput = null;
                try {
                    myOutput = new FileOutputStream(strOutFileName);
                    myInput = context.getAssets().open("ic_launcher.jpg");
                    byte[] buffer = new byte[1024];
                    int length = myInput.read(buffer);
                    while (length > 0) {
                        myOutput.write(buffer, 0, length);
                        length = myInput.read(buffer);
                    }
                    myOutput.flush();
                    myInput.close();
                    myOutput.close();
                } catch (Exception e) {
                    KLog.e("报错了---", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static int getWindowWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels; // 屏幕宽度（像素）
    }

    public static int getWindowHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels; // 屏幕高度（像素）
    }

    /**
     * 保存手机键盘高度
     *
     * @param rootView
     * @param context
     */
    public static void setSoftKeyBordWH(final View rootView, final Context context) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keyboardHeight = screenHeight - (r.bottom);
                KLog.d("keyboardHeight px -- ", "" + keyboardHeight);
                if (keyboardHeight > 400) {
                    KLog.d("获取到了键盘的高度 px -- ", "" + keyboardHeight);
                    AppConfig.getAppConfig(context).setPhoneKeyBordHeight(keyboardHeight);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        //防止调用多次 只要拿到键盘的高度就行了
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                } else {
                    AppConfig.getAppConfig(context).setPhoneKeyBordHeight(keyboardHeight);
                }
            }
        });
    }
}
