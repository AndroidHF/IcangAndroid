package com.example.hufeng.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.hufeng.aicangproject.R;

import java.lang.ref.WeakReference;

/**
 * Created by hufeng on 2016/4/5.
 */
public class BaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    protected Context mContext;
    protected ProgressDialog mProgressDilog;
    public FragmentManager mFragmentManager;
    protected View statusView = null;
    protected Bundle _Bundle;
    protected MyHandler myHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        mFragmentManager = getSupportFragmentManager();
        _Bundle = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black, null));
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public static int getStatusBarHeight(Context context){
        int result = 0;
        //使用getIdentifier()方法可以方便的获各应用包下的指定资源ID
        //参数1：id名
        //参数2：资源属性id
        //参数3：包名
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0){
            //获取资源尺寸的大小
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //针对Android4.4到5.X之间设置statusbar背景色
        int versionCode = Build.VERSION.SDK_INT;
        if (versionCode >= Build.VERSION_CODES.KITKAT && versionCode < Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            ViewGroup contentView = (ViewGroup) this.findViewById(android.R.id.content);
            statusView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(this));
            statusView.setBackgroundColor(getResources().getColor(R.color.black));
            contentView.addView(statusView,lp);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            this.getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

//    protected boolean showLoadingDialog(){
//        if (!PhoneUtil.isNetworkAvailable(this)){
//            UIHelper.ToastShow(this,"网络异常，请检查网络");
//            return false;
//        }
//
//        if (mProgressDilog == null){
//            mProgressDilog = new ProgressDialog(this);
//        }
//
//        mProgressDilog.setMessage("加载中……");
//        mProgressDilog.show();
//        return true;
//    }

//    protected boolean showLoadingDialog(String str){
//        if (!PhoneUtil.isNetworkAvailable(this)){
//            UIHelper.ToastShow(this,"网络异常，请检查网络");
//            return false;
//        }
//
//        if (mProgressDilog == null){
//            mProgressDilog = new ProgressDialog(this);
//        }
//
//        mProgressDilog.setMessage(str);
//        mProgressDilog.show();
//        return true;
//    }

    protected void dismissLoadingDialog(){
        if (mProgressDilog != null){
            if (myHandler == null){
                myHandler = new MyHandler(this);
            }
            myHandler.sendEmptyMessageDelayed(0,100);
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDilog != null){
            mProgressDilog.dismiss();
        }

        if (myHandler != null){
            myHandler.clear();
        }
        super.onDestroy();
    }

    static class MyHandler extends Handler{
        private WeakReference<BaseActivity> weakReference;
        public MyHandler(BaseActivity baseActivity){
            weakReference = new WeakReference<BaseActivity>(baseActivity);
        }

        public void clear(){
            weakReference.clear();
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity mActivity = weakReference.get();
            if (mActivity != null){
                mActivity.mProgressDilog.dismiss();
            }
            super.handleMessage(msg);
        }
    }
}
