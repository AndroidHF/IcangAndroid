package com.buycolle.aicang.ui.activity.usercentermenu.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.QuestionTypeActivity;
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
    @Bind(R.id.rl_question_type)
    RelativeLayout rlQuestionType;
    @Bind(R.id.tv_question_status_value)
    TextView tvQuestionValue;
    private final int QUESTION_TYPE = 666;
    private String opinion_type = "";//问题类型id
    private String question_type = "";//问题类型内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        myHeader.init("联系客服", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });

        rlQuestionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jumpForResult(mActivity, QuestionTypeActivity.class, QUESTION_TYPE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);

                if (TextUtils.isEmpty(question_type)){
                    UIHelper.t(mContext, "请选择您的问题类型");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    UIHelper.t(mContext, "请写下您的反馈意见");
                    btnSave.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(etContact.getText().toString().trim())){
                    UIHelper.t(mContext, "请填写您的联系方式");
                    btnSave.setEnabled(true);
                    return;
                }
                submit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUESTION_TYPE && resultCode == mActivity.RESULT_OK){
            opinion_type = data.getStringExtra("dir_id");
            question_type = data.getStringExtra("item_name");
            tvQuestionValue.setText(data.getStringExtra("item_name"));
            tvQuestionValue.setTextColor(mActivity.getResources().getColor(R.color.black));
        }
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("opinion_desc", etInput.getText().toString());
            if (!TextUtils.isEmpty(etContact.getText().toString().trim())) {
                jsonObject.put("tel", etContact.getText().toString());
            }
            if (!TextUtils.isEmpty(tvQuestionValue.getText().toString().trim())){
                jsonObject.put("opinion_type",opinion_type);
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
                        SubmitSuccessView();
                        //finish();
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

    /**
     * 数据提交后，还原界面
     */
    private void SubmitSuccessView() {
        question_type = "";
        opinion_type = "";

        tvQuestionValue.setText("请选择");
        tvQuestionValue.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));

        etInput.setText("请简要描述您所遇到的问题");
        etInput.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));

        etContact.setText("请留下您的QQ或邮箱地址");
        etContact.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
    }
}
