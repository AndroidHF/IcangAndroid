package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.MybuyNoGetBean;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
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
public class PaiMaiNoGetFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private MyAdapter myAdapter;
    private boolean isRun = false;
    private boolean isloadMore = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    private ArrayList<MybuyNoGetBean> mybuyNoGetBeen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mybuyNoGetBeen = new ArrayList<>();
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
        mApplication.apiClient.product_getsjoinfaillistbyapp(jsonObject, new ApiCallback() {
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
                                mybuyNoGetBeen.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MybuyNoGetBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MybuyNoGetBean>>() {
                            }.getType());
                            mybuyNoGetBeen.addAll(allDataArrayList);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mybuyNoGetBeen.size();
        }

        @Override
        public Object getItem(int i) {
            return mybuyNoGetBeen.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_mybuy_noget_item, null);
                holder.iv_paimai = (ImageView) convertView.findViewById(R.id.iv_paimai);
                holder.iv_rate = (ImageView) convertView.findViewById(R.id.iv_rate);
                holder.iv_channian = (ImageView) convertView.findViewById(R.id.iv_channian);

                holder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.rl_yikou_price_lay = (RelativeLayout) convertView.findViewById(R.id.rl_yikou_price_lay);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_pai_count = (TextView) convertView.findViewById(R.id.tv_good_pai_count);
                holder.tv_good_begin_price = (TextView) convertView.findViewById(R.id.tv_good_begin_price);
                holder.tv_yikou_price_value = (TextView) convertView.findViewById(R.id.tv_yikou_price_value);
                holder.tv_good_my_price = (TextView) convertView.findViewById(R.id.tv_good_my_price);
                holder.tv_good_max_price = (TextView) convertView.findViewById(R.id.tv_good_max_price);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_channian.setVisibility(View.VISIBLE);
            final MybuyNoGetBean mybuyNoGetBean = mybuyNoGetBeen.get(position);
            mApplication.setImages(mybuyNoGetBean.getCover_pic(), position, holder.iv_paimai);
            mApplication.setImages(mybuyNoGetBean.getRaretag_icon(), position, holder.iv_rate);
            holder.tv_good_title.setText(mybuyNoGetBean.getProduct_title());
            holder.tv_good_status.setText(mybuyNoGetBean.getSt_name());
            holder.tv_good_pai_count.setText(mybuyNoGetBean.getJp_count() + "");
            holder.tv_good_begin_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mybuyNoGetBean.getBegin_auct_price()));
            if (mybuyNoGetBean.getOpen_but_it() == 0) {//不开启一口价
                holder.rl_yikou_price_lay.setVisibility(View.VISIBLE);
                holder.tv_yikou_price_value.setText("￥" +"---");
            } else {
                holder.rl_yikou_price_lay.setVisibility(View.VISIBLE);
                holder.tv_yikou_price_value.setText("￥" + StringFormatUtil.getDoubleFormatNew(mybuyNoGetBean.getBut_it_price()));
            }
//            holder.tv_good_yikoujia_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mybuyNoGetBean.getBut_it_price()));
            holder.tv_good_my_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mybuyNoGetBean.getSelf_max_price()));
            holder.tv_good_max_price.setText("￥" + StringFormatUtil.getDoubleFormatNew(mybuyNoGetBean.getMax_pric()));
            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", mybuyNoGetBean.getProduct_id());
                    bundle.putBoolean("isFinish", true);
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            ImageView iv_paimai;
            ImageView iv_rate;
            ImageView iv_channian;

            RelativeLayout rl_yikou_price_lay;
            LinearLayout ll_parent;

            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_good_pai_count;
            TextView tv_good_begin_price;
            TextView tv_yikou_price_value;
            TextView tv_good_my_price;
            TextView tv_good_max_price;
        }

    }

}
