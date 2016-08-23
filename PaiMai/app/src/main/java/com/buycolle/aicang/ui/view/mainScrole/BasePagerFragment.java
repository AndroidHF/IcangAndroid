package com.buycolle.aicang.ui.view.mainScrole;/**
 * Created by cpoopc on 2015/9/15.
 */

import android.support.v4.view.ViewPager;

import com.buycolle.aicang.adapter.MyFragmentPagerAdapter;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.fragment.SubjectFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cpoopc
 * Date: 2015-09-15
 * Time: 10:31
 * Ver.: 0.1
 */
public abstract class BasePagerFragment extends BaseFragment {

    protected ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
    protected ListFragment listFragment;
    protected SubjectFragment subjectFragment;
    public void initFragmentPager( ViewPager viewPager, final ScrollableLayout mScrollLayout,String index) {
        listFragment =ListFragment.newInstance(index);
        fragmentList.add(listFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add("ListView");
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
//        pagerSlidingTabStrip.setViewPager(viewPager);
//        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                Log.e("onPageSelected", "page:" + i);
//                /** 标注当前页面 **/
//                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });

        viewPager.setCurrentItem(0);
    }


}
