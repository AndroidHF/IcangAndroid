package com.buycolle.aicang.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeFilterMenuBean;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.bean.ShangPinFilterBean;
import com.buycolle.aicang.bean.infomationbean.MessageCenterHomeBean;
import com.buycolle.aicang.event.HomeBackEvent;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.event.UpdateInfoAnimationEvent;
import com.buycolle.aicang.ui.activity.InfoCenterActivity;
import com.buycolle.aicang.ui.activity.SearchActivity;
import com.buycolle.aicang.ui.view.CircleIndicator;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.AutoScrollViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.HomeAddImagePagerAdapter;
import com.buycolle.aicang.ui.view.filterdialog.FilterDialog;
import com.buycolle.aicang.ui.view.mainScrole.BasePagerFragment;
import com.buycolle.aicang.ui.view.mainScrole.ListFragment;
import com.buycolle.aicang.ui.view.mainScrole.ScrollableLayout;
import com.buycolle.aicang.ui.view.recyclertablayout.RecyclerPagerAdapter;
import com.buycolle.aicang.ui.view.recyclertablayout.RecyclerTabAdapter;
import com.buycolle.aicang.ui.view.recyclertablayout.RecyclerTabEntity;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunter.reyclertablayout.RecyclerTabLayout;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/2.
 */
public class MainNewFragment extends BasePagerFragment implements ViewPager.OnPageChangeListener {

    private static final int[]    TAB_ICON_RES = {
            R.drawable.show_menu_6_sel,
            R.drawable.main_menu_all,
            R.drawable.show_menu_5_sel,
            R.drawable.show_menu_1_sel,
            R.drawable.show_menu_4_sel,
            R.drawable.show_menu_2_sel,
            R.drawable.show_menu_3_sel,
            R.drawable.show_menu_7_sel,
            R.drawable.show_menu_8_sel,
            R.drawable.another_icon_sel,
            };
    private static final String[] TAB_TITLE    = {
            "手办、模型", "全部", "周边", "漫画", "书籍", "BD、DVD", "游戏", "音乐、演出", "服装、COS", "其他"
    };

    @Bind(R.id.convenientBanner)
    AutoScrollViewPager mPagerBanner;

    @Bind(R.id.recycler_tab_home)
    RecyclerTabLayout mTabLayout;

    @Bind(R.id.viewpager)
    FixedViewPager   viewpager;
    @Bind(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
    //    @Bind(R.id.rl_search)
    //    RelativeLayout rl_search;
    @Bind(R.id.ll_search)
    LinearLayout     ll_search;
    @Bind(R.id.ll_event_menu)
    LinearLayout     ll_event_menu;
    @Bind(R.id.iv_show_smile)
    ImageView  iv_has_info;//有消息时显示
    @Bind(R.id.iv_show_smile2)
    ImageView iv_no_info;//没消息时显示
    @Bind(R.id.circle_indicator)
    CircleIndicator  circleIndicator;
    @Bind(R.id.ll_filter)
    LinearLayout     ll_filter;


    private ACache                        aCache;
    private ArrayList<HomeTopAddBeanNew>  homeTopAddBeens;
    private ArrayList<HomeFilterMenuBean> menuFilters;
    private int                           userTotalDelta;
    private ArrayList<BaseFragment>       fragList;

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类

    private String st_id      = "";//状态ID集
    private int    sort_item  = 0;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private String sort_value = "";// A:升序 B：降序,默认排序的sor_id

    private RecyclerPagerAdapter mPagerAdapter;

    private FilterDialog mFilterDialog;

    //消息列表内容
    ArrayList<MessageCenterHomeBean> messageCenterHomeBeans;
    private int pageIndex = 1;
    private int pageNum = 10;
    private String  business_id ;//交易信息id
    private String common_id ;//交流信息id
    private String other_id ;//其他信息id

    //消息首页列表的消息数
    private int infoCenterCount;




    //登录触发
    public void onEventMainThread(LoginEvent event) {

    }

    //登出触发
    public void onEventMainThread(LogOutEvent event) {

    }

    //
    public void onEventMainThread(HomeBackEvent event) {
        scrollableLayout.scrollTo(0, 0);
    }

    //刷新动画的状态
    public void onEventMainThread(UpdateInfoAnimationEvent event){
        if (event.getStatus() == 0){
            messageCenterHomeBeans.clear();
            loadInfoList();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        aCache = ACache.get(mApplication);
        homeTopAddBeens = new ArrayList<>();
        menuFilters = new ArrayList<>();
        messageCenterHomeBeans = new ArrayList<>();
        loadInfoList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO
        for (int i = 0; i < 10; i++) {
            fragmentList.add(ListFragment.newInstance(getMenuType(TAB_TITLE[i]) + ""));
//            scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
        }
        loadInfoList();
        fragList = new ArrayList<>();
        // TODO

        // TODO TAB INIT
        List<RecyclerTabEntity> tabData = new ArrayList<>();
        for (int i = 0; i < TAB_ICON_RES.length; i++) {
            tabData.add(new RecyclerTabEntity(TAB_TITLE[i], TAB_ICON_RES[i]));
        }
        mPagerAdapter = new RecyclerPagerAdapter(getChildFragmentManager(), fragmentList, Arrays.asList(TAB_TITLE));
        scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(1));
        viewpager.setAdapter(mPagerAdapter);
        RecyclerTabAdapter adapter = new RecyclerTabAdapter(getContext(), tabData, viewpager);
        mTabLayout.setUpWithAdapter(adapter);
        mTabLayout.setAutoSelectionMode(true);
        viewpager.setCurrentItem(mPagerAdapter.getCenterPosition(1));
        viewpager.addOnPageChangeListener(this);
        viewpager.setIsScrollabe(true);
        // TODO

//        initHomeMenu();

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", 1);
                UIHelper.jump(mActivity, SearchActivity.class, bundle);
            }
        });

        //筛选图标设置监听
        mFilterDialog = FilterDialog.getInstance();
        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterDialog.show(getContext(), v, new FilterDialog.DialogConfirmListener() {
                    @Override
                    public void onConfirm(String filter_id, String sort_id) {
                        int pos = viewpager.getCurrentItem() % 10;
                        ListFragment fragment = (ListFragment) fragmentList.get(pos);
                        fragment.filterData(fragment.index, filter_id, sort_id, false, false);
                    }
                });

                ACache aCache = ACache.get(mContext);
                JSONObject resultObj = aCache.getAsJSONObject(Constans.TAG_GOOD_FILTER);
                ArrayList<ShangPinFilterBean> shangPinTypeBeens = new ArrayList<>();
                if (resultObj != null) {
                    try {
                        JSONArray rows = resultObj.getJSONArray("rows");
                        ArrayList<ShangPinFilterBean> allDataArrayList = new Gson().fromJson(rows.toString(),
                                new TypeToken<List<ShangPinFilterBean>>() {
                                }.getType());
                        if (allDataArrayList.size() != 0) {
                            shangPinTypeBeens.addAll(allDataArrayList);
                            mFilterDialog.setData(shangPinTypeBeens);
                        } else {
                            loadData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    loadData();
                }

            }
        });

        /**
         * 首页图标的监听，跳转到消息中心界面
         */
        ll_event_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mContext, InfoCenterActivity.class);
            }
        });

        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_HOME_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                JSONArray adsArray = topaAdsObj.getJSONArray("rows");
                if (adsArray.length() > 0) {
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(),
                                                          new TypeToken<List<HomeTopAddBeanNew>>() {
                                                          }.getType());
                    mPagerBanner.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(
                            false));
                    circleIndicator.setVisibility(View.VISIBLE);
                    circleIndicator.setViewPager(mPagerBanner);
                    mPagerBanner.setInterval(5000);
                    mPagerBanner.startAutoScroll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadTopAds();
        } else {
            loadTopAds();
        }

        // TODO 设置滑动监听
        //        int type = getMenuType(name);
        //                listFragment.refreshByState(type, true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        boolean nearLeftEdge = (position <= TAB_TITLE.length);
        boolean nearRightEdge = (position >= mPagerAdapter.getCount() - TAB_TITLE.length);
        if (nearLeftEdge || nearRightEdge) {
            viewpager.setCurrentItem(mPagerAdapter.getCenterPosition(0), false);
        }
        int pos = position % 10;
        ListFragment fragment = (ListFragment) fragmentList.get(pos);
        scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(pos));
        if (!fragment.isFirst()) {
            fragmentList.get(pos).refreshByState(getMenuType(TAB_TITLE[pos]));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private void loadTopAds() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appbanner_getlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded()) return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            aCache.put(Constans.TAG_HOME_TOP_ADS, resultObj);
                            ArrayList<HomeTopAddBeanNew> homarrays = new Gson().fromJson(jsonArray.toString(),
                                    new TypeToken<List<HomeTopAddBeanNew>>() {
                                    }.getType());
                            mPagerBanner.setAdapter(new HomeAddImagePagerAdapter(mActivity, homarrays).setInfiniteLoop(
                                    false));
                            mPagerBanner.setInterval(5000);
                            circleIndicator.setVisibility(View.VISIBLE);
                            circleIndicator.setViewPager(mPagerBanner);
                            mPagerBanner.startAutoScroll();
                            homeTopAddBeens.clear();
                            homeTopAddBeens.addAll(homarrays);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded()) return;
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        mPagerBanner.startAutoScroll();
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        mPagerBanner.stopAutoScroll();
    }

    private boolean isShow = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void refreshByState(int state) {
        super.refreshByState(state);
        if (state == 0) {//首页的点击事件
            viewpager.setCurrentItem(mPagerAdapter.getCenterPosition(1));
            ListFragment fragment = (ListFragment) fragmentList.get(1);
            fragment.refresh(0);
            loadTopAds();
        }
    }

    /**
     * 获取标签对应的id
     *
     * @param name
     * @return
     */
    private int getMenuType(String name) {
        if ("漫画".equals(name)) {
            return 1;
        } else if ("BD、DVD".equals(name)) {
            return 2;
        } else if ("游戏".equals(name)) {
            return 3;
        } else if ("书籍".equals(name)) {
            return 4;
        } else if ("手办、模型".equals(name)) {
            return 5;
        } else if ("周边".equals(name)) {
            return 6;
        } else if ("音乐、演出".equals(name)) {
            return 7;
        } else if ("服装、COS".equals(name)) {
            return 8;
        } else if ("其他".equals(name)) {
            return 9;
        } else if ("全部".equals(name)) {
            return 0;
        }
        return 0;
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        MainApplication mApplication = MainApplication.getInstance();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.dirtionary_getFilterAndSortListByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        ACache aCache = ACache.get(mContext);
                        aCache.put(Constans.TAG_GOOD_FILTER, resultObj);
                        JSONArray rows = resultObj.getJSONArray("rows");
                        ArrayList<ShangPinFilterBean> allDataArrayList = new Gson().fromJson(rows.toString(),
                                new TypeToken<List<ShangPinFilterBean>>() {
                                }.getType());
                        mFilterDialog.setData(allDataArrayList);

                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    /**
     * 消息中心首页列表的内容
     */
    public void loadInfoList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid",LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id",LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("business_id","");
            jsonObject.put("common_id","");
            jsonObject.put("other_id","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.messageCenter_getNewMessagesByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (JSONUtil.isHasData(resultObj)) {
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MessageCenterHomeBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MessageCenterHomeBean>>() {
                            }.getType());
                            messageCenterHomeBeans.addAll(allDataArrayList);
                            infoCenterCount = messageCenterHomeBeans.get(0).getCount() + messageCenterHomeBeans.get(1).getCount() + messageCenterHomeBeans.get(2).getCount() + messageCenterHomeBeans.get(3).getCount();
                            Log.i("消息总数",infoCenterCount+"");
                            if (infoCenterCount > 0){
                                Log.i("消息总数2",infoCenterCount+"");
                                iv_has_info.setVisibility(View.VISIBLE);
                                iv_no_info.setVisibility(View.GONE);
                                Glide.with(mContext).load(R.drawable.pic_move).asGif().diskCacheStrategy(DiskCacheStrategy.RESULT).into(iv_has_info);
                            }else if (infoCenterCount <= 0){
                                iv_no_info.setVisibility(View.VISIBLE);
                                iv_has_info.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
    }

}
