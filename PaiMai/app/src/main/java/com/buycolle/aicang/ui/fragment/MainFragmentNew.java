package com.buycolle.aicang.ui.fragment;

/**
 * Created by hufeng on 2016/8/11.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
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

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class MainFragmentNew extends BasePagerFragment {
    //整个界面的滑动控件
    @Bind(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
    //搜索图标
    @Bind(R.id.ll_search)
    LinearLayout ll_search;
    //分类筛选图标
    @Bind(R.id.ll_filter)
    LinearLayout ll_filter;
    //首页banner轮播图控件
    @Bind(R.id.convenientBanner)
    AutoScrollViewPager autoScrollViewPager;
    //与banner图对应的小圆点控件
    @Bind(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    //分类容器
    @Bind(R.id.pagerStrip)
    FixedViewPager pagerStrip;

    //缓存机制
    private ACache aCache;

    //存放轮播图的集合
    private ArrayList<HomeTopAddBeanNew> homeTopAddBeens;

    //存放各个分类的集合
    private ArrayList<BaseFragment> fragList;

    //分类的初始化
    HomeFliterFragmentNew homeFliterFragmentNew;

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类

    private String st_id = "";//状态ID集
    private String sort_id;//排序id：47A表示剩余时间的升序排列，47B表示剩余时间的降序排列
    private String filter_id;//状态id
    private int sort_item = 0;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private String sort_value = "";// A:升序 B：降序,默认排序的sor_id


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建存放轮播图的集合
        homeTopAddBeens = new ArrayList<>();
        aCache = ACache.get(mApplication);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_new,container,false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化分类碎片的集合
        fragList = new ArrayList<>();

        //初始化分类的内容
        homeFliterFragmentNew = new HomeFliterFragmentNew();

        //将分类内容添加到集合中
        fragList.add(homeFliterFragmentNew);

        //分类适配器
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager(), fragList);
        pagerStrip.setAdapter(pagerAdapter);


        //搜索图标的监听
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", 1);
                UIHelper.jump(mActivity, SearchActivity.class, bundle);
            }
        });

        //筛选图标设置监听
        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MainFilterDialog(mContext,isSelectCate_Id, cate_id, st_id,sort_item,sort_value).setCallBack(new MainFilterDialog.CallBack() {
                    @Override
                    public void action(boolean ISSELECTCATE_ID, int CATE_ID,String ST_ID,int SORT_ITEM,String SORT_VALUE) {
                        KLog.d("返回来的数据---", "isSelectCate_Id==" + isSelectCate_Id + ",  " +
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

        //缓存机制：如果缓存中存在轮播图片，则调用，否则调用接口加载
        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_HOME_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                JSONArray adsArray = topaAdsObj.getJSONArray("rows");
                if (adsArray.length() > 0) {
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                    }.getType());
                    autoScrollViewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(false));
                    circleIndicator.setVisibility(View.VISIBLE);
                    circleIndicator.setViewPager(autoScrollViewPager);
                    autoScrollViewPager.setInterval(5000);
                    autoScrollViewPager.startAutoScroll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadTopAds();
        } else {
            loadTopAds();
        }



    }

    //调用AutoScrollViewPager控制自动翻页
    @Override
    public void onResume() {
        super.onResume();
        autoScrollViewPager.startAutoScroll();
    }

    //调用AutoScrollViewPager控制停止翻页


    @Override
    public void onPause() {
        super.onPause();
        autoScrollViewPager.stopAutoScroll();
    }

    /***
     * 获取bannner图片，加载bannner图，实现轮播
     */
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
                            autoScrollViewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homarrays).setInfiniteLoop(false));
                            autoScrollViewPager.setInterval(5000);
                            circleIndicator.setVisibility(View.VISIBLE);
                            circleIndicator.setViewPager(autoScrollViewPager);
                            autoScrollViewPager.startAutoScroll();
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
}

