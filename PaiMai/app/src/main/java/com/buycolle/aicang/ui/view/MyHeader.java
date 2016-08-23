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
    private ImageView iv_delete;
    private ImageView iv_store;
    private ImageView iv_share;
    private FrameLayout paipai_detai;

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
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_store = (ImageView) findViewById(R.id.iv_store);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        paipai_detai = (FrameLayout) findViewById(R.id.paipai_detai);
//        fl_back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mAction != null) {
//                    mAction.leftActio();
//                }
//            }
//        });
    }


    public void init(String title, Action action) {
        fl_back.setVisibility(View.VISIBLE);
        this.mAction = action;
        tv_common_topbar_title.setText(title);
        fl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.leftActio();
                }
            }
        });
    }

    public void initPost(String title){
        fl_back.setVisibility(View.GONE);
        iv_delete.setVisibility(View.VISIBLE);
        tv_common_topbar_title.setText(title);
    }

    public void initRePost(String title){
        fl_back.setVisibility(View.VISIBLE);
        iv_delete.setVisibility(View.VISIBLE);
        tv_common_topbar_title.setText(title);
    }

    public void  initShow(String title){
        fl_back.setVisibility(VISIBLE);
        tv_common_topbar_title.setText(title);
    }

    public void initPaiPinDetai(String title,Action action){
        fl_back.setVisibility(View.VISIBLE);
        paipai_detai.setVisibility(View.VISIBLE);
        this.mAction = action;
        tv_common_topbar_title.setText(title);
        fl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.leftActio();
                }
            }
        });
    }

    public void init(String title) {
        fl_back.setVisibility(View.GONE);
        tv_common_topbar_title.setText(title);
    }

    public void init(String title, int titilIcon, Action action) {
        fl_back.setVisibility(View.VISIBLE);
        this.mAction = action;
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(titilIcon);
        tv_common_topbar_title.setText(title);
        fl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction != null) {
                    mAction.leftActio();
                }
            }
        });
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
