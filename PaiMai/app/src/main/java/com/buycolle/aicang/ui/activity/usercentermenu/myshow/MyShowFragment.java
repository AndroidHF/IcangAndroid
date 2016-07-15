package com.buycolle.aicang.ui.activity.usercentermenu.myshow;

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
import com.buycolle.aicang.bean.MyShowPassBean;
import com.buycolle.aicang.event.LikeShowEvent;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.ShowDetailActivity;
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
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/5.
 */
public class MyShowFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private MyAdapter myAdapter;

    private ArrayList<MyShowPassBean> datas;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;


    //收藏触发
    public void onEventMainThread(LikeShowEvent event) {
        for (MyShowPassBean data : datas) {
            if (data.getShow_id() == event.getId()) {
                if (event.isStore()) {
                    data.setZ_id(1);
                    data.setZ_count(data.getZ_count() + 1);
                } else {
                    data.setZ_id(0);
                    data.setZ_count(data.getZ_count() - 1);
                }
                myAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

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
        loadData(false);
    }

    private void loadData(final boolean isloadMore) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.show_getownercentersucclistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if(!isloadMore){
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
                            ArrayList<MyShowPassBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyShowPassBean>>() {
                            }.getType());
                            datas.addAll(allDataArrayList);
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
                if(!isloadMore){
                   dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                list.onRefreshComplete();
                if(!isloadMore){
                    dismissLoadingDialog();
                }
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
        EventBus.getDefault().unregister(this);
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_myshow_item, null);
                holder.ll_status_myshow = (LinearLayout) convertView.findViewById(R.id.ll_status_myshow);
                holder.ll_like_lay = (LinearLayout) convertView.findViewById(R.id.ll_like_lay);
                holder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.iv_type_icon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
                holder.iv_show_main = (ImageView) convertView.findViewById(R.id.iv_show_main);
                holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
                holder.iv_user_image = (CircleImageView) convertView.findViewById(R.id.iv_user_image);
                holder.iv_type_name = (TextView) convertView.findViewById(R.id.iv_type_name);
                holder.tv_show_content = (TextView) convertView.findViewById(R.id.tv_show_content);
                holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
                holder.tv_like_content = (TextView) convertView.findViewById(R.id.tv_like_content);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MyShowPassBean myShowPassBean = datas.get(position);
            holder.ll_status_myshow.setVisibility(View.VISIBLE);
            mApplication.setImages(myShowPassBean.getCate_icon(), holder.iv_type_icon);
            holder.iv_type_name.setText(myShowPassBean.getCate_name());
            holder.tv_time.setText(myShowPassBean.getLast_update_date());
            //mApplication.setImages(myShowPassBean.getCover_pic(), holder.iv_show_main);
            //change by hufeng
            mApplication.setShowImages(myShowPassBean.getCover_pic(), holder.iv_show_main);
            holder.tv_show_content.setText(myShowPassBean.getTitle());
            //mApplication.setImages(myShowPassBean.getUser_avatar(), holder.iv_user_image);
            //change by :胡峰，头像的修改
            mApplication.setTouImages(myShowPassBean.getUser_avatar(),holder.iv_user_image);
            holder.tv_user_name.setText(myShowPassBean.getUser_nick());
            holder.tv_comment_content.setText(myShowPassBean.getComment_count() + "");
            holder.tv_like_content.setText(myShowPassBean.getZ_count() + "");
            if (myShowPassBean.getZ_id() > 0) {
                holder.iv_like.setImageResource(R.drawable.myshow_like_red_icon);
            } else {
                holder.iv_like.setImageResource(R.drawable.myshow_like_icon);
            }
            holder.ll_like_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mApplication.isLogin()) {
                        if (likeActionFlag) {
                            likeActionFlag = false;
                            likeUnlike(myShowPassBean);
                        } else {
                            if (likeActionShowToUserFlag) {
                                likeActionShowToUserFlag = false;
                                UIHelper.t(mContext, R.string.comment_action_more);
                            }
                        }
                    } else {
                        UIHelper.jump(mActivity, LoginActivity.class);
                    }
                }
            });
            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("show_id", myShowPassBean.getShow_id());
                    UIHelper.jump(mActivity, ShowDetailActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            LinearLayout ll_status_myshow;
            LinearLayout ll_parent;
            LinearLayout ll_like_lay;
            ImageView iv_type_icon;
            ImageView iv_show_main;
            ImageView iv_like;
            CircleImageView iv_user_image;
            TextView iv_type_name;
            TextView tv_show_content;
            TextView tv_comment_content;
            TextView tv_like_content;
            TextView tv_time;
            TextView tv_user_name;
        }

    }

    private boolean likeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean likeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void likeUnlike(final MyShowPassBean myShowPassBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (myShowPassBean.getZ_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", myShowPassBean.getShow_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_clickzshare(jsonObject, new ApiCallback() {
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
                            if (myShowPassBean.getZ_id() > 0) {
                                myShowPassBean.setZ_id(0);
                                myShowPassBean.setZ_count(myShowPassBean.getZ_count() - 1);

                            } else {
                                myShowPassBean.setZ_id(1);
                                myShowPassBean.setZ_count(myShowPassBean.getZ_count() + 1);

                            }
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
