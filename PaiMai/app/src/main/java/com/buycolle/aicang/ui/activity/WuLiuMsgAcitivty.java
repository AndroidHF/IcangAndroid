package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.WuLiuMsgBean;
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
 * Created by joe on 16/4/25.
 */
public class WuLiuMsgAcitivty extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @Bind(R.id.tv_buyer_tel)
    TextView tvBuyerTel;
    @Bind(R.id.tv_buyer_address)
    TextView tvBuyerAddress;
    @Bind(R.id.tv_wuliu_company)
    TextView tvWuliuCompany;
    @Bind(R.id.tv_wuliu_coder)
    TextView tvWuliuCoder;


    private int id;
    private WuLiuMsgBean wuLiuMsgBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuliu_msg);
        ButterKnife.bind(this);
        myHeader.init("查看发货信息", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        id = _Bundle.getInt("id");
        loadData();
    }

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

                        tvWuliuCompany.setText("物流公司：" + wuLiuMsgBean.getExpress_name());
                        tvWuliuCoder.setText("包裹单号：" + wuLiuMsgBean.getExpress_bill_num());

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
}
