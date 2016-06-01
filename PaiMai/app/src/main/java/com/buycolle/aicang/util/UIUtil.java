package com.buycolle.aicang.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class UIUtil {

    private static InputMethodManager mInputMethodManager;


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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

    public static int setNewHeight(Context context) {
        int pix = getWindowHeight(context);
        int head = dip2px(context, 130);
        return pix - head;
    }

    public float getWindowDensity() {
        DisplayMetrics metric = new DisplayMetrics();
        return metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
    }

    public int getWindowDensityDpi() {
        DisplayMetrics metric = new DisplayMetrics();
        return metric.densityDpi;// 屏幕密度DPI（120 / 160 / 240）
    }

    public static void hideSoftKeyboard(Context context, View et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive(et)) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public static void hideInputMethod(Activity act) {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        try {
            mInputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //强制显示或者关闭系统键盘
    public static void hideKeyBoard(final EditText txtSearchKey) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
            }
        }, 300);
    }

    /***
     * 获取焦点并且弹出软键盘
     *
     * @param view
     */
    public static void editActive(View view) {
        view.requestFocus();
        view.setFocusable(true);
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethodManager.showSoftInput(view, 0);
    }


}
