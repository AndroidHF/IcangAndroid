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
import com.buycolle.aicang.bean.ForcusSallerBean;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.MainUserCenterActivity;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/5.
 */
public class MyFocusSalerFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private MyAdapter myAdapter;

    private ArrayList<ForcusSallerBean> datas;
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
        mApplication.apiClient.appuser_getcollistbyapp(jsonObject, new ApiCallback() {
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
                            ArrayList<ForcusSallerBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ForcusSallerBean>>() {
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_myfocus_saller_item, null);
                holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
                holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_all_comment = (TextView) convertView.findViewById(R.id.tv_all_comment);
                holder.tv_good_comment = (TextView) convertView.findViewById(R.id.tv_good_comment);
                holder.tv_bad_comment = (TextView) convertView.findViewById(R.id.tv_bad_comment);
                holder.parent = (LinearLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ForcusSallerBean forcusSallerBean = datas.get(position);
            //mApplication.setImages(forcusSallerBean.getUser_avatar(), holder.profile_image);
            //change by :胡峰，头像的修改
            mApplication.setTouImages(forcusSallerBean.getUser_avatar(),holder.profile_image);
            holder.tv_user_name.setText(forcusSallerBean.getUser_nick());
            holder.tv_all_comment.setText(forcusSallerBean.getTotal_comment() + "");
            holder.tv_good_comment.setText(forcusSallerBean.getGood_comment() + "");
            holder.tv_bad_comment.setText(forcusSallerBean.getBad_comment() + "");
            holder.iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeUnlike(forcusSallerBean);
                }
            });
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid",forcusSallerBean.getUser_id());
                    UIHelper.jump(mActivity, MainUserCenterActivity.class,bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            LinearLayout parent;
            CircleImageView profile_image;
            ImageView iv_status;
            TextView tv_user_name;
            TextView tv_all_comment;
            TextView tv_good_comment;
            TextView tv_bad_comment;

        }

    }

    private void likeUnlike(final ForcusSallerBean sallerUserInfoBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", 2);
                jsonObject.put("aim_id", sallerUserInfoBean.getUser_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colseller(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {
                    showLoadingDialog("取消关注中...");
                }

                @Override
                public void onApiSuccess(String response) {
                    if (!isAdded()) {
                        return;
                    }
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            UIHelper.t(mContext, "取消关注成功");
                            datas.remove(sallerUserInfoBean);
                            myAdapter.notifyDataSetChanged();
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
                    if (!isAdded()) {
                        return;
                    }
                    dismissLoadingDialog();
                    UIHelper.t(mContext, "操作失败");
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }

}
