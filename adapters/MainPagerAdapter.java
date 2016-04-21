package com.example.hufeng.com.example.hufeng.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by hufeng on 2016/3/29.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private List<Fragment> fragList;
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public MainPagerAdapter(FragmentManager fm,List<Fragment> fragList) {
        super(fm);
        this.fm = fm;
        this.fragList = fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList == null ? 0:fragList.size();
    }
}
