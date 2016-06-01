package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.os.Bundle;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/6.
 */
public class ChaKanWuLiuActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_wuliu_company)
    TextView tvWuliuCompany;
    @Bind(R.id.tv_wuliu_code)
    TextView tvWuliuCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chakanwuliu);
        ButterKnife.bind(this);
        String name = _Bundle.getString("express_name");
        String number = _Bundle.getString("express_bill_num");
        tvWuliuCompany.setText("物流公司        " + name);
        tvWuliuCode.setText("包裹单号        " + number);
        myHeader.init("查看物流信息", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
    }
}
