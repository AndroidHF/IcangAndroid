package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.MyBuyPaiMainFinishBean;
import com.buycolle.aicang.event.MyBuyCommentEvent;
import com.buycolle.aicang.event.ShowProductPublicEvent;
import com.buycolle.aicang.event.ZhifueEvent;
import com.buycolle.aicang.ui.activity.ConectionActivity;
import com.buycolle.aicang.ui.activity.PaiMaiDealActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.PublicShowActivity;
import com.buycolle.aicang.ui.activity.WuLiuMsgAcitivty;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.MyLinkTextView;
import com.buycolle.aicang.ui.view.NoticeDialog;
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
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/5.
 */
public class PaiMaiFinishFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<MyBuyPaiMainFinishBean> myBuyPaiMainFinishBeans;
    private MyAdapter myAdapter;

    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    public void onEventMainThread(ZhifueEvent event) {
        pageIndex = 1;
        loadData(false);
    }

    public void onEventMainThread(MyBuyCommentEvent event) {
        pageIndex = 1;
        loadData(false);
    }

    //晒物完成之后
    public void onEventMainThread(ShowProductPublicEvent event) {
        pageIndex = 1;
        loadData(false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBuyPaiMainFinishBeans = new ArrayList<>();
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

        ////1 未付款，2 未发货 3 已发货 4 确认收货 5 失败了
        EventBus.getDefault().register(this);
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

    private boolean isHandRun = false;

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
        mApplication.apiClient.product_getsjoinsucclistbyapp(jsonObject, new ApiCallback() {
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
                                myBuyPaiMainFinishBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MyBuyPaiMainFinishBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyBuyPaiMainFinishBean>>() {
                            }.getType());
                            formatData(allDataArrayList);
                            myBuyPaiMainFinishBeans.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (!isHandRun && allDataArrayList.size() > 0) {
                                handler.sendEmptyMessage(1);
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

    private void formatData(ArrayList<MyBuyPaiMainFinishBean> allDataArrayList) {
        for (MyBuyPaiMainFinishBean mySaleMainIngBean : allDataArrayList) {
            if (mySaleMainIngBean.getPay_status() == 0) {//未付款
                    mySaleMainIngBean.setTime(mySaleMainIngBean.getLast_pay_remain_second() * 1000);
                    mySaleMainIngBean.setIsFinish(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    for (int index = 0; index < myBuyPaiMainFinishBeans.size(); index++) {
                        MyBuyPaiMainFinishBean goods = myBuyPaiMainFinishBeans.get(index);
                        long time = goods.getTime();
                        if (time > 1000) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                            isNeedCountTime = true;
                            goods.setTime(time - 1000);
                            goods.setIsFinish(false);
                        } else {
                            goods.setIsFinish(true);
                            if (goods.getPay_status() == 0) {
                                goods.setPay_status(2);
                            }
                            goods.setTime(0);
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                    if (isNeedCountTime) {
                        isHandRun = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        isHandRun = false;
                    }
                    break;
            }
        }
    };


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myBuyPaiMainFinishBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return myBuyPaiMainFinishBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mybuy_paimaifinish_item_new, null);

                holder.parent = (LinearLayout) convertView.findViewById(R.id.parent);
                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);

                holder.ll_status_weifukuang = (LinearLayout) convertView.findViewById(R.id.ll_status_weifukuang);
                holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
                holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
                holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
                holder.tv_secs = (TextView) convertView.findViewById(R.id.tv_secs);
                holder.btn_pay = (TextView) convertView.findViewById(R.id.btn_pay);

                holder.tv_msg_info = (MyLinkTextView) convertView.findViewById(R.id.tv_msg_info);

                holder.ll_status_yifahuo = (LinearLayout) convertView.findViewById(R.id.ll_status_yifahuo);
                holder.tv_wuliu = (TextView) convertView.findViewById(R.id.tv_wuliu);
                holder.tv_queren_shouhuo = (TextView) convertView.findViewById(R.id.tv_queren_shouhuo);

                holder.ll_status_queren_shouhuo = (LinearLayout) convertView.findViewById(R.id.ll_status_queren_shouhuo);
                holder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
                holder.tv_show_dan = (TextView) convertView.findViewById(R.id.tv_show_dan);
                holder.pai_status = (TextView) convertView.findViewById(R.id.pai_status);

                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_pai_count = (TextView) convertView.findViewById(R.id.tv_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.rl_yikou_price_lay = (RelativeLayout) convertView.findViewById(R.id.rl_yikou_price_lay);
                holder.tv_yikou_price_value = (TextView) convertView.findViewById(R.id.tv_yikou_price_value);
                holder.tv_good_my_price = (TextView) convertView.findViewById(R.id.tv_good_my_price);
                holder.tv_good_deal_price = (TextView) convertView.findViewById(R.id.tv_good_deal_price);
                holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.iv_status_tag = (ImageView) convertView.findViewById(R.id.iv_status_tag);
                holder.tv_callmaijia = (TextView) convertView.findViewById(R.id.tv_call_maijia);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.pai_status.setVisibility(View.GONE);

            final MyBuyPaiMainFinishBean myBuyPaiMainFinishBean = myBuyPaiMainFinishBeans.get(position);
            mApplication.setImages(myBuyPaiMainFinishBean.getRaretag_icon(), holder.iv_rate);
            holder.tv_order_no.setText("订单编号:"+myBuyPaiMainFinishBean.getOrder_no());
            mApplication.setImages(myBuyPaiMainFinishBean.getCover_pic(), position, holder.iv_paimai);
            holder.tv_pai_count.setText(myBuyPaiMainFinishBean.getJp_count() + "");

            holder.tv_good_title.setText(myBuyPaiMainFinishBean.getProduct_title());
            holder.tv_good_status.setText(myBuyPaiMainFinishBean.getSt_name());

            holder.tv_good_begin_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getBegin_auct_price()));
            holder.tv_good_my_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getSelf_max_price()));
            holder.tv_good_deal_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getMax_pric()));
            holder.rl_yikou_price_lay.setVisibility(View.GONE);
            if (myBuyPaiMainFinishBean.getOpen_but_it() == 0) {//不开启一口价
                holder.rl_yikou_price_lay.setVisibility(View.VISIBLE);
                holder.tv_yikou_price_value.setText("￥" +"---");
            } else {
                holder.rl_yikou_price_lay.setVisibility(View.VISIBLE);
                holder.tv_yikou_price_value.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainFinishBean.getBut_it_price()));
            }


            //未付款
            if (myBuyPaiMainFinishBean.getPay_status() == 0) {
                holder.iv_status_tag.setImageResource(R.drawable.zhongbiao_icon);
                if(!TextUtils.isEmpty(myBuyPaiMainFinishBean.getLast_pay_time())){
                    setViewVisible(holder.ll_status_weifukuang, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);
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
                    holder.btn_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
                            UIHelper.jump(mActivity, ZhiFuActivity.class, bundle);
                        }
                    });
                }else {
                    setViewVisible(holder.tv_msg_info, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);
                    holder.tv_msg_info.setText("系统结算中，请稍后刷新重试");
                }


//                String[] times = StringFormatUtil.getTimeFromInt(myBuyPaiMainFinishBean.getTime() / 1000);
//                if (!myBuyPaiMainFinishBean.isFinish()) {//还没结束才显示倒计时
//                    holder.tv_day.setText(times[0]);
//                    holder.tv_hour.setText(times[1]);
//                    holder.tv_min.setText(times[2]);
//                    holder.tv_secs.setText(times[3]);
//                } else {
//                    holder.tv_day.setText("00");
//                    holder.tv_hour.setText("00");
//                    holder.tv_min.setText("00");
//                    holder.tv_secs.setText("00");
//                }
//                holder.btn_pay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
//                        UIHelper.jump(mActivity, ZhiFuActivity.class, bundle);
//                    }
//                });
                //已经付款 -- 未发货---
            } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 0) {
                holder.iv_status_tag.setImageResource(R.drawable.zhongbiao_icon);

                setViewVisible(holder.tv_msg_info, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);
                holder.tv_msg_info.setText("请耐心等待卖家发货");

                //已经付款 -- 已发货---
            } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && myBuyPaiMainFinishBean.getOrder_status() == 1) {
                holder.iv_status_tag.setImageResource(R.drawable.zhongbiao_icon);
                setViewVisible(holder.ll_status_yifahuo, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);
                holder.tv_queren_shouhuo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        new NoticeDialog(mContext,"收货确认","注意！\n\n一旦您确认收货，\n平台会将货款汇给卖方。\n\n是否确认收货？").setCallBack(new NoticeDialog.CallBack() {
                            @Override
                            public void ok() {
                                // TODO: 16/4/8 确认收货
                                sureShouHuo(myBuyPaiMainFinishBean, view);
                            }

                            @Override
                            public void cancle() {

                            }
                        }).show();
                    }
                });
                holder.tv_wuliu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("express_name", myBuyPaiMainFinishBean.getExpress_name());
//                        bundle.putString("express_bill_num", myBuyPaiMainFinishBean.getExpress_bill_num());
//                        UIHelper.jump(mActivity, ChaKanWuLiuActivity.class, bundle);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", myBuyPaiMainFinishBean.getProduct_id());
                        UIHelper.jump(mActivity, WuLiuMsgAcitivty.class, bundle);
                    }
                });
                //已经付款 -- 已收货---
            } else if (myBuyPaiMainFinishBean.getPay_status() == 1 && (myBuyPaiMainFinishBean.getOrder_status() == 2||myBuyPaiMainFinishBean.getOrder_status() == 3)) {
                holder.iv_status_tag.setImageResource(R.drawable.chengjiao_cion);
                setViewVisible(holder.ll_status_queren_shouhuo, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);
                if (myBuyPaiMainFinishBean.getIs_eval() == 0) {//未评论
                    holder.tv_pingjia.setText("评价");
                    holder.tv_pingjia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
                            UIHelper.jump(mActivity, CommentActivity.class, bundle);
                        }
                    });
                } else {
                    holder.tv_pingjia.setText("已评价");
                    holder.tv_pingjia.setOnClickListener(null);
                }
                if (myBuyPaiMainFinishBean.getIs_sd() == 0) {//未晒单
                    holder.tv_show_dan.setText("去晒个单");
                    holder.tv_show_dan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("relate_item_id",myBuyPaiMainFinishBean.getProduct_id());
                            bundle.putBoolean("isShow",true);
                            UIHelper.jump(mActivity, PublicShowActivity.class,bundle);
                        }
                    });
                } else {
                    holder.tv_show_dan.setText("已晒单");
                    holder.tv_show_dan.setOnClickListener(null);
                }
            } else if (myBuyPaiMainFinishBean.getPay_status() == 2) {
                holder.iv_status_tag.setImageResource(R.drawable.weifukuang_icon);
                setViewVisible(holder.tv_msg_info, holder.ll_status_weifukuang, holder.tv_msg_info, holder.ll_status_yifahuo, holder.ll_status_queren_shouhuo);

                String linkText = "由于您未按时付款，导致交易失败<br/>您有可能受到惩罚请参阅" + "<font color='red'>" + "<a href=''>" + "《竞拍规则》" + "</a>" + "</font> ";
//                holder.tv_msg_info.setText("由于您未按时付款，导致交易失败\n您有可能受到惩罚请参阅《竞拍规则》");
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

            holder.iv_status_tag.setVisibility(View.VISIBLE);

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
                    bundle.putBoolean("isFinish", true);
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });

            /**
             * 联系买卖家的按钮监听
             */
            holder.tv_callmaijia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myBuyPaiMainFinishBean.getProduct_id());
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

            LinearLayout parent;

            ImageView iv_paimai;
            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_pai_count;
            TextView tv_good_begin_price;
            RelativeLayout rl_yikou_price_lay;
            TextView tv_yikou_price_value;
            TextView tv_good_my_price;
            TextView tv_good_deal_price;
            TextView tv_order_no;
            ImageView iv_rate;
            ImageView iv_status_tag;


            //未付款状态
            LinearLayout ll_status_weifukuang;
            TextView tv_day;
            TextView tv_hour;
            TextView tv_min;
            TextView tv_secs;
            TextView btn_pay;

            //等待发货和失败状态
            MyLinkTextView tv_msg_info;

            //已经支付  已发货
            LinearLayout ll_status_yifahuo;
            TextView tv_wuliu;
            TextView tv_queren_shouhuo;

            //已经收货  未确认
            LinearLayout ll_status_queren_shouhuo;
            TextView tv_pingjia;
            TextView tv_show_dan;
            TextView pai_status;
            TextView tv_callmaijia;
        }

    }

    private void sureShouHuo(final MyBuyPaiMainFinishBean myBuyPaiMainFinishBean, final View view) {
        view.setEnabled(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", myBuyPaiMainFinishBean.getProduct_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.winproductorder_surereceipt(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("确认收货中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded())
                    return;
                view.setEnabled(true);
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        myBuyPaiMainFinishBean.setOrder_status(2);
                        UIHelper.t(mContext, "确认收货成功");
                        myAdapter.notifyDataSetChanged();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                dismissLoadingDialog();
                view.setEnabled(true);
                UIHelper.t(mContext, R.string.net_error);
            }
        });
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
            // ds.setTextSize(50);
        }
    }


}
