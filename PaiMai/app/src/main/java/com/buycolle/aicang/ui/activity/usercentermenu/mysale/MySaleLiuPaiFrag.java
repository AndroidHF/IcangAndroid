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
import com.buycolle.aicang.bean.MySalePaiMainLiuPaiBean;
import com.buycolle.aicang.event.EditPostEvent;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.ReEditPostActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.NoticeDialog;
import com.buycolle.aicang.ui.view.ShowMainActionPop;
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
public class MySaleLiuPaiFrag extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<MySalePaiMainLiuPaiBean> mySalePaiMainLiuPaiBeans;
    private MyAdapter myAdapter;

    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySalePaiMainLiuPaiBeans = new ArrayList<>();
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
        mApplication.apiClient.product_getcenterendfaillistbyapp(jsonObject, new ApiCallback() {
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
                                mySalePaiMainLiuPaiBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MySalePaiMainLiuPaiBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MySalePaiMainLiuPaiBean>>() {
                            }.getType());
                            mySalePaiMainLiuPaiBeans.addAll(allDataArrayList);
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

    public void onEventMainThread(EditPostEvent event) {
        pageIndex=1;
        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mySalePaiMainLiuPaiBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return mySalePaiMainLiuPaiBeans.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mysale_liupai_item, null);
                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.iv_re_edit_info_icon = (ImageView) convertView.findViewById(R.id.iv_re_edit_info_icon);
                holder.iv_re_shangjia_inco_icon = (ImageView) convertView.findViewById(R.id.iv_re_shangjia_inco_icon);
                holder.tv_re_edit = (TextView) convertView.findViewById(R.id.tv_re_edit);
                holder.tv_re_shangjia = (TextView) convertView.findViewById(R.id.tv_re_shangjia);
                holder.tv_cancle = (TextView) convertView.findViewById(R.id.tv_cancle);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_pai_count = (TextView) convertView.findViewById(R.id.tv_good_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.tv_good_yikoujia_price = (TextView) convertView.findViewById(R.id.tv_good_yikoujia_price);
                holder.tv_good_begin_time = (TextView) convertView.findViewById(R.id.tv_good_begin_time);
                holder.tv_good_end_time = (TextView) convertView.findViewById(R.id.tv_good_end_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MySalePaiMainLiuPaiBean mySalePaiMainLiuPaiBean = mySalePaiMainLiuPaiBeans.get(position);
            mApplication.setImages(mySalePaiMainLiuPaiBean.getCover_pic(), position, holder.iv_paimai);
            mApplication.setImages(mySalePaiMainLiuPaiBean.getRaretag_icon(), position, holder.iv_rate);

            holder.tv_good_title.setText(mySalePaiMainLiuPaiBean.getProduct_title());
            holder.tv_good_status.setText(mySalePaiMainLiuPaiBean.getSt_name());
            holder.tv_good_pai_count.setText("0");
            holder.tv_good_begin_price.setText("￥"+StringFormatUtil.getDoubleFormatNew(mySalePaiMainLiuPaiBean.getBegin_auct_price()));
            if (mySalePaiMainLiuPaiBean.getOpen_but_it() == 0) {//不开启一口价
                holder.tv_good_yikoujia_price.setText("￥" +"---");
            } else {
                holder.tv_good_yikoujia_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mySalePaiMainLiuPaiBean.getBut_it_price()));
            }
            holder.tv_good_begin_time.setText("拍卖开始时间： "+mySalePaiMainLiuPaiBean.getPm_start_time());
            holder.tv_good_end_time.setText("拍卖结束时间： "+mySalePaiMainLiuPaiBean.getPm_end_time());

            holder.iv_re_edit_info_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ShowMainActionPop(mActivity).setGra(true).setType(true).show(view);
                }
            });
            holder.iv_re_shangjia_inco_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ShowMainActionPop(mActivity).setGra(true).setType(false).show(view);
                }
            });

            holder.tv_re_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("productId",mySalePaiMainLiuPaiBean.getProduct_id());
                    UIHelper.jump(mActivity, ReEditPostActivity.class,bundle);
                }
            });
            holder.tv_re_shangjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("productId",mySalePaiMainLiuPaiBean.getProduct_id());
                    UIHelper.jump(mActivity, MySaleReShanjiaActivity.class,bundle);
                }
            });
            holder.tv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new NoticeDialog(mContext, "温馨提示", "取消拍卖，该拍品信息将不存在\n该列表中，您确定取消吗？").setCallBack(new NoticeDialog.CallBack() {
                        @Override
                        public void ok() {
                            product_undo(mySalePaiMainLiuPaiBean);
                        }

                        @Override
                        public void cancle() {

                        }
                    }).show();
                }
            });
            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id",mySalePaiMainLiuPaiBean.getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class,bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            ImageView iv_paimai;
            ImageView iv_rate;
            private LinearLayout ll_parent;

            private ImageView iv_re_edit_info_icon;
            private ImageView iv_re_shangjia_inco_icon;

            private TextView tv_re_edit;
            private TextView tv_re_shangjia;
            private TextView tv_cancle;

            private TextView tv_good_title;
            private TextView tv_good_status;
            private TextView tv_good_pai_count;
            private TextView tv_good_begin_price;
            private TextView tv_good_yikoujia_price;
            private TextView tv_good_begin_time;
            private TextView tv_good_end_time;


        }

    }

    private void product_undo(final MySalePaiMainLiuPaiBean bean) {
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
                        mySalePaiMainLiuPaiBeans.remove(bean);
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
