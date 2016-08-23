package com.hunter.reyclertablayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    protected RecyclerTabLayout   mRecyclerTabLayout;
    protected LinearLayoutManager mLayoutManager;

    public RecyclerOnScrollListener(RecyclerTabLayout recyclerTabLayout, LinearLayoutManager layoutManager) {
        mRecyclerTabLayout = recyclerTabLayout;
        mLayoutManager = layoutManager;
    }

    public int mDx;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mDx += dx;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                if (mDx > 0) {
                    selectCenterTabForRightScroll();
                } else {
                    selectCenterTabForLeftScroll();
                }
                mDx = 0;
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_SETTLING:
        }
    }

    protected void selectCenterTabForRightScroll() {
        int first = mLayoutManager.findFirstVisibleItemPosition();
        int last = mLayoutManager.findLastVisibleItemPosition();
        int center = mRecyclerTabLayout.getWidth() / 2;
        for (int position = first; position <= last; position++) {
            View view = mLayoutManager.findViewByPosition(position);
            if (view.getLeft() + view.getWidth() >= center) {
                mRecyclerTabLayout.setCurrentItem(position, false);
                break;
            }
        }
    }

    protected void selectCenterTabForLeftScroll() {
        int first = mLayoutManager.findFirstVisibleItemPosition();
        int last = mLayoutManager.findLastVisibleItemPosition();
        int center = mRecyclerTabLayout.getWidth() / 2;
        for (int position = last; position >= first; position--) {
            View view = mLayoutManager.findViewByPosition(position);
            if (view.getLeft() <= center) {
                mRecyclerTabLayout.setCurrentItem(position, false);
                break;
            }
        }
    }
}

