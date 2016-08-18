package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.SubactivityBean;
import com.buycolle.aicang.event.HomeBackEvent;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.mainScrole.ScrollableLayout;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by hufeng on 2016/8/11.
 */
public class SubjectActivity extends BaseActivity {
    //顶部标题栏
    @Bind(R.id.my_header)
    MyHeader myHeader;
    //整体滑动
    @Bind(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;
    //banner图
    @Bind(R.id.convenientBanner)
    ImageView autoScrollViewPager;

    //列表页
    @Bind(R.id.vp_main_container)
    FixedViewPager fixedViewPager;

    @Bind(R.id.ll_bg)
    LinearLayout ll_bg;



    protected MainApplication mApplication ;
    private Context mContext;
    private int event_id;


    public void onEventMainThread(HomeBackEvent event) {
        scrollableLayout.scrollTo(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mApplication = (MainApplication) this.getApplication();
        mContext = this;
        aCache = ACache.get(mContext);
        event_id = _Bundle.getInt("event_id");
        loadTopAds();

        initSubFragmentPager(fixedViewPager,scrollableLayout,event_id);



        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_TOBE_SALLER_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                Object infos = topaAdsObj.get("infos");
                if (topaAdsObj.length() > 0) {
                    aCache.put(Constans.TAG_TOBE_SALLER_TOP_ADS, topaAdsObj);
                    homarrays = new Gson().fromJson(infos.toString(), new SubactivityBean().getClass());

                    WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                    int width = windowManager.getDefaultDisplay().getWidth();
                    ViewGroup.LayoutParams layoutParams = autoScrollViewPager.getLayoutParams();//获取当前的控件的参数
                    layoutParams.height = width/2;//将高度设置为宽度的二分之一
                    mApplication.setImages(homarrays.getBanner_url(), autoScrollViewPager);
                    autoScrollViewPager.setLayoutParams(layoutParams);//使得设置好的参数应用到控件中
                }else {
                    loadTopAds();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            loadTopAds();
        }

    }

    /**
     * add by 胡峰
     * banner图的获取
     * @param savedInstanceState
     *
     *
     */

    private ACache aCache;
    private SubactivityBean homarrays;
    private void loadTopAds() {
        JSONObject jsonObject = new JSONObject();
        try {

//                jsonObject.put("sessionid", LoginConfig.getUserInfo(this).getSessionid());
//                //Log.i("sessionid_banner_icon",LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("event_id",event_id);
                Log.i("event_id-----",event_id+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appEvent_getEventBannerByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        Object infos = resultObj.get("infos");
                        if (resultObj!=null) {
                            aCache.put(Constans.TAG_TOBE_SALLER_TOP_ADS, resultObj);
                            homarrays = new Gson().fromJson(infos.toString(),SubactivityBean.class);
                            Log.i("8888888",resultObj.get("infos")+"");
                                mApplication.setShowImages(homarrays.getBanner_url(), autoScrollViewPager);
                                myHeader.init(homarrays.getEvent_title(), new MyHeader.Action() {
                                    @Override
                                    public void leftActio() {
                                        finish();
                                    }
                                });
                            }


                        mApplication.setBackGround(homarrays.getBg_url(),ll_bg);



                            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            Log.i("width", width + "");
                            ViewGroup.LayoutParams layoutParams = autoScrollViewPager.getLayoutParams();//获取当前的控件的参数
                            layoutParams.height = width / 2;//将高度设置为宽度的二分之一
                            //Log.i("height",layoutParams.height + "");
                            autoScrollViewPager.setLayoutParams(layoutParams);
                        }else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }



}
