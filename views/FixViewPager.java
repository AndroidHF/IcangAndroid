package com.example.hufeng.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hufeng on 2016/3/29.
 */
public class FixViewPager extends ViewPager {
    private boolean isScrollable = false;
    public FixViewPager(Context context) {
        super(context);
    }

    public FixViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 通过设置布尔变量isScrollable的值，来决定ViewPager是否可以滚动。
     *      true：可以滑动
     *      false：不可以滑动
     */
    public void setIsScrollable(boolean isScrollable){
        this.isScrollable = isScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false){
            return false;
        }else {
            return super.onTouchEvent(ev);
        }
    }
}
