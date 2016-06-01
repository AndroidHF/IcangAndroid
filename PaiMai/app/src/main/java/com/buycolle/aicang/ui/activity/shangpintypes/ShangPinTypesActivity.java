package com.buycolle.aicang.ui.activity.shangpintypes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ShangPinTypeBean;
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
 * 商品类型
 * Created by joe on 16/3/13.
 */
public class ShangPinTypesActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.ll_list_parent)
    LinearLayout llListParent;
    private ArrayList<ShangPinTypeBean> shangPinTypeBeens;
    private MyAdapter myAdapter;

    private String selectParentName = "";
    private String selectParentId = "";

    private  boolean isFromShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpin_types);
        ButterKnife.bind(this);
        shangPinTypeBeens = new ArrayList<>();
        myHeader.init("商品类型", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        myAdapter = new MyAdapter(mContext);
        if(_Bundle!=null){
            isFromShow = _Bundle.getBoolean("isFromShow",false);
        }
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShangPinTypeBean shangPinTypeBean = shangPinTypeBeens.get(position);
                if(isFromShow){
                    Intent intent = new Intent();
                    intent.putExtra("cate_name",shangPinTypeBean.getCate_name());
                    intent.putExtra("cate_id",shangPinTypeBean.getCate_id());
                    intent.putExtra("level",1);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    if(shangPinTypeBean.getIs_level()==1){
                        selectParentName = shangPinTypeBean.getCate_name();
                        selectParentId = shangPinTypeBean.getCate_id();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", shangPinTypeBean.getCate_name());
                        bundle.putString("id", shangPinTypeBean.getCate_id());
                        UIHelper.jumpForResult(mActivity, ShangPinTypesSecondActivity.class, bundle, 1000);
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("cate_name",shangPinTypeBean.getCate_name());
                        intent.putExtra("cate_id",shangPinTypeBean.getCate_id());
                        intent.putExtra("level",1);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
            }
        });
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
        mApplication.apiClient.productcategory_gettoplistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray rows = resultObj.getJSONArray("rows");
                        ArrayList<ShangPinTypeBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ShangPinTypeBean>>() {
                        }.getType());
                        shangPinTypeBeens.addAll(allDataArrayList);
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
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return shangPinTypeBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return shangPinTypeBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_shangpin_types_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ShangPinTypeBean shangPinTypeBean = shangPinTypeBeens.get(position);
            viewHolder.tvTypeName.setText(shangPinTypeBean.getCate_name());
            if(isFromShow){
                viewHolder.iv_arrow.setVisibility(View.GONE);
            }else{
                if (shangPinTypeBean.getIs_level() == 0) {//没有
                    viewHolder.iv_arrow.setVisibility(View.GONE);
                } else {
                    viewHolder.iv_arrow.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.tv_type_name)
            TextView tvTypeName;
            @Bind(R.id.rl_manhua)
            RelativeLayout rlManhua;
            @Bind(R.id.iv_arrow)
            ImageView iv_arrow;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
