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
public class MyHeader extends RelativeLayout {

    private Context context;
    private FrameLayout fl_back;
    private TextView tv_common_topbar_title;
    private ImageView icon;

    public MyHeader(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        init();
    }

    private void init() {
        fl_back = (FrameLayout) findViewById(R.id.fl_back);
        tv_common_topbar_title = (TextView) findViewById(R.id.tv_common_topbar_title);
        icon = (ImageView) findViewById(R.id.iv_menu_icon);
        fl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.leftActio();
                }
            }
        });
    }


    public void init(String title, Action action) {
        fl_back.setVisibility(VISIBLE);
        this.mAction = action;
        tv_common_topbar_title.setText(title);
    }

    public void init(String title) {
        fl_back.setVisibility(GONE);
        tv_common_topbar_title.setText(title);
    }

    public void init(String title, int titilIcon, Action action) {
        fl_back.setVisibility(VISIBLE);
        this.mAction = action;
        icon.setVisibility(VISIBLE);
        icon.setImageResource(titilIcon);
        tv_common_topbar_title.setText(title);
    }


    public MyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        init();
    }

    public MyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

}
