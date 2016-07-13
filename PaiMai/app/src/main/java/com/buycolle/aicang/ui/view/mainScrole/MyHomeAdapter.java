package com.buycolle.aicang.ui.view.mainScrole;

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

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.HomeGoodsBean;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;


public class MyHomeAdapter extends BaseAdapter {

    private ArrayList<HomeGoodsBean> homeGoodsBeanArrayList;
    private Context mContext;
    private Activity mActivity;

    public MyHomeAdapter(Context mContext, ArrayList<HomeGoodsBean> strList) {
        this.homeGoodsBeanArrayList = strList;
        this.mContext = mContext;
        this.mActivity = (Activity) mContext;
    }

    @Override
    public int getCount() {
        return homeGoodsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeGoodsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder = null;
        if (convertView == null) {
            myholder = new Myholder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_home_main, null);
            myholder.iv_1 = (ImageView) convertView.findViewById(R.id.iv_1);
            myholder.iv_2 = (ImageView) convertView.findViewById(R.id.iv_2);
            myholder.ll_item_1 = (LinearLayout) convertView.findViewById(R.id.ll_item_1);
            myholder.ll_item_2 = (LinearLayout) convertView.findViewById(R.id.ll_item_2);

            myholder.ll_yikoujia_lay_1 = (LinearLayout) convertView.findViewById(R.id.ll_yikoujia_lay_1);
            myholder.tv_good_title_1 = (TextView) convertView.findViewById(R.id.tv_good_title_1);
            myholder.tv_yikou_price_value_1 = (TextView) convertView.findViewById(R.id.tv_yikou_price_value_1);
            myholder.tv_baoyou_1 = (TextView) convertView.findViewById(R.id.tv_baoyou_1);
            myholder.tv_time_1 = (TextView) convertView.findViewById(R.id.tv_time_1);
            myholder.tv_count_1 = (TextView) convertView.findViewById(R.id.tv_count_1);
            myholder.tv_curret_price_1 = (TextView) convertView.findViewById(R.id.tv_curret_price_1);
            myholder.iv_rate_1 = (ImageView) convertView.findViewById(R.id.iv_rate_1);

            myholder.ll_yikoujia_lay_2 = (LinearLayout) convertView.findViewById(R.id.ll_yikoujia_lay_2);
            myholder.tv_good_title_2 = (TextView) convertView.findViewById(R.id.tv_good_title_2);
            myholder.tv_yikou_price_value_2 = (TextView) convertView.findViewById(R.id.tv_yikou_price_value_2);
            myholder.tv_baoyou_2 = (TextView) convertView.findViewById(R.id.tv_baoyou_2);
            myholder.tv_time_2 = (TextView) convertView.findViewById(R.id.tv_time_2);
            myholder.tv_count_2 = (TextView) convertView.findViewById(R.id.tv_count_2);
            myholder.tv_curret_price_2 = (TextView) convertView.findViewById(R.id.tv_curret_price_2);
            myholder.iv_rate_2 = (ImageView) convertView.findViewById(R.id.iv_rate_2);


            convertView.setTag(myholder);

        } else {
            myholder = (Myholder) convertView.getTag();

        }
        final HomeGoodsBean homeGoodsBean = homeGoodsBeanArrayList.get(position);
        if (homeGoodsBean.isSingle()) {
            myholder.ll_item_1.setVisibility(View.VISIBLE);
            myholder.ll_item_2.setVisibility(View.GONE);
            myholder.tv_good_title_1.setText(homeGoodsBean.getHomeGoodsChildBeens().get(0).getProduct_title());
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(0).getCover_pic(), myholder.iv_1);
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).getOpen_but_it() == 0) {//没有一口价
                myholder.ll_yikoujia_lay_1.setVisibility(View.VISIBLE);
                //change by:胡峰：没有一口价，显示“无”
                //myholder.tv_yikou_price_value_1.setText("￥ " + "---");
                myholder.tv_yikou_price_value_1.setText("无");
            } else {
                myholder.ll_yikoujia_lay_1.setVisibility(View.VISIBLE);
                myholder.tv_yikou_price_value_1.setText("￥"+ StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getBut_it_price()));
            }
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).getExpress_out_type() == 1) {//买家
                myholder.tv_baoyou_1.setText("到付");
            } else {
                myholder.tv_baoyou_1.setText("包邮");
            }
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).isFinish()) {
                myholder.tv_time_1.setText("已结束");
            } else {
                myholder.tv_time_1.setText(StringFormatUtil.getHomeDaoJiShiTime(homeGoodsBean.getHomeGoodsChildBeens().get(0).getTime() / 1000));
            }
            //无人出价的时候，显示起拍价，而不是显示0，如果有人出价的时候，显示当前价格
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).getJp_count() == 0){
                myholder.tv_curret_price_1.setText("￥"+StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getBegin_auct_price()));
            }else {
                myholder.tv_curret_price_1.setText("￥"+StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getMax_pric()));
            }
            myholder.tv_count_1.setText(homeGoodsBean.getHomeGoodsChildBeens().get(0).getJp_count() + "");
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(0).getRaretag_icon(), myholder.iv_rate_1);
            myholder.ll_item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", homeGoodsBean.getHomeGoodsChildBeens().get(0).getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
        } else {
            myholder.ll_item_1.setVisibility(View.VISIBLE);
            myholder.ll_item_2.setVisibility(View.VISIBLE);

            myholder.tv_good_title_1.setText(homeGoodsBean.getHomeGoodsChildBeens().get(0).getProduct_title());
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(0).getCover_pic(), myholder.iv_1);
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).getOpen_but_it() == 0) {//没有一口价
                myholder.ll_yikoujia_lay_1.setVisibility(View.VISIBLE);
                //change by :胡峰，没有开启一口价显示"无"
                //myholder.tv_yikou_price_value_1.setText("￥ " + "---");
                myholder.tv_yikou_price_value_1.setText("无");
            } else {
                myholder.ll_yikoujia_lay_1.setVisibility(View.VISIBLE);
                myholder.tv_yikou_price_value_1.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getBut_it_price()));
            }
            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).getExpress_out_type() == 1) {//买家
                myholder.tv_baoyou_1.setText("到付");
            } else {
                myholder.tv_baoyou_1.setText("包邮");
            }
            if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getMax_pric())) > Double.valueOf(StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getBegin_auct_price()))) {
                myholder.tv_curret_price_1.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getMax_pric()));
            } else {
                myholder.tv_curret_price_1.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(0).getBegin_auct_price()));
            }
            myholder.tv_count_1.setText(homeGoodsBean.getHomeGoodsChildBeens().get(0).getJp_count() + "");
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(0).getRaretag_icon(), myholder.iv_rate_1);

            if (homeGoodsBean.getHomeGoodsChildBeens().get(0).isFinish()) {
                myholder.tv_time_1.setText("已结束");
            } else {
                myholder.tv_time_1.setText(StringFormatUtil.getHomeDaoJiShiTime(homeGoodsBean.getHomeGoodsChildBeens().get(0).getTime() / 1000));
            }

            //add by :胡峰，透明色的修改

            myholder.tv_good_title_2.setText(homeGoodsBean.getHomeGoodsChildBeens().get(1).getProduct_title());
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(1).getCover_pic(), myholder.iv_2);

            if (homeGoodsBean.getHomeGoodsChildBeens().get(1).getOpen_but_it() == 0) {//没有一口价
                myholder.ll_yikoujia_lay_2.setVisibility(View.VISIBLE);
                //change by：胡峰，没有一口价，显示"无"
                //myholder.tv_yikou_price_value_2.setText("￥ " + "---");
                myholder.tv_yikou_price_value_2.setText("无");
            } else {
                myholder.ll_yikoujia_lay_2.setVisibility(View.VISIBLE);
                myholder.tv_yikou_price_value_2.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(1).getBut_it_price()));
            }
            if (homeGoodsBean.getHomeGoodsChildBeens().get(1).getExpress_out_type() == 1) {//买家
                myholder.tv_baoyou_2.setText("到付");
            } else {
                myholder.tv_baoyou_2.setText("包邮");
            }
            if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(1).getMax_pric())) > Double.valueOf(StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(1).getBegin_auct_price()))) {
                myholder.tv_curret_price_2.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(1).getMax_pric()));
            } else {
                myholder.tv_curret_price_2.setText("￥" + StringFormatUtil.getDoubleFormatNew(homeGoodsBean.getHomeGoodsChildBeens().get(1).getBegin_auct_price()));
            }
            myholder.tv_count_2.setText(homeGoodsBean.getHomeGoodsChildBeens().get(1).getJp_count() + "");
            MainApplication.getInstance().setImages(homeGoodsBean.getHomeGoodsChildBeens().get(1).getRaretag_icon(), myholder.iv_rate_2);

            if (homeGoodsBean.getHomeGoodsChildBeens().get(1).isFinish()) {
                myholder.tv_time_2.setText("已结束");
            } else {
                myholder.tv_time_2.setText(StringFormatUtil.getHomeDaoJiShiTime(homeGoodsBean.getHomeGoodsChildBeens().get(1).getTime() / 1000));
            }

            myholder.ll_item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", homeGoodsBean.getHomeGoodsChildBeens().get(0).getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });
            myholder.ll_item_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", homeGoodsBean.getHomeGoodsChildBeens().get(1).getProduct_id());
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }
            });

        }

        return convertView;
    }

    public class Myholder {

        ImageView iv_1;
        ImageView iv_2;
        LinearLayout ll_item_1;
        LinearLayout ll_item_2;


        LinearLayout ll_yikoujia_lay_1;
        TextView tv_good_title_1;
        TextView tv_yikou_price_value_1;
        TextView tv_baoyou_1;
        TextView tv_time_1;
        TextView tv_count_1;
        TextView tv_curret_price_1;
        ImageView iv_rate_1;

        LinearLayout ll_yikoujia_lay_2;
        TextView tv_good_title_2;
        TextView tv_yikou_price_value_2;
        TextView tv_baoyou_2;
        TextView tv_time_2;
        TextView tv_count_2;
        TextView tv_curret_price_2;
        ImageView iv_rate_2;


    }
}
