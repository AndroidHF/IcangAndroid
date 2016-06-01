package com.buycolle.aicang.ui.activity.usercentermenu.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/4.
 */
public class FeedBackActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.et_contact)
    EditText etContact;
    @Bind(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        myHeader.init("意见反馈", new MyHeader.Action() {
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
                    UIHelper.t(mContext, "请写下您的反馈意见");
                    btnSave.setEnabled(true);
                    return;
                }
                submit();
            }
        });
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("opinion_desc", etInput.getText().toString());
            if (!TextUtils.isEmpty(etContact.getText().toString().trim())) {
                jsonObject.put("tel", etContact.getText().toString());
            }
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("supply_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.opinion_submitbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (JSONUtil.isOK(jsonObject1)) {
                        UIHelper.t(mContext, R.string.up_success);
                        finish();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject1));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btnSave.setEnabled(true);
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                btnSave.setEnabled(true);
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }
}
