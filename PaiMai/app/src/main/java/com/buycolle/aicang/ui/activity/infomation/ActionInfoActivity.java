package com.buycolle.aicang.ui.activity.infomation;

import android.os.Bundle;
import android.webkit.WebView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/10/10.
 */
public class ActionInfoActivity extends BaseActivity {
    @Bind(R.id.notice_info)
    MyHeader myHeader;//顶部标题
    @Bind(R.id.web_notice_info)
    WebView webNoticeInfo;//显示webview内容的控件

    private String context = "";//webview的内容
    private String type = "";//公告的标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_info);
        ButterKnife.bind(this);
        context  = _Bundle.getString("context");
        type = _Bundle.getString("type");
        myHeader.init(type, new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        webNoticeInfo.loadDataWithBaseURL("",context,"text/html","utf-8",null);

    }
}
