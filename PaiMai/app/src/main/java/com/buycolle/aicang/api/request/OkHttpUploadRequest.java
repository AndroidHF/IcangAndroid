package com.buycolle.aicang.api.request;

import android.util.Pair;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 */
public class OkHttpUploadRequest extends OkHttpPostRequest
{
    private Pair<String, File>[] files;


    protected OkHttpUploadRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, Pair<String, File>[] files)
    {
        super(url, tag, params, headers, null, null, null,null);
        this.files = files;
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

    @Override
    public RequestBody buildRequestBody()
    {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        url = appendParams(url, params);

        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                Pair<String, File> filePair = files[i];
                String fileKeyName = filePair.first;
                File file = filePair.second;
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=" + fileKeyName + "; filename="+ fileName ),
                        fileBody);
            }
        }

        return builder.build();
    }


    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/json;charset=UTF-8";

        }
        return contentTypeFor;
    }

}
