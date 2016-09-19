package com.buycolle.aicang.ui.activity.usercentermenu.mybuy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.UpdateTanNoticeEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MyHeader;
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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/4.
 */
public class MyBuyActivity extends BaseActivity implements IWeiboHandler.Response {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_total_cost)
    TextView tvTotalCost;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.ll_expand_lay)
    LinearLayout llExpandLay;
    @Bind(R.id.iv_exland_icon)
    ImageView ivExlandIcon;
    @Bind(R.id.tv_paimai_ing)
    TextView tvPaimaiIng;
    @Bind(R.id.tv_paimai_finish)
    TextView tvPaimaiFinish;
    @Bind(R.id.tv_paimai_not_get)
    TextView tvPaimaiNotGet;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    @Bind(R.id.iv_paimaiing_notice_icon)
    ImageView point_paimaiing;
    @Bind(R.id.iv_paimai_finish_notice_icon)
    ImageView point_paimai_finish;
    @Bind(R.id.iv_paimai_noget_notice_icon)
    ImageView point_paimai_noget;

    private ArrayList<TextView> tvArrayList;
    private ArrayList<ImageView> imageViewArrayList;

    private BaseFragment paiMainIngFrag, paiMainFinishFrag, paiNoFrag;
    private List<BaseFragment> fragList;
    private MainPagerAdapter pagerAdapter;

    private int my_buy_ing;
    private int my_buy_end;

    private boolean isShowTotal = false;

    private int cost;

    @OnClick(R.id.tv_paimai_ing)
    public void paiMainIng() {
        initStatus(0);

    }

    @OnClick(R.id.tv_paimai_finish)
    public void paiMainFinish() {
        initStatus(1);

    }

    @OnClick(R.id.tv_paimai_not_get)
    public void paiMainNo() {
        initStatus(2);
    }

    @OnClick(R.id.iv_share)
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

    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    Tencent mTencent;

    private void shareToSina() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToSina(this, mWeiboShareAPI, bitmap, Constans.SHARE_URL, "我在荟玩总共剁了" + cost + "元");

    }

    private void shareToWechat() {

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToWeChat(this, thumb, Constans.SHARE_URL, "我在荟玩总共剁了" + cost + "元", "下载荟玩，一言不合就剁手");
    }

    private void shareToCicle() {
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToCicle(this, thumb, Constans.SHARE_URL, "我在荟玩总共剁了" + cost + "元", "下载荟玩，一言不合就剁手");
    }

    private void shareToQQ() {
        ShareUtil.shareToQQ(this, mTencent, Constans.SHARE_URL, "我在荟玩总共剁了" + cost + "元", "下载荟玩，一言不合就剁手", FileUtil.APP_DOWNLOAD_LOGO_PATH + FileUtil.APP_DOWNLOAD_LOGO_NAME, new BaseUiListener());
    }


    @OnClick(R.id.iv_exland_icon)
    public void expand() {
        if (isShowTotal) {
            llExpandLay.setVisibility(View.GONE);
            isShowTotal = false;
        } else {
            llExpandLay.setVisibility(View.VISIBLE);
            isShowTotal = true;
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybuy);
        ButterKnife.bind(this);
        tvArrayList = new ArrayList<>();
        imageViewArrayList = new ArrayList<>();
        myHeader.init("我的竞拍", R.drawable.usercenter_menu_1, new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        fragList = new ArrayList<>();
        tvArrayList.add(tvPaimaiIng);
        tvArrayList.add(tvPaimaiFinish);
        tvArrayList.add(tvPaimaiNotGet);
        imageViewArrayList.add(point_paimaiing);
        imageViewArrayList.add(point_paimai_finish);
        imageViewArrayList.add(point_paimai_noget);
        paiMainIngFrag = new PaiMaiIngFragment();
        paiMainFinishFrag = new PaiMaiFinishFragment();
        paiNoFrag = new PaiMaiNoGetFragment();
        fragList.add(paiMainIngFrag);
        fragList.add(paiMainFinishFrag);
        fragList.add(paiNoFrag);

        my_buy_ing = _Bundle.getInt("my_buy_ing");
        Log.i("my_buy_ing",my_buy_ing+"");
        my_buy_end = _Bundle.getInt("my_buy_end");
        Log.i("my_buy_ing",my_buy_end+"");


        mTencent = Tencent.createInstance(Constans.APP_TX_KEY, this.getApplicationContext());

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constans.APP_KEY);
        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragList);
        vpMainContainer.setAdapter(pagerAdapter);
        vpMainContainer.setOffscreenPageLimit(fragList.size() - 1);
        vpMainContainer.setCurrentItem(0);
        initStatus(0);
        //mApplication.setImages(LoginConfig.getUserInfo(mContext).getUser_avatar(), profileImage);
        //change by :胡峰，头像的处理
        mApplication.setTouImages(LoginConfig.getUserInfo(mContext).getUser_avatar(), profileImage);
        tvName.setText(LoginConfig.getUserInfo(mContext).getUser_nick());



        if (_Bundle != null) {
            if (_Bundle.getBoolean("isPush")) {
                Log.i("type----",_Bundle.getInt("type")+"");
//                vpMainContainer.setCurrentItem(0);
//                initStatus(0);
                //推送点击跳转界面的处理
//                if (_Bundle.getInt("type") == 1){//出价被超，跳转到我买到的，正在拍卖的列表
//                    vpMainContainer.setCurrentItem(0);
//                    initStatus(0);
//                }else
                if (_Bundle.getInt("type") == 2 || _Bundle.getInt("type") == 3 ||_Bundle.getInt("type") == 4|| _Bundle.getInt("type") == 8){//中拍提醒、付款提醒、卖家已经发货，付款还剩3小时提醒，跳转到我买到的已落拍的列表界面
                    vpMainContainer.setCurrentItem(1);
                    initStatus(1);
                }
            }
        }
        loadCostData();
        //setTanNotice();


    }

    /**
     * 获取用户消费
     */
    private void loadCostData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appuser_getusercostinfosbyapp(jsonObject, new ApiCallback() {
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
                        JSONObject infosObj = resultObj.getJSONObject("infos");
                        cost = infosObj.getInt("purchase_cost");
                        tvTotalCost.setText("总计剁手 " + infosObj.getString("purchase_cost") + "元");
                        //tvTotalCost.setText("总计消费 " + 50000 + "元");
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
            }
        });
    }


    private void initStatus(int index) {
        for (int i = 0; i < tvArrayList.size(); i++) {
            if (index == i) {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_orange_black);
                if (i == 0){//表示拍卖中的被选中
                        point_paimaiing.setVisibility(View.GONE);
                        my_buy_ing = 0;
                        if (my_buy_end > 0){
                            point_paimai_finish.setVisibility(View.VISIBLE);
                        }else {
                            point_paimai_finish.setVisibility(View.GONE);
                        }
                        UpdateRedTipNotice("my_buy_ing");
                }else if (i == 1){
                    point_paimai_finish.setVisibility(View.GONE);
                    my_buy_end = 0;
                    if (my_buy_ing > 0){
                        point_paimaiing.setVisibility(View.VISIBLE);
                    }else {
                        point_paimaiing.setVisibility(View.GONE);
                    }
                    UpdateRedTipNotice("my_buy_end");
                }else {
                    if (my_buy_end > 0){
                        point_paimai_finish.setVisibility(View.VISIBLE);
                    }else {
                        point_paimai_finish.setVisibility(View.GONE);
                    }

                    if (my_buy_ing > 0){
                        point_paimaiing.setVisibility(View.VISIBLE);
                    }else {
                        point_paimaiing.setVisibility(View.GONE);
                    }
                }
            } else {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }
        vpMainContainer.setCurrentItem(index);
    }

    private void setTanNotice() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("my_seller", 1);
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_updatetipbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        EventBus.getDefault().post(new UpdateTanNoticeEvent(0));
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void UpdateRedTipNotice(String clean_tip){
        if(!LoginConfig.isLogin(mContext)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clean_tip",clean_tip);
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_deleteredtipbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        EventBus.getDefault().post(new UpdateTanNoticeEvent(0));
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });

    }
}
