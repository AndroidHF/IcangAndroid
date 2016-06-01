package com.buycolle.aicang.ui.activity.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.event.EditUserInfoEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/4.
 */
public class ZhifubaoInfoActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_card_info)
    TextView tvCardInfo;
    @Bind(R.id.btn_save)
    Button btnSave;

    //更新个人资料
    public void onEventMainThread(EditUserInfoEvent event) {
        if (event.getStatus() == 3) {//支付宝更新
            String zhifubao = LoginConfig.getUserInfoZhifuBao(mContext);
            tvCardInfo.setText(zhifubao);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhifubaoinfo);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (_Bundle != null) {
            tvCardInfo.setText(_Bundle.getString("zhifubao"));
        }
        myHeader.init("支付宝收款账号", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mActivity, EditZhifubaoInfoActivity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
