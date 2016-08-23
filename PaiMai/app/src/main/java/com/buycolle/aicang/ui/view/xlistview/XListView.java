/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.buycolle.aicang.ui.view.xlistview;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIUtil;


public class XListView extends ListView implements OnScrollListener {

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    // -- header view
    private XListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;//
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private XListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 500; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 0; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 2.3f; // support iOS like pull
    // feature.
    private Context context;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        this.context = context;
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
            isShowFoot(false);
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void isShowFoot(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
        } else {
            mPullLoading = false;
            mFooterView.show();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (mPullRefreshing == true) {
                mPullRefreshing = false;

                // SimpleDateFormat formatter = new SimpleDateFormat(
                // "yyyy-MM-dd HH:mm:ss");
                // Date curDate = new Date(System.currentTimeMillis());//
                // 获取当前时间
                // String str = formatter.format(curDate);
                // if (firstInit) {
                // firstInit = false;
                // cancleOnScrolling();
                // }

                resetHeaderHeight();
                setRefreshTime(StringFormatUtil.getNowTime());
            }
            if (mPullLoading == true) {
                mPullLoading = false;
            }

        }

        ;
    };

    /**
     * 结束刷新
     */
    public void onRefreshComplete() {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    // /**
    // * stop refresh, reset header view.
    // */
    // public void stopRefresh() {
    // if (mPullRefreshing == true) {
    // mPullRefreshing = false;
    // resetHeaderHeight();
    // SimpleDateFormat formatter = new SimpleDateFormat(
    // "yyyy-MM-dd HH:mm:ss");
    // Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    // String str = formatter.format(curDate);
    // setRefreshTime(str);
    // }
    // }
    //
    // /**
    // * stop load more, reset footer view.
    // */
    // public void stopLoadMore() {
    // if (mPullLoading == true) {
    // mPullLoading = false;
    // mFooterView.hide();
    // // mFooterView.setState(XListViewFooter.STATE_NORMAL);
    // }
    // }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    // private void cancleOnScrolling() {
    // if (mScrollListener instanceof OnXScrollListener) {
    // OnXScrollListener l = (OnXScrollListener) mScrollListener;
    // // l.onXScrolling(this);
    // l.onXScrolling(null);
    // }
    //
    // }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        // System.out.println(mPullRefreshing + "----" + height + "---"
        // + mHeaderViewHeight);
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to initDialog all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        // if (mEnablePullLoad && !mPullLoading) {
        // if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
        // // more.
        // mFooterView.setState(XListViewFooter.STATE_READY);
        // } else {
        // mFooterView.setState(XListViewFooter.STATE_NORMAL);
        // }
        // }
        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        if (mListViewListener != null) {
            mListViewListener.onLastItemVisible();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0) && getFirstVisiblePosition() != 0 && !mEnablePullLoad) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default://放手
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.&& mFooterView.getBottomMargin() >=
                    // PULL_LOAD_MORE_DELTA

                    // if (mEnablePullLoad) {
                    // startLoadMore();
                    // }
                    resetFooterHeight();//最后设置底部的视图
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void startRefreshing() {
        int visibleHeight = UIUtil.dip2px(context, 60);
        // mHeaderView.setVisiableHeight(180);
        updateHeaderHeight(visibleHeight);
        invokeOnScrolling();
        // this.scrollTo(0, 70);
        mPullRefreshing = true;
        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    private boolean mLastItemVisible;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mEnablePullLoad && mLastItemVisible) {
            startLoadMore();
        }
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (stickyView != null) {
            onScrollChanged();
            if(firstVisibleItem==0){
                stickyView.setVisibility(View.GONE);
            }
        }

        // send to user's listener
        mTotalItemCount = totalItemCount;
        // System.out.println(firstVisibleItem +"--"+
        // visibleItemCount+"----"+(totalItemCount - 1));
        mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private View stickyView = null;
    private View stickyHeaderMenuView = null;

    public void setSticky(View stickyView,View menuView){
        this.stickyView = stickyView;
        this.stickyHeaderMenuView = menuView;
        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    /**
     * 设置sticky
     */
    private void onScrollChanged() {
        View v = getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        if (getFirstVisiblePosition() == 1) {
            stickyView.setTranslationY(
                    Math.max(0, stickyHeaderMenuView.getTop() + top));
            if(stickyHeaderMenuView.getTop()-(-top)<30){
                stickyView.setVisibility(View.VISIBLE);
            }else{
                stickyView.setVisibility(View.GONE);
            }
        }
        if(getFirstVisiblePosition() >=2){
            stickyView.setTranslationY(0);
            stickyView.setVisibility(View.VISIBLE);
        }
    }


    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {

        public void onRefresh();

        public void onLastItemVisible();
    }
}
