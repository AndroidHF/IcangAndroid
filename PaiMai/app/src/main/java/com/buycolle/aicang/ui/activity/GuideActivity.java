package com.buycolle.aicang.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.buycolle.aicang.MainActivity;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.ViewPagerAdapter;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

/**
 * author: hufeng
 * date:2016/06/01
 * functional description:引导界面
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    //定义ViewPager对象
    private ViewPager viewPager;
    //定义引导界面的各个view对象
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    //定义一个ArrayList来存放View对象
    private ArrayList<View> list;
    //定义开始按钮对象
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //实例化Viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //实例化各个界面的布局对象
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        view1 = layoutInflater.inflate(R.layout.guide_view1,null);
        view2 = layoutInflater.inflate(R.layout.guide_view2,null);
        view3 = layoutInflater.inflate(R.layout.guide_view3,null);
        view4 = layoutInflater.inflate(R.layout.guide_view4,null);

        //实例化ArrayList对象
        list = new ArrayList<View>();

        //将要显示的view对象添加到List集合中
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);

        //viewpager设置监听
        viewPager.addOnPageChangeListener(this);

        //设置适配器数据
        viewPager.setAdapter(new ViewPagerAdapter(list));

        //实例化开始按钮对象
        btnStart = (Button) view4.findViewById(R.id.btnStart);

        //给开始按钮设置监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //存入数据并提交
                SplashActivity.sharedPreferences.edit().putInt("VERSION", SplashActivity.VERSION).commit();
                UIHelper.jump(GuideActivity.this, MainActivity.class);
                finish();
            }
        });


    }

    /**
     *当页面滑动时候调用
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 新的页面被选中时候调用
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * 滑动状态改变时候调用
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
