package com.buycolle.aicang.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *可固定页卡的ViewPager，默认为固定页卡。
 * @author Lance
 *
 */
public class FixedViewPager extends ViewPager {
	private boolean isScrollable=false;
	
	public FixedViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public FixedViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 通过设置
	 * @param isScrollable 布尔量，通过设定它来决定ViewPager是否可以滑动。
	 * 						true:可滑动；false:不可滑动。
	 */
	public void setIsScrollabe(boolean isScrollable){
		this.isScrollable=isScrollable;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}



}
