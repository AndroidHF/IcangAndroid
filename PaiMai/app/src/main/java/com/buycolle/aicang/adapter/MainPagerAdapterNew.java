package com.buycolle.aicang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.buycolle.aicang.ui.fragment.BaseFragment;

import java.util.List;

/**
 * Created by hufeng on 2016/8/11.
 */
public class MainPagerAdapterNew extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<BaseFragment> fragList;

    public MainPagerAdapterNew(FragmentManager fm) {
        super(fm);
        this.fm=fm;

    }



    public MainPagerAdapterNew(FragmentManager fm, List<BaseFragment> fragList) {
        super(fm);
        this.fm=fm;
        this.fragList = fragList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return fragList.get(position%fragList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragList.get(position%fragList.size()).getView());
    }
}
