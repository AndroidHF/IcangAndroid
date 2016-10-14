package com.buycolle.aicang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.NoticeBean;
import com.buycolle.aicang.ui.view.MyExpandTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/3.
 */
public class UserCenterNoticeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NoticeBean> noticeBeans;

    public UserCenterNoticeAdapter(Context context, ArrayList<NoticeBean> noticeBeans) {
        this.context = context;
        this.noticeBeans = noticeBeans;

    }

    @Override
    public int getCount() {
        return noticeBeans==null? 0:noticeBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_usercenter_notice_lay, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NoticeBean noticeBean = noticeBeans.get(position);
        if (position == 0) {
            holder.llTopBlackLine.setVisibility(View.VISIBLE);
            if (noticeBeans.size() == 1) {
                holder.llBottomBlackLineLage.setVisibility(View.VISIBLE);
                holder.llBottomBlackLineSmall.setVisibility(View.GONE);
            } else {
                holder.llBottomBlackLineLage.setVisibility(View.GONE);
                holder.llBottomBlackLineSmall.setVisibility(View.VISIBLE);
            }
        } else {
            holder.llTopBlackLine.setVisibility(View.GONE);
            if (position == noticeBeans.size() - 1) {
                holder.llBottomBlackLineLage.setVisibility(View.VISIBLE);
                holder.llBottomBlackLineSmall.setVisibility(View.GONE);
            } else {
                holder.llBottomBlackLineLage.setVisibility(View.GONE);
                holder.llBottomBlackLineSmall.setVisibility(View.VISIBLE);
            }
        }

        holder.tv_time.setText(noticeBean.getCreate_date());
        holder.expandTv.setText(noticeBean.getContext());
        holder.expandTv.setOnStateChangeListener(new MyExpandTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isExpand) {
                noticeBean.setIsExpand(isExpand);
            }
        });
        holder.expandTv.setIsExpand(noticeBean.isExpand());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_usercenter_notice_lay.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.ll_top_black_line)
        LinearLayout llTopBlackLine;
        @Bind(R.id.expand_tv)
        MyExpandTextView expandTv;
        @Bind(R.id.ll_bottom_black_line_lage)
        LinearLayout llBottomBlackLineLage;
        @Bind(R.id.ll_bottom_black_line_small)
        LinearLayout llBottomBlackLineSmall;
        @Bind(R.id.tv_time)
        TextView tv_time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
