package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.JingPaiJiLuBean;
import com.buycolle.aicang.ui.view.MyHeader;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/10.
 */
public class JingPaiJiLuActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;


    private ArrayList<JingPaiJiLuBean> datas;
    private int product_id;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingpai_jilu);
        ButterKnife.bind(this);
        datas = new ArrayList<>();
        product_id = _Bundle.getInt("product_id");
        myHeader.init("竞拍记录", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);
        list.setPullRefreshEnable(false);

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
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("product_id", product_id);
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.productjpfollow_getcurproducthislist(jsonObject, new ApiCallback() {
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
                        if (JSONUtil.isHasData(resultObj)) {
                            if (pageIndex == 1) {
                                datas.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<JingPaiJiLuBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<JingPaiJiLuBean>>() {
                            }.getType());
                            datas.addAll(allDataArrayList);
                            myAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isFinishing())
                    return;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_jingpai_jilu_item, null);
                holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_pai_time = (TextView) convertView.findViewById(R.id.tv_pai_time);
                holder.tv_paipin_pai_count = (TextView) convertView.findViewById(R.id.tv_paipin_pai_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            JingPaiJiLuBean jingPaiJiLuBean = datas.get(position);
            //change by :胡峰，头像的处理
            mApplication.setTouImages(jingPaiJiLuBean.getUser_avatar(), holder.profile_image);
            holder.tv_user_name.setText(jingPaiJiLuBean.getUser_nick());
            holder.tv_pai_time.setText(jingPaiJiLuBean.getCreate_date());
            holder.tv_paipin_pai_count.setText("￥" + StringFormatUtil.getDoubleFormatNew(jingPaiJiLuBean.getPrice()));
            return convertView;
        }


        public class ViewHolder {

            CircleImageView profile_image;
            TextView tv_user_name;
            TextView tv_paipin_pai_count;
            TextView tv_pai_time;


        }

    }
}
