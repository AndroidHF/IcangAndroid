package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainActivity;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by joe on 16/3/2.
 */
public class SplashActivity extends BaseActivity {

    private static final int sleepTime = 3000;
    /****
     * add by :胡峰
     */
    public static final int VERSION =  1;
    public static SharedPreferences sharedPreferences;
    private boolean isPush= false;
    private int id=0;
    private int type=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        isPush = getIntent().getBooleanExtra("isPush",false);
        /***
         * add by :胡峰，个推引入
         */
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(LoginConfig.isLogin(mApplication)){
            login();
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
                if(isPush){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isPush",true);
                    bundle.putInt("type",type);
                    bundle.putInt("id",id);
                    UIHelper.jump(mActivity,MainActivity.class,bundle);
                }else{
                    /****
                     * add by :胡峰，引导界面的跳转
                     */
//                    sharedPreferences = getSharedPreferences("Y_Setting", Context.MODE_PRIVATE);
//                    if (sharedPreferences.getInt("VERSION", 0) != VERSION) {
//                        UIHelper.jump(mActivity,GuideActivity.class);
//                    }else{
                        /***
                         * add by :胡峰
                         * 功能：分享界面的跳转
                         */
                        String action = getIntent().getAction();
                        if (Intent.ACTION_VIEW.equals(action)){
                            Uri uri = getIntent().getData();//获取跳转界面传递过来的数据
                            if (uri != null){
                                if (uri.getPath().equals("/item")||uri.getPath().equals("/event")){//对于一般拍品和拍卖会拍品跳转界面的标识
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("product_id",Integer.valueOf(uri.getQuery()));//将pruduct_id传递给要跳的界面
                                    UIHelper.jump(SplashActivity.this,PaiPinDetailActivity.class,bundle);
                                }else{//晒物界面的跳转标识
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("show_id",Integer.valueOf(uri.getQuery()));//将表示晒物的唯一标识show_id传递给跳转的界面
                                    UIHelper.jump(SplashActivity.this,ShowDetailActivity.class,bundle);
                                }
                            }
                        }else {
                            UIHelper.jump(mActivity,MainActivity.class);
                        }
//                    }
                    finish();
                }
                finish();
            }
        }).start();
    }

    public void login() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_login_pwd", LoginConfig.getUserInfoPassWord(mContext));
            jsonObject.put("user_login_name", LoginConfig.getUserInfo(mContext).getUser_phone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.login_byappordinaryuser(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("resultInfos");
                        UserBean userBean = new Gson().fromJson(userInfoObj.toString(), UserBean.class);
                        LoginConfig.setUserInfo(mApplication, userBean);
                    } else {
                        UIHelper.t(mApplication, JSONUtil.getServerMessage(resultObj));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
