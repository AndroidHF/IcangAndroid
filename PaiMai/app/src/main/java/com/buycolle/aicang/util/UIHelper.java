package com.buycolle.aicang.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UIHelper {

	public static void jump(Activity context, Class clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}
	public static void jump(Context context, Class clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	public static void jump(Activity context, Class clazz, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void jumpForResult(Activity context, Class clazz, int requestCode) {
		Intent intent = new Intent(context, clazz);
		context.startActivityForResult(intent, requestCode);
	}

	public static void jumpForResult(Activity context, Class clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
	}
	public static void jumpForResultByFragment(Fragment fragment, Class clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(fragment.getActivity(), clazz);
		intent.putExtras(bundle);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void jumpForResultByFragment(Fragment fragment, Class clazz, int requestCode) {
		Intent intent = new Intent(fragment.getActivity(), clazz);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void setResultBack(Activity context, Class clazz, Bundle bundle, int resultCode) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.setResult(resultCode, intent);
		context.finish();
	}

	/**
	 * 点击返回监听事件
	 */
	public static OnClickListener finish(final Activity activity) {
		return new OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

	public static void t(Context context, String msg) {
		Toast.makeText(context, msg == null ? "null" : msg, Toast.LENGTH_SHORT).show();
	}

	public static void t(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
}
