package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.faq.MyFAQActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.MyBuyActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.myfoucus.MyFocusActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mysale.MySaleActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.myshow.MyShowActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.setting.SettingActivity;
import com.buycolle.aicang.util.UIHelper;

/**
 * Created by joe on 16/3/3.
 */
public class UserCenterMenuAdapter extends BaseAdapter {

    private Activity activity;

    private boolean showMyBuyIcon = false;
    private boolean showMySaleIcon = false;
    private boolean showMyFocusIcon = false;

    public UserCenterMenuAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setStatus(int my_buy, int my_seller, int my_show) {
        showMyBuyIcon = my_buy == 1 ? true : false;
        showMySaleIcon = my_seller == 1 ? true : false;
        showMyFocusIcon = my_show == 1 ? true : false;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_usercenter_menu, null);
        RelativeLayout menu1 = (RelativeLayout) contentView.findViewById(R.id.rl_my_buy);
        RelativeLayout menu2 = (RelativeLayout) contentView.findViewById(R.id.rl_my_sale);
        RelativeLayout menu3 = (RelativeLayout) contentView.findViewById(R.id.rl_my_show);
        RelativeLayout menu4 = (RelativeLayout) contentView.findViewById(R.id.rl_my_foucus);
        RelativeLayout menu5 = (RelativeLayout) contentView.findViewById(R.id.rl_my_fqa);
        RelativeLayout menu6 = (RelativeLayout) contentView.findViewById(R.id.rl_my_setting);


        ImageView iv_mybuy_notice_icon = (ImageView) contentView.findViewById(R.id.iv_mybuy_notice_icon);
        ImageView iv_sale_notice_icon = (ImageView) contentView.findViewById(R.id.iv_sale_notice_icon);
        ImageView iv_show_notice_icon = (ImageView) contentView.findViewById(R.id.iv_show_notice_icon);


        iv_mybuy_notice_icon.setVisibility(showMyBuyIcon ? View.VISIBLE : View.GONE);
        iv_sale_notice_icon.setVisibility(showMySaleIcon ? View.VISIBLE : View.GONE);
        iv_show_notice_icon.setVisibility(showMyFocusIcon ? View.VISIBLE : View.GONE);


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, MyBuyActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, MySaleActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, MyShowActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, MyFocusActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, MyFAQActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UIHelper.jump(activiy, Demo?.class);
                if (MainApplication.getInstance().isLogin()) {
                    UIHelper.jump(activity, SettingActivity.class);
                } else {
                    gotoLogin();
                }
            }
        });
        return contentView;
    }

    private void gotoLogin() {
        UIHelper.jump(activity, LoginActivity.class);
    }
}
