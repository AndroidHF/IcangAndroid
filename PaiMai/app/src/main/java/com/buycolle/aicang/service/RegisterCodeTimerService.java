package com.buycolle.aicang.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.buycolle.aicang.util.RegisterCodeTimer;
import com.buycolle.aicang.util.superlog.KLog;


/**
 * 可以用作验证码的服务
 *
 * Created by joe on 15/9/23.
 */
public class RegisterCodeTimerService extends Service {
    private static Handler mHandler;
    private static RegisterCodeTimer mCodeTimer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        if(mCodeTimer!=null){
            mCodeTimer.cancel();
        }
        //一分钟
        mCodeTimer = new RegisterCodeTimer(60000, 1000, mHandler);
        mCodeTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        KLog.d("onDestroy 退出", "好的 onDestroy");
        super.onDestroy();
    }

    /**
     * 设置Handler
     */
    public static void setHandler(Handler handler) {
        mHandler = handler;
    }
}
