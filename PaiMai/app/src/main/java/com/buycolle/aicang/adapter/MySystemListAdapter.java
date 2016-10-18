package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
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
public class MySystemListAdapter extends BaseAdapter {

    private Activity activity;
    private Context context;
    private MainApplication mApplication;
    private ArrayList<MyTradeListBean> myTradeListBeans;
    private int type;

    public MySystemListAdapter(Activity activity, Context context, ArrayList<MyTradeListBean> myTradeListBeans, MainApplication mApplication,int type){
        this.activity = activity;
        this.context = context;
        this.myTradeListBeans = myTradeListBeans;
        this.mApplication = mApplication;
        this.type = type;
    }
    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.view_list_system,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyTradeListBean myTradeListBean = myTradeListBeans.get(position);
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
                if (myTradeListBean.getType() == 5 || myTradeListBean.getType() == 13) {//关注的还有30分钟、订阅拍卖会还有20分钟结束，跳转到拍品详情界面
                    UIHelper.jump(activity, PaiPinDetailActivity.class, bundle);
                }
//                else if (myTradeListBean.getType() == 6 || myTradeListBean.getType() == 7 || myTradeListBean.getType() == 11 || myTradeListBean.getType() == 12) {//冻结账户和解锁账户、卖家身份审核通过和不通过，跳转到首页
//                    UIHelper.jump(activity, MainActivity.class, bundle);
//                }
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
            if (type == 4){
                //jsonObject.put("other_id",id+"");
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
