package com.buycolle.aicang;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.api.ApiClient;
import com.buycolle.aicang.api.OkHttpClientManager;
import com.buycolle.aicang.bean.ProvinceBean;
import com.buycolle.aicang.reciver.PushNotificationHelper;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.AppJsonFileReader;
import com.buycolle.aicang.util.ForegroundUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by joe on 16/3/2.
 */
public class MainApplication extends MultiDexApplication {


    private ArrayList<Activity> allActivity = new ArrayList<>();
    public static PushNotificationHelper pushNotificationHelper = new PushNotificationHelper();

    public static String cityJson;
    private ACache aCache;


    /**
     * 所有省
     */
    public String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    public Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    public Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();


    public void addActivity(Activity act) {
        allActivity.add(act);
    }

    private static MainApplication mainApplication;
    public static final int LOGIN_REQUESTCODE_BY_FRAG = 999;
    OkHttpClient client;
    public ApiClient apiClient;

    public static MainApplication getInstance() {
        return mainApplication;
    }


    public void exitActivity() {
        for (int i = 0, len = allActivity.size(); i < len; i++) {
            Activity act = allActivity.get(i);
            if (act != null) {
                act.finish();
            }
        }
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "900027602", false);
        aCache = ACache.get(this);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        ForegroundUtil.init(this);
        pushNotificationHelper.init(this);

        apiClient = new ApiClient(this);
        client = OkHttpClientManager.getInstance().getOkHttpClient();
        client.setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        initCity();
        initSearchHot();
    }

    private void initSearchHot() {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ent_id", 1);
            if (isLogin()) {
                jsonObject1.put("sessionid", LoginConfig.getUserInfo(this).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiClient.dirtionary_gethotsearchlistbyapp(jsonObject1, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        aCache.put(Constans.TAG_HOT_SEARCH_GOOD, resultObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(com.squareup.okhttp.Request request, Exception e) {

            }
        }, "拍品");
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("ent_id", 2);
            if (isLogin()) {
                jsonObject1.put("sessionid", LoginConfig.getUserInfo(this).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiClient.dirtionary_gethotsearchlistbyapp(jsonObject2, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        aCache.put(Constans.TAG_HOT_SEARCH_EVENT, resultObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(com.squareup.okhttp.Request request, Exception e) {

            }
        }, "拍卖会");
        JSONObject jsonObject3 = new JSONObject();
        try {
            jsonObject3.put("ent_id", 2);
            if (isLogin()) {
                jsonObject1.put("sessionid", LoginConfig.getUserInfo(this).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiClient.dirtionary_gethotsearchlistbyapp(jsonObject3, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        aCache.put(Constans.TAG_HOT_SEARCH_SHOW, resultObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(com.squareup.okhttp.Request request, Exception e) {

            }
        }, "initDialog");


    }


    /**
     * 初始化城市
     */
    private void initCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cityJson = AppJsonFileReader.getJson(getBaseContext(),
                        "area.json");
                try {
                    JSONArray jsonArray = new JSONArray(cityJson);
                    ArrayList<ProvinceBean> allDataArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ProvinceBean>>() {
                    }.getType());
                    int allProvinceSize = allDataArrayList.size();
                    mProvinceDatas = new String[allProvinceSize];
                    for (int i = 0; i < allProvinceSize; i++) {
                        mProvinceDatas[i] = allDataArrayList.get(i).getArea_name();
                        String[] cityNames = new String[allDataArrayList.get(i).getCityList().size()];
                        for (int i1 = 0; i1 < allDataArrayList.get(i).getCityList().size(); i1++) {
                            cityNames[i1] = allDataArrayList.get(i).getCityList().get(i1).getArea_name();
                            String[] dists = new String[allDataArrayList.get(i).getCityList().get(i1).getDistinctList().size()];
                            for (int i2 = 0; i2 < allDataArrayList.get(i).getCityList().get(i1).getDistinctList().size(); i2++) {
                                dists[i2] = allDataArrayList.get(i).getCityList().get(i1).getDistinctList().get(i2).getArea_name();
                            }
                            mDistrictDatasMap.put(allDataArrayList.get(i).getCityList().get(i1).getArea_name(), dists);
                        }
                        mCitisDatasMap.put(allDataArrayList.get(i).getArea_name(), cityNames);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isLogin() {
        return LoginConfig.isLogin(this);
    }

    public boolean isSaller() {
        Log.i("啦啦啦啦啦啦啦",LoginConfig.getUserInfo(this).getUser_type()+"");
        return LoginConfig.getUserInfo(this).getUser_type() == 2 && isLogin();
    }


    public void gotoLogInByFrag(Fragment fragment) {
        UIHelper.jumpForResultByFragment(fragment, LoginActivity.class, LOGIN_REQUESTCODE_BY_FRAG);
    }

    public void setImages(String url, final int position, final ImageView imageView) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .error(R.drawable.default_image)
                .placeholder(R.drawable.default_image)
                .dontAnimate()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.glide_tag_id, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.glide_tag_id);
                    }
                });
    }

    public void setImages(String url, final ImageView imageView) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.default_image)
                .into(imageView);
    }



    /**
     * add by :胡峰
     * 晒物和竞拍会的默认首图加载时候的图片加载方法
     * @param url
     * @param imageView
     */
    public void setShowImages(String url, final ImageView imageView) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.default_image_show)
                .into(imageView);
    }

    public void setShowImages2(String url, final ImageView imageView) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .into(imageView);
    }

    public void setBackGround(String url, final LinearLayout linearLayout){
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable db = new BitmapDrawable(resource);
                        linearLayout.setBackgroundDrawable(db);
                    }
                });
    }



    /**
     * add by :胡峰
     * 头像的处理
     */
    public void setTouImages(String url,final ImageView imageView){
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.default_image_tou)
                .into(imageView);
    }

    public void setShoufaImages(String url,final ImageView imageView){
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .error(R.drawable.shoufa_360_bg)
                .placeholder(R.drawable.shoufa_360_bg)
                .into(imageView);
    }

    public void updatePushId() {
        String id = JPushInterface.getRegistrationID(this);
        if (id == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("push_token", id);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(this).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(this).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiClient.appuser_updatepushtoken(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {

            }

            @Override
            public void onApiFailure(com.squareup.okhttp.Request request, Exception e) {

            }
        });

    }
}
