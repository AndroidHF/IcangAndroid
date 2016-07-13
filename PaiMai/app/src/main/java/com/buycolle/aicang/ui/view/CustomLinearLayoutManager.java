package com.buycolle.aicang.ui.view;


import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.buycolle.aicang.util.superlog.KLog;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = CustomLinearLayoutManager.class.getSimpleName();

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
        View firstVisibleChild =recyclerView.getChildAt(0);
        int itemWidth = firstVisibleChild.getWidth();
        int currentPosition = recyclerView.getChildAdapterPosition(firstVisibleChild);
        //绝对值
        int distanceInPixels = Math.abs((currentPosition - position) * itemWidth);
        if (distanceInPixels == 0) {
            distanceInPixels = (int) Math.abs(firstVisibleChild.getX());
        }
        KLog.d("距离--","smoothScrollToPosition:distance"+distanceInPixels);
        KLog.d("距离--","position-----"+position);
        SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext(), distanceInPixels, 1000);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        super.scrollToPositionWithOffset(position, offset);
    }


    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return super.computeHorizontalScrollRange(state);
    }

    private class SmoothScroller extends LinearSmoothScroller {
        private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 500;
        private final float distanceInPixels;
        private final float duration;

        public SmoothScroller(Context context, int distanceInPixels, int duration) {
            super(context);
            this.distanceInPixels = distanceInPixels;
            float millisPerPx = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
            this.duration = distanceInPixels < TARGET_SEEK_SCROLL_DISTANCE_PX ? (int) (Math.abs(distanceInPixels) * millisPerPx) : duration;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return CustomLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);

        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            float proportion = (float) dx / distanceInPixels;
            int time= (int) (duration*proportion);
            time=time<100?100:time;
            time=time>500?500:time;
            return time;
        }
        @Override
        protected float calculateSpeedPerPixel
                (DisplayMetrics displayMetrics) {
            return 50f/displayMetrics.densityDpi;
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return LinearSmoothScroller.SNAP_TO_START;
        }
    }

}
