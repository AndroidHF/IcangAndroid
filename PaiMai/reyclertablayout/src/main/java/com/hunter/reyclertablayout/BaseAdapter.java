package com.hunter.reyclertablayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context   mContext;
    protected List<T>   mData;
    protected ViewPager mViewPager;

    protected int mIndicatorPosition;

    protected OnItemClickListener mListener;

    public interface OnItemClickListener<T> {

        void onItemClick(int position, T data);
    }

    public BaseAdapter(Context context, List<T> data, ViewPager viewPager) {
        mData = data;
        mContext = context;
        mViewPager = viewPager;
    }

    protected abstract void onBindItemData(BaseViewHolder holder, int position);

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        onBindItemData(holder, position);

        holder.mConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mViewPager.setCurrentItem(pos, true);
                }

                if (mListener != null) {
                    mListener.onItemClick(pos, mData.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mViewPager.getAdapter().getCount();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setCurrentIndicatorPosition(int indicatorPosition) {
        mIndicatorPosition = indicatorPosition;
    }

    public int getCurrentIndicatorPosition() {
        return mIndicatorPosition;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
