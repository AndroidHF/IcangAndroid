/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.buycolle.aicang.ui.view.autoscrollviewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.SubjectActivity;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class HomeAddImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private Activity activity;
    private ArrayList<HomeTopAddBeanNew> adds;

    private int size;
    private boolean isInfiniteLoop;

    public HomeAddImagePagerAdapter(Context context, ArrayList<HomeTopAddBeanNew> imageIdList) {
        this.context = context;
        this.activity = (Activity) context;
        this.adds = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : adds.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_main_header, null);
            holder.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MainApplication.getInstance().setImages(adds.get(getPosition(position)).getBanner_icon(), holder.iv_item);
        holder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动作类型 1：url链接 2：拍品广告 3:纯图片 4:活动界面
                if (adds.get(getPosition(position)).getAction_type() == 1) {
                    Uri uri = Uri.parse(adds.get(getPosition(position)).getBanner_link());
                    Log.i("banner_link",adds.get(getPosition(position)).getBanner_link());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Log.i("link_url", String.valueOf(uri));
                    context.startActivity(intent);
                }
                if (adds.get(getPosition(position)).getAction_type() == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", adds.get(getPosition(position)).getTarget_id());
                    UIHelper.jump(activity, PaiPinDetailActivity.class, bundle);
                }

                if (adds.get(getPosition(position)).getAction_type() == 4){
                    Bundle bundle = new Bundle();
                    bundle.putInt("event_id",adds.get(getPosition(position)).getTarget_id());
                    UIHelper.jump(activity, SubjectActivity.class,bundle);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_item;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public HomeAddImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
