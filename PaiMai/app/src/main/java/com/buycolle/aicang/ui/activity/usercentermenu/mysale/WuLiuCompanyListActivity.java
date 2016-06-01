package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.WuLiuBean;
import com.buycolle.aicang.ui.activity.BaseActivity;
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
 * Created by joe on 16/3/6.
 */
public class WuLiuCompanyListActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;

    ArrayList<WuLiuBean> wuLiuBeanArrayList;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.ll_list_parent)
    LinearLayout llListParent;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuliucompanylist);
        ButterKnife.bind(this);
        wuLiuBeanArrayList = new ArrayList<>();
        myHeader.init("物流公司", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WuLiuBean wuLiuBean = wuLiuBeanArrayList.get(position);
                Intent intent = new Intent();
                intent.putExtra("id", wuLiuBean.getExpre_id());
                intent.putExtra("name", wuLiuBean.getExpress_name());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        loadData();
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.express_getlistbyapp(jsonObject, new ApiCallback() {
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
                        JSONArray rows = resultObj.getJSONArray("rows");
                        ArrayList<WuLiuBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<WuLiuBean>>() {
                        }.getType());
                        wuLiuBeanArrayList.addAll(allDataArrayList);
                        myAdapter.notifyDataSetChanged();
                        if (allDataArrayList.size() > 0) {
                            llListParent.setVisibility(View.VISIBLE);
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
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wuLiuBeanArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return wuLiuBeanArrayList.get(i);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_wuliu_item, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(wuLiuBeanArrayList.get(position).getExpress_name());
            return convertView;
        }

        public class ViewHolder {

            TextView tv_name;

        }

    }


}
