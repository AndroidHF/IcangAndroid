package com.example.hufeng.views;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hufeng.aicangproject.BuildConfig;
import com.example.hufeng.aicangproject.LoginActivity;
import com.example.hufeng.aicangproject.R;
import com.example.hufeng.com.example.hufeng.beans.ProvinceBean;
import com.example.hufeng.com.example.hufeng.utils.LoginConfig;
import com.example.hufeng.com.example.hufeng.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hufeng on 2016/4/11.
 */
public class MainApplication extends MultiDexApplication {
    private ArrayList<Activity> allActivity = new ArrayList<>();
    public static String cityJson;
    private ACache aCache;

    public String[] mProvinceDatas;
    public Map<String,String[]> mCitisDataMap = new HashMap<>();
    public Map<String,String[]> mDistrictDatasMap = new HashMap<>();

    public void addActivity(Activity act){
        allActivity.add(act);
    }

    private static MainApplication mainApplication;
    public static final int LOGIN_REQUESTCODE_BY_FRAG = 999;
    OkHttpClient client;



    public void exitActivity(){
        for (int i = 0;i< allActivity.size();i++){
            Activity act = allActivity.get(i);
            if (act != null){
                act.finish();
            }
        }
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        aCache = ACache.get(this);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        client = new OkHttpClient();
        client.setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        initCity();

    }

    private void initCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cityJson = AppJsonFileReader.getJson(getBaseContext(),"area.json");
                try {
                    JSONArray jsonArray = new JSONArray(cityJson);
                    ArrayList<ProvinceBean> allDataArrayList = new Gson().fromJson(jsonArray.toString(),new TypeToken<ProvinceBean>(){}.getType());
                    int allProvinceSize = allDataArrayList.size();
                    mProvinceDatas = new String[allProvinceSize];
                    for (int i = 0;i<allProvinceSize;i++){
                        mProvinceDatas[i] = allDataArrayList.get(i).getArea_name();
                        String[] cityNames = new String[allDataArrayList.get(i).getCityList().size()];
                        for (int i1 = 0;i1<allDataArrayList.get(i).getCityList().size();i1++){
                            cityNames[i1] = allDataArrayList.get(i).getCityList().get(i1).getArea_name();
                            //String[] dists = new String[allDataArrayList.get(i).getDistincList().size()];
                            for (int i2 = 0;i2<allDataArrayList.get(i).getCityList().get(i1).getDistincList().size();i2++){
                                //dists[i2] = allDataArrayList.get(i).getCityList().get(i1).getDistincList().get(i2).getArea_name();
                            }
                            //mDistrictDatasMap.put(allDataArrayList.get(i).getCityList().get(i1).getArea_name(),dists);
                        }
                        mCitisDataMap.put(allDataArrayList.get(i).getArea_name(),cityNames);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isLogin(){
        return LoginConfig.isLogin(this);
    }

    public boolean isSaller(){
       return LoginConfig.getUserInfo(this).getUser_type() == 2 && isLogin();
    }

    public void gotoLogInByFrag(Fragment fragment){
        UIHelper.jump(fragment.getContext(), LoginActivity.class);
    }

    public void setImages(String url,final int position,final ImageView imageView){
        Glide.with(this)
                .load(url)
                .fitCenter()
                .dontAnimate()
                .placeholder(R.mipmap.default_image)
                .into(imageView);
    }

    public void updatePushId(){
        String id = JPushInterface.getRegistrationID(this);
        if (id == null){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("push_token",id);
            jsonObject.put("sessionid",LoginConfig.getUserInfo(this).getSessionid());
            jsonObject.put("user_id",LoginConfig.getUserInfo(this).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
