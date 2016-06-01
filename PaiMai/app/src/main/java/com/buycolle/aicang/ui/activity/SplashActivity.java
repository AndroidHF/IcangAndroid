package com.buycolle.aicang.ui.activity;

import android.os.Bundle;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainActivity;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
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

    private boolean isPush= false;
    private int id=0;
    private int type=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        isPush = getIntent().getBooleanExtra("isPush",false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(LoginConfig.isLogin(mApplication)){
            login();
//            mApplication.updatePushId();
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
                    UIHelper.jump(mActivity,MainActivity.class);
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
