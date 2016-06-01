package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.buycolle.aicang.R;


/**
 * Created by joe on 16/1/4.
 */
public class SexSelectDialog extends Dialog implements View.OnClickListener {



    private int index = 0;//0都没选择 1 男人  2 女人


    public interface OnPicChooserListener {
        void share(int position);
    }

    private Context context;
    private Activity _activity;

    private OnPicChooserListener listner;

    private TextView tv_cancle;
    private TextView tv_sure;
    private TextView tv_man;
    private TextView tv_women;


    public SexSelectDialog(Context context) {
        super(context);
        init(context);
    }

    public SexSelectDialog(Context context, int theme) {
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

        View contentView = View.inflate(context, R.layout.dialog_sex, null);
        tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);
        tv_sure = (TextView) contentView.findViewById(R.id.tv_sure);
        tv_man = (TextView) contentView.findViewById(R.id.tv_man);
        tv_women = (TextView) contentView.findViewById(R.id.tv_women);
        tv_cancle.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        tv_man.setOnClickListener(this);
        tv_women.setOnClickListener(this);
        this.setContentView(contentView);

    }

    public void  showDialog(int index){
        this.index = index;
        if(index==0){
            tv_man.setBackgroundResource(R.color.white);
            tv_women.setBackgroundResource(R.color.white);
        }
        if(index==1){
            tv_man.setBackgroundResource(R.color.orange);
            tv_women.setBackgroundResource(R.color.white);
        }
        if(index==2){
            tv_man.setBackgroundResource(R.color.white);
            tv_women.setBackgroundResource(R.color.orange);
        }
        show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_sure:
                if (listner != null) {
                    if(index==1){
                        listner.share(1);
                    }
                    if(index==2){
                        listner.share(2);
                    }
                }
                dismiss();
                break;
            case R.id.tv_man:
                index=1;
                tv_man.setBackgroundResource(R.color.orange);
                tv_women.setBackgroundResource(R.color.white);
                break;
            case R.id.tv_women:
                index=2;
                tv_man.setBackgroundResource(R.color.white);
                tv_women.setBackgroundResource(R.color.orange);
                break;
        }
    }

    public SexSelectDialog setListner(OnPicChooserListener listner) {
        this.listner = listner;
        return this;
    }

}