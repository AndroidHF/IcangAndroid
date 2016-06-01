package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.buycolle.aicang.bean.HomeGoodsBean;

import java.util.ArrayList;

/**
 * Created by joe on 16/3/24.
 */
public class HomeDataAdapter extends BaseAdapter {

    private Context context;
    private Activity mActivity;
    private ArrayList<HomeGoodsBean> homeGoodsBeanArrayList;

    public HomeDataAdapter(Context context, ArrayList<HomeGoodsBean> beans) {
        this.context = context;
        this.mActivity = (Activity) context;
        this.homeGoodsBeanArrayList = beans;
    }

    @Override
    public int getCount() {
        return homeGoodsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeGoodsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
