package com.buycolle.aicang.ui.activity.usercentermenu.myfoucus;

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
import com.buycolle.aicang.bean.MyFocusGoodsBean;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.xlistview.XListView;
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
public class MyFocusGoodsFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private MyAdapter myAdapter;

    private ArrayList<MyFocusGoodsBean> datas;
    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = new ArrayList<>();

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
        ibFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setSelection(0);
            }
        });
        loadData(false);
    }

    private void loadData(final boolean isloadMore) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getcollistbyapp(jsonObject, new ApiCallback() {
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
                                datas.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MyFocusGoodsBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyFocusGoodsBean>>() {
                            }.getType());
                            datas.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        } else {
                            if (pageIndex == 1) {
                                datas.clear();
                            }
                            myAdapter.notifyDataSetChanged();
                            list.isShowFoot(false);
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
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_myfocus_good_item, null);
                holder.iv_good_image = (ImageView) convertView.findViewById(R.id.iv_good_image);
                holder.ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);
                holder.iv_range = (ImageView) convertView.findViewById(R.id.iv_range);
                holder.iv_star_icon = (ImageView) convertView.findViewById(R.id.iv_star_icon);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_good_desc = (TextView) convertView.findViewById(R.id.tv_good_desc);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MyFocusGoodsBean myFocusGoodsBean = datas.get(position);

            mApplication.setImages(myFocusGoodsBean.getCover_pic(), holder.iv_good_image);
            mApplication.setImages(myFocusGoodsBean.getRaretag_icon(), holder.iv_range);
            holder.iv_star_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (likeActionFlag) {
                        likeActionFlag = false;
                        likeUnlike(myFocusGoodsBean);
                    } else {
                        if (likeActionShowToUserFlag) {
                            likeActionShowToUserFlag = false;
                            UIHelper.t(mContext, R.string.comment_action_more);
                        }
                    }
                }
            });
            holder.tv_good_title.setText(myFocusGoodsBean.getProduct_title());
            holder.tv_good_status.setText(myFocusGoodsBean.getSt_name());
            holder.tv_good_desc.setText(myFocusGoodsBean.getProduct_intro());
            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myFocusGoodsBean.getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            LinearLayout ll_main;
            ImageView iv_good_image;
            ImageView iv_range;
            ImageView iv_star_icon;

            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_good_desc;
        }

    }

    private boolean likeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean likeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void likeUnlike(final MyFocusGoodsBean myShowPassBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (myShowPassBean.getCol_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", myShowPassBean.getProduct_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colproduct(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    if (!isAdded()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            //add by :胡峰，关注的拍品的取消提示
                            UIHelper.t(mContext, "取消关注成功");
                            datas.remove(myShowPassBean);
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
                    if (!isAdded()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    UIHelper.t(mContext, "操作失败");
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }

}
