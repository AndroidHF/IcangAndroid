package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;


/**
 * Created by joe on 16/1/4.
 */
public class SelectPicDialog extends Dialog implements View.OnClickListener {

    public interface OnPicChooserListener {
        void select(int position);
    }

    private Context context;
    private Activity _activity;

    private OnPicChooserListener listner;

    private RelativeLayout rl_camera, rl_gallary;
    private TextView tv_cancle;

    public SelectPicDialog(Context context) {
        super(context);
        init(context);
    }

    public SelectPicDialog(Context context, int theme) {
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

        View contentView = View.inflate(context, R.layout.dialog_select_imgs, null);
        rl_camera = (RelativeLayout) contentView.findViewById(R.id.rl_camera);
        rl_gallary = (RelativeLayout) contentView.findViewById(R.id.rl_gallary);
        tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(this);
        rl_gallary.setOnClickListener(this);
        rl_camera.setOnClickListener(this);
        this.setContentView(contentView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_camera:
                if (listner != null) {
                    listner.select(1);
                }
                dismiss();
                break;
            case R.id.rl_gallary:
                if (listner != null) {
                    listner.select(2);
                }
                dismiss();
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
        }
    }

    public SelectPicDialog setListner(OnPicChooserListener listner) {
        this.listner = listner;
        return this;
    }

}