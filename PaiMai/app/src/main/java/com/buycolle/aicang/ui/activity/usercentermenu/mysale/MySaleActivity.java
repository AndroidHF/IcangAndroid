package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

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
public class MySaleActivity extends BaseActivity implements IWeiboHandler.Response {


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
    @Bind(R.id.tv_liupai)
    TextView tvLiupai;
    @Bind(R.id.tv_shenhe_status)
    TextView tvShenheStatus;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    @Bind(R.id.iv_paimaiing_notice_icon)
    ImageView point_myselling;
    @Bind(R.id.iv_paimai_finish_notice_icon)
    ImageView point_maysell_finish;
    private ArrayList<TextView> tvArrayList;

    private int my_seller_ing;
    private int my_seller_selt;

    int cost;

    private BaseFragment paiMainIngFrag, paiMainFinishFrag, liuPaiFrag,shenheStatusFrag;
    private List<BaseFragment> fragList;
    private MainPagerAdapter pagerAdapter;

    private boolean isShowTotal = false;


    @OnClick(R.id.tv_paimai_ing)
    public void paiMainIng() {
        initStatus(0);
    }

    @OnClick(R.id.tv_paimai_finish)
    public void paiMainFinish() {
        initStatus(1);
    }

    @OnClick(R.id.tv_liupai)
    public void liuPai() {
        initStatus(2);
    }

    @OnClick(R.id.tv_shenhe_status)
    public void shenHeStatus() {
        initStatus(3);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    Tencent mTencent;

    private void shareToSina() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToSina(this, mWeiboShareAPI, bitmap, Constans.SHARE_URL, "我在会玩总共卖出"+cost+"元");

    }
    private void shareToWechat() {

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToWeChat(this, thumb, Constans.SHARE_URL,"我在会玩总共卖出"+cost+"元", "下载会玩，卖周边发家致富");
    }

    private void shareToCicle() {
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ShareUtil.shareToCicle(this, thumb, Constans.SHARE_URL, "我在会玩总共卖出"+cost+"元", "下载会玩，卖周边发家致富");
    }

    private void shareToQQ() {
        ShareUtil.shareToQQ(this, mTencent, Constans.SHARE_URL,"我在会玩总共卖出"+cost+"元", "下载会玩，卖周边发家致富", FileUtil.APP_DOWNLOAD_LOGO_PATH + FileUtil.APP_DOWNLOAD_LOGO_NAME, new BaseUiListener());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysale);
        ButterKnife.bind(this);
        tvArrayList = new ArrayList<>();
        myHeader.init("我的出品", R.drawable.usercenter_menu_2, new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        fragList = new ArrayList<>();
        tvArrayList.add(tvPaimaiIng);
        tvArrayList.add(tvPaimaiFinish);
        tvArrayList.add(tvLiupai);
        tvArrayList.add(tvShenheStatus);
        paiMainIngFrag = new MySalePaiMaiIngFrag();
        paiMainFinishFrag = new MySalePaiMaiOkFrag();
        liuPaiFrag = new MySaleLiuPaiFrag();
        shenheStatusFrag = new MySaleShenHeFrag();
        my_seller_ing = _Bundle.getInt("my_seller_ing");
        my_seller_selt = _Bundle.getInt("my_seller_selt");
        fragList.add(paiMainIngFrag);
        fragList.add(paiMainFinishFrag);
        fragList.add(liuPaiFrag);
        fragList.add(shenheStatusFrag);
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
        mApplication.setTouImages(LoginConfig.getUserInfo(mContext).getUser_avatar(), profileImage);
        tvName.setText(LoginConfig.getUserInfo(mContext).getUser_nick());

        if(_Bundle!=null){
            if(_Bundle.getBoolean("isPush")){
                //推送界面的跳转
                if (_Bundle.getInt("type") == 9 || _Bundle.getInt("type") == 10){//发货、卖家东西卖出、买家付款
                    Log.i("type-----",_Bundle.getInt("type")+"");
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
                        cost = infosObj.getInt("business_cost");
                        tvTotalCost.setText("总计回血 " +infosObj.getString("business_cost") + "元");
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
                    point_myselling.setVisibility(View.GONE);
                    my_seller_ing = 0;
                    if (my_seller_selt > 0){
                        point_maysell_finish.setVisibility(View.VISIBLE);
                    }else {
                        point_maysell_finish.setVisibility(View.GONE);
                    }
                    UpdateRedTipNotice("my_seller_ing");
                }else if (i == 1){
                    point_maysell_finish.setVisibility(View.GONE);
                    my_seller_selt = 0;
                    if (my_seller_ing > 0){
                        point_myselling.setVisibility(View.VISIBLE);
                    }else {
                        point_myselling.setVisibility(View.GONE);
                    }
                    UpdateRedTipNotice("my_seller_selt");
                }else {
                    if (my_seller_selt > 0){
                        point_maysell_finish.setVisibility(View.VISIBLE);
                    }else {
                        point_maysell_finish.setVisibility(View.GONE);
                    }

                    if (my_seller_ing > 0){
                        point_myselling.setVisibility(View.VISIBLE);
                    }else {
                        point_myselling.setVisibility(View.GONE);
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
                        EventBus.getDefault().post(new UpdateTanNoticeEvent(1));
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
