package com.buycolle.aicang.ui.view.filterdialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseFilterAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mData;
    protected Context mContext;

    protected int mLayoutId;

    protected OnItemClickListener mListener;

    public interface OnItemClickListener<T> {

        void onItemClick(int position, T data);
    }

    public BaseFilterAdapter(Context context, List<T> data, int layoutId) {
        mData = data;
        mContext = context;
        mLayoutId = layoutId;
    }

    protected abstract void onBindItemData(BaseViewHolder holder, int position);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        onBindItemData(holder, position);

        if (mListener != null) {
            holder.mConvertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mListener.onItemClick(pos, mData.get(pos));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public T getItemData(int position) {
        return mData.get(position);
    }

    public List<T> getData() {
        return mData;
    }

    public void add(T data, int position) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void reloadData(List<T> data) {
        clean();
        addAll(data);
    }

    public void addAll(List<T> list) {
        mData.addAll(list);
        notifyItemRangeInserted(0, mData.size());
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clean() {
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setLayoutId(int layoutId) {
        mLayoutId = layoutId;
    }

}
