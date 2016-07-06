package com.buycolle.aicang.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.buycolle.aicang.Constans;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;

/**
 * Created by joe on 16/4/24.
 */
public class ShareUtil {


    /**
     * 分享到微信
     *
     * @param activity
     * @param thumb     缩略图
     * @param url       分享链接
     * @param title     标题
     * @param descTitle 描述
     */
    public static void shareToWeChat(Activity activity, Bitmap thumb, String url, String title, String descTitle) {
        String appId = Constans.APP_ID;
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        iwxapi.registerApp(appId);
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = descTitle;
        wxMediaMessage.thumbData = Bitmap2Bytes(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    /**
     * 分享到朋友圈
     *
     * @param activity
     * @param thumb     缩略图
     * @param url       分享链接
     * @param title     标题
     * @param descTitle 描述
     */
    public static void shareToCicle(Activity activity, Bitmap thumb, String url, String title, String descTitle) {
        String appId = Constans.APP_ID;
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        iwxapi.registerApp(appId);
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = descTitle;
        wxMediaMessage.thumbData = Bitmap2Bytes(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }


    /**
     * 分享到QQ
     *
     * @param activity
     * @param mTencent
     * @param localPath      本地路径
     * @param baseUiListener 回调
     */
    public static void shareToQQ(Activity activity, Tencent mTencent, String link, String title, String subTitle, String localPath, IUiListener baseUiListener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, subTitle);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, link);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localPath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"荟玩");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(activity, params, baseUiListener);
    }

    /***
     * 分享到微博
     * @param activity
     * @param mWeiboShareAPI
     * @param thumb
     * @param link
     * @param title
     */
    public static void shareToSina(Activity activity, IWeiboShareAPI mWeiboShareAPI, Bitmap thumb, String link, String title) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        TextObject textObject = new TextObject();
        textObject.text = title;
        weiboMessage.textObject = textObject;

        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        imageObject.setImageObject(thumb);
        weiboMessage.imageObject = imageObject;

        weiboMessage.mediaObject = getWebpageObj(thumb, link);

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(activity, request);
    }

    private static WebpageObject getWebpageObj(Bitmap bitmap, String link) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "详情查看";
        mediaObject.description = "最棒的二次元商品拍卖平台";
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = link;
        mediaObject.defaultText = "最棒的二次元商品拍卖平台";
        return mediaObject;
    }

    private static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bm != null){
            bm.compress(Bitmap.CompressFormat.PNG, 50, baos);
        }
        return baos.toByteArray();
    }
}
