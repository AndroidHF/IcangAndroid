package com.buycolle.aicang.ui.activity.usercentermenu.faq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.MyAskAnGetAskBean;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.xlistview.XListView;
import com.buycolle.aicang.util.SmileUtils;
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
public class MyAskFragment extends BaseFragment {

    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<MyAskAnGetAskBean> datas;

    private MyAdapter myAdapter;

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
        list.setPullRefreshEnable(false);
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

    private void loadData(final boolean isloadMore) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("owner_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.commoncomment_getselfsendlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore && !isAdded()) {
                    showLoadingDialog();
                } else {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (JSONUtil.isHasData(resultObj)) {
                            if (pageIndex == 1) {
                                datas.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<MyAskAnGetAskBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyAskAnGetAskBean>>() {
                            }.getType());
                            datas.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        } else {

                            tv_null.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isloadMore && !isAdded()) {
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded())
                    return;
                dismissLoadingDialog();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_faq_myask_item, null);
                holder.ll_other_huida = (LinearLayout) convertView.findViewById(R.id.ll_other_huida);
                holder.ll_parent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
                holder.tv_good_title = (TextView) convertView.findViewById(R.id.tv_good_title);
                holder.tv_good_status = (TextView) convertView.findViewById(R.id.tv_good_status);
                holder.tv_my_ask = (TextView) convertView.findViewById(R.id.tv_my_ask);
                holder.tv_my_name = (TextView) convertView.findViewById(R.id.tv_my_name);
                holder.tv_my_ask_time = (TextView) convertView.findViewById(R.id.tv_my_ask_time);
                holder.tv_other_asw = (TextView) convertView.findViewById(R.id.tv_other_asw);
                holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
                holder.tv_other_name = (TextView) convertView.findViewById(R.id.tv_other_name);
                holder.tv_other_asw_time = (TextView) convertView.findViewById(R.id.tv_other_asw_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MyAskAnGetAskBean myAskAnGetAskBean = datas.get(position);
            holder.tv_good_title.setText(myAskAnGetAskBean.getProduct_title());
            holder.tv_good_status.setText(myAskAnGetAskBean.getSt_name());
            holder.tv_my_ask.setText(SmileUtils.getSmiledCommentText(mContext,myAskAnGetAskBean.getContext()));

            holder.tv_my_name.setText(myAskAnGetAskBean.getC_user_nick());
            holder.tv_my_ask_time.setText(myAskAnGetAskBean.getCreate_date());
            if(myAskAnGetAskBean.getSubVec().size()>0){
                holder.ll_other_huida.setVisibility(View.VISIBLE);
                holder.tv_other_asw.setText(SmileUtils.getSmiledCommentText(mContext,myAskAnGetAskBean.getSubVec().get(0).getContext()));
                //change by :胡峰。头像的处理
                mApplication.setTouImages(myAskAnGetAskBean.getSubVec().get(0).getC_user_avatar(),holder.profile_image);
                holder.tv_other_name.setText(myAskAnGetAskBean.getSubVec().get(0).getC_user_nick());
                holder.tv_other_asw_time.setText(myAskAnGetAskBean.getSubVec().get(0).getCreate_date());
            }else{
                holder.ll_other_huida.setVisibility(View.GONE);
            }
            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id",myAskAnGetAskBean.getProduct_id());
                    UIHelper.jump(mActivity,AskAndQuestionNewActivity.class,bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            LinearLayout ll_other_huida;
            LinearLayout ll_parent;

            TextView tv_good_title;
            TextView tv_good_status;
            TextView tv_my_ask;
            TextView tv_my_name;
            TextView tv_my_ask_time;
            TextView tv_other_asw;
            CircleImageView profile_image;
            TextView tv_other_name;
            TextView tv_other_asw_time;
        }

    }

}
