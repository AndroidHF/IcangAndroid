package com.buycolle.aicang.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeFilterMenuBean;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.bean.MainMenuPage;
import com.buycolle.aicang.event.HomeBackEvent;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.ui.activity.SearchActivity;
import com.buycolle.aicang.ui.view.CircleIndicator;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MainFilterDialog;
import com.buycolle.aicang.ui.view.autoscrollviewpager.AutoScrollViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.HomeAddImagePagerAdapter;
import com.buycolle.aicang.ui.view.mainScrole.BasePagerFragment;
import com.buycolle.aicang.ui.view.mainScrole.ScrollableLayout;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.UIUtil;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/2.
 */
public class MainFragment extends BasePagerFragment {


    @Bind(R.id.convenientBanner)
    AutoScrollViewPager viewPager;
    @Bind(R.id.pagerStrip)
    FixedViewPager pagerStrip;
    @Bind(R.id.viewpager)
    FixedViewPager viewpager;
    @Bind(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
//    @Bind(R.id.rl_search)
//    RelativeLayout rl_search;
    @Bind(R.id.ll_search)
    LinearLayout ll_search;
    @Bind(R.id.ll_event_menu)
    LinearLayout ll_event_menu;
    @Bind(R.id.iv_show_smile)
    ImageView iv_show_smile;
    @Bind(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    private ACache aCache;
    private ArrayList<HomeTopAddBeanNew> homeTopAddBeens;
    private ArrayList<HomeFilterMenuBean> menuFilters;
    HomeFilterFrament homeFilterFrament;
    private int userTotalDelta;
    private ArrayList<BaseFragment> fragList;
    @Bind(R.id.ll_filter)
    LinearLayout ll_filter;
    private boolean isSelectCate_Id = false;
    private int cate_id;//分类

    private String st_id = "";//状态ID集
    private int sort_item = 0;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private String sort_value = "";// A:升序 B：降序,默认排序的sor_id



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

        String index = "0";
        initFragmentPager(viewpager, scrollableLayout, index);
        fragList = new ArrayList<>();
        homeFilterFrament = new HomeFilterFrament();
        fragList.add(homeFilterFrament);
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager(), fragList);
        pagerStrip.setAdapter(pagerAdapter);
        pagerStrip.setOffscreenPageLimit(fragList.size() - 1);
        int currentIndex = Integer.MAX_VALUE/2;
        pagerStrip.setCurrentItem(currentIndex);


        initHomeMenu();

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", 1);
                UIHelper.jump(mActivity, SearchActivity.class, bundle);
            }
        });

        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new MainFilterDialog(mContext,isSelectCate_Id, cate_id, st_id,sort_item,sort_value).setCallBack(new MainFilterDialog.CallBack() {
                   @Override
                   public void action(boolean ISSELECTCATE_ID, int CATE_ID,String ST_ID,int SORT_ITEM,String SORT_VALUE) {
                       KLog.d("返回来的数据---", "isSelectCate_Id==" + isSelectCate_Id+ ",  " +
                                       "cate_id==" + cate_id + ",  " +//
                                       "st_id==" + st_id + ",  "+
                                       "sort_item=="+sort_item+","+
                                       "sort_value == "+sort_value
                       );
                       isSelectCate_Id = ISSELECTCATE_ID;
                       cate_id = CATE_ID;
                       st_id = ST_ID;
                       sort_item = SORT_ITEM;
                       sort_value = SORT_VALUE;
                   }
               }).show();
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
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                    }.getType());
                    viewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(false));
                    circleIndicator.setVisibility(View.VISIBLE);
                    circleIndicator.setViewPager(viewPager);
                    viewPager.setInterval(5000);
                    viewPager.startAutoScroll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadTopAds();
        } else {
            loadTopAds();
        }

        homeFilterFrament.setCallBack(new HomeFilterFrament.ActionCallBack() {
            @Override
            public void callBack(String name) {
                int type = getMenuType(name);
                listFragment.refreshByState(type, true);
            }
        });
    }



    private int currentIndex = 1;

    private void initHomeMenu() {

        String initMenusStr = LoginConfig.getHomeMenu(mContext);
        String[] strings = initMenusStr.split(",");
        menuFilters.clear();
        for (int i = 0; i < strings.length; i++) {
            menuFilters.add(new HomeFilterMenuBean(strings[i], getTitleBeanRes(strings[i]), false));
        }
        //添加一个空数据
        menuFilters.add(new HomeFilterMenuBean("", 0, true));
    }


    private GalleryRecyclerAdapter galleryRecyclerAdapter;


    public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        private ArrayList<HomeFilterMenuBean> homeFilterMenuBeens;

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        }

        public GalleryRecyclerAdapter(Context context, ArrayList<HomeFilterMenuBean> datas) {
            mInflater = LayoutInflater.from(context);
            homeFilterMenuBeens = new ArrayList<>();
            this.homeFilterMenuBeens = datas;
        }

        /**
         * 创建Item View  然后使用ViewHolder来进行承载
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = mInflater.inflate(R.layout.aaaaa_demo, parent, false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(UIUtil.getWindowWidth(mContext) / 3, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemClick(view, (int) view.getTag());
                }
            });
            return viewHolder;
        }

        /**
         * 进行绑定数据
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            HomeFilterMenuBean homeFilterMenuBean = homeFilterMenuBeens.get(position);
            holder.itemView.setTag(position);
            if (position == 0) {
                holder.tv_name.setVisibility(View.GONE);
                holder.iv_menu.setVisibility(View.VISIBLE);
                holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());

            } else if (position == 1) {
                holder.tv_name.setVisibility(View.VISIBLE);
                holder.iv_menu.setVisibility(View.VISIBLE);
                holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());
                holder.tv_name.setText(homeFilterMenuBean.getName());
            } else {
                if (homeFilterMenuBean.isEmpty()) {//最后一个
                    holder.tv_name.setVisibility(View.GONE);
                    holder.iv_menu.setVisibility(View.GONE);
                } else {
                    holder.tv_name.setVisibility(View.VISIBLE);
                    holder.iv_menu.setVisibility(View.VISIBLE);
                    holder.iv_menu.setImageResource(homeFilterMenuBean.getResId());
                    holder.tv_name.setText(homeFilterMenuBean.getName());
                }
            }
        }

        @Override
        public int getItemCount() {
            return homeFilterMenuBeens.size();
        }


        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout parent;
            TextView tv_name;
            ImageView iv_menu;

            public ViewHolder(View view) {
                super(view);
                parent = (LinearLayout) view.findViewById(R.id.ll_parent);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
            }
        }

    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
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
                if (!isAdded())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            aCache.put(Constans.TAG_HOME_TOP_ADS, resultObj);
                            ArrayList<HomeTopAddBeanNew> homarrays = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                            }.getType());
                            viewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homarrays).setInfiniteLoop(false));
                            viewPager.setInterval(5000);
                            circleIndicator.setVisibility(View.VISIBLE);
                            circleIndicator.setViewPager(viewPager);
                            viewPager.startAutoScroll();
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
                if (!isAdded())
                    return;
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        viewPager.startAutoScroll();
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    private boolean isShow = true;

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


    /**
     * 顶部标题的
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<MainMenuPage> mDatas;
        private ArrayList<View> mListViews;

        public MyViewPagerAdapter(ArrayList<MainMenuPage> mDatas, ArrayList<View> mListViews) {
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
            this.mDatas = mDatas;//构造方法，参数是我们的页卡，这样比较方便。
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {    //这个方法用来实例化页卡
            View view = mListViews.get(position);

            LinearLayout ll_lay_1 = (LinearLayout) view.findViewById(R.id.ll_lay_1);
            LinearLayout ll_lay_2 = (LinearLayout) view.findViewById(R.id.ll_lay_2);
            LinearLayout ll_lay_3 = (LinearLayout) view.findViewById(R.id.ll_lay_3);

            ImageView iv_menu_icon_1 = (ImageView) view.findViewById(R.id.iv_menu_icon_1);
            ImageView iv_menu_all_icon_1 = (ImageView) view.findViewById(R.id.iv_menu_all_icon_1);
            final TextView tv_menu_title_1 = (TextView) view.findViewById(R.id.tv_menu_title_1);

            ImageView iv_menu_icon_2 = (ImageView) view.findViewById(R.id.iv_menu_icon_2);
            ImageView iv_menu_all_icon_2 = (ImageView) view.findViewById(R.id.iv_menu_all_icon_2);
            final TextView tv_menu_title_2 = (TextView) view.findViewById(R.id.tv_menu_title_2);

            ImageView iv_menu_icon_3 = (ImageView) view.findViewById(R.id.iv_menu_icon_3);
            ImageView iv_menu_all_icon_3 = (ImageView) view.findViewById(R.id.iv_menu_all_icon_3);
            final TextView tv_menu_title_3 = (TextView) view.findViewById(R.id.tv_menu_title_3);

//            if (position == 0) {
//                if (mDatas.get(0).getMainMenuBeans().size() == 2) {
//                    setInVisible(iv_menu_icon_1, iv_menu_icon_2, iv_menu_icon_3, iv_menu_all_icon_1, iv_menu_all_icon_2, iv_menu_all_icon_3, tv_menu_title_1, tv_menu_title_2, tv_menu_title_3);
//                    setVisible(iv_menu_icon_1, tv_menu_title_1, iv_menu_all_icon_2);
//                    iv_menu_icon_1.setImageResource(R.drawable.main_menu_gexinghua);
//                    tv_menu_title_1.setText("个性化");
//                } else {
//                    setInVisible(iv_menu_icon_1, iv_menu_icon_2, iv_menu_icon_3, iv_menu_all_icon_1, iv_menu_all_icon_2, iv_menu_all_icon_3, tv_menu_title_1, tv_menu_title_2, tv_menu_title_3);
//                    setVisible(iv_menu_icon_1, tv_menu_title_1, iv_menu_all_icon_2, iv_menu_icon_3, tv_menu_title_3);
//                    iv_menu_icon_1.setImageResource(mDatas.get(0).getMainMenuBeans().get(0).getRes());
//                    tv_menu_title_1.setText(mDatas.get(0).getMainMenuBeans().get(0).getTitle());
//                    iv_menu_all_icon_2.setImageResource(mDatas.get(0).getMainMenuBeans().get(1).getRes());
//                    tv_menu_title_2.setText(mDatas.get(0).getMainMenuBeans().get(1).getTitle());
//                    iv_menu_icon_3.setImageResource(mDatas.get(0).getMainMenuBeans().get(2).getRes());
//                    tv_menu_title_3.setText(mDatas.get(0).getMainMenuBeans().get(2).getTitle());
//                }
//            } else {
                setInVisible(iv_menu_icon_1, iv_menu_icon_2, iv_menu_icon_3, iv_menu_all_icon_1, iv_menu_all_icon_2, iv_menu_all_icon_3, tv_menu_title_1, tv_menu_title_2, tv_menu_title_3);
                if (mDatas.get(position).getMainMenuBeans().size() == 1) {
                    setVisible(iv_menu_icon_1, tv_menu_title_1);
                    iv_menu_icon_1.setImageResource(mDatas.get(position).getMainMenuBeans().get(0).getRes());
                    tv_menu_title_1.setText(mDatas.get(position).getMainMenuBeans().get(0).getTitle());
                } else if (mDatas.get(position).getMainMenuBeans().size() == 2) {
                    setVisible(iv_menu_icon_1, tv_menu_title_1, iv_menu_icon_2, tv_menu_title_2);
                    iv_menu_icon_1.setImageResource(mDatas.get(position).getMainMenuBeans().get(0).getRes());
                    tv_menu_title_1.setText(mDatas.get(position).getMainMenuBeans().get(0).getTitle());
                    iv_menu_icon_2.setImageResource(mDatas.get(position).getMainMenuBeans().get(1).getRes());
                    tv_menu_title_2.setText(mDatas.get(position).getMainMenuBeans().get(1).getTitle());
                } else {
                    setVisible(iv_menu_icon_1, tv_menu_title_1, iv_menu_icon_2, tv_menu_title_2, iv_menu_icon_3, tv_menu_title_3);
                    iv_menu_icon_1.setImageResource(mDatas.get(position).getMainMenuBeans().get(0).getRes());
                    tv_menu_title_1.setText(mDatas.get(position).getMainMenuBeans().get(0).getTitle());
                    iv_menu_icon_2.setImageResource(mDatas.get(position).getMainMenuBeans().get(1).getRes());
                    tv_menu_title_2.setText(mDatas.get(position).getMainMenuBeans().get(1).getTitle());
                    iv_menu_icon_3.setImageResource(mDatas.get(position).getMainMenuBeans().get(2).getRes());
                    tv_menu_title_3.setText(mDatas.get(position).getMainMenuBeans().get(2).getTitle());
                }
            //}
            ll_lay_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(tv_menu_title_1.getText().toString().trim())) {
//                        if ("个性化".equals(tv_menu_title_1.getText().toString())) {
//                            ArrayList<MainMenuBean> mainMenuBeans = new ArrayList<MainMenuBean>();
//                            String initMenusStr = LoginConfig.getHomeMenu(mContext);
//                            String[] strings = initMenusStr.split(",");
//                            if (strings.length > 2) {//有选择
//                                for (int i = 2; i < strings.length; i++) {
//                                    mainMenuBeans.add(new MainMenuBean(strings[i], getTitleBeanRes(strings[i])));
//                                }
//                            }
//                            new HomeMenuDialog(mContext, mainMenuBeans).setCallBack(new HomeMenuDialog.CallBack() {
//                                @Override
//                                public void ok(boolean isChange) {
//                                    if (isChange) {
//                                        initHomeMenu();
//                                        listFragment.refreshByState(0);
//                                    } else {//没选中
//                                        initHomeMenu();
//                                        listFragment.refreshByState(0);
//                                    }
//                                }
//
//                                @Override
//                                public void cancle() {
//
//                                }
//                            }).initDialog();
//                        } else {
//                            int type = getMenuType(tv_menu_title_1.getText().toString().trim());
//                            listFragment.refreshByState(type);
//                        }
                        initHomeMenu();
                        listFragment.refreshByState(0);
                    }
                }
            });
            ll_lay_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(tv_menu_title_2.getText().toString().trim())) {
                        int type = getMenuType(tv_menu_title_2.getText().toString().trim());
                        listFragment.refreshByState(type);
                    } else {
                        if (position == 0) {
                            listFragment.refreshByState(0);
                        } else {
                            int type = getMenuType(tv_menu_title_2.getText().toString().trim());
                            listFragment.refreshByState(type);
                        }
                    }
                }
            });
            ll_lay_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(tv_menu_title_3.getText().toString().trim())) {
                        int type = getMenuType(tv_menu_title_3.getText().toString().trim());
                        listFragment.refreshByState(type);
                    }
                }
            });
            container.addView(view, 0);//添加页卡
            return view;
        }

        @Override
        public int getCount() {
            return mListViews.size();//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//官方提示这样写
        }

        private void setInVisible(View... views) {
            for (View view1 : views) {
                view1.setVisibility(View.GONE);
            }
        }

        private void setVisible(View... views) {
            for (View view1 : views) {
                view1.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void refreshByState(int state) {
        super.refreshByState(state);
        if (state == 0) {//首页的点击事件
            listFragment.refresh(0);
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
        }
        return 0;
    }

}
