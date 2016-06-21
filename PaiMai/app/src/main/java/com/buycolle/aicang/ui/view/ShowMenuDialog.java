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

/**
 * Created by joe on 16/3/7.
 */
public class ShowMenuDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;

    private LinearLayout ll_show_menu_all;
    private LinearLayout ll_show_menu_1;
    private LinearLayout ll_show_menu_2;
    private LinearLayout ll_show_menu_3;
    private LinearLayout ll_show_menu_4;
    private LinearLayout ll_show_menu_5;
    private LinearLayout ll_show_menu_6;
    private LinearLayout ll_show_menu_7;
    private LinearLayout ll_show_menu_8;
    private LinearLayout ll_show_menu_9;
    private LinearLayout ll_show_menu;


    public ShowMenuDialog(Context context) {
        super(context, R.style.show_menu_dialog);
        this.mContext = context;
        this.mActivity = (Activity) context;
        init(context);
    }

    private void init(Context context) {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.LEFT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View view = View.inflate(context, R.layout.dialog_show_menu, null);
        ll_show_menu_all = (LinearLayout) view.findViewById(R.id.ll_show_menu_all);
        ll_show_menu_1 = (LinearLayout) view.findViewById(R.id.ll_show_menu_1);
        ll_show_menu_2 = (LinearLayout) view.findViewById(R.id.ll_show_menu_2);
        ll_show_menu_3 = (LinearLayout) view.findViewById(R.id.ll_show_menu_3);
        ll_show_menu_4 = (LinearLayout) view.findViewById(R.id.ll_show_menu_4);
        ll_show_menu_5 = (LinearLayout) view.findViewById(R.id.ll_show_menu_5);
        ll_show_menu_6 = (LinearLayout) view.findViewById(R.id.ll_show_menu_6);
        ll_show_menu_7 = (LinearLayout) view.findViewById(R.id.ll_show_menu_7);
        ll_show_menu_8 = (LinearLayout) view.findViewById(R.id.ll_show_menu_8);
        ll_show_menu_9 = (LinearLayout) view.findViewById(R.id.ll_show_menu_9);
        ll_show_menu = (LinearLayout) view.findViewById(R.id.ll_show_menu);
        this.setContentView(view);

        ll_show_menu.setOnClickListener(this);
        ll_show_menu_all.setOnClickListener(this);
        ll_show_menu_1.setOnClickListener(this);
        ll_show_menu_2.setOnClickListener(this);
        ll_show_menu_3.setOnClickListener(this);
        ll_show_menu_4.setOnClickListener(this);
        ll_show_menu_5.setOnClickListener(this);
        ll_show_menu_6.setOnClickListener(this);
        ll_show_menu_7.setOnClickListener(this);
        ll_show_menu_8.setOnClickListener(this);
        ll_show_menu_9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_show_menu_all:
                if (callBack != null) {
                    dismiss();
                    callBack.action(0);
                }
                break;
            case R.id.ll_show_menu_1:
                if (callBack != null) {
                    dismiss();
                    callBack.action(1);
                }
                break;
            case R.id.ll_show_menu_2:
                if (callBack != null) {
                    dismiss();
                    callBack.action(2);
                }
                break;
            case R.id.ll_show_menu_3:
                if (callBack != null) {
                    dismiss();
                    callBack.action(3);
                }
                break;
            case R.id.ll_show_menu_4:
                if (callBack != null) {
                    dismiss();
                    callBack.action(4);
                }
                break;
            case R.id.ll_show_menu_5:
                if (callBack != null) {
                    dismiss();
                    callBack.action(5);
                }
                break;
            case R.id.ll_show_menu_6:
                if (callBack != null) {
                    dismiss();
                    callBack.action(6);
                }
                break;
            case R.id.ll_show_menu_7:
                if (callBack != null) {
                    dismiss();
                    callBack.action(7);
                }
                break;
            case R.id.ll_show_menu_8:
                if (callBack != null) {
                    dismiss();
                    callBack.action(8);
                }
                break;
            case R.id.ll_show_menu_9:
                if (callBack != null) {
                    dismiss();
                    callBack.action(9);
                }
                break;
            case R.id.ll_show_menu:
                dismiss();
                break;
        }
    }

    private CallBack callBack;

    public ShowMenuDialog setCallBack(CallBack call) {
        this.callBack = call;
        return this;
    }

    public interface CallBack {
        void action(int position);
    }
}
