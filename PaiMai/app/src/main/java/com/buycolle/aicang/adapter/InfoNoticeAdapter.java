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

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.infomationbean.InfoNoticeBean;
import com.buycolle.aicang.ui.activity.infomation.NoticeInfoActivity;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/10/9.
 */
public class InfoNoticeAdapter extends BaseAdapter {

    private Context context;
    private Activity activity;
    private ArrayList<InfoNoticeBean> infoNoticeBeans;

    public InfoNoticeAdapter(Activity activity, Context context, ArrayList<InfoNoticeBean> infoNoticeBeans){
        this.activity = activity;
        this.context = context;
        this.infoNoticeBeans = infoNoticeBeans;
    }
    @Override
    public int getCount() {
        return infoNoticeBeans == null? 0:infoNoticeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return infoNoticeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_info_notice,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (infoNoticeBeans == null){
            holder.llNoticeHas.setVisibility(View.GONE);
            holder.llNoticeNo.setVisibility(View.VISIBLE);
        }else {
            holder.llNoticeHas.setVisibility(View.VISIBLE);
            holder.llNoticeNo.setVisibility(View.GONE);
            final InfoNoticeBean infoNoticeBean = infoNoticeBeans.get(position);
            //设置公告类型的值
            holder.tvNoticeTitle.setText(infoNoticeBean.getType());
            //设置公告时间
            holder.tvNoticeTime.setText(infoNoticeBean.getCreate_date());
            //设置置顶图标
            if (infoNoticeBean.getRemark() > 0){
                holder.ivNoticeStick.setVisibility(View.VISIBLE);
            }else {
                holder.ivNoticeStick.setVisibility(View.GONE);
            }

            //设置公告的具体标题
            holder.tvNoticeInfo.setText(infoNoticeBean.getTitle());

            //设置公告的跳转
            //跳转时需要的数据
            final Bundle bundle = new Bundle();
            bundle.putString("new_context",infoNoticeBean.getNew_context().toString().trim());
//            bundle.putString("type",infoNoticeBean.getType());
            holder.rlNoticeJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.jump(activity,NoticeInfoActivity.class,bundle);
                }
            });
        }

        return convertView;
    }

    public static class ViewHolder{
        ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        @Bind(R.id.ll_notice_has)
        LinearLayout llNoticeHas;//有公告显示的界面

        @Bind(R.id.ll_notice_no)
        LinearLayout llNoticeNo;//没有公告显示的界面

        @Bind(R.id.tv_notice_title)
        TextView tvNoticeTitle;//公告总的标题

        @Bind(R.id.tv_notice_time)
        TextView tvNoticeTime;//公告时间

        @Bind(R.id.iv_notice_stick)
        ImageView ivNoticeStick;//公告置顶按钮

        @Bind(R.id.tv_notice_info)
        TextView tvNoticeInfo;//公告的具体内容

        @Bind(R.id.rl_notice_jump)
        RelativeLayout rlNoticeJump;//公告跳转控件

    }
}
