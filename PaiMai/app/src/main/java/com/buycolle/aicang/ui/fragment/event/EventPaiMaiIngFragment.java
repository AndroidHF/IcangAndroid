package com.buycolle.aicang.ui.fragment.event;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.EventPaiMaiIngBean;
import com.buycolle.aicang.event.ChuJiaEvent;
import com.buycolle.aicang.event.EventBackEvent;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.fragment.BaseScrollListFragment;
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

import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/7.
 */
public class EventPaiMaiIngFragment extends BaseScrollListFragment {


    private ArrayList<EventPaiMaiIngBean> mySaleMainIngBeans;
    private MyAdapter myAdapter;
    private boolean isRun = false;

    private int pageIndex = 1;
    private int pageNum = 10;
    private boolean isNeedCountTime = false;

    public void onEventMainThread(ChuJiaEvent event) {
        boolean has = false;
        for (EventPaiMaiIngBean homeGoodsBean : mySaleMainIngBeans) {
            if (homeGoodsBean.getProduct_id() == event.getStatus()) {
                has = true;
                break;
            }
        }
        if (has) {
            pageIndex = 1;
            loadData(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mySaleMainIngBeans = new ArrayList<>();
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);
        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
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
                EventBus.getDefault().post(new EventBackEvent(0));
            }
        });

        loadData(false);
    }

    private void loadData(final boolean isLoadMore) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getauctinglistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore && !isAdded()){
                    tv_null.setVisibility(View.GONE);
                    showLoadingDialog();
                }else{
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
                        if (JSONUtil.isHasData(resultObj)){
                            JSONArray jsonArray = resultObj.getJSONArray("rows");
                            ArrayList<EventPaiMaiIngBean> resultArray = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<EventPaiMaiIngBean>>() {
                            }.getType());
                            if (pageIndex == 1) {
                                tv_null.setVisibility(View.GONE);
                                mySaleMainIngBeans.clear();
                            }
                            ArrayList<EventPaiMaiIngBean> resultFormatS = formatData(resultArray);
                            mySaleMainIngBeans.addAll(resultFormatS);
                            if (pageIndex == 1 && resultArray.size() > 0 && headIsRun == false) {
                                handler.sendEmptyMessage(1);
                            }
                            myAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        }else {
                            if (pageIndex == 1){
                                mySaleMainIngBeans.clear();
                                list.isShowFoot(false);
                                myAdapter.notifyDataSetChanged();
                            }
                            tv_null.setText("暂无数据");
                            tv_null.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (pageIndex == 1) {
                    list.onRefreshComplete();
                }
                if (!isLoadMore && !isAdded()){
                    dismissLoadingDialog();
                }else {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                if (pageIndex == 1) {
                    list.onRefreshComplete();
                }
                UIHelper.t(mContext, R.string.net_error);
                if (isLoadMore) {
                    pageIndex--;
                }
                isRun = false;
            }
        });

    }

    private ArrayList<EventPaiMaiIngBean> formatData(ArrayList<EventPaiMaiIngBean> resultArray) {
        ArrayList<EventPaiMaiIngBean> ings = new ArrayList<>();
        for (EventPaiMaiIngBean eventPaiMaiIngBean : resultArray) {
            eventPaiMaiIngBean.setTime(eventPaiMaiIngBean.getPm_end_remain_second() * 1000);
            ings.add(eventPaiMaiIngBean);
        }
        return ings;
    }

    private boolean headIsRun = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    for (int index = 0; index < mySaleMainIngBeans.size(); index++) {
                        EventPaiMaiIngBean goods = mySaleMainIngBeans.get(index);
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
                        headIsRun = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        headIsRun = false;
                    }
                    break;
            }
        }
    };

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mySaleMainIngBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return mySaleMainIngBeans.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_event_main_list, null);


                holder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
                holder.iv_notice = (ImageView) convertView.findViewById(R.id.iv_notice);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.ll_time_count = (LinearLayout) convertView.findViewById(R.id.ll_time_count);
                holder.ll_finish_time = (LinearLayout) convertView.findViewById(R.id.ll_finish_time);
                holder.ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);


                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_pai_count = (TextView) convertView.findViewById(R.id.tv_pai_count);
                holder.tv_yikoujia_title = (TextView) convertView.findViewById(R.id.tv_yikoujia_title);
                holder.tv_yikoujia_value = (TextView) convertView.findViewById(R.id.tv_yikoujia_value);
                holder.tv_jiage_title = (TextView) convertView.findViewById(R.id.tv_jiage_title);
                holder.tv_jiage_value = (TextView) convertView.findViewById(R.id.tv_jiage_value);


                holder.tv_daojishi_title = (TextView) convertView.findViewById(R.id.tv_daojishi_title);
                holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
                holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
                holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
                holder.tv_secs = (TextView) convertView.findViewById(R.id.tv_secs);

                holder.tv_yikoujia_biaoshi = (TextView) convertView.findViewById(R.id.yikoujia_biaoshi);//一口价标识
                holder.tv_qipaijia_biaoshi = (TextView) convertView.findViewById(R.id.qipaijia_biaoshi);//起拍价标识

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final EventPaiMaiIngBean myBuyPaiMainIngBean = mySaleMainIngBeans.get(position);
            holder.tv_title.setText(myBuyPaiMainIngBean.getProduct_title());
            //change by :胡峰，默认加载图片的修改
            mApplication.setShowImages(myBuyPaiMainIngBean.getCover_pic(), holder.iv_cover);

            //add by 胡峰：正在进行的拍卖会列表的星级处理
            ViewGroup.LayoutParams layoutParams = holder.iv_rate.getLayoutParams();
            layoutParams.width = 79;
            layoutParams.height = 40;
            holder.iv_rate.setLayoutParams(layoutParams);
            mApplication.setImages(myBuyPaiMainIngBean.getRaretag_icon(), holder.iv_rate);

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
            holder.ll_time_count.setVisibility(View.VISIBLE);
            holder.iv_rate.setVisibility(View.VISIBLE);
            holder.iv_notice.setVisibility(View.GONE);
            holder.ll_finish_time.setVisibility(View.GONE);


            holder.tv_daojishi_title.setText("拍卖剩余时间");
            holder.tv_pai_count.setText(myBuyPaiMainIngBean.getJp_count() + "");
            if (myBuyPaiMainIngBean.getOpen_but_it() == 1) {//一口价
                holder.tv_yikoujia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_yikoujia_title.setText("一口价");
                holder.tv_yikoujia_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBut_it_price()));
            } else {
                holder.tv_yikoujia_title.setText("一口价");
                //change by :胡峰，没有开启一口价，改为“无”
                holder.tv_yikoujia_biaoshi.setVisibility(View.GONE);
                holder.tv_yikoujia_value.setTextColor(mContext.getResources().getColor(R.color.black_tv));
                holder.tv_yikoujia_value.setText("无");
            }
            if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric())) >= Double.valueOf(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()))) {
                holder.tv_jiage_title.setText("现价");
                holder.tv_qipaijia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_jiage_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric()));
            } else {
                holder.tv_jiage_title.setText("起拍价");
                holder.tv_qipaijia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_jiage_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()));
            }

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myBuyPaiMainIngBean.getProduct_id());
                    bundle.putBoolean("isEvent", true);
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            ImageView iv_notice;
            ImageView iv_cover;
            ImageView iv_rate;
            LinearLayout ll_time_count;
            LinearLayout ll_finish_time;
            LinearLayout ll_main;

            TextView tv_title;
            TextView tv_pai_count;
            TextView tv_yikoujia_title;
            TextView tv_yikoujia_value;
            TextView tv_jiage_title;
            TextView tv_jiage_value;


            TextView tv_daojishi_title;
            TextView tv_day;
            TextView tv_hour;
            TextView tv_min;
            TextView tv_secs;

            TextView tv_yikoujia_biaoshi;
            TextView tv_qipaijia_biaoshi;

        }

    }

    @Override
    public void refreshByState(int state) {
        super.refreshByState(state);
        if (state == 0) {
            list.setSelection(0);
            pageIndex = 1;
            loadData(false);
        }
    }
}
