package com.buycolle.aicang.util;

import android.util.Log;
import android.util.Xml;

import com.buycolle.aicang.bean.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by hufeng on 2016/7/26.
 */
public class UpdataInfoParser {
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception{
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();
        while(type != XmlPullParser.END_DOCUMENT ){
            switch (type) {
                case XmlPullParser.START_TAG:
                    if("version".equals(parser.getName())){
                        info.setVersion(parser.nextText());
                        Log.i("version", info.getVersion());
                    }else if ("url".equals(parser.getName())){
                        info.setUrl(parser.nextText());
                        Log.i("url",info.getUrl());
                    }else if("context".equals(parser.getName())){
                        info.setContext(parser.nextText());
                        Log.i("context",info.getContext());
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
}
