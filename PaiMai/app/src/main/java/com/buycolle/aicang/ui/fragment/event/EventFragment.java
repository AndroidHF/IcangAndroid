package com.buycolle.aicang.ui.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.adapter.MyFragmentPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.event.EventBackEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.ui.activity.SearchActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.AutoScrollViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.HomeAddImagePagerAdapter;
import com.buycolle.aicang.ui.view.autoscrollviewpager.ImagePagerAdapter;
import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;
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
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/2.
 */
public class EventFragment extends BaseFragment {


    @Bind(R.id.iv_show_smile)
    ImageView ivShowSmile;
    @Bind(R.id.ll_event_menu)
    LinearLayout llEventMenu;
//    @Bind(R.id.rl_search)
//    RelativeLayout rlSearch;
    @Bind(R.id.ll_search)
    LinearLayout ll_search;
    @Bind(R.id.rl_event_top)
    RelativeLayout rlEventTop;
    @Bind(R.id.lltop)
    LinearLayout lltop;
    @Bind(R.id.convenientBanner)
    AutoScrollViewPager convenientBanner;
    @Bind(R.id.rl_head)
    RelativeLayout rlHead;
    @Bind(R.id.pagerStrip)
    LinearLayout pagerStrip;
    @Bind(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
    ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
    @Bind(R.id.viewpager)
    FixedViewPager viewpager;
    @Bind(R.id.tv_paimai_ing)
    TextView tvPaimaiIng;
    @Bind(R.id.tv_paimai_comming)
    TextView tvPaimaiComming;
    @Bind(R.id.tv_paimai_finish)
    TextView tvPaimaiFinish;
    @Bind(R.id.ll_tab_parent)
    LinearLayout llTabParent;

    private ScrollAbleFragment paiMaiIngFrag, paiMaiCommingFrag, paiMaiFinishFrag;
    private MainPagerAdapter pagerAdapter;
    private ArrayList<TextView> tvArrayList;

    private ACache aCache;
    private ArrayList<HomeTopAddBeanNew> homeTopAddBeens;

    private int event_type = 0;//正在进行


    private String[] images = {"http://www.qqc5.com/uploads/allimg/150311/1-1503111G401.jpg",
            "http://pic.87g.com/upload/2016/0316/20160316085552941.jpg",
            "http://www.xdjy369.com/d/file/contents/2014/08/53f7ebc8bdf45.jpg",
            "http://cdn.duitang.com/uploads/item/201409/02/20140902120555_LWnF2.thumb.700_0.jpeg",
            "http://dimg04.c-ctrip.com/images/t1/vacations/212000/211409/e3fcc97f5d5348e48bb230f3a1a334d6_C_500_280.jpg"
    };

    //    //登录触发
    public void onEventMainThread(LoginEvent event) {
    }

    public void onEventMainThread(EventBackEvent event) {
        initStatus(event.getIndex());
    }

    @OnClick(R.id.tv_paimai_ing)
    public void paiMainIng() {
        initStatus(0);
    }

    @OnClick(R.id.tv_paimai_comming)
    public void paiMainFinish() {
        initStatus(1);
    }

    @OnClick(R.id.tv_paimai_finish)
    public void liuPai() {
        initStatus(2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentList = new ArrayList<>();
        tvArrayList = new ArrayList<>();
        homeTopAddBeens = new ArrayList<>();
        aCache = ACache.get(mApplication);

        tvArrayList.add(tvPaimaiIng);
        tvArrayList.add(tvPaimaiComming);
        tvArrayList.add(tvPaimaiFinish);
        paiMaiIngFrag = new EventPaiMaiIngFragment();
        paiMaiCommingFrag = new EventPaiMaiCommingFragment();
        paiMaiFinishFrag = new EventPaiMaiFinishFragment();
        fragmentList.add(paiMaiIngFrag);
        fragmentList.add(paiMaiCommingFrag);
        fragmentList.add(paiMaiFinishFrag);
        List<String> titleList = new ArrayList<>();
        titleList.add("paiMaiIngFrag");
        viewpager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        viewpager.setOffscreenPageLimit(fragmentList.size() - 1);
        viewpager.setCurrentItem(0);
        scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        scrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                if (maxY != 0){
                    if (currentY / maxY > 0.5) {
                        llTabParent.setBackgroundResource(R.color.bg_gray);
                    } else {
                        llTabParent.setBackgroundResource(R.color.transparent);
                    }
                }
            }
        });

        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_EVENT_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                JSONArray adsArray = topaAdsObj.getJSONArray("rows");
                if (adsArray.length() > 0) {
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                    }.getType());
                    WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    int width = windowManager.getDefaultDisplay().getWidth();
                    Log.i("width", width + "");
                    ViewGroup.LayoutParams layoutParams = convenientBanner.getLayoutParams();//获取当前的控件的参数
                    layoutParams.height = width/2;
                    convenientBanner.setLayoutParams(layoutParams);
                    convenientBanner.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(false));
                    convenientBanner.setInterval(5000);
                    convenientBanner.startAutoScroll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadTopAds();
        } else {
            loadTopAds();
        }


        convenientBanner.setAdapter(new ImagePagerAdapter(mContext, Arrays.asList(images)).setInfiniteLoop(true));
        convenientBanner.setInterval(5000);
        convenientBanner.startAutoScroll();

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", 2);
                //拍卖会的类型
                bundle.putInt("event_type", event_type);
                KLog.d("类型---", event_type + "---");
                UIHelper.jump(mActivity, SearchActivity.class, bundle);
            }
        });
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
        mApplication.apiClient.appbanner_getcarouselphotobyapp(jsonObject, new ApiCallback() {
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
                            aCache.put(Constans.TAG_EVENT_TOP_ADS, resultObj);
                            ArrayList<HomeTopAddBeanNew> homarrays = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                            }.getType());
                            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            Log.i("width", width + "");
                            ViewGroup.LayoutParams layoutParams = convenientBanner.getLayoutParams();//获取当前的控件的参数
                            layoutParams.height = width/2;//将高度设置为宽度的三分之一
                            convenientBanner.setLayoutParams(layoutParams);
                            convenientBanner.setAdapter(new HomeAddImagePagerAdapter(mActivity, homarrays).setInfiniteLoop(false));
                            convenientBanner.setInterval(5000);
                            convenientBanner.startAutoScroll();
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


    private boolean isShow = true;

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startAutoScroll();
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopAutoScroll();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    private void initStatus(int index) {
        event_type = index;
        for (int i = 0; i < tvArrayList.size(); i++) {
            if (index == i) {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_orange_black);
            } else {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }
        viewpager.setCurrentItem(index);
        scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(index));
        scrollableLayout.scrollTo(0, 0);
        fragmentList.get(index).refreshByState(0);
    }
}
