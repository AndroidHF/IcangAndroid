package com.buycolle.aicang.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by hufeng on 2016/5/4.
 * 功能描述：ViewPager适配器，用来绑定数据和view
 */
public class ViewPagerAdapter extends PagerAdapter {
    // 界面列表
    private ArrayList<View> views;

    public ViewPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

    /**
     * 获取当前界面数
     * @return
     */
    @Override
    public int getCount() {
        return views==null?0:views.size();
    }

    /**
     * 判断是否由对象生成
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 销毁position位置的界面
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    /**
     * 初始化position位置的界面
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       container.addView(views.get(position),0);
        return views.get(position);
    }
}
