package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.buycolle.aicang.R;
import com.buycolle.aicang.util.PackageUtil;
import com.buycolle.aicang.util.UIHelper;


/**
 * Created by joe on 16/1/4.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    public interface OnPicChooserListener {
        void share(int position);
    }

    private Context context;
    private Activity _activity;

    private OnPicChooserListener listner;

    private LinearLayout ll_wechat, ll_circle, ll_qq, ll_sina;

    public ShareDialog(Context context) {
        super(context);
        init(context);
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this._activity = (Activity) context;
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        View contentView = View.inflate(context, R.layout.dialog_share, null);
        ll_wechat = (LinearLayout) contentView.findViewById(R.id.ll_wechat);
        ll_circle = (LinearLayout) contentView.findViewById(R.id.ll_circle);
        ll_qq = (LinearLayout) contentView.findViewById(R.id.ll_qq);
        ll_sina = (LinearLayout) contentView.findViewById(R.id.ll_sina);
        ll_sina.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_circle.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        this.setContentView(contentView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat:
                if (listner != null) {
                    if (PackageUtil.isWeixinAvilible(context)) {
                        listner.share(1);
                    } else {
                        UIHelper.t(context, "您的设备还未安装微信");
                    }
                }
                dismiss();
                break;
            case R.id.ll_circle:
                if (listner != null) {
                    if (PackageUtil.isWeixinAvilible(context)) {
                        listner.share(2);
                    } else {
                        UIHelper.t(context, "您的设备还未安装微信");
                    }
                }
                dismiss();
                break;
            case R.id.ll_qq:
                if (listner != null) {
                    if (PackageUtil.isQQClientAvailable(context)) {
                        listner.share(3);
                    } else {
                        UIHelper.t(context, "您的设备还未安装QQ");
                    }
                }
                dismiss();
                break;
            case R.id.ll_sina:
                if (listner != null) {
                    if (PackageUtil.isWeiboClientAvailable(context)) {
                        listner.share(4);
                    } else {
                        UIHelper.t(context, "您的设备还未安装新浪微博");
                    }
                }
                dismiss();
                break;
        }
    }

    public ShareDialog setListner(OnPicChooserListener listner) {
        this.listner = listner;
        return this;
    }

}