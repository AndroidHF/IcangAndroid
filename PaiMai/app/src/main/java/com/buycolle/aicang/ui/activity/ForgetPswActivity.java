package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.service.RegisterCodeTimerService;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.Md5Util;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.Validator;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/4.
 */
public class ForgetPswActivity extends BaseActivity {

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
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_phone_code)
    EditText etPhoneCode;

    private TimeHandler mCodeHandler = new TimeHandler(this);
    private Intent mIntent;

    private boolean pswIsVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        ButterKnife.bind(this);
        initTimer();
        myHeader.init("找回密码", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validator.isMobile(etPhone.getText().toString())) {
                    UIHelper.t(mContext, "请输入正确的电话号码");
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
            }
        });

    }

    private void subMit() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            UIHelper.t(mContext, "电话号码不能为空");
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
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_login_pwd", Md5Util.getEncodeByMD5(etPsw.getText().toString().trim()));
            jsonObject.put("user_login_name", etPhone.getText().toString().trim());
            jsonObject.put("verific_code", etPhoneCode.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.login_forgetloginpwd(jsonObject, new ApiCallback() {
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
                        UIHelper.t(mContext, "设置成功");
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

    static class TimeHandler extends Handler {
        private WeakReference<ForgetPswActivity> weakReference;

        public TimeHandler(ForgetPswActivity activity) {
            weakReference = new WeakReference<ForgetPswActivity>(activity);
        }

        public void clear() {
            weakReference.clear();
        }

        @Override
        public void dispatchMessage(Message msg) {
            ForgetPswActivity activity = this.weakReference.get();
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
            tvGetCode.setText("重获验证码");
            tvGetCode.setClickable(true);
            tvGetCode.setEnabled(true);
            setBtnRegisterCaptchaClick(true);
        }
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

    /**
     * 获取手机验证码
     */
    private void getPhontCode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", 2);
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

}
