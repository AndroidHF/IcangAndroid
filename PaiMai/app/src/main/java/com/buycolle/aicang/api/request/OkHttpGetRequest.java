package com.buycolle.aicang.api.request;

import android.text.TextUtils;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 */
public class OkHttpGetRequest extends OkHttpRequest
{
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    JSONObject jsonObject;

    protected OkHttpGetRequest(String url, String tag, Map<String, String> params, Map<String, String> headers,JSONObject jsonObject)
    {
        super(url, tag, params, headers);
        this.jsonObject = jsonObject;
    }


    @Override
    protected Request buildRequest()
    {
        if (TextUtils.isEmpty(url))
        {
            throw new IllegalArgumentException("url can not be empty!");
        }
        url = appendParams(url, params);
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder,headers);
        builder.url(url).tag(tag);
        return builder.build();
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        RequestBody requestBody = null;
        if(jsonObject!=null){
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString());
        }
        return requestBody;
    }


    private String appendParams(String url, Map<String, String> params)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty())
        {
            for (String key : params.keySet())
            {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
