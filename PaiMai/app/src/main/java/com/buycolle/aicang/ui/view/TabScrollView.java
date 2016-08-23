package com.buycolle.aicang.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.TabInfo;

import java.util.List;

/**
 * Created by Ruily on 16/4/11.
 */
public class TabScrollView extends ViewGroup {
    private int mTouchSlop;
    private Scroller mScroller;
    private int mTabWidth;

    private int lastX;
    private int mStart;

    private int mCurrentPos;

    private  List<TabInfo> mainTabs;

    private OnPageChangeListener mOnPageChangeListener;

    public TabScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mTabWidth = dm.widthPixels / 3;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(getChildCount() * mTabWidth, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                view.layout(i * mTabWidth, t, (i + 1) * mTabWidth, b);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else {
            if (getScrollX() == 0 && mCurrentPos == 0) return;
            if (getScrollX() % mTabWidth == 0) {
                int position = getScrollX() / mTabWidth + 1;  //可以加滑动结束监听
                resetPosition(position);
            }
        }
    }

    private void resetPosition(int position) {
//        if (position != mCurrentPos) {
            if (mOnPageChangeListener != null) {
                mCurrentPos = position;
                mOnPageChangeListener.onPageChange(mCurrentPos,mainTabs.get(position).getText());
            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                mStart = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                int dx = lastX - x;
                if (getScrollX() < 0) {
                    dx = 0;
                }
                if (getScrollX() > getWidth() - mTabWidth * 2) {
                    dx = 0;
                }
                scrollBy(dx, 0);

                lastX = x;
                break;
            case MotionEvent.ACTION_UP:
                int scrollOffset = checkAlignment();
                if (scrollOffset > 0) {
                    if (scrollOffset > mTabWidth / 2) {
                        mScroller.startScroll(getScrollX(), 0, mTabWidth - scrollOffset, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -scrollOffset, 0);
                    }
                } else {
                    if (-scrollOffset > mTabWidth / 2) {
                        mScroller.startScroll(getScrollX(), 0, -mTabWidth - scrollOffset, 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -scrollOffset, 0);
                    }
                }

                postInvalidate();
                break;
        }
        return true;
    }

    private int checkAlignment() {
        int mEnd = getScrollX();
        boolean isRight = ((mEnd - mStart) > 0);

        int lastPrev = mEnd % mTabWidth;
        int lastNext = mTabWidth - lastPrev;

        if (isRight) {
            return lastPrev;
        } else {
            return -lastNext;
        }
    }


    private int mLastX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mLastX - x) > mTouchSlop) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        mLastX = x;
        lastX = x;
        return intercepted;
    }

    public void setCurrentItem(int pos) {
        if (pos == getChildCount()) return;
        int offset = (pos - 1) * mTabWidth - getScrollX();
        if ((pos == 0 && offset < 0) || offset == 0) {
            resetPosition(pos);
            return;
        }
        mScroller.startScroll(getScrollX(), 0, offset, 0);
        postInvalidate();

    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     * 回调接口
     */
    public interface OnPageChangeListener {
        void onPageChange(int currentPage,String name);
    }


    /**
     * 添加Tab
     *
     * @param tabList
     */


    public void addTabList(List<TabInfo> tabList) {
        mainTabs = tabList;
        resetPosition(1);
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        removeAllViews();
        for (int i = 0, len = tabList.size(); i < len; i++) {
            TabInfo mInfo = tabList.get(i);
            LinearLayout tabView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item, null);
            ImageView ivIcon = (ImageView) tabView.findViewById(R.id.iv_icon);
            TextView mTvItem = (TextView) tabView.findViewById(R.id.tv_item);
            LinearLayout ll_line = (LinearLayout) tabView.findViewById(R.id.ll_line);
            mTvItem.setTextColor(ContextCompat.getColor(getContext(), tabTextColor));
            mTvItem.setTextSize(tabTextSize);
            if (!mInfo.isHideText()) {
                mTvItem.setText(mInfo.getText());
            } else {
                mTvItem.setVisibility(View.GONE);
            }
            ivIcon.setImageResource(mInfo.getImgRes());
            final int finalI = i;
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentItem(finalI);
                }
            });
            addView(tabView);
        }
    }

    private int tabTextSize = 11;
    private int tabTextColor = R.color.black_tv;

    /**
     * tab text size
     *
     * @param size
     */
    public void setTabTextSize(int size) {
        this.tabTextSize = size;
    }

    public void setTabTextColor(int resColor) {
        this.tabTextColor = resColor;
    }
}
