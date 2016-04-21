package com.example.hufeng.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hufeng.aicangproject.R;

/**
 * Created by hufeng on 2016/4/5.
 */
public class MyHeader extends RelativeLayout {
    private Context context;
    private FrameLayout flBack;
    private TextView toBarTitle;
    private ImageView ivIcon;
    private Action mAction;
    public MyHeader(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_myheader,this);
        //寻找控件
        init();
    }

    public MyHeader(Context context,AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_myheader,this);
        init();
    }

    public MyHeader(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_myheader,this);
        init();
    }
    private void init() {
        flBack = (FrameLayout) findViewById(R.id.fl_back);
        toBarTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        ivIcon = (ImageView) findViewById(R.id.iv_menu_icon);
        flBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAction != null){
                    mAction.leftActio();
                }
            }
        });

    }

    public void init(String title,Action action){
        flBack.setVisibility(VISIBLE);
        this.mAction = action;
        toBarTitle.setText(title);
    }

    public void init(String title){
        flBack.setVisibility(GONE);
        toBarTitle.setText(title);
    }

    public void init(String title,int titleIcon,Action action){
        flBack.setVisibility(VISIBLE);
        this.mAction = action;
        ivIcon.setVisibility(VISIBLE);
        ivIcon.setImageResource(titleIcon);
        toBarTitle.setText(title);
    }

    public MyHeader setAction(Action action){
        this.mAction = action;
        return this;
    }

    public interface Action{
        void leftActio();
    }
}
