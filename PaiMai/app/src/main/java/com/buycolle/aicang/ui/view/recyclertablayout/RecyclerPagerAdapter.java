package com.buycolle.aicang.ui.view.recyclertablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;

import java.util.List;

public class RecyclerPagerAdapter extends PagerAdapter {

    private static final int NUMBER_OF_LOOPS = 100;
    private FragmentManager          mManager;
    private List<ScrollAbleFragment> mFragments;
    private List<String>             mTitle;

    public RecyclerPagerAdapter(FragmentManager manager, List<ScrollAbleFragment> data, List<String> title) {
        mManager = manager;
        mFragments = data;
        mTitle = title;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = mFragments.get(position % mTitle.size());
        if (!fragment.isAdded()) {
            mManager.beginTransaction().add(fragment, fragment.getClass().getName()).commitAllowingStateLoss();
            mManager.executePendingTransactions();
        }
        View view = fragment.getView();
        if (view.getParent() == null) {
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mTitle == null? 0:mTitle.size() * NUMBER_OF_LOOPS;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public String getPageTitle(int position) {
        return getValueAt(position);
    }

    public int getCenterPosition(int position) {
        return mTitle.size() * NUMBER_OF_LOOPS / 2 + position;
    }

    public String getValueAt(int position) {
        if (mTitle.size() == 0) {
            return null;
        }
        return mTitle.get(position % mTitle.size());
    }

}
