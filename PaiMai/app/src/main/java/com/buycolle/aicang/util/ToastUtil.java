package com.buycolle.aicang.util;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hufeng on 2016/9/22.
 */
public class ToastUtil {
    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}
