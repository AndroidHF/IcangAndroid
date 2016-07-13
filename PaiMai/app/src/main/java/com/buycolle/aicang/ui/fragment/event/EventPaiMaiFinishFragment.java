package com.buycolle.aicang.ui.fragment.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.EventPaiMaiIngBean;
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
public class EventPaiMaiFinishFragment extends BaseScrollListFragment {

    private ArrayList<EventPaiMaiIngBean> mySaleMainIngBeans;

    private boolean isRun = false;
    private MyAdapter myAdapter;
    private int pageIndex = 1;
    private int pageNum = 10;
    private boolean isNeedCountTime = false;

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
                pageIndex=1;
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
                EventBus.getDefault().post(new EventBackEvent(2));
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
        mApplication.apiClient.product_getauctendlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore&&!isAdded()){
                    showLoadingDialog();
                }else {
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
                            mySaleMainIngBeans.addAll(resultArray);
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
                if(pageIndex==1){
                    list.onRefreshComplete();
                }
                if (!isLoadMore&&!isAdded()){
                    dismissLoadingDialog();
                }else {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                if(pageIndex==1){
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
                holder.iv_notice = (ImageView) convertView.findViewById(R.id.iv_notice);
                holder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.ll_time_count = (LinearLayout) convertView.findViewById(R.id.ll_time_count);
                holder.ll_finish_time = (LinearLayout) convertView.findViewById(R.id.ll_finish_time);
                holder.ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);

                holder.ll_1 = (LinearLayout) convertView.findViewById(R.id.ll_1);
                holder.rl_2 = (RelativeLayout) convertView.findViewById(R.id.rl_2);
                holder.rl_3 = (RelativeLayout) convertView.findViewById(R.id.rl_3);


                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
                holder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);

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

            //add by 胡峰：已经结束的拍卖会列表的星级处理
            ViewGroup.LayoutParams layoutParams = holder.iv_rate.getLayoutParams();
            layoutParams.width = 79;
            layoutParams.height = 40;
            holder.iv_rate.setLayoutParams(layoutParams);
            mApplication.setImages(myBuyPaiMainIngBean.getRaretag_icon(), holder.iv_rate);


            holder.tv_start_time.setText("开始时间：" + myBuyPaiMainIngBean.getPm_start_time());
            holder.tv_end_time.setText("结束时间：" + myBuyPaiMainIngBean.getPm_end_time());
            holder.ll_time_count.setVisibility(View.GONE);
            holder.iv_rate.setVisibility(View.VISIBLE);
            holder.iv_notice.setVisibility(View.GONE);
            holder.ll_finish_time.setVisibility(View.VISIBLE);
            holder.tv_pai_count.setText(myBuyPaiMainIngBean.getJp_count() + "");


            if (myBuyPaiMainIngBean.getOpen_but_it() == 1) {//一口价
                holder.tv_yikoujia_title.setText("一口价");
                holder.tv_yikoujia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_yikoujia_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBut_it_price()));
            } else {
                holder.tv_yikoujia_title.setText("起拍价");
                holder.tv_yikoujia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_yikoujia_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()));
            }
            holder.tv_jiage_title.setText("成交价");
            if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric())) > Double.valueOf(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()))) {
                holder.tv_qipaijia_biaoshi.setVisibility(View.VISIBLE);
                holder.tv_jiage_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getMax_pric()));
            } else {
                if(myBuyPaiMainIngBean.getJp_count()==0){
                    //
                    holder.tv_qipaijia_biaoshi.setVisibility(View.GONE);
                    holder.tv_jiage_value.setText("——");
                }else{
                    holder.tv_qipaijia_biaoshi.setVisibility(View.VISIBLE);
                    holder.tv_jiage_value.setText(StringFormatUtil.getDoubleFormatNew(myBuyPaiMainIngBean.getBegin_auct_price()));
                }
            }
            holder.ll_1.setBackgroundResource(R.drawable.shape_orange_black);
            holder.rl_2.setBackgroundResource(R.drawable.shape_orange_black);
            holder.rl_3.setBackgroundResource(R.drawable.shape_orange_black);

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id",myBuyPaiMainIngBean.getProduct_id());
                    bundle.putBoolean("isFinish", true);
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

            LinearLayout ll_1;
            RelativeLayout rl_2;
            RelativeLayout rl_3;

            TextView tv_start_time;
            TextView tv_title;
            TextView tv_end_time;

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
