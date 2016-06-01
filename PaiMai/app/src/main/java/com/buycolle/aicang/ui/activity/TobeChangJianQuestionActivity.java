package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ChangJianQstBean;
import com.buycolle.aicang.ui.view.MyHeader;
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
 * Created by joe on 16/5/27.
 */
public class TobeChangJianQuestionActivity extends  BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.expand_list)
    ExpandableListView expandList;

    ArrayList<ChangJianQstBean> changJianQstBeens;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changjianquestion);
        ButterKnife.bind(this);
        changJianQstBeens = new ArrayList<>();
        myHeader.init("常见问题", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        myAdapter = new MyAdapter();
        expandList.setGroupIndicator(null);//将控件默认的左边箭头去掉，
        expandList.setAdapter(myAdapter);
        loadData();
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.commonquestion_getsellerlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        changJianQstBeens = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ChangJianQstBean>>() {
                        }.getType());
                        myAdapter.notifyDataSetChanged();
                        if(jsonArray.length()>0){
                            expandList.setVisibility(View.VISIBLE);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
            }
        });
    }

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return changJianQstBeens.size();
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return changJianQstBeens.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return changJianQstBeens.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.qst_expand_header, null);
            LinearLayout ll_line = (LinearLayout) view.findViewById(R.id.ll_line);
            ImageView iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_title.setText(changJianQstBeens.get(groupPosition).getQuest_title());
            if(isExpanded){
                iv_arrow.setImageResource(R.drawable.usercenter_notice_arrow_up);
                ll_line.setVisibility(View.GONE);
            }else{
                iv_arrow.setImageResource(R.drawable.usercenter_notice_arrow_down);
                ll_line.setVisibility(View.VISIBLE);
            }
            if(groupPosition==changJianQstBeens.size()-1){
                ll_line.setVisibility(View.GONE);
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.qst_expand_foot, null);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            LinearLayout ll_line = (LinearLayout) view.findViewById(R.id.ll_line);
            tv_content.setText(Html.fromHtml(changJianQstBeens.get(groupPosition).getAnswer_context()));
            if(groupPosition==changJianQstBeens.size()-1){
                ll_line.setVisibility(View.GONE);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
