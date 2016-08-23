package com.buycolle.aicang.ui.view.filterdialog;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.buycolle.aicang.R;

import java.util.List;

public class FilterTagAdapter extends BaseFilterAdapter<String> {

    public boolean[] mIsSelected;

    public FilterTagAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_home_filter_tag);
    }

    @Override
    protected void onBindItemData(BaseViewHolder holder, final int position) {

        CheckBox checkBox = holder.getView(R.id.cbox_item_filter_tag);
        checkBox.setText(mData.get(position));


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsSelected[position] = isChecked;
            }
        });
        checkBox.setChecked(mIsSelected[position]);
    }


    public void resetData(){
        for (int i = 0; i < mIsSelected.length; i++) {
            mIsSelected[i] = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<String> list) {
        super.addAll(list);
        mIsSelected = new boolean[mData.size()];
    }
}
