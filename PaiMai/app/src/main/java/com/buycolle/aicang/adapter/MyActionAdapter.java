package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.infomationbean.MyActionListBean;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.SubjectActivity;
import com.buycolle.aicang.ui.activity.infomation.ActionInfoActivity;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/10/14.
 */
public class MyActionAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private MainApplication mApplication;
    private ArrayList<MyActionListBean> myActionListBeans;

    public MyActionAdapter(Activity activity,Context context,ArrayList<MyActionListBean> myActionListBeans,MainApplication mApplication){
        this.activity = activity;
        this.context = context;
        this.myActionListBeans = myActionListBeans;
        this.mApplication = mApplication;
    }


    @Override
    public int getCount() {
        return myActionListBeans == null ? 0:myActionListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return myActionListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_list_activity,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyActionListBean myActionListBean = myActionListBeans.get(position);
        //设置时间
        holder.tvActivityTime.setText(myActionListBean.getCreate_date());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth() - 64;
        ViewGroup.LayoutParams layoutParams = holder.ivActivity.getLayoutParams();//获取当前的控件的参数
        layoutParams.width = width;
        layoutParams.height = width / 3;//将高度设置为宽度的三分之一
        holder.ivActivity.setLayoutParams(layoutParams);
        mApplication.setImages(myActionListBean.getBanner_icon(),holder.ivActivity);

        //活动的跳转
        holder.llActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myActionListBean.getAction_type() == 1){//url链接
                    Uri uri = Uri.parse(myActionListBean.getBanner_link());
                    Log.i("banner_link", myActionListBean.getBanner_link());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Log.i("link_url", String.valueOf(uri));
                    context.startActivity(intent);
                }else if (myActionListBean.getAction_type() == 2){//拍品广告
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", myActionListBean.getTarget_id());
                    UIHelper.jump(activity, PaiPinDetailActivity.class, bundle);
                }else if (myActionListBean.getAction_type() == 4){//内部活动界面
                    Bundle bundle = new Bundle();
                    bundle.putInt("event_id",myActionListBean.getTarget_id());
                    UIHelper.jump(activity, SubjectActivity.class,bundle);
                }else if (myActionListBean.getAction_type() == 5){//跳转到外部的webview
                    Bundle bundle = new Bundle();
                    bundle.putString("type","活动WebView");
                    bundle.putString("context",myActionListBean.getContext());
                    UIHelper.jump(activity, ActionInfoActivity.class,bundle);
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder{
        ViewHolder(View view){ButterKnife.bind(this,view);}

        //活动的整体的跳转
        @Bind(R.id.ll_activity)
        LinearLayout llActivity;

        //创建时间
        @Bind(R.id.tv_activity_time)
        TextView tvActivityTime;

        //活动图片
        @Bind(R.id.iv_activity)
        ImageView ivActivity;
    }
}
