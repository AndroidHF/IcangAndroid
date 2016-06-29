package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PaiPinDetailBean;
import com.buycolle.aicang.event.MyBuyCommentEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/6.
 */
public class CommentActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_paimai)
    ImageView ivPaimai;
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;
    @Bind(R.id.tv_good_status)
    TextView tvGoodStatus;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.ll_good_comment_lay)
    LinearLayout llGoodCommentLay;
    @Bind(R.id.ll_bad_comment_lay)
    LinearLayout llBadCommentLay;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.tv_good)
    TextView tvGood;
    @Bind(R.id.tv_bad)
    TextView tvBad;


    private PaiPinDetailBean paiPinDetailBean;

    private int product_id;

    private boolean hasInitGoodOrBad = false;
    private boolean is_good = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        product_id = _Bundle.getInt("product_id");
        myHeader.init("评价", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        loadData(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入您的评论内容");
                    return;
                }
                if (!hasInitGoodOrBad) {
                    UIHelper.t(mContext, "请你选择好评还是坏评论");
                    return;
                }

                btnSave.setEnabled(false);
                submit();

            }
        });

        llGoodCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStatus(1);
            }
        });
        llBadCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStatus(2);
            }
        });
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);
            jsonObject.put("is_good", is_good ? 1 : 2);
            jsonObject.put("eval_content", etContent.getText().toString());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.productevaluate_submitwinorder(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                btnSave.setEnabled(true);
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "评论成功");
                        EventBus.getDefault().post(new MyBuyCommentEvent(0));
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
                btnSave.setEnabled(true);
                UIHelper.t(mContext, R.string.net_error);
                dismissLoadingDialog();
            }
        });
    }


    private void loadData(final boolean isAction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getpreauctdetailidbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isAction) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        paiPinDetailBean = new Gson().fromJson(userInfoObj.toString(), PaiPinDetailBean.class);
                        initData();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!isAction) {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                if (!isAction) {
                    dismissLoadingDialog();
                }
            }
        });
    }

    private void initData() {
        mApplication.setImages(paiPinDetailBean.getCover_pic(), ivPaimai);
        tvGoodTitle.setText(paiPinDetailBean.getProduct_title());
        tvGoodStatus.setText(paiPinDetailBean.getSt_name());
        tvContent.setText(paiPinDetailBean.getProduct_desc());
    }

    private void initStatus(int type) {
        hasInitGoodOrBad = true;
        if (type == 1) {//好的
            is_good = true;
            tvGood.setTextColor(getResources().getColor(R.color.black_tv));
            tvBad.setTextColor(getResources().getColor(R.color.gray_tv));
            //add by :胡峰，评价选中时候的背景色的添加
            llGoodCommentLay.setBackgroundResource(R.color.orange);
            llBadCommentLay.setBackgroundResource(R.color.transparent);
        } else {
            is_good = false;
            tvGood.setTextColor(getResources().getColor(R.color.gray_tv));
            tvBad.setTextColor(getResources().getColor(R.color.black_tv));
            //add by :胡峰，评价选中时候的背景色的添加
            llGoodCommentLay.setBackgroundResource(R.color.transparent);
            llBadCommentLay.setBackgroundResource(R.color.orange);
        }
    }
}
