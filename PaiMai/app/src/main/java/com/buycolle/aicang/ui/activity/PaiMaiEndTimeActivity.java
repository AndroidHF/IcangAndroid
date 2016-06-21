package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.view.MyHeader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/6/21.
 */
public class PaiMaiEndTimeActivity extends BaseActivity {
    @Bind(R.id.my_header)
    MyHeader my_header;//头部标题

    //半天相关
    @Bind(R.id.rl_1)
    RelativeLayout rl_1;
    @Bind(R.id.tv_1)
    TextView tv_1;
    @Bind(R.id.iv_1)
    ImageView iv_1;

    //1天相关
    @Bind(R.id.rl_2)
    RelativeLayout rl_2;
    @Bind(R.id.tv_2)
    TextView tv_2;
    @Bind(R.id.iv_2)
    ImageView iv_2;

    //2天相关
    @Bind(R.id.rl_3)
    RelativeLayout rl_3;
    @Bind(R.id.tv_3)
    TextView tv_3;
    @Bind(R.id.iv_3)
    ImageView iv_3;

    //3天相关
    @Bind(R.id.rl_4)
    RelativeLayout rl_4;
    @Bind(R.id.tv_4)
    TextView tv_4;
    @Bind(R.id.iv_4)
    ImageView iv_4;

    //4天相关
    @Bind(R.id.rl_5)
    RelativeLayout rl_5;
    @Bind(R.id.tv_5)
    TextView tv_5;
    @Bind(R.id.iv_5)
    ImageView iv_5;

    //5天相关
    @Bind(R.id.rl_6)
    RelativeLayout rl_6;
    @Bind(R.id.tv_6)
    TextView tv_6;
    @Bind(R.id.iv_6)
    ImageView iv_6;

    //6天相关
    @Bind(R.id.rl_7)
    RelativeLayout rl_7;
    @Bind(R.id.tv_7)
    TextView tv_7;
    @Bind(R.id.iv_7)
    ImageView iv_7;

    //一周相关
    @Bind(R.id.rl_8)
    RelativeLayout rl_8;
    @Bind(R.id.tv_8)
    TextView tv_8;
    @Bind(R.id.iv_8)
    ImageView iv_8;

    //10天相关
    @Bind(R.id.rl_9)
    RelativeLayout rl_9;
    @Bind(R.id.tv_9)
    TextView tv_9;
    @Bind(R.id.iv_9)
    ImageView iv_9;

    //两周相关
    @Bind(R.id.rl_10)
    RelativeLayout rl_10;
    @Bind(R.id.tv_10)
    TextView tv_10;
    @Bind(R.id.iv_10)
    ImageView iv_10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paimai_end_time);
        ButterKnife.bind(this);

        my_header.init("拍卖时间", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });

        //设置监听，传递数据
        //半天监听
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_1.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","1");
                intent.putExtra("time",tv_1.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //1天监听
        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_2.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","2");
                intent.putExtra("time",tv_2.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //2天监听
        rl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_3.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","3");
                intent.putExtra("time",tv_3.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //3天监听
        rl_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_4.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","4");
                intent.putExtra("time",tv_4.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //4天监听
        rl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_5.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","5");
                intent.putExtra("time",tv_5.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //5天监听
        rl_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_6.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","6");
                intent.putExtra("time",tv_6.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //6天监听
        rl_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_7.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","7");
                intent.putExtra("time",tv_7.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //一周监听
        rl_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_8.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","8");
                intent.putExtra("time",tv_8.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //10天监听
        rl_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_9.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","9");
                intent.putExtra("time",tv_9.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //两周监听
        rl_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_10.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra("value","10");
                intent.putExtra("time",tv_10.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
