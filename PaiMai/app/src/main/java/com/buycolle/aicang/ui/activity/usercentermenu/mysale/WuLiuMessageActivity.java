package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

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
import com.buycolle.aicang.bean.WuLiuMsgBean;
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

/**
 * Created by joe on 16/3/6.
 */
public class WuLiuMessageActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_wuliu_company_value)
    TextView tvWuliuCompanyValue;
    @Bind(R.id.rl_wuliu_company)
    RelativeLayout rlWuliuCompany;
    @Bind(R.id.tv_goods_status_value)
    EditText tvGoodsStatusValue;
    @Bind(R.id.btn_save)
    Button btnSave;


    @Bind(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @Bind(R.id.tv_buyer_tel)
    TextView tvBuyerTel;
    @Bind(R.id.tv_buyer_address)
    TextView tvBuyerAddress;

    private int id;
    private int wuliuCompayNo;
    private String wuliuCompayName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuliumsg);
        ButterKnife.bind(this);
        myHeader.init("填写物流信息", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });

        id = _Bundle.getInt("id");
        rlWuliuCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResult(mActivity, WuLiuCompanyListActivity.class, 1000);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(wuliuCompayName)) {
                    UIHelper.t(mContext, "请选择物流公司");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(tvGoodsStatusValue.getText().toString().trim())) {
                    UIHelper.t(mContext, "请填写包裹订单");
                    btnSave.setEnabled(true);
                    return;
                }
                submit();
            }
        });

        loadData();

    }

    private WuLiuMsgBean wuLiuMsgBean;

    /**
     * 加载买家物流信息
     */
    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", id);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.winproductorder_getreceiptinfosbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject msg = resultObj.getJSONObject("infos");
                        wuLiuMsgBean = new Gson().fromJson(msg.toString(), WuLiuMsgBean.class);

                        tvBuyerName.setText("收货人：" + wuLiuMsgBean.getReceipt_name());
                        tvBuyerTel.setText("TEL：" + wuLiuMsgBean.getReceipt_tel());
                        tvBuyerAddress.setText(wuLiuMsgBean.getProvince() + wuLiuMsgBean.getCity() + wuLiuMsgBean.getDistrict() + wuLiuMsgBean.getReceipt_address());

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
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });


    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("express_bill_num", tvGoodsStatusValue.getText().toString());
            jsonObject.put("product_id", id);
            jsonObject.put("express_id", wuliuCompayNo);
            jsonObject.put("express_name", wuliuCompayName);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.winproductorder_submitexpressinfosbysellerapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                btnSave.setEnabled(true);
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, R.string.up_success);
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
                btnSave.setEnabled(true);
                UIHelper.t(mContext, R.string.net_error);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            wuliuCompayNo = data.getIntExtra("id", 0);
            wuliuCompayName = data.getStringExtra("name");
            tvWuliuCompanyValue.setText(data.getStringExtra("name"));
            tvWuliuCompanyValue.setTextColor(getResources().getColor(R.color.black_tv));
        }
    }
}
