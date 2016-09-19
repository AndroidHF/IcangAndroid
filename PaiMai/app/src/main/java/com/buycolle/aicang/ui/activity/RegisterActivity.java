package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.service.RegisterCodeTimerService;
import com.buycolle.aicang.ui.activity.usercentermenu.setting.UserDealActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.Md5Util;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.Validator;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/3.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.ll_get_code)
    LinearLayout llGetCode;
    @Bind(R.id.iv_psw)
    ImageView ivPsw;
    @Bind(R.id.iv_psw_visible)
    ImageView ivPswVisible;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.rl_psw)
    RelativeLayout rlPsw;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_phone_code)
    EditText etPhoneCode;
    //add by hufeng :注册的协议
    @Bind(R.id.tv_xieyi)
    TextView tv_xieyi;//协议内容
    @Bind(R.id.cb_xieyi_status)
    CheckBox cb_xieyi_status;//是否选中
    @Bind(R.id.et_version_code)
    EditText etVersionCode;//邀请码
    private TimeHandler mCodeHandler = new TimeHandler(this);
    private Intent mIntent;

    private boolean pswIsVisible = false;

    private boolean isFromLogin = false;

    private void setPswVisible(boolean flag) {
        if (flag) {
            etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPswVisible.setImageResource(R.drawable.login_psw_vislble);
        } else {
            etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPswVisible.setImageResource(R.drawable.login_psw_unvislble);
        }
        etPsw.setSelection(etPsw.getText().toString().length());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);
        ButterKnife.bind(this);
        initTimer();
        if (_Bundle != null) {
            isFromLogin = _Bundle.getBoolean("isFromLogin", false);
        }
        myHeader.init("注册", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validator.isMobile(etPhone.getText().toString())) {
                    UIHelper.t(mContext, "请输入正确的手机号码");
                    return;
                }
                tvGetCode.setEnabled(false);
                getPhontCode();

            }
        });
        ivPswVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPswVisible(pswIsVisible);
                pswIsVisible = pswIsVisible ? false : true;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subMit();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    new LoginActivity().btnLogin.callOnClick();
//                }
//                UIHelper.jump(mActivity, MainActivity.class);
            }
        });

        /**
         * add by :胡峰
         * app协议的监听
         */
        tv_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(RegisterActivity.this, UserDealActivity.class);
            }
        });

    }


    /**
     * 获取手机验证码
     */
    private void getPhontCode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", 1);
            jsonObject.put("user_login_name", etPhone.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.login_getphonecheckcodebyordinaryuser(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "已成功发送手机验证码");
                        setBtnRegisterCaptchaClick(false);
                        startService(mIntent);
                    } else {
                        tvGetCode.setEnabled(true);
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                tvGetCode.setEnabled(true);
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    private void subMit() {
        btnRegister.setEnabled(false);
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            UIHelper.t(mContext, "昵称不能为空");
            btnRegister.setEnabled(true);
            return;
        }else try {
            if (etUsername.getText().toString().trim().getBytes("GBK").length>20){
                Log.i("hufeng --",etUsername.getText().toString().trim().getBytes().length+"");
                UIHelper.t(mContext, "昵称请输入20个字节内");
                btnRegister.setEnabled(true);
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            UIHelper.t(mContext, "请输入手机号码");
            btnRegister.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(etPhoneCode.getText().toString())) {
            UIHelper.t(mContext, "短信验证码不能为空");
            btnRegister.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(etPsw.getText().toString())) {
            UIHelper.t(mContext, "密码不能为空");
            btnRegister.setEnabled(true);
            return;
        }else if(etPsw.getText().toString().trim().length() < 6 || etPsw.getText().toString().trim().length() > 20){
            UIHelper.t(mContext,"请输入6-20个字符");
            btnRegister.setEnabled(true);
            return;
        }

        //邀请码的逻辑
        if (etVersionCode.getText().toString().trim().length() == 0){
            etVersionCode.setText("");
            btnRegister.setEnabled(true);
        } else if (etVersionCode.getText().toString().trim().length() > 20) {
            UIHelper.t(mContext,"请输入正确的邀请码");
            btnRegister.setEnabled(true);
            return;
        }

        //add by hufeng:注册协议逻辑
        if(!cb_xieyi_status.isChecked()){
            UIHelper.t(mContext,"您必须同意用户协议");
            btnRegister.setEnabled(true);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_login_pwd", Md5Util.getEncodeByMD5(etPsw.getText().toString().trim()));
            jsonObject.put("user_phone", etPhone.getText().toString().trim());
            jsonObject.put("user_nick", etUsername.getText().toString().trim());
            jsonObject.put("verific_code", etPhoneCode.getText().toString().trim());
            jsonObject.put("invitation_code",etVersionCode.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.login_register2ordinaryuser(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "注册成功");
                        if (!isFromLogin) {
                            UIHelper.jump(mActivity, LoginActivity.class);
////                            login();
////                            new LoginActivity().login();
////                            UIHelper.jump(mActivity, MainActivity.class);
//                            EventBus.getDefault().post(new LoginEvent());

                        }
                        finish();
                    } else {
                        btnRegister.setEnabled(true);
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                btnRegister.setEnabled(true);
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    private void setBtnRegisterCaptchaClick(boolean isClickable) {
        if (isClickable) {
            tvGetCode.setBackgroundResource(R.color.orange);
            tvGetCode.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
        } else {
            tvGetCode.setBackgroundResource(R.color.bg_gray);
            tvGetCode.setTextColor(ContextCompat.getColor(mContext, R.color.gray_tv));
        }
    }

    private void initTimer() {
        RegisterCodeTimerService.setHandler(mCodeHandler);
        mIntent = new Intent(this, RegisterCodeTimerService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
        ButterKnife.unbind(this);
        mCodeHandler.clear();

    }

    static class TimeHandler extends Handler {
        private WeakReference<RegisterActivity> weakReference;

        public TimeHandler(RegisterActivity activity) {
            weakReference = new WeakReference<RegisterActivity>(activity);
        }

        public void clear() {
            weakReference.clear();
        }

        @Override
        public void dispatchMessage(Message msg) {
            RegisterActivity activity = this.weakReference.get();
            if (activity != null) {
                activity.updateCaptchaText((int) msg.obj);
            }
            super.dispatchMessage(msg);
        }
    }

    /**
     * 更新显示时间
     *
     * @param time
     */
    private void updateCaptchaText(int time) {
        if (time >= 0) {
            StringBuffer strBuffer = new StringBuffer("获取验证码");
            strBuffer.append("(");
            strBuffer.append(time);
            strBuffer.append(")");
            tvGetCode.setText(new String(strBuffer));
        } else {
            tvGetCode.setText("获取验证码");
            tvGetCode.setClickable(true);
            tvGetCode.setEnabled(true);
            setBtnRegisterCaptchaClick(true);
        }
    }

    public void login(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_login_pwd", Md5Util.getEncodeByMD5(etPsw.getText().toString().trim()));
            jsonObject.put("user_login_name", etPhone.getText().toString().trim());
            jsonObject.put("platfrom", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.login_byappordinaryuser(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("登录中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("resultInfos");
                        UserBean userBean = new Gson().fromJson(userInfoObj.toString(), UserBean.class);
                        LoginConfig.updateUserInfoPassWord(mContext, Md5Util.getEncodeByMD5(etPsw.getText().toString().trim()));
                        LoginConfig.setUserInfo(mContext, userBean);
                        EventBus.getDefault().post(new LoginEvent());
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }
}
