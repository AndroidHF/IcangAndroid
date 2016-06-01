package com.buycolle.aicang.ui.activity;

import android.os.Bundle;

import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joe on 16/3/4.
 */
public class PaiMaiDealActivity extends CommenWebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHeader.init("拍卖协议", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        loadData();
    }

    private void loadData() {
        mApplication.apiClient.writer_getpaimaixybyapp(new JSONObject(), new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        StringBuilder sb2 = new StringBuilder("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\"name=\"viewport\"/><meta content=\"telephone=no\"name=\"format-detection\"/><meta content=\"email=no\"name=\"format-detection\"/><style type=\"text/css\">a,button,input{-webkit-tap-highlight-color:rgba(0,0,0,0);-webkit-tap-highlight-color:transparent}img{max-width:100%}*{word-wrap:break-word}</style></head><body></body></html>");
                        sb2.insert(sb2.indexOf("</body>"), resultObj.getString("context"));
                        myWebView.loadDataWithBaseURL("", sb2.toString(), "text/html", "utf-8", null);
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        myWebView.removeAllViews();
        myWebView.destroy();
        super.onDestroy();
    }
}
