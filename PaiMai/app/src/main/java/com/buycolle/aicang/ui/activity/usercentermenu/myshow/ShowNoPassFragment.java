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
import com.buycolle.aicang.bean.MyShowNoPassBean;
import com.buycolle.aicang.event.EditShowEvent;
import com.buycolle.aicang.ui.activity.EditPublicShowActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.ShowMainActionPop;
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
public class ShowNoPassFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;

    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;

    @Bind(R.id.tv_null)
    TextView tv_null;

    private MyAdapter myAdapter;

    private ArrayList<MyShowNoPassBean> datas;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    public void onEventMainThread(EditShowEvent event) {
        pageIndex = 1;
        loadData(false);
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
        mApplication.apiClient.show_getownercenterfaillistbyapp(jsonObject, new ApiCallback() {
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
                            ArrayList<MyShowNoPassBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyShowNoPassBean>>() {
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
                list.onRefreshComplete();
                isRun = false;
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
                holder.ll_status_nopass = (LinearLayout) convertView.findViewById(R.id.ll_status_nopass);
                holder.tv_no_pass = (TextView) convertView.findViewById(R.id.tv_no_pass);
                holder.iv_notice_nopass_msg = (ImageView) convertView.findViewById(R.id.iv_notice_nopass_msg);
                holder.iv_type_icon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
                holder.iv_show_main = (ImageView) convertView.findViewById(R.id.iv_show_main);
                holder.iv_user_image = (CircleImageView) convertView.findViewById(R.id.iv_user_image);
                holder.iv_type_name = (TextView) convertView.findViewById(R.id.iv_type_name);
                holder.tv_show_content = (TextView) convertView.findViewById(R.id.tv_show_content);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_edit_show = (TextView) convertView.findViewById(R.id.tv_edit_show);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MyShowNoPassBean bean = datas.get(position);

            holder.tv_no_pass.setVisibility(View.VISIBLE);

            if(bean.getCheck_status()==0){//0未审核，1不成功
                holder.ll_status_nopass.setVisibility(View.GONE);
                holder.tv_edit_show.setVisibility(View.GONE);
                holder.iv_notice_nopass_msg.setVisibility(View.GONE);
                holder.tv_no_pass.setText("审核中");
            }else{
                holder.ll_status_nopass.setVisibility(View.VISIBLE);
                holder.tv_edit_show.setVisibility(View.VISIBLE);
                holder.iv_notice_nopass_msg.setVisibility(View.VISIBLE);
                holder.tv_no_pass.setText("未通过");
                holder.iv_notice_nopass_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ShowMainActionPop(mActivity).setGra(true).setMsg(bean.getCheck_reason()).show(view);
                    }
                });
            }

            mApplication.setImages(bean.getCate_icon(),holder.iv_type_icon);
            holder.iv_type_name.setText(bean.getCate_name());
            holder.tv_time.setText(bean.getLast_update_date());
            //mApplication.setImages(bean.getCover_pic(),holder.iv_show_main);
            //change by hufeng
            mApplication.setShowImages(bean.getCover_pic(),holder.iv_show_main);
            holder.tv_show_content.setText(bean.getTitle());
            //mApplication.setImages(bean.getUser_avatar(),holder.iv_user_image);
            //change by :胡峰，头像的处理
            mApplication.setTouImages(bean.getUser_avatar(),holder.iv_user_image);
            holder.tv_user_name.setText(bean.getUser_nick());

            holder.tv_edit_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("show_id", bean.getShow_id());
                    UIHelper.jump(mActivity, EditPublicShowActivity.class, bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView tv_no_pass;
            LinearLayout ll_status_nopass;
            TextView tv_edit_show;
            ImageView iv_notice_nopass_msg;

            ImageView iv_type_icon;
            ImageView iv_show_main;
            CircleImageView iv_user_image;
            TextView iv_type_name;
            TextView tv_show_content;
            TextView tv_time;
            TextView tv_user_name;
        }

    }

}
