package com.hunter.reyclertablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerTabLayout extends RecyclerView {

    protected static final long  DEFAULT_SCROLL_DURATION      = 200;
    protected static final float DEFAULT_POSITION_THRESHOLD   = 0.6f;
    protected static final float POSITION_THRESHOLD_ALLOWABLE = 0.001f;

    protected Paint mIndicatorPaint;
    protected int   mIndicatorHeight;

    private Context mContext;

    protected LinearLayoutManager      mLinearLayoutManager;
    protected RecyclerOnScrollListener mRecyclerOnScrollListener;
    protected ViewPager                mViewPager;
    protected BaseAdapter              mAdapter;

    protected int     mIndicatorPosition;
    protected int     mIndicatorOffset;
    protected int     mScrollOffset;
    protected float   mOldPositionOffset;
    protected float   mPositionThreshold;
    protected boolean mRequestScrollToTab;
    protected boolean mScrollEnabled;

    public RecyclerTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        setWillNotDraw(false);

        initLayoutManager();

        mIndicatorPaint = new Paint();
        getAttributes(attrs);

        mPositionThreshold = DEFAULT_POSITION_THRESHOLD;

    }

    private void initLayoutManager() {
        mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return mScrollEnabled;
            }
        };
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(mLinearLayoutManager);
        setItemAnimator(null);
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.rtl_RecyclerTabLayout);
        setIndicatorColor(a.getColor(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorColor, 0));
        setIndicatorHeight(a.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorHeight, 0));
        mScrollEnabled = a.getBoolean(R.styleable.rtl_RecyclerTabLayout_rtl_scrollEnabled, true);

        a.recycle();
    }


    @Override
    protected void onDetachedFromWindow() {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        super.onDetachedFromWindow();
    }

    public void setIndicatorColor(int color) {
        mIndicatorPaint.setColor(color);
    }

    public void setIndicatorHeight(int indicatorHeight) {
        mIndicatorHeight = indicatorHeight;
    }

    public void setUpWithAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        mViewPager = adapter.getViewPager();
        mViewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener(this));
        setAdapter(adapter);
        scrollToTab(mViewPager.getCurrentItem());
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, smoothScroll);
            scrollToTab(mViewPager.getCurrentItem());
            return;
        }

        if (smoothScroll && position != mIndicatorPosition) {
            startAnimation(position);
        } else {
            scrollToTab(position);
        }
    }

    protected void startAnimation(final int position) {
        float distance = 1;

        View view = mLinearLayoutManager.findViewByPosition(position);
        if (view != null) {
            float currentX = view.getX() + view.getMeasuredWidth() / 2f;
            float centerX = getMeasuredWidth() / 2f;
            distance = Math.abs(centerX - currentX) / view.getMeasuredWidth();
        }

        ValueAnimator animator;
        if (position < mIndicatorPosition) {
            animator = ValueAnimator.ofFloat(distance, 0);
        } else {
            animator = ValueAnimator.ofFloat(-distance, 0);
        }
        animator.setDuration(DEFAULT_SCROLL_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollToTab(position, (float) animation.getAnimatedValue(), true);
            }
        });
        animator.start();
    }

    protected void scrollToTab(int position) {
        scrollToTab(position, 0, false);
        mAdapter.setCurrentIndicatorPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    protected void scrollToTab(int position, float positionOffset, boolean fitIndicator) {
        int scrollOffset = 0;

        View selectedView = mLinearLayoutManager.findViewByPosition(position);
        View nextView = mLinearLayoutManager.findViewByPosition(position + 1);

        if (selectedView != null) {
            int width = getMeasuredWidth();
            float scroll1 = width / 2f - selectedView.getMeasuredWidth() / 2f;

            if (nextView != null) {
                float scroll2 = width / 2f - nextView.getMeasuredWidth() / 2f;

                float scroll = scroll1 + (selectedView.getMeasuredWidth() - scroll2);
                float dx = scroll * positionOffset;
                scrollOffset = (int) (scroll1 - dx);

                mScrollOffset = (int) dx;
                mIndicatorOffset = (int) ((scroll1 - scroll2) * positionOffset);

            } else {
                scrollOffset = (int) scroll1;
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }
            if (fitIndicator) {
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }

            if (mAdapter != null && mIndicatorPosition == position) {
                updateCurrentIndicatorPosition(position, positionOffset - mOldPositionOffset, positionOffset);
            }

            mIndicatorPosition = position;

        } else {
            if (getMeasuredWidth() > 0) {
                scrollOffset = (int) ((getMeasuredWidth()) / 2f);
            }
            mRequestScrollToTab = true;
        }

        stopScroll();
        mLinearLayoutManager.scrollToPositionWithOffset(position, scrollOffset);

        if (mIndicatorHeight > 0) {
            invalidate();
        }

        mOldPositionOffset = positionOffset;
    }

    protected void updateCurrentIndicatorPosition(int position, float dx, float positionOffset) {
        int indicatorPosition = -1;
        if (dx > 0 && positionOffset >= mPositionThreshold - POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position + 1;

        } else if (dx < 0 && positionOffset <= 1 - mPositionThreshold + POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position;
        }
        if (indicatorPosition >= 0 && indicatorPosition != mAdapter.getCurrentIndicatorPosition()) {
            mAdapter.setCurrentIndicatorPosition(indicatorPosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setAutoSelectionMode(boolean autoSelect) {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        if (autoSelect) {
            mRecyclerOnScrollListener = new RecyclerOnScrollListener(this, mLinearLayoutManager);
            addOnScrollListener(mRecyclerOnScrollListener);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        View view = mLinearLayoutManager.findViewByPosition(mIndicatorPosition);
        if (view == null) {
            if (mRequestScrollToTab) {
                mRequestScrollToTab = false;
                scrollToTab(mViewPager.getCurrentItem());
            }
            return;
        }
        mRequestScrollToTab = false;

        int left = view.getLeft() + mScrollOffset - mIndicatorOffset;
        int right = view.getRight() + mScrollOffset + mIndicatorOffset;
        int top = getHeight() - mIndicatorHeight;
        int bottom = getHeight();

        canvas.drawRect(left, top, right, bottom, mIndicatorPaint);
    }

}
