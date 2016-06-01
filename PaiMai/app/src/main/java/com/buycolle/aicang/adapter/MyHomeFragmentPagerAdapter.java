package com.buycolle.aicang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyHomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<ScrollAbleFragment> fragmentList;
    private List<String> titleList;

    public MyHomeFragmentPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
