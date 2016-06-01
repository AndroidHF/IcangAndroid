package com.buycolle.aicang.ui.activity.usercentermenu.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PushSettingBean;
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
 * Created by joe on 16/3/4.
 */
public class PushSettingActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;

    @Bind(R.id.iv_chaoyue)
    ImageView ivChaoyue;
    @Bind(R.id.iv_zhongbiao)
    ImageView ivZhongbiao;
    @Bind(R.id.iv_pay)
    ImageView ivPay;
    @Bind(R.id.iv_fahuo)
    ImageView ivFahuo;
    @Bind(R.id.ll_content_lay)
    LinearLayout llContentLay;

    private PushSettingBean pushSettingBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_setting);
        ButterKnife.bind(this);
        myHeader.init("推送设置", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        loadData();

        ivChaoyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0表示提醒 1表示不提醒
                ivChaoyue.setEnabled(false);
                set(1, pushSettingBean.getPm_price_tip() == 0 ? 1 : 0);
            }
        });
        ivZhongbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0表示提醒 1表示不提醒
                ivZhongbiao.setEnabled(false);
                set(2, pushSettingBean.getZp_tip() == 0 ? 1 : 0);
            }
        });
        ivPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0表示提醒 1表示不提醒
                ivPay.setEnabled(false);
                set(3, pushSettingBean.getPay_tip() == 0 ? 1 : 0);
            }
        });
        ivFahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0表示提醒 1表示不提醒
                ivFahuo.setEnabled(false);
                set(4, pushSettingBean.getFh_tip() == 0 ? 1 : 0);
            }
        });
    }

    private void set(final int type, final int value) {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (type == 1) {//超越
                jsonObject.put("pm_price_tip", value+"");
            } else if (type == 2) {//中标
                jsonObject.put("zp_tip", value+"");
            } else if (type == 3) {//付款
                jsonObject.put("pay_tip", value+"");
            } else if (type == 4) {//发货
                jsonObject.put("fh_tip", value+"");
            }
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.userpushcfg_setbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("设置中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (JSONUtil.isOK(jsonObject1)) {
                        UIHelper.t(mContext, R.string.setting_success);
                        loadData();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (type == 1) {//超越
                    ivChaoyue.setEnabled(true);
                } else if (type == 2) {//中标
                    ivZhongbiao.setEnabled(true);
                } else if (type == 3) {//付款
                    ivPay.setEnabled(true);
                } else if (type == 4) {//发货
                    ivFahuo.setEnabled(true);
                }
                dismissLoadingDialog();
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
                if (type == 1) {//超越
                    ivChaoyue.setEnabled(true);
                } else if (type == 2) {//中标
                    ivZhongbiao.setEnabled(true);
                } else if (type == 3) {//付款
                    ivPay.setEnabled(true);
                } else if (type == 4) {//发货
                    ivFahuo.setEnabled(true);
                }
            }
        });
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.userpushcfg_getbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("加载推送设置信息中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (JSONUtil.isOK(jsonObject1)) {
                        JSONObject userInfoObj = jsonObject1.getJSONObject("infos");
                        pushSettingBean = new Gson().fromJson(userInfoObj.toString(), PushSettingBean.class);
                        reSetStatus();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject1));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llContentLay.setVisibility(View.VISIBLE);
                dismissLoadingDialog();
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    private void reSetStatus() {
        if (pushSettingBean.getFh_tip() == 0) {//发货提醒 0表示提醒 1表示不提醒
            ivFahuo.setImageResource(R.drawable.setting_on);
        } else {
            ivFahuo.setImageResource(R.drawable.setting_off);
        }
        if (pushSettingBean.getPay_tip() == 0) {//付款提醒 0表示提醒 1表示不提醒
            ivPay.setImageResource(R.drawable.setting_on);
        } else {
            ivPay.setImageResource(R.drawable.setting_off);
        }
        if (pushSettingBean.getPm_price_tip() == 0) {//出价被超提醒 0表示提醒 1表示不提醒
            ivChaoyue.setImageResource(R.drawable.setting_on);
        } else {
            ivChaoyue.setImageResource(R.drawable.setting_off);
        }
        if (pushSettingBean.getZp_tip() == 0) {//中拍提醒 0表示提醒 1表示不提醒
            ivZhongbiao.setImageResource(R.drawable.setting_on);
        } else {
            ivZhongbiao.setImageResource(R.drawable.setting_off);
        }
    }
}
