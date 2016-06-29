package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainActivity;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.util.KeyboardWatcher;
import com.buycolle.aicang.util.Md5Util;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/2.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_psw_visible)
    ImageView iv_psw_visible;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.tv_forget_psw)
    TextView tvForgetPsw;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;

    KeyboardWatcher keyboardWatcher;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    private boolean isDoubleLogin = false;


    private boolean pswIsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setPswVisible(pswIsVisible);

        isDoubleLogin = getIntent().getBooleanExtra("isDoubleLogin", false);
        if(isDoubleLogin){
            LoginConfig.clear(mContext);
            EventBus.getDefault().post(new LogOutEvent());
        }

        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(new KeyboardWatcher.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShown(int keyboardSize) {
                KLog.d("过来了---onKeyboardShown--", "----" + keyboardSize);
            }

            @Override
            public void onKeyboardClosed() {
                KLog.d("过来了---onKeyboardClosed--", "----onKeyboardClosed");

            }
        });
        PhoneUtil.setSoftKeyBordWH(llRoot, mContext);

    }

    @OnClick(R.id.btn_login)
    public void login() {
        btnLogin.setEnabled(false);
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            UIHelper.t(mContext, "请输入手机号码");
            btnLogin.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(etPsw.getText().toString().trim())) {
            UIHelper.t(mContext, "请输入密码");
            btnLogin.setEnabled(true);
            return;
        }
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
                        btnLogin.setEnabled(true);
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
                btnLogin.setEnabled(true);
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (isDoubleLogin) {
                Intent intent1 = new Intent(mContext, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.tv_forget_psw)
    public void forgetPsw() {
        UIHelper.jump(this, ForgetPswActivity.class);
    }

    @OnClick(R.id.iv_close)
    public void close() {
        if (isDoubleLogin) {
            Intent intent1 = new Intent(mContext, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        finish();
    }

    @OnClick(R.id.btn_register)
    public void register() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromLogin", true);
        UIHelper.jump(this, RegisterActivity.class, bundle);
    }

    @OnClick(R.id.iv_psw_visible)
    public void visiblePsw() {
        setPswVisible(pswIsVisible);
        pswIsVisible = pswIsVisible ? false : true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void setPswVisible(boolean flag) {
        if (flag) {
            etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iv_psw_visible.setImageResource(R.drawable.login_psw_vislble);
        } else {
            etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iv_psw_visible.setImageResource(R.drawable.login_psw_unvislble);
        }
        etPsw.setSelection(etPsw.getText().toString().length());
    }

}
