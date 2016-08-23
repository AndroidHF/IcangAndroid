package com.buycolle.aicang.ui.view.recyclertablayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.hunter.reyclertablayout.BaseAdapter;
import com.hunter.reyclertablayout.BaseViewHolder;

import java.util.List;

public class RecyclerTabAdapter extends BaseAdapter<RecyclerTabEntity> {

    private static final int TAB_LIMIT = 3;

    public RecyclerTabAdapter(Context context, List<RecyclerTabEntity> data, ViewPager viewPager) {
        super(context, data, viewPager);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_tab, parent, false);

        TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_item_recycler_tab_text);
        int width = parent.getMeasuredWidth() / TAB_LIMIT;
        tvTitle.setMaxWidth(width);
        tvTitle.setMinWidth(width);

        return new BaseViewHolder(itemView);
    }

    @Override
    protected void onBindItemData(BaseViewHolder holder, int position) {
        RecyclerTabEntity entity = mData.get(position % mData.size());
        holder.setTvText(R.id.tv_item_recycler_tab_text, entity.getTitle());
        holder.setIvResource(R.id.iv_item_recycler_tab_icon, entity.getIcon());
    }

}