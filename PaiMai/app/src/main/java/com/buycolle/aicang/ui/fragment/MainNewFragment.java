package com.buycolle.aicang.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeFilterMenuBean;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.bean.ShangPinFilterBean;
import com.buycolle.aicang.event.HomeBackEvent;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
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
    ImageView        iv_show_smile;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        aCache = ACache.get(mApplication);
        homeTopAddBeens = new ArrayList<>();
        menuFilters = new ArrayList<>();
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

        //        ll_event_menu.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                if (!isShow) {
        //                    iv_show_smile.setImageResource(R.drawable.event_top_icon);
        //                } else {
        //                    iv_show_smile.setImageResource(R.drawable.smail_none);
        //                }
        //                isShow = isShow ? false : true;
        //            }
        //        });

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

//    private int currentIndex = 1;

//    private void initHomeMenu() {
//
//        String initMenusStr = LoginConfig.getHomeMenu(mContext);
//        String[] strings = initMenusStr.split(",");
//        menuFilters.clear();
//        for (int i = 0; i < strings.length; i++) {
//            menuFilters.add(new HomeFilterMenuBean(strings[i], getTitleBeanRes(strings[i]), false));
//        }
//        //添加一个空数据
//        menuFilters.add(new HomeFilterMenuBean("", 0, true));
//    }

//    private GalleryRecyclerAdapter galleryRecyclerAdapter;

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

//    public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {
//
//        private LayoutInflater                  mInflater;
//        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
//
//        private ArrayList<HomeFilterMenuBean> homeFilterMenuBeens;
//
//        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener
//                                                               onRecyclerViewItemClickListener) {
//            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
//        }
//
//        public GalleryRecyclerAdapter(Context context, ArrayList<HomeFilterMenuBean> datas) {
//            mInflater = LayoutInflater.from(context);
//            homeFilterMenuBeens = new ArrayList<>();
//            this.homeFilterMenuBeens = datas;
//        }
//
//        /**
//         * 创建Item View  然后使用ViewHolder来进行承载
//         *
//         * @param parent
//         * @param viewType
//         * @return
//         */
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            final View view = mInflater.inflate(R.layout.aaaaa_demo, parent, false);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(UIUtil.getWindowWidth(mContext) / 3,
//                                                                       ViewGroup.LayoutParams.MATCH_PARENT);
//            view.setLayoutParams(params);
//            ViewHolder viewHolder = new ViewHolder(view);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onRecyclerViewItemClickListener.onItemClick(view, (int) view.getTag());
//                }
//            });
//            return viewHolder;
//        }
//
//        /**
//         * 进行绑定数据
//         *
//         * @param holder
//         * @param position
//         */
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            HomeFilterMenuBean homeFilterMenuBean = homeFilterMenuBeens.get(position);
//            holder.itemView.setTag(position);
//            if (position == 0) {
//                holder.tv_name.setVisibility(View.GONE);
//                holder.iv_menu.setVisibility(View.VISIBLE);
//                holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());
//
//            } else if (position == 1) {
//                holder.tv_name.setVisibility(View.VISIBLE);
//                holder.iv_menu.setVisibility(View.VISIBLE);
//                holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());
//                holder.tv_name.setText(homeFilterMenuBean.getName());
//            } else {
//                if (homeFilterMenuBean.isEmpty()) {//最后一个
//                    holder.tv_name.setVisibility(View.GONE);
//                    holder.iv_menu.setVisibility(View.GONE);
//                } else {
//                    holder.tv_name.setVisibility(View.VISIBLE);
//                    holder.iv_menu.setVisibility(View.VISIBLE);
//                    holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());
//                    holder.tv_name.setText(homeFilterMenuBean.getName());
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return homeFilterMenuBeens.size();
//        }
//
//        //自定义的ViewHolder，持有每个Item的的所有界面元素
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            LinearLayout parent;
//            TextView     tv_name;
//            ImageView    iv_menu;
//
//            public ViewHolder(View view) {
//                super(view);
//                parent = (LinearLayout) view.findViewById(R.id.ll_parent);
//                tv_name = (TextView) view.findViewById(R.id.tv_name);
//                iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
//            }
//        }
//
//    }

//    public interface OnRecyclerViewItemClickListener {
//
//        void onItemClick(View view, int position);
//    }

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

//    private int getTitleBeanRes(String title) {
//        if ("个性化".equals(title)) {
//            return R.drawable.main_menu_gexinghua;
//        } else if ("全部".equals(title)) {
//            return R.drawable.main_menu_all;
//        } else if ("漫画".equals(title)) {
//            return R.drawable.show_menu_1_sel;
//        } else if ("BD、DVD".equals(title)) {
//            return R.drawable.show_menu_2_sel;
//        } else if ("游戏".equals(title)) {
//            return R.drawable.show_menu_3_sel;
//        } else if ("书籍".equals(title)) {
//            return R.drawable.show_menu_4_sel;
//        } else if ("手办、模型".equals(title)) {
//            return R.drawable.show_menu_6_sel;
//        } else if ("周边".equals(title)) {
//            return R.drawable.show_menu_5_sel;
//        } else if ("音乐、演出".equals(title)) {
//            return R.drawable.show_menu_7_sel;
//        } else if ("服装、COS".equals(title)) {
//            return R.drawable.show_menu_8_sel;
//        } else if ("其他".equals(title)) {
//            return R.drawable.another_icon_sel;
//        }
//        return 0;
//    }

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

}
