package com.example.hufeng.fragments;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hufeng.aicangproject.R;
import com.example.hufeng.aicangproject.SearchActivity;
import com.example.hufeng.com.example.hufeng.utils.UIHelper;
import com.example.hufeng.views.FixViewPager;

import java.util.ArrayList;
import java.util.List;

import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by hufeng on 2016/3/29.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private ScrollableLayout scrollableLayout;
    private ImageView showSmile;
    private RelativeLayout rlSearch;
    private Boolean flag = true;
    private ViewPager viewPager;
    private List<View> listViews;
    private Drawable[] icons;
    private FixViewPager fixViewPager;
    private MyPagerAdapter myPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        showSmile = (ImageView) view.findViewById(R.id.iv_show_smile);
        rlSearch = (RelativeLayout) view.findViewById(R.id.rl_search);
        viewPager = (ViewPager) view.findViewById(R.id.convenientBanner);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showSmile.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        listViews = new ArrayList<View>();
        // 添加的View实际就是ImageView
        // 获取自定义R.array的方法
        TypedArray array = getResources().obtainTypedArray(R.array.icons); // 获取自定义的Array数组
        // 初始化
        icons = new Drawable[array.length()];
        for (int i = 0; i < array.length(); i++) {
            icons[i] = array.getDrawable(i);
        }
        array.recycle();

        // 动态构建ImageView,多少?实际图片有多少就要动态创建ImageView
        // 全部创建完的ImageView需要放到全局的容器中
        for (int i = 0; i < icons.length; i++) {
            ImageView iv = new ImageView(getContext());
            // 设置对应的属性(layout_width 和 layout_heigth属性)
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                    LinearLayout.LayoutParams.MATCH_PARENT // 高度
            ));
            // 图片源的属性
            iv.setImageDrawable(icons[i]);
            // scaleType的属性(当前设置的图片宽度和高度原始图片的高度宽度不一样,这个属性才有效果)
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            // 添加到容器里
            listViews.add(iv);
        }
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_show_smile:
                if (flag){
                    showSmile.setImageResource(R.mipmap.smail_none);
                    flag = false;
                }else {
                    showSmile.setImageResource(R.mipmap.event_top_icon);
                    flag = true;
                }
                break;
            case R.id.rl_search:
                UIHelper.jumpForResultByFragment(this,SearchActivity.class);
        }

    }

    public class MyPagerAdapter extends PagerAdapter {

        // 1.支持数据源List<View>
        // 2.支持数据源(List<Fragment>)
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listViews.size();
        }

        // 写法固定
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            // TODO Auto-generated method stub
            return view == obj;
        }

        // 选择:instantiateItem的第一个参数ViewGroup而不是View
        /**
         * 第一个参数:显示视图的容器 第二个参数:数据的下标
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View view = listViews.get(position);
            // 添加到系统提供的容器
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // 把不需要的position对应视图从容器中移除(提高效率)
            View view = listViews.get(position);
            // View view2 = (View) object; 和第一种写法的效果一样
            container.removeView(view);
        }

    }
}
