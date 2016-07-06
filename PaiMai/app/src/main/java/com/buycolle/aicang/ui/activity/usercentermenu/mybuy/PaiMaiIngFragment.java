package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.buycolle.aicang.bean.MyBuyPaiMainIngBean;
import com.buycolle.aicang.event.ChuJiaEvent;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.JingPaiDialog;
import com.buycolle.aicang.ui.view.JingPaiSureNoticeDialog;
import com.buycolle.aicang.ui.view.YiKouJiaDialog;
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
public class PaiMaiIngFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<MyBuyPaiMainIngBean> myBuyPaiMainIngBeans;
    private MyAdapter myAdapter;

    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    private boolean isAction = false;

    //详情出价事件
    public void onEventMainThread(ChuJiaEvent event) {
        isAction = true;
        pageIndex = 1;
        loadData(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBuyPaiMainIngBeans = new ArrayList<>();
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
        mApplication.apiClient.product_getsjoininglistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore) {
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
                                myBuyPaiMainIngBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MyBuyPaiMainIngBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyBuyPaiMainIngBean>>() {
                            }.getType());
                            formatData(allDataArrayList);
                            myBuyPaiMainIngBeans.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (!isHandRun && allDataArrayList.size() > 0) {
                                handler.sendEmptyMessage(1);
                            }
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        }else{
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
                if (!isloadMore) {
                    dismissLoadingDialog();
                }
                isAction = false;
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                list.onRefreshComplete();
//                if (!isAction && !isloadMore) {
//                    dismissLoadingDialog();
//                }
                isAction = false;
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }
            }
        });

    }

    private void formatData(ArrayList<MyBuyPaiMainIngBean> allDataArrayList) {
        for (MyBuyPaiMainIngBean mySaleMainIngBean : allDataArrayList) {
            mySaleMainIngBean.setTime(mySaleMainIngBean.getPm_end_remain_second() * 1000);
            mySaleMainIngBean.setIsFinish(false);
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
                    for (int index = 0; index < myBuyPaiMainIngBeans.size(); index++) {
                        MyBuyPaiMainIngBean goods = myBuyPaiMainIngBeans.get(index);
                        long time = goods.getTime();
                        if (time > 1000) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                            isNeedCountTime = true;
                            goods.setTime(time - 1000);
                            goods.setIsFinish(false);
                        } else {
                            goods.setIsFinish(true);
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


    YiKouJiaDialog yiKouJiaDialog;
    JingPaiDialog jingpaiDialog;
    JingPaiSureNoticeDialog jingPaiSureNoticeDialog;


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myBuyPaiMainIngBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return myBuyPaiMainIngBeans.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mybuy_paimaiing_item, null);
                holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
                holder.parent = (LinearLayout) convertView.findViewById(R.id.parent);
                holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
                holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
                holder.tv_secs = (TextView) convertView.findViewById(R.id.tv_secs);
                holder.pai_status = (TextView) convertView.findViewById(R.id.pai_status);
                holder.btn_pai_again = (TextView) convertView.findViewById(R.id.btn_pai_again);
                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.rl_yikou_price_lay = (RelativeLayout) convertView.findViewById(R.id.rl_yikou_price_lay);


                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_pai_count = (TextView) convertView.findViewById(R.id.tv_good_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.tv_good_yikoujia_price = (TextView) convertView.findViewById(R.id.tv_good_yikoujia_price);
                holder.tv_good_my_price = (TextView) convertView.findViewById(R.id.tv_good_my_price);
                holder.tv_good_max_price = (TextView) convertView.findViewById(R.id.tv_good_max_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MyBuyPaiMainIngBean myBuyPaiMainIngBean = myBuyPaiMainIngBeans.get(position);

            holder.tv_good_title.setText(myBuyPaiMainIngBean.getProduct_title());
            holder.tv_good_status.setText(myBuyPaiMainIngBean.getSt_name());
            holder.tv_good_pai_count.setText(myBuyPaiMainIngBean.getJp_count() + "");
            holder.tv_good_begin_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()));

            holder.rl_yikou_price_lay.setVisibility(View.VISIBLE);
            if (myBuyPaiMainIngBean.getOpen_but_it() == 1) {
                holder.tv_good_yikoujia_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBut_it_price()));
            } else {
                holder.tv_good_yikoujia_price.setText("￥" + "---");
            }

            holder.tv_good_my_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getSelf_max_price()));
            holder.tv_good_max_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric()));

            mApplication.setImages(myBuyPaiMainIngBean.getCover_pic(), position, holder.iv_paimai);
            mApplication.setImages(myBuyPaiMainIngBean.getRaretag_icon(), position, holder.iv_rate);
            String[] times = StringFormatUtil.getTimeFromInt(myBuyPaiMainIngBean.getTime() / 1000);
            if (!myBuyPaiMainIngBean.isFinish()) {//还没结束才显示倒计时
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

            if (myBuyPaiMainIngBean.getMax_pric().equals(myBuyPaiMainIngBean.getSelf_max_price())) {//是否被超越了
                holder.btn_pai_again.setVisibility(View.GONE);
                holder.pai_status.setText("您是目前的最高出价者");
                holder.pai_status.setBackgroundResource(R.color.orange);
            } else {
                holder.btn_pai_again.setVisibility(View.VISIBLE);
                holder.pai_status.setText("您的出价已被超过");
                holder.pai_status.setBackgroundResource(R.color.red);
                holder.btn_pai_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myBuyPaiMainIngBean.getOpen_but_it() == 1) {//开启一口价
                            yiKouJiaDialog = new YiKouJiaDialog(mContext, "竞拍", StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric()), StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBut_it_price()), true).setCallBack(new YiKouJiaDialog.CallBack() {
                                @Override
                                public void ok(String value, int type) {
                                    jingPaiSureNoticeDialog = new JingPaiSureNoticeDialog(mContext, value, type).setCallBack(new JingPaiSureNoticeDialog.CallBack() {
                                        @Override
                                        public void ok(String content, int type) {
                                            yiKouJiaDialog.dismiss();
                                            if (type == 1) {//竞价
                                                productjpfollow_submitbyapp(content, type, myBuyPaiMainIngBean.getProduct_id());
                                            } else {//一口价
                                                productjpfollow_submitbyapp(content, type, myBuyPaiMainIngBean.getProduct_id());
                                            }
                                        }

                                        @Override
                                        public void cancle() {

                                        }
                                    });
                                    jingPaiSureNoticeDialog.show();
                                }

                                @Override
                                public void cancle() {

                                }
                            });
                            yiKouJiaDialog.show();
                        } else {
                            jingpaiDialog = new JingPaiDialog(mContext, "竞拍", StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric()), true).setCallBack(new JingPaiDialog.CallBack() {
                                @Override
                                public void ok(String value) {
                                    jingPaiSureNoticeDialog = new JingPaiSureNoticeDialog(mContext, value, 1).setCallBack(new JingPaiSureNoticeDialog.CallBack() {
                                        @Override
                                        public void ok(String content, int type) {
                                            jingpaiDialog.dismiss();
                                            productjpfollow_submitbyapp(content, type, myBuyPaiMainIngBean.getProduct_id());
                                        }

                                        @Override
                                        public void cancle() {

                                        }
                                    });
                                    jingPaiSureNoticeDialog.show();
                                }

                                @Override
                                public void cancle() {

                                }
                            });
                            jingpaiDialog.show();
                        }
                    }
                });
            }

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myBuyPaiMainIngBean.getProduct_id());
                    bundle.putBoolean("isFinish", false);
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            LinearLayout parent;

            TextView tv_day;
            TextView tv_hour;
            TextView tv_min;
            TextView tv_secs;
            TextView pai_status;
            TextView btn_pai_again;
            ImageView iv_paimai;

            ImageView iv_rate;

            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_good_pai_count;
            TextView tv_good_begin_price;
            TextView tv_good_yikoujia_price;
            TextView tv_good_my_price;
            TextView tv_good_max_price;

            RelativeLayout rl_yikou_price_lay;


        }

    }

    private void productjpfollow_submitbyapp(String value, final int type, final int product_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("price", value);
            jsonObject.put("type", type);
            jsonObject.put("product_id", product_id);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.productjpfollow_submitbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        pageIndex = 1;
                        loadData(false);
                        if (type == 1) {//1：竞拍 2：一口价 默认为1
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", product_id);
                            UIHelper.jump(mActivity, JingjiaAgainFinishActivity.class, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",product_id);
                            UIHelper.jump(mActivity, JingjiaAgainFinishActivity.class, bundle);

                        }
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
                if (!isAdded())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                dismissLoadingDialog();
            }
        });
    }

}
