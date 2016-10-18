package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.infomationbean.MyTradeListBean;
import com.buycolle.aicang.ui.activity.ConectionActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.faq.MyFAQActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.MyBuyActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mysale.MySaleActivity;
import com.buycolle.aicang.util.UIHelper;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/10/11.
 */
public class MyTradeListAdapter extends BaseAdapter {

    private Activity activity;
    private Context context;
    private MainApplication mApplication;
    private ArrayList<MyTradeListBean> myTradeListBeans;
    private int type;

    public MyTradeListAdapter(Activity activity,Context context,ArrayList<MyTradeListBean> myTradeListBeans,MainApplication mApplication,int type){
        this.activity = activity;
        this.context = context;
        this.myTradeListBeans = myTradeListBeans;
        this.mApplication = mApplication;
        this.type = type;
    }
    @Override
    public int getCount() {
        Log.i("单页显示列表的第一条消息", myTradeListBeans.size() + "");
        return myTradeListBeans == null ? 0:myTradeListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return myTradeListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_list_trade,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyTradeListBean myTradeListBean = myTradeListBeans.get(position);
        mApplication.setImages(myTradeListBean.getCover_pic(), holder.ivListPic);
        holder.tvListTime.setText(myTradeListBean.getCreate_date());
        holder.tvListInfo.setText(myTradeListBean.getContent());
        if (myTradeListBean.getIs_read() == 0){
            holder.ivListNew.setVisibility(View.VISIBLE);
        }else {
            holder.ivListNew.setVisibility(View.GONE);
        }

        //商品的跳转到拍品详情界面
        final ViewHolder finalHolder = holder;
        holder.llTradeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.ivListNew.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPush", true);
                bundle.putInt("id", myTradeListBean.getKey_id());
                bundle.putInt("type", myTradeListBean.getType());
                if (myTradeListBean.getType() == 1) {//出价被超
                    UIHelper.jump(activity, PaiPinDetailActivity.class, bundle);
                } else if (myTradeListBean.getType() == 2 || myTradeListBean.getType() == 3 || myTradeListBean.getType() == 4 || myTradeListBean.getType() == 8) {//
                    UIHelper.jump(activity, MyBuyActivity.class, bundle);
                } else if (myTradeListBean.getType() == 9 || myTradeListBean.getType() == 10) {
                    UIHelper.jump(activity, MySaleActivity.class, bundle);
                } else if (myTradeListBean.getType() == 15 || myTradeListBean.getType() == 16) {
                    UIHelper.jump(activity, MyFAQActivity.class, bundle);
                } else if (myTradeListBean.getType() == 17) {
                    UIHelper.jump(activity, ConectionActivity.class, bundle);
                }
                UpdateMessageInfo(myTradeListBean.getId());
            }
        });

        return convertView;
    }


    public static class ViewHolder{
        ViewHolder(View view){ButterKnife.bind(this,view);}

        //整体的消息列表
        @Bind(R.id.ll_trade_list)
        LinearLayout llTradeList;

        //列表商品图
        @Bind(R.id.iv_list_pic)
        ImageView ivListPic;
        //创建时间
        @Bind(R.id.tv_list_time)
        TextView tvListTime;
        //新消息图标
        @Bind(R.id.iv_list_new)
        ImageView ivListNew;
        //消息内容
        @Bind(R.id.tv_list_info)
        TextView tvListInfo;
    }

    /**
     * 更新消息中心消息为已读
     * @param id
     */
    public void UpdateMessageInfo(int id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(context).getSessionid());
            if (type == 1){
                jsonObject.put("id",id+"");
            }else if(type == 2){
                jsonObject.put("id",id+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.messageCenter_updateIsReadByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {

            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
    }
}
