package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.view.MyHeader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/2.
 */
public class CommenWebActivity extends BaseActivity {

    @Bind(R.id.web_content)
    public WebView myWebView;
    @Bind(R.id.my_header)
    public MyHeader myHeader;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenweb);
        ButterKnife.bind(this);
        myWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.setFocusable(false);
        myWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }
}
