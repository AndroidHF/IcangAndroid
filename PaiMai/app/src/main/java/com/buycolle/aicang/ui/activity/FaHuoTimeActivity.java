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
 * 商品类型
 * Created by joe on 16/3/13.
 */
public class FaHuoTimeActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.rl_1)
    RelativeLayout rl1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.rl_2)
    RelativeLayout rl2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.rl_3)
    RelativeLayout rl3;
    @Bind(R.id.iv_4)
    ImageView iv4;
    @Bind(R.id.rl_4)
    RelativeLayout rl4;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.tv_4)
    TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fahuo_time);
        ButterKnife.bind(this);
        myHeader.init("发货时间", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value","1");
                intent.putExtra("name",tv1.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value","2");
                intent.putExtra("name",tv2.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value","3");
                intent.putExtra("name",tv3.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value","4");
                intent.putExtra("name",tv4.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
