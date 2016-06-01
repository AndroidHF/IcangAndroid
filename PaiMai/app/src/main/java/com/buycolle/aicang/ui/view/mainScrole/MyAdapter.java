package com.buycolle.aicang.ui.view.mainScrole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.util.UIHelper;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyAdapter extends BaseAdapter {

    private List<String> strList;
    private Context mContext;

    public MyAdapter(Context mContext, List<String> strList) {
        this.strList = strList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder = null;
        if (convertView == null) {
            myholder = new Myholder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_home_main, null);
            myholder.iv_1 = (ImageView) convertView.findViewById(R.id.iv_1);
            myholder.ll_item_1 = (LinearLayout) convertView.findViewById(R.id.ll_item_1);
            myholder.ll_item_2 = (LinearLayout) convertView.findViewById(R.id.ll_item_2);
            convertView.setTag(myholder);
        } else {
            myholder = (Myholder) convertView.getTag();

        }
        MainApplication.getInstance().setImages(Constans.canglaoshiPath,myholder.iv_1);
        myholder.ll_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mContext, PaiPinDetailActivity.class);
            }
        });
        myholder.ll_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mContext, PaiPinDetailActivity.class);
            }
        });
        return convertView;
    }

    public class Myholder {

        ImageView iv_1;
        LinearLayout ll_item_1;
        LinearLayout ll_item_2;
    }
}
