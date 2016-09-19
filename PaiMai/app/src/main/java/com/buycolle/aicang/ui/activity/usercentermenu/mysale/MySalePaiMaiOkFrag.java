package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.MySalePaiMainOkBean;
import com.buycolle.aicang.ui.activity.ConectionActivity;
import com.buycolle.aicang.ui.activity.PaiMaiDealActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.WuLiuMsgAcitivty;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.MyLinkTextView;
import com.buycolle.aicang.ui.view.xlistview.XListView;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/5.
 */
public class MySalePaiMaiOkFrag extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;


    private ArrayList<MySalePaiMainOkBean> mySalePaiMainOkBeans;
    private MyAdapter myAdapter;
    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySalePaiMainOkBeans = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_paimai_ing, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);

        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > Constans.MAX_SHOW_FLOAT_BTN) {
                    ibFloatBtn.setVisibility(View.VISIBLE);
                } else {
                    ibFloatBtn.setVisibility(View.GONE);
                }

            }
        });
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                loadData(false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(true);
                }
            }
        });
        ibFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setSelection(0);
            }
        });
        loadData(isloadMore);

    }

    private boolean isCount = false;

    private void loadData(final boolean isloadMore) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getcenterendlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore){
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (JSONUtil.isHasData(resultObj)) {
                            if (pageIndex == 1) {
                                mySalePaiMainOkBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MySalePaiMainOkBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MySalePaiMainOkBean>>() {
                            }.getType());
                            formatData(allDataArrayList);
                            mySalePaiMainOkBeans.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (pageIndex == 1) {
                                if (!isCount) {
                                    handler.sendEmptyMessage(1);
                                    isCount = true;
                                }
                            }
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        }else {
                           tv_null.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                list.onRefreshComplete();
                if (!isloadMore){
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                list.onRefreshComplete();
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }
            }
        });

    }

    private void formatData(ArrayList<MySalePaiMainOkBean> allDataArrayList) {
        for (MySalePaiMainOkBean mySaleMainIngBean : allDataArrayList) {
            if (mySaleMainIngBean.getPay_status() == 0 && mySaleMainIngBean.getOrder_status() == 0) {
                if (!TextUtils.isEmpty(mySaleMainIngBean.getLast_pay_time())) {
                    mySaleMainIngBean.setTime(mySaleMainIngBean.getLast_pay_remain_second()*1000);
                    mySaleMainIngBean.setIsFinish(false);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    for (int index = 0; index < mySalePaiMainOkBeans.size(); index++) {
                        MySalePaiMainOkBean goods = mySalePaiMainOkBeans.get(index);
                        long time = goods.getTime();
                        if (time > 1000) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                            isNeedCountTime = true;
                            goods.setTime(time - 1000);
                            goods.setIsFinish(false);
                        } else {
                            goods.setIsFinish(true);
                            if (goods.getPay_status() == 0 && goods.getOrder_status() == 0) {
                                goods.setPay_status(2);
                                goods.setOrder_status(0);
                            }
                            goods.setTime(0);
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                    if (isNeedCountTime) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mySalePaiMainOkBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return mySalePaiMainOkBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mysale_paimaiok_item_new, null);


                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);

                holder.ll_status_weifukuang = (LinearLayout) convertView.findViewById(R.id.ll_status_weifukuang);
                holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
                holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
                holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
                holder.tv_secs = (TextView) convertView.findViewById(R.id.tv_secs);
                holder.btn_pay = (TextView) convertView.findViewById(R.id.btn_pay);


                holder.tv_msg_info = (MyLinkTextView) convertView.findViewById(R.id.tv_msg_info);

                holder.ll_status_after_fukuang = (LinearLayout) convertView.findViewById(R.id.ll_status_after_fukuang);
                holder.tv_after_msg = (TextView) convertView.findViewById(R.id.tv_after_msg);
                holder.tv_after_pay = (TextView) convertView.findViewById(R.id.tv_after_pay);
                holder.tv_action_after = (TextView) convertView.findViewById(R.id.tv_action_after);

                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_pai_count = (TextView) convertView.findViewById(R.id.tv_good_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.tv_good_yikoujia_price = (TextView) convertView.findViewById(R.id.tv_good_yikoujia_price);
                holder.tv_good_deal_price = (TextView) convertView.findViewById(R.id.tv_good_deal_price);
                holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
                holder.tv_callMaijia = (TextView) convertView.findViewById(R.id.tv_call_maijia);

                //add by :胡峰，包邮不包邮的提醒控件
                holder.tv_baoyou_show = (TextView) convertView.findViewById(R.id.tv_baoyou_show);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MySalePaiMainOkBean myBuyPaiMainFinishBean = mySalePaiMainOkBeans.get(position);
            mApplication.setImages(myBuyPaiMainFinishBean.getCover_pic(), position, holder.iv_paimai);
            mApplication.setImages(myBuyPaiMainFinishBean.getRaretag_icon(), position, holder.iv_rate);

            holder.tv_good_title.setText(myBuyPaiMainFinishBean.getProduct_title());
            holder.tv_good_status.setText(myBuyPaiMainFinishBean.getSt_name());
            holder.tv_order_no.setText("订单编号:" + myBuyPaiMainFinishBean.getOrder_no());
            holder.tv_good_pai_count.setText(myBuyPaiMainFinishBean.getJp_count() + "");
            holder.tv_good_begin_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getBegin_auct_price()));


            if(myBuyPaiMainFinishBean.getPm_status()==2){

                if (myBuyPaiMainFinishBean.getOpen_but_it() == 0) {//不开启一口价
                    holder.tv_good_yikoujia_price.setText("￥" +"---");
                } else {
                    holder.tv_good_yikoujia_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getBut_it_price()));
                }
                holder.tv_good_deal_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getMax_pric()));

                ////1 未付款，2 未发货 3 已发货 4 确认收货 5 失败了
                if (myBuyPaiMainFinishBean.getPay_status() == 0 && myBuyPaiMainFinishBean.getOrder_status() == 0) {
                    if (!TextUtils.isEmpty(myBuyPaiMainFinishBean.getLast_pay_time())) {
                        setViewVisible(holder.ll_status_weifukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                        String[] times = StringFormatUtil.getTimeFromInt(myBuyPaiMainFinishBean.getTime() / 1000);
                        if (!myBuyPaiMainFinishBean.isFinish()) {//还没结束才显示倒计时
                            holder.tv_day.setText(times[0]);
                            holder.tv_hour.setText(times[1]);
                            holder.tv_min.setText(times[2]);
                            holder.tv_secs.setText(times[3]);
                        } else {
                            holder.tv_day.setText("00");
                            holder.tv_hour.setText("00");
                            holder.tv_min.setText("00");
                            holder.tv_secs.setText("00");
                        }
                        holder.btn_pay.setText("请耐心等待买家付款");
                    } else {
                        setViewVisible(holder.btn_pay, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                        holder.btn_pay.setText("提示系统正常处理订单，稍后进行");
                    }
                } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 0) {
                    setViewVisible(holder.ll_status_after_fukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                    holder.tv_action_after.setVisibility(View.VISIBLE);
                    //add by ：胡峰，包邮不包邮提示控件可见
                    holder.tv_baoyou_show.setVisibility(View.VISIBLE);
                    holder.tv_after_pay.setVisibility(View.GONE);
                    holder.tv_after_msg.setText("买家已付款，请尽快安排发货");
                    holder.tv_action_after.setText("我要发货");

                    //add by :胡峰，包邮不包邮提醒信息：1代表不包邮，2代表包邮
                    Log.i("express_out_type---",myBuyPaiMainFinishBean.getExpress_out_type()+"");
                    if (myBuyPaiMainFinishBean.getExpress_out_type() == 2){
                        holder.tv_baoyou_show.setText("※您在上架时选择的是包邮，请在发货时承担运费");
                    }else {
                        holder.tv_baoyou_show.setText("※您在上架时选择的是不包邮，请在发货时选择到付");
                    }

                    holder.tv_action_after.setBackgroundResource(R.drawable.shape_orange_black);
                    holder.tv_action_after.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // TODO: 16/3/6 去提交物流信息
                            currentClickIndex = position;
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", myBuyPaiMainFinishBean.getProduct_id());
                            UIHelper.jumpForResultByFragment(mFragment, WuLiuMessageActivity.class, bundle, 1000);
                        }
                    });
                } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 1) {
                    setViewVisible(holder.ll_status_after_fukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                    holder.tv_action_after.setVisibility(View.VISIBLE);
                    holder.tv_baoyou_show.setVisibility(View.GONE);
                    holder.tv_after_pay.setVisibility(View.GONE);
                    holder.tv_after_msg.setText("您已发货，等待买家确认收货");
                    holder.tv_action_after.setText("发货信息");
                    holder.tv_action_after.setBackgroundResource(R.drawable.shape_orange_black);
                    holder.tv_action_after.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 16/4/25 去看物流信息
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", myBuyPaiMainFinishBean.getProduct_id());
                            UIHelper.jump(mActivity, WuLiuMsgAcitivty.class, bundle);
                        }
                    });
                } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 2) {
                    setViewVisible(holder.ll_status_after_fukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                    holder.tv_after_msg.setText("买家确认收货，请等待平台结算");
                    holder.tv_action_after.setVisibility(View.GONE);
                    holder.tv_baoyou_show.setVisibility(View.GONE);
                    holder.tv_after_pay.setVisibility(View.GONE);
                } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 3){//add by huefng:当公司打款的时候显示
                    setViewVisible(holder.ll_status_after_fukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                    holder.tv_after_msg.setText("交易已完成");
                    holder.tv_after_pay.setText("※平台已向您汇出货款，请注意查收");
                    holder.tv_after_pay.setVisibility(View.VISIBLE);
                    holder.tv_action_after.setVisibility(View.GONE);
                    holder.tv_baoyou_show.setVisibility(View.GONE);
                }else if (myBuyPaiMainFinishBean.getPay_status() == 2 && myBuyPaiMainFinishBean.getOrder_status() == 0) {
                    setViewVisible(holder.tv_msg_info, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                    String linkText = "由于买家未按时付款，导致交易失败<br/>该买家可能受到惩罚请参阅" + "<font color='red'>" + "<a href=''>" + "《竞拍规则》" + "</a>" + "</font> ";
                    Spanned contentSpan = Html.fromHtml(linkText);
                    SpannableStringBuilder style = new SpannableStringBuilder(contentSpan);
                    style.clearSpans();// 必须
                    URLSpan[] urls = contentSpan.getSpans(0, contentSpan.length(), URLSpan.class);
                    for (URLSpan url : urls) {
                        style.setSpan(new MyPostURLSpan(url.getURL(), position), contentSpan.getSpanStart(url), contentSpan.getSpanEnd(url),
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    holder.tv_msg_info.setText(style);
                    holder.tv_msg_info.setMovementMethod(MyLinkTextView.LocalLinkMovementMethod.getInstance());
                }

            }else {
                setViewVisible(holder.tv_msg_info, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_after_fukuang);
                holder.tv_msg_info.setText("系统正在核算拍卖结果...");
            }


            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });

            /**
             * 联系买卖家的按钮监听
             */
            holder.tv_callMaijia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id",myBuyPaiMainFinishBean.getProduct_id());
                    UIHelper.jump(mActivity, ConectionActivity.class,bundle);
                }
            });

            return convertView;
        }

        private void setViewVisible(View view, View... views) {
            for (View view1 : views) {
                view1.setVisibility(View.GONE);
            }
            view.setVisibility(View.VISIBLE);
        }

        public class ViewHolder {

            LinearLayout ll_parent;
            ImageView iv_paimai;
            ImageView iv_rate;

            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_good_pai_count;
            TextView tv_good_begin_price;
            TextView tv_good_yikoujia_price;
            TextView tv_good_deal_price;
            TextView tv_order_no;


            //未付款状态
            LinearLayout ll_status_weifukuang;
            TextView tv_day;
            TextView tv_hour;
            TextView tv_min;
            TextView tv_secs;
            TextView btn_pay;

            //失败状态
            MyLinkTextView tv_msg_info;

            //已经支付  已发货
            LinearLayout ll_status_after_fukuang;
            TextView tv_after_msg;
            TextView tv_action_after;
            TextView tv_after_pay;

            //add by :胡峰，包邮不包邮提醒
            TextView tv_baoyou_show;
            TextView tv_callMaijia;

        }

    }

    private int currentClickIndex = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //物流信息返回来
        if (requestCode == 1000 && resultCode == mActivity.RESULT_OK) {
            mySalePaiMainOkBeans.get(currentClickIndex).setOrder_status(1);
            myAdapter.notifyDataSetChanged();
        }
    }

    private class MyPostURLSpan extends ClickableSpan {
        private String mUrl;

        public MyPostURLSpan(String url, int position) {
            // TODO Auto-generated constructor stub
            super();
            mUrl = url;
        }


        @Override
        public void onClick(View widget) {
            UIHelper.jump(mActivity, PaiMaiDealActivity.class);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // TODO Auto-generated method stub
            super.updateDrawState(ds);
            // 设置样式
            ds.setColor(mContext.getResources().getColor(R.color.red));
            ds.setUnderlineText(false);
        }
    }

}
