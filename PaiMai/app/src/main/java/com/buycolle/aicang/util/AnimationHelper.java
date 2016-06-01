package com.buycolle.aicang.util;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by XuYang on 15/4/17.
 */
public class AnimationHelper {

	private static final int DURATION = 300;

	/**
	 * 放大进入
	 *
	 * @param view
	 * @return
	 */
	public static void zoomIn(final View view, int duration) {
		view.setVisibility(View.VISIBLE);
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1f);
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1f);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(animatorX, animatorY);
		set.setDuration(duration).start();
	}

	/**
	 * 缩小消失
	 *
	 * @param view
	 * @return
	 */
//	public static void zoomOut(final View view, int duration) {
//		ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0f);
//		ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0f);
//		AnimatorSet set = new AnimatorSet();
//		set.playTogether(animatorX, animatorY);
//		set.addListener(new AnimatorAdapter() {
//			@Override
//			public void onAnimationEnd(Animator arg0) {
//				view.setVisibility(View.INVISIBLE);
//			}
//		});
//		set.setDuration(duration).start();
//
//	}

	/**
	 * 淡入
	 *
	 * @param view
	 * @return
	 */
	public static ObjectAnimator getFadeInAnimator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1f);
		animator.setDuration(DURATION);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(View.VISIBLE);
			}
		});
		return animator;
	}

	/**
	 * 淡出
	 *
	 * @param view
	 * @return
	 */
	public static ObjectAnimator getFadeOutAnimator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
		animator.setDuration(DURATION);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
			}
		});
		return animator;
	}

	/**
	 * 淡出
	 *
	 * @param view
	 * @return
	 */
	public static ObjectAnimator test(final View view, final int finalVisiblity) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
		animator.setDuration(DURATION);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(finalVisiblity);
			}
		});
		return animator;
	}

	/**
	 * 旋转-90度
	 *
	 * @param view
	 * @return
	 */
	public static ObjectAnimator getRotationPositive90Animator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 90);
		animator.setDuration(DURATION);
		return animator;
	}
	public static ObjectAnimator getRotationPositive180Animator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 180);
		animator.setDuration(DURATION);
		return animator;
	}

	/**
	 * 旋转-90度
	 *
	 * @param view
	 * @return
	 */
	public static ObjectAnimator getRotatioNegative90Animator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -90, 0);
		animator.setDuration(DURATION);
		return animator;
	}
	public static ObjectAnimator getRotatioNegative180Animator(final View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -180, 0);
		animator.setDuration(DURATION);
		return animator;
	}

}
