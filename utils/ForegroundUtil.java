package com.example.hufeng.com.example.hufeng.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hufeng on 2016/4/18.
 */
public class ForegroundUtil implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 500;
    public static final String TAG = ForegroundUtil.class.getName();

    public interface Listener{
        public void onBecameForeground();
        public void onBecameBackground();
    }

    private static ForegroundUtil instance;
    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<>();
    private Runnable check;

    public static ForegroundUtil init(Application application){
        if (instance == null){
            instance = new ForegroundUtil();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static ForegroundUtil get(Application application){
        if (instance == null){
            init(application);
        }
        return instance;
    }

    public static ForegroundUtil get(Context ctx){
        if (instance == null){
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application){
                init((Application) appCtx);
            }
            //Foreground没有初始化，或者Foreground没有获取当前的应用类
            throw new IllegalStateException("Foreground is not initialisted and"+"cannot obtain the Application object");
        }
        return instance;
    }

    public static ForegroundUtil get(){
        if (instance == null){
            throw new IllegalStateException("Foreground is not initialisted - invoke"+"at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null){
            handler.removeCallbacks(check);
        }

        if (wasBackground){
            Log.i(TAG,"went foreground");
            for (Listener listener :listeners){
                try {
                    listener.onBecameForeground();
                }catch (Exception e){
                    Log.e(TAG,"Listener threw exception",e);
                }
            }
        }else{
            Log.i(TAG,"still foreground");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null){
            handler.removeCallbacks(check);
            handler.postDelayed(check = new Runnable() {
                @Override
                public void run() {
                    if (foreground && paused){
                        for (Listener listener :listeners){
                            try {
                                listener.onBecameBackground();
                            }catch (Exception e){
                                Log.e(TAG,"Listener threw exception",e);
                            }
                        }
                    }else {
                        Log.i(TAG,"still foreground");
                    }
                }
            },CHECK_DELAY);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
