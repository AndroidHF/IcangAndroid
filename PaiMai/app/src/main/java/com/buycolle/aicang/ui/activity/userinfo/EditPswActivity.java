package com.buycolle.aicang.ui.activity.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.Md5Util;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joe on 16/3/4.
 */
public class EditPswActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_psw)
    ImageView ivPsw;
    @Bind(R.id.iv_psw_visible)
    ImageView ivPswVisible;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.iv_psw_1)
    ImageView ivPsw1;
    @Bind(R.id.iv_psw_visible_1)
    ImageView ivPswVisible1;
    @Bind(R.id.et_psw_1)
    EditText etPsw1;
    @Bind(R.id.btn_save)
    Button btnSave;
    private boolean pswIsVisible = false;
    private boolean pswIsVisible_1 = false;


    @OnClick(R.id.iv_psw_visible)
    public void visiblePsw() {
        setPswVisible(pswIsVisible);
        pswIsVisible = pswIsVisible ? false : true;
    }
    @OnClick(R.id.iv_psw_visible_1)
    public void visiblePsw_1() {
        setPswVisible_1(pswIsVisible_1);
        pswIsVisible_1 = pswIsVisible_1 ? false : true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_psw);
        ButterKnife.bind(this);
        myHeader.init("修改密码", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(etPsw.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入旧密码");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etPsw1.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入新密码");
                    btnSave.setEnabled(true);
                    return;
                }else if (etPsw1.getText().toString().trim().length() < 6 || etPsw1.getText().toString().trim().length() > 20){
                    UIHelper.t(mContext, "请输入新密码为6-20个字符");
                    btnSave.setEnabled(true);
                    return;
                }
                updateUserInfo(etPsw.getText().toString().trim(),etPsw1.getText().toString().trim());
            }
        });
    }

    private void updateUserInfo(final String oldPsw,final String newPsw) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_login_pwd", Md5Util.getEncodeByMD5(newPsw));
            jsonObject.put("old_login_pwd", Md5Util.getEncodeByMD5(oldPsw));
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.login_updateloginpwd(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("更新中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    btnSave.setEnabled(true);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, R.string.update_success);
                        finish();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.t(mContext,R.string.net_error);
                btnSave.setEnabled(true);
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
    private void setPswVisible_1(boolean flag) {
        if (flag) {
            etPsw1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPswVisible1.setImageResource(R.drawable.login_psw_vislble);
        } else {
            etPsw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPswVisible1.setImageResource(R.drawable.login_psw_unvislble);
        }
        etPsw1.setSelection(etPsw1.getText().toString().length());
    }
}
