package com.buycolle.aicang.util;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.buycolle.aicang.MainApplication;


/**
 * 双击退出
 * @author anjiao
 */
public class DoubleClickExitHelper {

	private final Activity mActivity;
	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;
	private static final int DELAYBACKTIME=3000;//延迟双击退出时间
	private MainApplication mainApp;

	public DoubleClickExitHelper(Activity activity) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if(isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if(mBackToast != null){
				mBackToast.cancel();
			}
			mainApp = (MainApplication) mActivity.getApplication();
			mainApp.exitActivity();
			return true;
		} else {
			isOnKeyBacking = true;
			if(mBackToast == null) {
				mBackToast = Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_LONG);
			}
			mBackToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 250);
			mBackToast.show();
			mHandler.postDelayed(onBackTimeRunnable, DELAYBACKTIME);
			return true;
		}
	}

	private Runnable onBackTimeRunnable = new Runnable() {

		@Override
		public void run() {
			isOnKeyBacking = false;
			if(mBackToast != null){
				mBackToast.cancel();
			}
		}
	};
}
