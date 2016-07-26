package com.buycolle.aicang.ui.activity.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joe on 16/3/6.
 */
public class YunFeiActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_saler)
    ImageView ivSaler;
    @Bind(R.id.rl_saler)
    RelativeLayout rlSaler;
    @Bind(R.id.iv_buyer)
    ImageView ivBuyer;
    @Bind(R.id.rl_buyer)
    RelativeLayout rlBuyer;

    private String type = "包邮";

    @OnClick(R.id.rl_saler)
    public void selectSaler() {
        ivSaler.setVisibility(View.VISIBLE);
        ivBuyer.setVisibility(View.GONE);
        type = "包邮";
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.rl_buyer)
    public void selectBuyer() {
        ivSaler.setVisibility(View.GONE);
        ivBuyer.setVisibility(View.VISIBLE);
        type = "到付";
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yunfei);
        ButterKnife.bind(this);
        myHeader.init("运费", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
    }
}
