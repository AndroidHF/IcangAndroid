package com.buycolle.aicang.api;

import com.squareup.okhttp.Request;

/**
 * Created by joe on 16/1/15.
 */
public interface ApiCallback {
    void onApiStart();
    void onApiSuccess(String response);
    void onApiFailure(Request request, Exception e);
}
