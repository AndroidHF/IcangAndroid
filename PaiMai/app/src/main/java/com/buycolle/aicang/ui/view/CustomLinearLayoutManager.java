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

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
//                          int widthSpec, int heightSpec) {
//
//        final int widthMode = View.MeasureSpec.getMode(widthSpec);
//        final int heightMode = View.MeasureSpec.getMode(heightSpec);
//        final int widthSize = View.MeasureSpec.getSize(widthSpec);
//        final int heightSize = View.MeasureSpec.getSize(heightSpec);
//        int width = 0;
//        int height = 0;
//        for (int i = 0; i < getItemCount(); i++) {
//            measureScrapChild(recycler, i,
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    mMeasuredDimension);
//
//            if (getOrientation() == HORIZONTAL) {
//                width = width + mMeasuredDimension[0];
//                if (i == 0) {
//                    height = mMeasuredDimension[1];
//                }
//            } else {
//                height = height + mMeasuredDimension[1];
//                if (i == 0) {
//                    width = mMeasuredDimension[0];
//                }
//            }
//        }
//        switch (widthMode) {
//            case View.MeasureSpec.EXACTLY:
//                width = widthSize;
//            case View.MeasureSpec.AT_MOST:
//            case View.MeasureSpec.UNSPECIFIED:
//        }
//
//        switch (heightMode) {
//            case View.MeasureSpec.EXACTLY:
//                height = heightSize;
//            case View.MeasureSpec.AT_MOST:
//            case View.MeasureSpec.UNSPECIFIED:
//        }
//
//        setMeasuredDimension(width, height);
//    }
//
//    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
//                                   int heightSpec, int[] measuredDimension) {
//        try {
//            // dynamic add data there may cause a mistake of "IndexOutOfBoundsException"
//            View view = recycler.getViewForPosition(0);
//
//            if (view != null) {
//                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
//
//                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
//                        getPaddingLeft() + getPaddingRight(), p.width);
//
//                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
//                        getPaddingTop() + getPaddingBottom(), p.height);
//
//                view.measure(childWidthSpec, childHeightSpec);
//                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
//                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
//                recycler.recycleView(view);
//            }
//        } catch (Exception e) {
//        } finally {
//        }
//    }


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

//public class CustomLinearLayoutManager extends LinearLayoutManager {
//    private static final float MILLISECONDS_PER_INCH = 50f;
//    private Context mContext;
//
//    public CustomLinearLayoutManager(Context context) {
//        super(context);
//        mContext = context;
//    }
//
//    @Override
//    public void smoothScrollToPosition(RecyclerView recyclerView,
//                                       RecyclerView.State state, final int position) {
//
//        LinearSmoothScroller smoothScroller =
//                new LinearSmoothScroller(mContext) {
//
//                    //This controls the direction in which smoothScroll looks
//                    //for your view
//                    @Override
//                    public PointF computeScrollVectorForPosition
//                    (int targetPosition) {
//                        return CustomLinearLayoutManager.this
//                                .computeScrollVectorForPosition(targetPosition);
//                    }
//
//                    //This returns the milliseconds it takes to
//                    //scroll one pixel.
//                    @Override
//                    protected float calculateSpeedPerPixel
//                    (DisplayMetrics displayMetrics) {
//                        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
//                    }
//                };
//
//        smoothScroller.setTargetPosition(position);
//        startSmoothScroll(smoothScroller);
//    }
//}
