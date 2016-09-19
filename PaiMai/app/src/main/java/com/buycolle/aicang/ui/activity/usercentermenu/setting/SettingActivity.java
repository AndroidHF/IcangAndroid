package com.buycolle.aicang.ui.activity.usercentermenu.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.TextVersionActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeDialog;
import com.buycolle.aicang.ui.view.ShareDialog;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.ShareUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * Created by joe on 16/3/4.
 */
public class SettingActivity extends BaseActivity implements IWeiboHandler.Response {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @Bind(R.id.rl_user_deal)
    RelativeLayout rlUserDeal;
    @Bind(R.id.rl_push_setting)
    RelativeLayout rlPushSetting;
    @Bind(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @Bind(R.id.rl_share_app)
    RelativeLayout rlShareApp;
    @Bind(R.id.rl_feed_back)
    RelativeLayout rlFeedBack;
    @Bind(R.id.btn_logout)
    Button btnLogout;
    @Bind(R.id.rl_text_version)
    RelativeLayout rlTextVersion;//检测新版本


    @OnClick(R.id.rl_about_us)
    public void aboutUs() {
        UIHelper.jump(mActivity, AboutUsActivity.class);
    }

    @OnClick(R.id.rl_user_deal)
    public void userDeal() {
        UIHelper.jump(mActivity, UserDealActivity.class);
    }

    @OnClick(R.id.rl_push_setting)
    public void pushSetting() {
        UIHelper.jump(mActivity, PushSettingActivity.class);
    }

    @OnClick(R.id.rl_share_app)
    public void share() {
        new ShareDialog(mActivity, R.style.bottom_dialog).setListner(new ShareDialog.OnPicChooserListener() {
            @Override
            public void share(int position) {

                switch (position) {
                    case 1:
                        shareToWechat();
                        break;
                    case 2:
                        shareToCicle();
                        break;
                    case 3:
                        shareToQQ();
                        break;
                    case 4:
                        shareToSina();
                        break;
                }


            }
        }).show();
    }

    private void shareToSina() {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constans.APP_KEY);
        mWeiboShareAPI.registerApp();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToSina(this, mWeiboShareAPI, bitmap, Constans.SHARE_URL, Constans.shareTitle_Type_1);
    }

    Tencent mTencent;
    private IWXAPI iwxapi;

    private void shareToWechat() {

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToWeChat(this, thumb, Constans.SHARE_URL, Constans.shareTitle_Type_1, Constans.shareSubTitle_Type_1);
    }

    private void shareToCicle() {
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToCicle(this, thumb, Constans.SHARE_URL, Constans.shareTitle_Type_1, Constans.shareSubTitle_Type_1);
    }

    private void shareToQQ() {
        ShareUtil.shareToQQ(this, mTencent, Constans.SHARE_URL, Constans.shareTitle_Type_1, Constans.shareSubTitle_Type_1, FileUtil.APP_DOWNLOAD_LOGO_PATH + FileUtil.APP_DOWNLOAD_LOGO_NAME, new BaseUiListener());
    }

    /**
     * 微博的回调
     *
     * @param baseResp
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    @OnClick(R.id.rl_feed_back)
    public void feedBack() {
        UIHelper.jump(mActivity, FeedBackActivity.class);
    }

    @OnClick(R.id.rl_clear_cache)
    public void clearCache() {
        new NoticeDialog(mContext, "清除缓存", "确定清除所有缓存？").setCallBack(new NoticeDialog.CallBack() {
            @Override
            public void ok() {
                new UploadImageAsyncTask().execute();
                FileUtil.clearShareCache();
            }

            @Override
            public void cancle() {

            }
        }).show();
    }

    private class UploadImageAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            Glide.get(mActivity).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            UIHelper.t(mContext, "清除缓存成功");
        }
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        new NoticeDialog(mContext, "退出提示", "您确定退出当前账号吗？").setCallBack(new NoticeDialog.CallBack() {
            @Override
            public void ok() {
                logOut();
            }

            @Override
            public void cancle() {

            }
        }).show();
    }

    private void logOut() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.login_logoutplat(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        LoginConfig.clear(mContext);
                        EventBus.getDefault().post(new LogOutEvent());
                        UIHelper.jump(mActivity, LoginActivity.class);
                        mApplication.pushNotificationHelper.cancellAll();
                        finish();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                LoginConfig.clear(mContext);
                EventBus.getDefault().post(new LogOutEvent());
                finish();
            }
        });
    }

    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_new);
        ButterKnife.bind(this);
        myHeader.init("设置", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        mTencent = Tencent.createInstance(Constans.APP_TX_KEY, this.getApplicationContext());

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constans.APP_KEY);
        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        /**
         * 检测新版本的按钮监听
         */
        rlTextVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity, TextVersionActivity.class);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
