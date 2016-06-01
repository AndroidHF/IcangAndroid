package com.buycolle.aicang.ui.activity.usercentermenu.myfoucus;

import android.os.Bundle;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MyHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joe on 16/3/4.
 */
public class MyFocusActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_focus_saler)
    TextView tvFocusSaler;
    @Bind(R.id.tv_focus_goods)
    TextView tvFocusGoods;
    @Bind(R.id.tv_focus_show)
    TextView tvFocusShow;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    private ArrayList<TextView> tvArrayList;

    private BaseFragment myFocusSaler, myFocusGoods, myFocusShow;
    private List<BaseFragment> fragList;
    private MainPagerAdapter pagerAdapter;

    private boolean isShowTotal = false;


    @OnClick(R.id.tv_focus_saler)
    public void paiMainIng() {
        initStatus(0);
    }

    @OnClick(R.id.tv_focus_goods)
    public void paiMainFinish() {
        initStatus(1);
    }

    @OnClick(R.id.tv_focus_show)
    public void liuPai() {
        initStatus(2);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfocus);
        ButterKnife.bind(this);
        tvArrayList = new ArrayList<>();
        myHeader.init("我的关注", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        fragList = new ArrayList<>();
        tvArrayList.add(tvFocusSaler);
        tvArrayList.add(tvFocusGoods);
        tvArrayList.add(tvFocusShow);
        myFocusSaler = new MyFocusSalerFragment();
        myFocusGoods = new MyFocusGoodsFragment();
        myFocusShow = new MyFocusShowFragment();
        fragList.add(myFocusSaler);
        fragList.add(myFocusGoods);
        fragList.add(myFocusShow);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragList);
        vpMainContainer.setAdapter(pagerAdapter);
        vpMainContainer.setOffscreenPageLimit(fragList.size() - 1);
        vpMainContainer.setCurrentItem(0);

    }

    private void initStatus(int index) {
        for (int i = 0; i < tvArrayList.size(); i++) {
            if (index == i) {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_orange_black);
            } else {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }
        vpMainContainer.setCurrentItem(index);
    }
}
