package com.buycolle.aicang.ui.activity.comment;

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
import com.buycolle.aicang.bean.CommentBean;
import com.buycolle.aicang.ui.activity.SubjectActivity;
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
public class CommentsFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<CommentBean> commentBeanArrayList;
    private MyAdapter myAdapter;
    private int currentPosition = 0;//0 所有  1，号  2 怀

    private int userid;

    MainComentActivity mainComentActivity;
    SubjectActivity subjectActivity;

    public static CommentsFragment newInstance(int position, int userid) {
        CommentsFragment orderFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("userid", userid);
        orderFragment.setArguments(args);
        return orderFragment;
    }

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    private void loadData(final boolean isloadMore) {
        isRun = true;
        tv_null.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("seller_user_id", userid);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (currentPosition == 0) {
            mApplication.apiClient.productevaluate_getlistallevaluate(jsonObject, new ApiCallback() {
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
                            int total = resultObj.getInt("total");
                            mainComentActivity.tvAllComment.setText("全部评论  " + total);
                            if (JSONUtil.isHasData(resultObj)) {
                                if (pageIndex == 1) {
                                    tv_null.setVisibility(View.GONE);
                                    commentBeanArrayList.clear();
                                }
                                JSONArray rows = resultObj.getJSONArray("rows");
                                ArrayList<CommentBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<CommentBean>>() {
                                }.getType());
                                commentBeanArrayList.addAll(allDataArrayList);
                                myAdapter.notifyDataSetChanged();
                                if (JSONUtil.isCanLoadMore(resultObj)) {
                                    list.isShowFoot(true);
                                } else {
                                    list.isShowFoot(false);
                                }
                            }else {
                                if (pageIndex == 1){
                                    commentBeanArrayList.clear();
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
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (!isAdded())
                        return;
                    if(pageIndex==1){
                        list.onRefreshComplete();
                    }
                    UIHelper.t(mContext, R.string.net_error);
                    if (isloadMore) {
                        pageIndex--;
                    }
                }
            });

        } else if (currentPosition == 1) {
            mApplication.apiClient.productevaluate_getlistgoodevaluate(jsonObject, new ApiCallback() {
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
                            int total = resultObj.getInt("total");
                            mainComentActivity.tvGoodCommentCount.setText("" + total);
                            if (JSONUtil.isHasData(resultObj)) {
                                if (pageIndex == 1) {
                                    tv_null.setVisibility(View.GONE);
                                    commentBeanArrayList.clear();
                                }
                                JSONArray rows = resultObj.getJSONArray("rows");
                                ArrayList<CommentBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<CommentBean>>() {
                                }.getType());
                                commentBeanArrayList.addAll(allDataArrayList);
                                myAdapter.notifyDataSetChanged();
                                if (JSONUtil.isCanLoadMore(resultObj)) {
                                    list.isShowFoot(true);
                                } else {
                                    list.isShowFoot(false);
                                }
                            }else{
                                if (pageIndex == 1){
                                    commentBeanArrayList.clear();
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
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (!isAdded())
                        return;
                    if(pageIndex==1){
                        list.onRefreshComplete();
                    }
                    UIHelper.t(mContext, R.string.net_error);
                    if (isloadMore) {
                        pageIndex--;
                    }
                }
            });

        } else {
            mApplication.apiClient.productevaluate_getlistbadevaluate(jsonObject, new ApiCallback() {
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
                            int total = resultObj.getInt("total");
                            mainComentActivity.tvBadCommentCount.setText("" + total);
                            if (JSONUtil.isHasData(resultObj)) {
                                if (pageIndex == 1) {
                                    tv_null.setVisibility(View.GONE);
                                    commentBeanArrayList.clear();
                                }
                                JSONArray rows = resultObj.getJSONArray("rows");
                                ArrayList<CommentBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<CommentBean>>() {
                                }.getType());
                                commentBeanArrayList.addAll(allDataArrayList);
                                myAdapter.notifyDataSetChanged();
                                if (JSONUtil.isCanLoadMore(resultObj)) {
                                    list.isShowFoot(true);
                                } else {
                                    list.isShowFoot(false);
                                }
                            }else {
                                if (pageIndex == 1){
                                    commentBeanArrayList.clear();
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
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (!isAdded())
                        return;
                    if(pageIndex==1){
                        list.onRefreshComplete();
                    }
                    UIHelper.t(mContext, R.string.net_error);
                    if (isloadMore) {
                        pageIndex--;
                    }
                }
            });

        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentBeanArrayList = new ArrayList<>();
        currentPosition = getArguments().getInt("position");
        userid = getArguments().getInt("userid");
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
        mainComentActivity = (MainComentActivity) getActivity();
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
                list.setSelection(0);
            }
        });
        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return commentBeanArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return commentBeanArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_main_comment, null);
                holder.iv_tag_icon = (ImageView) convertView.findViewById(R.id.iv_tag_icon);
                holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CommentBean commentBean = commentBeanArrayList.get(i);
            holder.tv_comment_content.setText(commentBean.getEval_content());
            holder.tv_good_title.setText(commentBean.getProduct_title());
            holder.tv_user_name.setText(commentBean.getUser_nick());
            holder.tv_time.setText(commentBean.getCreate_date());
            if (currentPosition == 0) {
                if (commentBean.getIs_good()==1) {
                    holder.iv_tag_icon.setImageResource(R.drawable.comment_good);
                } else {
                    holder.iv_tag_icon.setImageResource(R.drawable.comment_bad);
                }
            } else if (currentPosition == 1) {
                holder.iv_tag_icon.setImageResource(R.drawable.comment_good);
            } else {
                holder.iv_tag_icon.setImageResource(R.drawable.comment_bad);
            }
            return convertView;
        }

        public class ViewHolder {
            ImageView iv_tag_icon;
            TextView tv_comment_content;

            TextView tv_good_title;
            TextView tv_user_name;
            TextView tv_time;
        }

    }

}
