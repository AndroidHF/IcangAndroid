package com.buycolle.aicang.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.SmileGVAdapter;
import com.buycolle.aicang.ui.view.MeasuredGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/4/21.
 */
public class SmileFragment extends BaseFragment {

    private View view;

    ViewPager vp_smile_page;

    //表情相关
    private List<List<View>> smileList;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentInteractionListener) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_smile, null);
            initFrag(view);
        }
        return view;
    }

    private void initFrag(View view) {
        vp_smile_page = (ViewPager) view.findViewById(R.id.vp_smile_page);

        initSmilList();

    }

    //初始化表情数据
    private void initSmilList() {
        smileList = new ArrayList<List<View>>();
        initInnerSmile();
        initSmileViewPager();
    }
    //change by:胡峰，表情添加，长度限制
    private void initInnerSmile() {
        smileList.add(getChildGridView(getSmileRes(18), 18));
    }

    //取得环信表情资源
    public List<String> getSmileRes(int getSum) {
        List<String> resList = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            resList.add(filename);
        }
        return resList;

    }

    //获取表情gridview的子view
    private List<View> getChildGridView(List<String> list, int showNums) {
        List<View> viewList = null;
        if (list != null && list.size() != 0 && showNums > 0) {
            viewList = new ArrayList<>();
            int size = list.size() / showNums;
            for (int i = 0; i <= size; i++) {
                View view = View.inflate(mActivity, R.layout.smile_vp_item, null);
                MeasuredGridView gv = (MeasuredGridView) view.findViewById(R.id.mgv_smile_vp_item);
                gv.setHorizontalSpacing(0);
                gv.setVerticalSpacing(0);
                ArrayList<String> childList = new ArrayList<>();
                if (i == size) {
                    childList.addAll(list.subList(showNums * i, list.size()));
                } else {
                    childList.addAll(list.subList(showNums * i, showNums * (i + 1)));
                }
                if (childList.size() > 1) {
                    final SmileGVAdapter smileAdater = new SmileGVAdapter(mActivity, childList);
                    gv.setAdapter(smileAdater);
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String filename = (String) smileAdater.getItem(position);
                            mListener.onSmileClick(filename);
                        }
                    });
                    viewList.add(view);
                }
            }
        }
        return viewList;
    }

    /**
     * 用于Fragment与ChatActivity交互的接口
     */
    public interface OnFragmentInteractionListener {
        void onSmileClick(String filename);
    }


    //初始化表情viewpager
    private void initSmileViewPager() {
        SmilePagerAdapter mPagerAdapter = new SmilePagerAdapter(smileList);
        vp_smile_page.setAdapter(mPagerAdapter);
    }

    class SmilePagerAdapter extends PagerAdapter {
        private List<List<View>> views;
        private List<View> mList;

        public SmilePagerAdapter(List<List<View>> views) {
            this.views = views;
            mList = new ArrayList<>();
            for (List<View> list : views) {
                mList.addAll(list);
            }
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mList.get(arg1));
            return mList.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mList.get(arg1));

        }

    }
}
