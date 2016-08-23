package com.buycolle.aicang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.HomeFilterMenuBean;
import com.buycolle.aicang.bean.MainMenuBean;
import com.buycolle.aicang.bean.TabInfo;
import com.buycolle.aicang.ui.view.HomeMenuDialog;
import com.buycolle.aicang.ui.view.TabScrollView;

import java.util.ArrayList;

/**
 * Created by joe on 16/4/28.
 */
public class HomeFilterFrament extends BaseFragment {

    TabScrollView tab_view;
    ArrayList<TabInfo> tabInfos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo, container, false);
        tab_view = (TabScrollView) view.findViewById(R.id.tab_view);
        return view;
    }

    private ArrayList<HomeFilterMenuBean> menuFilters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuFilters = new ArrayList<>();

    }

    private int getTitleBeanRes(String title) {
        if ("个性化".equals(title)){
            return R.drawable.main_menu_gexinghua;
        }else if ("全部".equals(title)) {
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
            return R.drawable.another_icon_sel;
        }
        return 0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String initMenusStr = LoginConfig.getHomeMenu(mContext);
        String[] strings = initMenusStr.split(",");
        menuFilters.clear();
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
        tab_view.addTabList(tabInfos);
        tab_view.setOnPageChangeListener(new TabScrollView.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage, String name) {
                if (currentPage == 0) {
                    ArrayList<MainMenuBean> mainMenuBeans = new ArrayList<>();
                    String initMenusStr = LoginConfig.getHomeMenu(mContext);
                    String[] strings = initMenusStr.split(",");
                    if (strings.length > 2) {//有选择
                        for (int i = 2; i < strings.length; i++) {
                            mainMenuBeans.add(new MainMenuBean(strings[i], getTitleBeanRes(strings[i])));
                        }
                    }
                    new HomeMenuDialog(mContext, mainMenuBeans).setCallBack(new HomeMenuDialog.CallBack() {
                        @Override
                        public void ok(boolean isChange) {
                            String initMenusStr = LoginConfig.getHomeMenu(mContext);
                            String[] strings = initMenusStr.split(",");
                            menuFilters.clear();
                            for (int i = 0; i < strings.length; i++) {
                                menuFilters.add(new HomeFilterMenuBean(strings[i], getTitleBeanRes(strings[i]), false));
                            }
                            tabInfos.clear();
                            for (int i = 0; i < menuFilters.size(); i++) {
                                TabInfo tabInfo = new TabInfo();
                                tabInfo.setHideText(false);
                                tabInfo.setText(menuFilters.get(i).getName());
                                tabInfo.setImgRes(menuFilters.get(i).getResId());
                                tabInfos.add(tabInfo);
                            }
                            tab_view.addTabList(tabInfos);
                        }

                        @Override
                        public void cancle() {

                        }
                    }).show();
                } else {
                    if (actionCallBack != null) {
                        actionCallBack.callBack(name);
                    }
                }
            }
        });
    }

    public ActionCallBack actionCallBack;

    public void setCallBack(ActionCallBack callBack) {
        this.actionCallBack = callBack;
    }

    public interface ActionCallBack {
        void callBack(String name);
    }


}
