package com.buycolle.aicang.ui.view.filterdialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.ShangPinFilterBean;

import java.util.List;

public class FilterOrderAdapter extends BaseFilterAdapter<String> {

    public boolean[] mIsSelected;
    public boolean   mIsChecked;

    private List<ShangPinFilterBean> mOrders;

    public FilterOrderAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_home_filter_order);
    }

    @Override
    protected void onBindItemData(BaseViewHolder holder, final int position) {

        final CheckBox checkBox = holder.getView(R.id.cbox_item_filter_order);

        checkBox.setText(mData.get(position));

        checkBox.setTextColor(mIsSelected[position] ? Color.BLACK : 0xff808080);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setTextColor(Color.BLACK);
                resetColor();
                mIsSelected[position] = true;
                mIsChecked = isChecked;
            }
        });

        drawArrow(checkBox, position);
    }

    private void resetColor() {
        for (int i = 0; i < mIsSelected.length; i++) {
            mIsSelected[i] = false;
        }

        notifyDataSetChanged();
    }

    public void resetData() {
        mIsSelected[0] = true;
        for (int i = 1; i < mIsSelected.length; i++) {
            mIsSelected[i] = false;
        }

        notifyDataSetChanged();
    }

    private void drawArrow(CheckBox checkBox, int position) {
        if (mIsSelected[position]) {
            Drawable drawable;
            if (mOrders.get(position).getDir_id() == 46 || mOrders.get(position).getDir_id() == 50) {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.sort_low);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.sel_home_filter_order_arrow);
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            checkBox.setCompoundDrawables(null, null, drawable, null);
        } else {
            checkBox.setCompoundDrawables(null, null, null, null);
        }
    }

    public void addAll(List<String> list, List<ShangPinFilterBean> orders) {
        super.addAll(list);
        mIsSelected = new boolean[mData.size()];
        mIsSelected[0] = true;

        mOrders = orders;
    }
}
