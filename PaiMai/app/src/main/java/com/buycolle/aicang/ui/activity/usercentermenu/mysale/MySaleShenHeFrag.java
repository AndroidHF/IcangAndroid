package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.buycolle.aicang.bean.mysale.MySaleShenHeBean;
import com.buycolle.aicang.event.EditPostEvent;
import com.buycolle.aicang.ui.activity.ReEditPostActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
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
public class MySaleShenHeFrag extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<MySaleShenHeBean> mySaleShenHeBeanArrayList;
    private MyAdapter myAdapter;
    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;



    public void onEventMainThread(EditPostEvent event) {
        pageIndex=1;
        loadData(false);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySaleShenHeBeanArrayList = new ArrayList<>();
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
        EventBus.getDefault().register(this);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);

    }

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
        mApplication.apiClient.product_getCenterCheckListByApp(jsonObject, new ApiCallback() {
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
                                mySaleShenHeBeanArrayList.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MySaleShenHeBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MySaleShenHeBean>>() {
                            }.getType());
                            mySaleShenHeBeanArrayList.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
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


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mySaleShenHeBeanArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mySaleShenHeBeanArrayList.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mysale_shenhe_item, null);
                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.tv_re_edit = (TextView) convertView.findViewById(R.id.tv_re_edit);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_pai_count = (TextView) convertView.findViewById(R.id.tv_good_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.tv_good_yikoujia_price = (TextView) convertView.findViewById(R.id.tv_good_yikoujia_price);
                holder.tv_good_check_faile_resean = (TextView) convertView.findViewById(R.id.tv_good_check_faile_resean);
                holder.tv_cancle = (TextView) convertView.findViewById(R.id.tv_cancle);
                holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tv_cancle_watting = (TextView) convertView.findViewById(R.id.tv_cancle_watting);
                holder.ll_weitongguo_lay = (LinearLayout) convertView.findViewById(R.id.ll_weitongguo_lay);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MySaleShenHeBean mySaleShenHeBean = mySaleShenHeBeanArrayList.get(position);


            mApplication.setImages(mySaleShenHeBean.getCover_pic(), position, holder.iv_paimai);
            mApplication.setImages(mySaleShenHeBean.getRaretag_icon(), position, holder.iv_rate);

            holder.tv_good_title.setText(mySaleShenHeBean.getProduct_title());
            holder.tv_good_status.setText(mySaleShenHeBean.getSt_name());
            holder.tv_good_pai_count.setText(mySaleShenHeBean.getJp_count());
            holder.tv_good_begin_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mySaleShenHeBean.getBegin_auct_price()));
            if (mySaleShenHeBean.getOpen_but_it() == 0) {//不开启一口价
                holder.tv_good_yikoujia_price.setText("￥" +"---");
            } else {
                holder.tv_good_yikoujia_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mySaleShenHeBean.getBut_it_price()));
            }

            if ("0".equals(mySaleShenHeBean.getCheck_status())) {//审核中
                holder.ll_weitongguo_lay.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_cancle_watting.setVisibility(View.VISIBLE);
                holder.tv_cancle_watting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new NoticeDialog(mContext, "温馨提示", "取消拍卖，该拍品信息将不存在\n该列表中，您确定取消吗？").setCallBack(new NoticeDialog.CallBack() {
                            @Override
                            public void ok() {
                                product_undo(mySaleShenHeBean);
                            }

                            @Override
                            public void cancle() {

                            }
                        }).show();
                    }
                });
            } else {//失败了
                holder.tv_cancle_watting.setVisibility(View.GONE);
                holder.ll_weitongguo_lay.setVisibility(View.VISIBLE);
                holder.tv_status.setVisibility(View.GONE);
                holder.tv_re_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("productId",Integer.valueOf(mySaleShenHeBean.getProduct_id()));
                        UIHelper.jump(mActivity, ReEditPostActivity.class,bundle);
                    }
                });
                holder.tv_good_check_faile_resean.setText(mySaleShenHeBean.getCheck_reason());
                holder.tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new NoticeDialog(mContext, "温馨提示", "取消拍卖，该拍品信息将不存在\n该列表中，您确定取消吗？").setCallBack(new NoticeDialog.CallBack() {
                            @Override
                            public void ok() {
                                product_undo(mySaleShenHeBean);
                            }

                            @Override
                            public void cancle() {

                            }
                        }).show();
                    }
                });
            }
            return convertView;
        }

        public class ViewHolder {
            private ImageView iv_paimai;
            private ImageView iv_rate;
            private TextView tv_status;
            private LinearLayout ll_weitongguo_lay;
            private TextView tv_good_title;
            private TextView tv_good_status;
            private TextView tv_good_pai_count;
            private TextView tv_good_begin_price;
            private TextView tv_good_yikoujia_price;
            private TextView tv_good_check_faile_resean;
            private TextView tv_re_edit;
            private TextView tv_cancle;
            private TextView tv_cancle_watting;


        }

    }

    private void product_undo(final MySaleShenHeBean bean) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", bean.getProduct_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_undo(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "取消成功");
                        mySaleShenHeBeanArrayList.remove(bean);
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
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }
}
