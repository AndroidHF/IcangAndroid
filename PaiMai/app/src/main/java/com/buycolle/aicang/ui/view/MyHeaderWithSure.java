package com.buycolle.aicang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;

/**
 * Created by joe on 16/3/2.
 */
public class MyHeaderWithSure extends RelativeLayout {

    private Context context;
    private FrameLayout fl_back;
    private TextView tv_common_topbar_title, tv_right_sure;
    private ImageView icon;
//    private TextView left;
//    private TextView right;

    public MyHeaderWithSure(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        init();
    }

    private void init() {
        fl_back = (FrameLayout) findViewById(R.id.fl_back);
        tv_common_topbar_title = (TextView) findViewById(R.id.tv_common_topbar_title);
        tv_right_sure = (TextView) findViewById(R.id.tv_right_sure);
        icon = (ImageView) findViewById(R.id.iv_menu_icon);
//        left = (TextView) findViewById(R.id.left);
//        right = (TextView) findViewById(R.id.right);
        fl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.leftActio();
                }
            }
        });
        tv_right_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.sureActio();
                }
            }
        });
    }

    public TextView getSureTv() {
        return tv_right_sure;
    }


    public void init(String title, Action action) {
        fl_back.setVisibility(VISIBLE);
        this.mAction = action;
        tv_common_topbar_title.setText(title);
//        left.setVisibility(VISIBLE);
//        right.setVisibility(VISIBLE);
    }

    public void init(String title, String sureStr, Action action) {
        fl_back.setVisibility(VISIBLE);
        tv_right_sure.setVisibility(VISIBLE);
//        left.setVisibility(VISIBLE);
//        right.setVisibility(VISIBLE);
        this.mAction = action;
        tv_common_topbar_title.setText(title);
        tv_right_sure.setText(sureStr);
    }

    public void init(String title) {
        fl_back.setVisibility(GONE);
        tv_common_topbar_title.setText(title);
    }

    public void init(String title, int titilIcon, Action action) {
        fl_back.setVisibility(VISIBLE);
        this.mAction = action;
        icon.setVisibility(VISIBLE);
//        left.setVisibility(VISIBLE);
//        right.setVisibility(VISIBLE);
        icon.setImageResource(titilIcon);
        tv_common_topbar_title.setText(title);
    }


    public MyHeaderWithSure(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        init();
    }

    public MyHeaderWithSure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        init();
    }

    private Action mAction;

    public void setAction(Action action) {
        this.mAction = action;
    }

    public interface Action {
        void leftActio();

        void sureActio();
    }

}
