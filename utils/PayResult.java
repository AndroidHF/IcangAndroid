package com.example.hufeng.com.example.hufeng.utils;

import android.text.TextUtils;

/**
 * Created by hufeng on 2016/4/20.
 */
public class PayResult {
    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(String rawResult){
        if (TextUtils.isEmpty(rawResult)){
            return;
        }

        String[] resultParams = rawResult.split(";");
        for (String resultParam:resultParams){
            if (resultParam.startsWith("resultStatus")){
                resultStatus = gatValue(resultParam,"resultStatus");
            }

            if (resultParam.startsWith("result")){
                result = gatValue(resultParam,"result");
            }

            if (resultParam.startsWith("memo")){
                memo = gatValue(resultParam,"memo");

            }
        }
    }

     private String gatValue(String resultParam, String resultStatus) {
        return null;
    }

    @Override
    public String toString() {
        return "resultStatus={"+ resultStatus+"}; result={"+ result + "}; memo={"+ memo + "}";
    }

}
