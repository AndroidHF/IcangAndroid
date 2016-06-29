package com.buycolle.aicang.ui.activity.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.EditUserInfoEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.Validator;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/4.
 */
public class BindEmailActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);
        if(_Bundle!=null){
            etInput.setText(_Bundle.getString("email"));
        }
        myHeader.init("绑定电子邮箱", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入邮箱");
                    btnSave.setEnabled(true);
                    return;
                }
                if (!Validator.isEmail(etInput.getText().toString())) {
                    UIHelper.t(mContext, "请输入正确的邮箱格式");
                    btnSave.setEnabled(true);
                    return;
                }
                updateUserInfo(etInput.getText().toString().trim());
            }
        });
    }

    private void updateUserInfo(final String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", value);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appuser_updateinfosbyapp(jsonObject, new ApiCallback() {
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
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, R.string.update_success);
                        LoginConfig.updateUserInfoEmail(mContext, value);
                        EventBus.getDefault().post(new EditUserInfoEvent(2));
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
}
