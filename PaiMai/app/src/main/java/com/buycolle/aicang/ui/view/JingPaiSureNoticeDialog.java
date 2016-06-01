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
public class JingPaiSureNoticeDialog extends Dialog {

    private Context mContext;

    public CallBack callBack;

    private TextView tv_title;
    private TextView tv_cancle;
    private TextView tv_sure;
    private TextView tv_content;

    private int typeValue = 1;
    private String value = "";


    public JingPaiSureNoticeDialog(final Context context, String current, int type) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_jingpai_sure);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_content = (TextView) findViewById(R.id.tv_content);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        value = current;
        setCanceledOnTouchOutside(false);
        this.typeValue = type;
        if (type == 1) {
            tv_title.setText("出价提示");
            tv_content.setText("确定以" + current + "元的价格\n进行竞拍？");
        } else {
            tv_title.setText("一口价提示");
            tv_content.setText("确定以" + current + "元的一口价\n进行竞拍？");
        }

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
                    callBack.ok(value,typeValue);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    public JingPaiSureNoticeDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok(String content,int type);

        void cancle();
    }
}
