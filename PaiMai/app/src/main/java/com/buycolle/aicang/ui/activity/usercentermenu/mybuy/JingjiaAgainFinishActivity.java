package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PMResultBean;
import com.buycolle.aicang.event.ZhifueEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.StringFormatUtil;
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
 * Created by joe on 16/3/5.
 */
public class JingjiaAgainFinishActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_day)
    TextView tvDay;
    @Bind(R.id.tv_hour)
    TextView tvHour;
    @Bind(R.id.tv_min)
    TextView tvMin;
    @Bind(R.id.tv_secs)
    TextView tvSecs;
    @Bind(R.id.btn_pai_again)
    TextView btnPaiAgain;
    @Bind(R.id.iv_paimai)
    ImageView ivPaimai;
    @Bind(R.id.pai_status)
    TextView paiStatus;
    @Bind(R.id.iv_range)
    ImageView ivRange;
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;
    @Bind(R.id.tv_good_status)
    TextView tvGoodStatus;
    @Bind(R.id.tv_pai_count)
    TextView tvPaiCount;
    @Bind(R.id.tv_good_begin_price)
    TextView tvGoodBeginPrice;
    @Bind(R.id.rl_yikou_price)
    RelativeLayout rlYikouPrice;
    @Bind(R.id.tv_good_my_price)
    TextView tvGoodMyPrice;
    @Bind(R.id.tv_good_max_price)
    TextView tvGoodMaxPrice;
    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    @Bind(R.id.tv_yikou_price_value)
    TextView tvYikouPriceValue;
    @Bind(R.id.tv_good_time_title)
    TextView tvGoodTimeTitle;
    @Bind(R.id.tv_tixing)
    TextView tvTiXing;

    //    private int type = 1;
    private String value;
    private long time;

    private PMResultBean detailBean;

    private int id;

    public void onEventMainThread(ZhifueEvent event) {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingjiaagainfinish_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        myHeader.init("出价订单", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        if (_Bundle != null) {
            id = _Bundle.getInt("id");
            loadData(false);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isFinishing()) {
                        break;
                    }
                    boolean isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    if (time > 1000) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                        isNeedCountTime = true;
                        time = time - 1000;
                        String[] times = getTimeFromInt(time / 1000);
                        tvDay.setText(times[0]);
                        tvHour.setText(times[1]);
                        tvMin.setText(times[2]);
                        tvSecs.setText(times[3]);
                    } else {
                        tvDay.setText("00");
                        tvHour.setText("00");
                        tvMin.setText("00");
                        tvSecs.setText("00");
                    }
                    if (isNeedCountTime) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };

    public String[] getTimeFromInt(long time) {
        long day = time / (1 * 60 * 60 * 24);
        long hour = time / (1 * 60 * 60) % 24;
        long minute = time / (1 * 60) % 60;
        long second = time / (1) % 60;

        String[] data = new String[4];
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        data[0] = dayStr;

        String hourStr = "";
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        data[1] = hourStr;

        String minStr = "";
        if (minute < 10) {
            minStr = "0" + minute;
        } else {
            minStr = "" + minute;
        }
        data[2] = minStr;

        String SecsStr = "";
        if (second < 10) {
            SecsStr = "0" + second;
        } else {
            SecsStr = "" + second;
        }
        data[3] = SecsStr;
        return data;
    }

    private void loadData(final boolean isAction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getcurpmresultbyapp(jsonObject, new ApiCallback() {
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
                        detailBean = new Gson().fromJson(userInfoObj.toString(), PMResultBean.class);
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
        ll_content.setVisibility(View.VISIBLE);
        mApplication.setImages(detailBean.getCover_pic(), ivPaimai);
        mApplication.setImages(detailBean.getRaretag_icon(), ivRange);
        tvGoodTitle.setText(detailBean.getProduct_title());
        tvGoodStatus.setText(detailBean.getSt_name());
        tvGoodBeginPrice.setText("￥" + StringFormatUtil.getDoubleFormatNew(detailBean.getBegin_auct_price()));
        tvGoodMyPrice.setText("￥" + StringFormatUtil.getDoubleFormatNew(detailBean.getSelf_max_price()+""));
        tvPaiCount.setText(detailBean.getJp_count() + "");
        if (detailBean.getOpen_but_it() == 0) {//没有开启一口价
            rlYikouPrice.setVisibility(View.VISIBLE);
            tvYikouPriceValue.setText("￥" + "---");
        } else {
            rlYikouPrice.setVisibility(View.VISIBLE);
            tvYikouPriceValue.setText("￥" + StringFormatUtil.getDoubleFormatNew(detailBean.getBut_it_price()));
        }
        tvGoodMaxPrice.setText("￥" + StringFormatUtil.getDoubleFormatNew(detailBean.getMax_pric()));


        if (detailBean.getPm_status() == 1) {//1：正在进行 2：已结束
            tvTiXing.setVisibility(View.VISIBLE);
            tvContent.setText("您已成功出价\n请耐心等待拍卖结束！");
            tvGoodTimeTitle.setText("拍卖剩余时间");
            if (detailBean.getCol_id() > 0) {
                btnPaiAgain.setOnClickListener(null);
                btnPaiAgain.setBackgroundResource(R.drawable.shape_gray_black);
                btnPaiAgain.setText("这个拍品已加入我的关注");
            }else{
                btnPaiAgain.setText("将这个拍品加入我的关注");
                btnPaiAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnPaiAgain.setEnabled(false);
                        likeUnlike();
                    }
                });
            }
            time = detailBean.getPm_end_remain_second()*1000;
            handler.sendEmptyMessage(1);
        } else {
            paiStatus.setVisibility(View.GONE);
            tvTiXing.setVisibility(View.GONE);
            tvContent.setText("您已成功拍得该商品\n请在72小时内付款！");
            btnPaiAgain.setText("付款");
            tvGoodTimeTitle.setText("付款剩余时间");
            btnPaiAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", detailBean.getProduct_id());
                    UIHelper.jump(mActivity, ZhiFuActivity.class,bundle);
                }
            });
            time = detailBean.getLast_pay_remain_second()*1000;
            handler.sendEmptyMessage(1);
        }

    }

    private void likeUnlike() {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 1);
                jsonObject.put("aim_id", detailBean.getProduct_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colproduct(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {
                    showLoadingDialog("关注中...");
                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            btnPaiAgain.setOnClickListener(null);
                            btnPaiAgain.setBackgroundResource(R.drawable.shape_gray_black);
                            btnPaiAgain.setText("这个拍品已加入我的关注");
                        } else {
                            btnPaiAgain.setEnabled(true);
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        btnPaiAgain.setEnabled(true);
                        e.printStackTrace();
                    }
                    dismissLoadingDialog();
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing()) {
                        return;
                    }
                    btnPaiAgain.setEnabled(true);
                    UIHelper.t(mContext, "操作失败");
                    dismissLoadingDialog();
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }

}
