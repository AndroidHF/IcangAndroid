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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.infomationbean.MessageCenterHomeBean;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.infomation.MyActionActivity;
import com.buycolle.aicang.ui.activity.infomation.MyDirectActivity;
import com.buycolle.aicang.ui.activity.infomation.MySystemActivity;
import com.buycolle.aicang.ui.activity.infomation.MyTradeActivity;
import com.buycolle.aicang.ui.view.NoticeDialog;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/10/9.
 * 消息中心的适配器，用于加载消息中心的消息界面
 */
public class InfoCentAdapterNew extends BaseAdapter {
    private Activity activity;
    private Context context;
    private ArrayList<MessageCenterHomeBean> messageCenterHomeBeans;
    private MainApplication mApplication;



    public InfoCentAdapterNew(Activity activity,Context context,ArrayList<MessageCenterHomeBean> messageCenterHomeBeans,MainApplication mApplication){
        this.activity = activity;
        this.context = context;
        this.messageCenterHomeBeans = messageCenterHomeBeans;
        this.mApplication = mApplication;
    }
    @Override
    public int getCount() {
        return messageCenterHomeBeans == null ? 0:messageCenterHomeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return messageCenterHomeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_info_center_new,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MessageCenterHomeBean messageCenterHomeBean = messageCenterHomeBeans.get(position);
        if (position == 0){//表示我的交易
            holder.ivMyInfoIcon.setImageResource(R.drawable.my_trade_pic);
            holder.tvMyInfoTitle.setText("我的交易");//设置标题
            if (messageCenterHomeBean.getCount() == 0){
                holder.tvMyInfoCount.setVisibility(View.GONE);
                holder.tvMyInfoTime.setVisibility(View.GONE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfo.setText("现在没有新的消息，敬请期待！");
            }else {
                holder.tvMyInfoCount.setVisibility(View.VISIBLE);
                holder.tvMyInfoTime.setVisibility(View.VISIBLE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfoCount.setText(messageCenterHomeBean.getCount()+"");//设置消息数
                holder.tvMyInfoTime.setText(messageCenterHomeBean.getCreate_date());//设置时间
                holder.tvMyInfo.setText(messageCenterHomeBean.getTitle());//设置标题内容
            }


        }else if (position == 1){//表示我的私信
            holder.ivMyInfoIcon.setImageResource(R.drawable.direct_messages_pic);
            holder.tvMyInfoTitle.setText("我的私信");//设置标题
            if (messageCenterHomeBean.getCount() == 0){
                holder.tvMyInfoCount.setVisibility(View.GONE);
                holder.tvMyInfoTime.setVisibility(View.GONE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfo.setText("现在没有新的消息，敬请期待！");
            }else {
                holder.tvMyInfoCount.setVisibility(View.VISIBLE);
                holder.tvMyInfoTime.setVisibility(View.VISIBLE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfoCount.setText(messageCenterHomeBean.getCount()+"");//设置消息数
                holder.tvMyInfoTime.setText(messageCenterHomeBean.getCreate_date());//设置时间
                holder.tvMyInfo.setText(messageCenterHomeBean.getTitle());//设置标题内容
            }

        }else if (position == 2){//表示活动信息
            holder.ivMyInfoIcon.setImageResource(R.drawable.action_massage_pic);
            holder.tvMyInfoTitle.setText("活动信息");//设置标题
            if (messageCenterHomeBean.getCount() == 0){
                holder.tvMyInfoCount.setVisibility(View.GONE);
                holder.tvMyInfoTime.setVisibility(View.GONE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfo.setText("现在没有新的活动，敬请期待！");
            }else {
                holder.tvMyInfoCount.setVisibility(View.GONE);
                holder.tvMyInfoTime.setVisibility(View.VISIBLE);
                holder.llActivityLogo.setVisibility(View.VISIBLE);
                mApplication.setImages(messageCenterHomeBean.getLogo_url(),holder.ivActivityLogo);//设置活动图标
                holder.tvMyInfoTime.setText(messageCenterHomeBean.getCreate_date());//设置时间
                holder.tvMyInfo.setText(messageCenterHomeBean.getTitle());//设置标题内容
            }

        }else if (position == 3){//表示系统信息
            holder.ivMyInfoIcon.setImageResource(R.drawable.system_messages_pic);
            holder.tvMyInfoTitle.setText("系统消息");
            if (messageCenterHomeBean.getCount() == 0){
                holder.tvMyInfoCount.setVisibility(View.GONE);
                holder.tvMyInfoTime.setVisibility(View.GONE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfo.setText("现在没有新的消息，敬请期待！");
            }else {
                holder.tvMyInfoCount.setVisibility(View.VISIBLE);
                holder.tvMyInfoTime.setVisibility(View.VISIBLE);
                holder.llActivityLogo.setVisibility(View.GONE);
                holder.tvMyInfoCount.setText(messageCenterHomeBean.getCount()+"");//设置消息数
                holder.tvMyInfoTime.setText(messageCenterHomeBean.getCreate_date());//设置时间
                holder.tvMyInfo.setText(messageCenterHomeBean.getTitle());//设置标题内容
            }

        }



        //跳转的逻辑
        final ViewHolder finalHolder = holder;
        holder.rlMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getInstance().isLogin()){
                    if (position == 0){
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",messageCenterHomeBean.getType());
                        UIHelper.jump(activity, MyTradeActivity.class,bundle);
                    }else if (position == 1){
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", messageCenterHomeBean.getType());
                        UIHelper.jump(activity, MyDirectActivity.class, bundle);

                    }else if (position == 3){
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",messageCenterHomeBean.getType());
                        UIHelper.jump(activity, MySystemActivity.class,bundle);
                    }else  if (position == 2){
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",messageCenterHomeBean.getType());
                        UIHelper.jump(activity, MyActionActivity.class, bundle);
                    }
                }else {
                    if (position == 2){
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",messageCenterHomeBean.getType());
                        UIHelper.jump(activity, MyActionActivity.class, bundle);
                    }else {
                        new NoticeDialog(context,"温馨提示","请先登录账号").setCallBack(new NoticeDialog.CallBack() {
                            @Override
                            public void ok() {
                                UIHelper.jump(activity, LoginActivity.class);
                            }

                            @Override
                            public void cancle() {

                            }
                        }).show();
                    }

                }

                finalHolder.tvMyInfoCount.setVisibility(View.GONE);
            }
        });
        return convertView;
    }

    public static class ViewHolder{
        ViewHolder(View view){ButterKnife.bind(this,view);}

        //消息控件的整体
        @Bind(R.id.rl_my_info)
        RelativeLayout rlMyInfo;

        //消息类型的图标
        @Bind(R.id.iv_my_info_icon)
        ImageView ivMyInfoIcon;

        //消息的标题
        @Bind(R.id.tv_my_info_title)
        TextView tvMyInfoTitle;

        //创建时间
        @Bind(R.id.tv_my_info_time)
        TextView tvMyInfoTime;

        //消息数
        @Bind(R.id.tv_my_info_count)
        TextView tvMyInfoCount;

        //活动的logo总的控件
        @Bind(R.id.ll_activity_logo)
        LinearLayout llActivityLogo;

        //活动的具体图标
        @Bind(R.id.iv_activity_logo)
        ImageView ivActivityLogo;

        //消息内容
        @Bind(R.id.tv_my_info)
        TextView tvMyInfo;

    }
}
