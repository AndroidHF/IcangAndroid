package com.buycolle.aicang.ui.fragment;

/**
 * Created by hufeng on 2016/8/11.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.HomeFilterMenuBean;
import com.buycolle.aicang.bean.TabInfo;
import com.buycolle.aicang.ui.view.TabScrollView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class HomeFliterFragmentNew extends BaseFragment {
    TabScrollView tabScrollView;
    ArrayList<TabInfo> tabInfos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo_new,container,false);
        tabScrollView = (TabScrollView) view.findViewById(R.id.tab_view);
        return view;
    }

    private ArrayList<HomeFilterMenuBean> menuFilters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuFilters = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String initMenusStr = LoginConfig.getHomeMenu(mContext);
        String[] strings = initMenusStr.split(",");
        for (int i = 0; i < strings.length; i++) {
            menuFilters.add(new HomeFilterMenuBean(strings[i], getTitleBeanRes(strings[i]), false));
        }

        for (int i = 0; i < menuFilters.size(); i++) {
            TabInfo tabInfo = new TabInfo();
            tabInfo.setHideText(false);
            tabInfo.setText(menuFilters.get(i).getName());
            tabInfo.setImgRes(menuFilters.get(i).getResId());
            tabInfos.add(tabInfo);
        }

        tabScrollView.addTabList(tabInfos);

    }

    private int getTitleBeanRes(String title) {
        if ("全部".equals(title)) {
            return R.drawable.main_menu_all;
        } else if ("漫画".equals(title)) {
            return R.drawable.show_menu_1_sel;
        } else if ("BD、DVD".equals(title)) {
            return R.drawable.show_menu_2_sel;
        } else if ("游戏".equals(title)) {
            return R.drawable.show_menu_3_sel;
        } else if ("书籍".equals(title)) {
            return R.drawable.show_menu_4_sel;
        } else if ("手办、模型".equals(title)) {
            return R.drawable.show_menu_6_sel;
        } else if ("周边".equals(title)) {
            return R.drawable.show_menu_5_sel;
        } else if ("音乐、演出".equals(title)) {
            return R.drawable.show_menu_7_sel;
        } else if ("服装、COS".equals(title)) {
            return R.drawable.show_menu_8_sel;
        } else if ("其他".equals(title)) {
            return R.drawable.show_menu_9_sel;
        }
        return 0;
    }
}

