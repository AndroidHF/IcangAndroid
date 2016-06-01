package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PayBean;
import com.buycolle.aicang.bean.WXPayBean;
import com.buycolle.aicang.event.EditAddressEvent;
import com.buycolle.aicang.event.WXZhifuEvent;
import com.buycolle.aicang.event.ZhifueEvent;
import com.buycolle.aicang.pay.PayResult;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.userinfo.EditAddressActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/6.
 */
public class ZhiFuActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_edit_address)
    TextView tvEditAddress;
    @Bind(R.id.tv_price_title)
    TextView tvPriceTitle;
    @Bind(R.id.tv_price_value)
    TextView tvPriceValue;
    @Bind(R.id.tv_submit_pay)
    TextView tvSubmitPay;
    @Bind(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @Bind(R.id.iv_paimai)
    ImageView ivPaimai;
    @Bind(R.id.iv_range)
    ImageView ivRange;
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;
    @Bind(R.id.tv_good_status)
    TextView tvGoodStatus;
    @Bind(R.id.tv_good_desc)
    TextView tvGoodDesc;
    @Bind(R.id.tv_address_user_name)
    TextView tvAddressUserName;
    @Bind(R.id.tv_address_user_tel)
    TextView tvAddressUserTel;
    @Bind(R.id.tv_address_user_address)
    TextView tvAddressUserAddress;
    @Bind(R.id.tv_send_msg_status)
    TextView tvSendMsgStatus;
    @Bind(R.id.tv_send_msg_address)
    TextView tvSendMsgAddress;
    @Bind(R.id.tv_send_msg_time)
    TextView tvSendMsgTime;
    @Bind(R.id.iv_zhifubao_pay_icon)
    ImageView ivZhifubaoPayIcon;
    @Bind(R.id.tv_zhifubao_pay_title)
    TextView tvZhifubaoPayTitle;
    @Bind(R.id.ll_zhifubao_pay_lay)
    LinearLayout llZhifubaoPayLay;
    @Bind(R.id.iv_wechat_pay_icon)
    ImageView ivWechatPayIcon;
    @Bind(R.id.iv_wechat_pay_title)
    TextView ivWechatPayTitle;
    @Bind(R.id.ll_wechat_pay_lay)
    LinearLayout llWechatPayLay;

    private int product_id;
    private PayBean paiPinDetailBean;

    private boolean addressHasInit = false;
    private IWXAPI api;


    public void onEventMainThread(EditAddressEvent event) {
        loadData(true);
    }

    public void onEventMainThread(WXZhifuEvent event) {
        if (event.getStatus() == 0) {
            Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
            finish();
            EventBus.getDefault().post(new ZhifueEvent(1));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhifu);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        myHeader.init("支付", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        if (_Bundle != null) {
            product_id = _Bundle.getInt("product_id");
            tvEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.jump(mActivity, EditAddressActivity.class);
                }
            });
            loadData(false);
            initPayLayVisible(1);

            llZhifubaoPayLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPayLayVisible(1);
                }
            });
            llWechatPayLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPayLayVisible(2);
                }
            });
            tvSubmitPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSubmitPay.setEnabled(false);
                    if (addressHasInit) {
                        getOrderInfo(isZhiFuBaoPay);
                    } else {
                        tvSubmitPay.setEnabled(true);
                        UIHelper.t(mContext, "请您完善您的地址信息");
                    }
                }
            });
        }
    }

    private Handler alipayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new ZhifueEvent(1));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 获取订单信息
     *
     * @param isZhiFuBaoPay
     */
    private void getOrderInfo(boolean isZhiFuBaoPay) {
        if (isZhiFuBaoPay) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("order_no", paiPinDetailBean.getOrder_no());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mApplication.apiClient.payrecord_buildaliprepay(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {
                    showLoadingDialog("生成订单中...");
                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing())
                        return;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            final String payInfo = resultObj.getString("pay_str");
                            new Thread() {
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(mActivity);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(payInfo, true);
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = result;
                                    alipayHandler.sendMessage(msg);
                                }
                            }.start();
                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tvSubmitPay.setEnabled(true);
                    dismissLoadingDialog();
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing())
                        return;
                    tvSubmitPay.setEnabled(true);
                    UIHelper.t(mContext, R.string.net_error);
                    dismissLoadingDialog();
                }
            });
        } else {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("order_no", paiPinDetailBean.getOrder_no());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mApplication.apiClient.payrecord_buildwxprepay(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {
                    showLoadingDialog("生成订单中...");
                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing())
                        return;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            JSONObject payInfo = resultObj.getJSONObject("infos");
                            WXPayBean orderInfo = new Gson().fromJson(payInfo.toString(), WXPayBean.class);

                            api = WXAPIFactory.createWXAPI(mActivity, orderInfo.getAppid());

                            PayReq req = new PayReq();
                            req.appId = orderInfo.getAppid();
                            req.partnerId = orderInfo.getPartnerid();
                            req.prepayId = orderInfo.getPrepayid();
                            req.nonceStr = orderInfo.getNoncestr();
                            req.timeStamp = orderInfo.getTimestamp();
                            req.packageValue = payInfo.getString("package");
                            req.sign = orderInfo.getPaySign();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.registerApp(orderInfo.getAppid());
                            api.sendReq(req);

                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tvSubmitPay.setEnabled(true);
                    dismissLoadingDialog();
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing())
                        return;
                    tvSubmitPay.setEnabled(true);
                    UIHelper.t(mContext, R.string.net_error);
                    dismissLoadingDialog();
                }
            });
        }


    }


    private boolean isZhiFuBaoPay = true;

    /**
     * 初始化支付视图
     *
     * @param type 1表示支付宝 2表示微信
     */
    private void initPayLayVisible(int type) {
        if (type == 1) {
            isZhiFuBaoPay = true;
            ivZhifubaoPayIcon.setImageResource(R.drawable.commen_zhifubao_icon);
            tvZhifubaoPayTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
            ivWechatPayTitle.setTextColor(ContextCompat.getColor(mContext, R.color.gray_tv));
            ivWechatPayIcon.setImageResource(R.drawable.wechat_gray);
        } else {
            isZhiFuBaoPay = false;
            ivWechatPayTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
            ivWechatPayIcon.setImageResource(R.drawable.wechat_sel_icon);
            ivZhifubaoPayIcon.setImageResource(R.drawable.zhifubao_unsel_icon);
            tvZhifubaoPayTitle.setTextColor(ContextCompat.getColor(mContext, R.color.gray_tv));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadData(final boolean isAction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.winproductorder_getprepayinfosbyapp(jsonObject, new ApiCallback() {
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
                        paiPinDetailBean = new Gson().fromJson(userInfoObj.toString(), PayBean.class);
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

        tvGoodTitle.setText(paiPinDetailBean.getProduct_title());
        tvGoodStatus.setText(paiPinDetailBean.getSt_name());
        tvGoodDesc.setText(paiPinDetailBean.getProduct_desc());
        mApplication.setImages(paiPinDetailBean.getCover_pic(), ivPaimai);
        //发货信息
        if (paiPinDetailBean.getExpress_out_type() == 1) {
            tvSendMsgStatus.setText("运费承担方            买家");
        } else {
            tvSendMsgStatus.setText("运费承担方            卖家");
        }
        tvSendMsgAddress.setText("发货地区                " + paiPinDetailBean.getFahou_city());
        if (paiPinDetailBean.getFahuo_time_type()==1) {
            tvSendMsgTime.setText("预计发货时间        " + "当天发货");
        } else if (paiPinDetailBean.getFahuo_time_type()==2) {
            tvSendMsgTime.setText("预计发货时间        " + "1-3天");
        } else if (paiPinDetailBean.getFahuo_time_type()==3) {
            tvSendMsgTime.setText("预计发货时间        " + "1周以内");
        } else if (paiPinDetailBean.getFahuo_time_type()==4) {
            tvSendMsgTime.setText("预计发货时间        " + "2-3周以内");
        }
        if (paiPinDetailBean.getCj_type() == 1) {//没有开启一口价
            tvPriceTitle.setText("竞拍价");
        } else {
            tvPriceTitle.setText("一口价");
        }
        tvPriceValue.setText("￥" + StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getOrder_price()));


        if (!TextUtils.isEmpty(paiPinDetailBean.getReceipt_name())) {
            addressHasInit = true;
            tvAddressUserName.setText("收货人：" + paiPinDetailBean.getReceipt_name());
            tvAddressUserTel.setText("TEL：" + paiPinDetailBean.getReceipt_tel());
            tvAddressUserAddress.setText("地址：" + paiPinDetailBean.getProvince() + paiPinDetailBean.getCity() + paiPinDetailBean.getDistrict() + paiPinDetailBean.getReceipt_address());
        } else {
            addressHasInit = false;
            tvAddressUserName.setText("收货人：" + "");
            tvAddressUserTel.setText("TEL：" + "");
            tvAddressUserAddress.setText("地址：" + "");
        }

    }

}
