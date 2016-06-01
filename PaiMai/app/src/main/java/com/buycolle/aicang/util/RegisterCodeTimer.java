package com.buycolle.aicang.util;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * 注册验证码计时器
 *
 */
public class RegisterCodeTimer extends CountDownTimer {
	private static Handler mHandler;
	public static final int IN_RUNNING = 1001;
	public static final int END_RUNNING = 1002;

	public RegisterCodeTimer(long millisInFuture, long countDownInterval,
							 Handler handler) {
		super(millisInFuture, countDownInterval);
		mHandler = handler;
	}

	// 结束
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (mHandler != null)
			mHandler.obtainMessage(END_RUNNING, -1).sendToTarget();
	}
	//返回的秒数
	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		if (mHandler != null)
			mHandler.obtainMessage(IN_RUNNING,(int)(millisUntilFinished / 1000)).sendToTarget();
	}

}
