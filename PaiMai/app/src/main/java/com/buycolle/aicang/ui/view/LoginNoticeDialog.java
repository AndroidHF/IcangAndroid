package com.buycolle.aicang.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.buycolle.aicang.R;


/**
 * Created by joe on 16/1/5.
 */
public class LoginNoticeDialog extends Dialog {

    private Context mContext;

    public CallBack callBack;

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_cancle;
    private TextView tv_sure;


    public LoginNoticeDialog(final Context context, String title, String content) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.login_dialog_notice);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);

        tv_title.setText(title);
        tv_content.setText(content);


        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    callBack.cancle();
                }
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    callBack.ok();
                }
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public LoginNoticeDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok();

        void cancle();
    }
}
