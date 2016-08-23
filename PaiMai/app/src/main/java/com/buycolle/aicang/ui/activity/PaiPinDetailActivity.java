package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.ImageViewerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PaiPinDetailBean;
import com.buycolle.aicang.event.ChuJiaEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.event.UpdateTanNoticeEvent;
import com.buycolle.aicang.ui.activity.comment.MainComentActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.JingjiaAgainFinishActivity;
import com.buycolle.aicang.ui.view.HackyViewPager;
import com.buycolle.aicang.ui.view.JingPaiDialog;
import com.buycolle.aicang.ui.view.JingPaiSureNoticeDialog;
import com.buycolle.aicang.ui.view.MyExpandTextView;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.ShareDialog;
import com.buycolle.aicang.ui.view.XScrollView.XScrollView;
import com.buycolle.aicang.ui.view.YiKouJiaDialog;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.ShareUtil;
import com.buycolle.aicang.util.SmileUtils;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by joe on 16/3/9.
 */
public class PaiPinDetailActivity extends BaseActivity implements IWeiboHandler.Response {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    HackyViewPager viewPage;
    ImageView ivRange;
    TextView ivPageCount;
    TextView tvPaipinName;
    TextView tvPaipinDesc;
    //TextView tvPaipinTime;
    //TextView tvPaipinPaiCount;
    TextView tvPaipinValueNow;
    TextView tvPaipinValueYikoujia;
    LinearLayout llXianjia;
//    ImageView ivStore;
//    ImageView ivShare;
    TextView tvShangpinValue;
    MyExpandTextView tvPaipinDescDetail;
    TextView tvPaipinZhuangtai;
    TextView tvKuaidiValue;
    TextView tvKuaidiFahuoAddress;
    TextView tvKuaidiFahuoTime;
    ImageView ivQuestion;
    TextView tvQuestion;
    ImageView ivAws;
    TextView tvAsw;
    TextView tvGotoMoreAsk;
    CircleImageView profileImage;
    TextView tvKuaidiUsername;
    TextView tvKuaidiUserComment;
    TextView tvKuaidiUserCommentGood;
    TextView tvKuaidiUserCommentBad;
    TextView tvKuaidiPaimaixieyi;
    TextView tvKuaidiChangjianwenti;
    TextView tvKuaidiJubao;
    @Bind(R.id.ll_maimaimai)
    LinearLayout llMaimaimai;
    LinearLayout llJingpaiUser;
    LinearLayout llUserCenter;
    LinearLayout ll_user_comment;
    RelativeLayout rl_top_images;
    LinearLayout ll_jubao;
    RelativeLayout rl_yikoujia;
    //LinearLayout ll_clock;
    @Bind(R.id.tv_maimaimai)
    TextView tvMaimaimai;
    @Bind(R.id.mScrollView)
    XScrollView mScrollView;
    TextView tv_jiage_change;

    TextView tvDay;//天
    TextView tvHour;//时
    TextView tvMintue;//分
    TextView tvSec;//秒

    ImageView ivHammer;//锤子
    TextView tvCount;//竞拍人数
    @Bind(R.id.iv_store)
    ImageView ivStore;
    @Bind(R.id.iv_share)
    ImageView ivShare;


    private boolean isEvent = false;//是否是拍卖会


    private void initView() {
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
                loadData(false);
            }

            @Override
            public void onLoadMore() {

            }
        });
        mScrollView.setRefreshTime(getTime());
        View mainContent = LayoutInflater.from(this).inflate(R.layout.view_xscroll_content, null);
        viewPage = (HackyViewPager) mainContent.findViewById(R.id.view_page);
        ivRange = (ImageView) mainContent.findViewById(R.id.iv_range);
        ivPageCount = (TextView) mainContent.findViewById(R.id.iv_page_count);
        tvPaipinName = (TextView) mainContent.findViewById(R.id.tv_paipin_name);
        tvPaipinDesc = (TextView) mainContent.findViewById(R.id.tv_paipin_desc);
        //tvPaipinTime = (TextView) mainContent.findViewById(R.id.tv_paipin_time);
        //tvPaipinPaiCount = (TextView) mainContent.findViewById(R.id.tv_paipin_pai_count);
        tvPaipinValueNow = (TextView) mainContent.findViewById(R.id.tv_paipin_value_now);
        tvPaipinValueYikoujia = (TextView) mainContent.findViewById(R.id.tv_paipin_value_yikoujia);
        llXianjia = (LinearLayout) mainContent.findViewById(R.id.ll_xianjia);
//        ivStore = (ImageView) mainContent.findViewById(R.id.iv_store);
//        ivShare = (ImageView) mainContent.findViewById(R.id.iv_share);
        tvShangpinValue = (TextView) mainContent.findViewById(R.id.tv_shangpin_value);
        tvPaipinDescDetail = (MyExpandTextView) mainContent.findViewById(R.id.tv_paipin_desc_detail);
        tvPaipinZhuangtai = (TextView) mainContent.findViewById(R.id.tv_paipin_zhuangtai);
        tvKuaidiValue = (TextView) mainContent.findViewById(R.id.tv_kuaidi_value);
        tvKuaidiFahuoAddress = (TextView) mainContent.findViewById(R.id.tv_kuaidi_fahuo_address);
        tvKuaidiFahuoTime = (TextView) mainContent.findViewById(R.id.tv_kuaidi_fahuo_time);
        ivQuestion = (ImageView) mainContent.findViewById(R.id.iv_question);
        tvQuestion = (TextView) mainContent.findViewById(R.id.tv_question);
        ivAws = (ImageView) mainContent.findViewById(R.id.iv_aws);
        tvAsw = (TextView) mainContent.findViewById(R.id.tv_asw);
        tvGotoMoreAsk = (TextView) mainContent.findViewById(R.id.tv_goto_more_ask);
        profileImage = (CircleImageView) mainContent.findViewById(R.id.profile_image);
        tvKuaidiUsername = (TextView) mainContent.findViewById(R.id.tv_kuaidi_username);
        tvKuaidiUserComment = (TextView) mainContent.findViewById(R.id.tv_kuaidi_user_comment);
        tvKuaidiUserCommentGood = (TextView) mainContent.findViewById(R.id.tv_kuaidi_user_comment_good);
        tvKuaidiUserCommentBad = (TextView) mainContent.findViewById(R.id.tv_kuaidi_user_comment_bad);
        tvKuaidiPaimaixieyi = (TextView) mainContent.findViewById(R.id.tv_kuaidi_paimaixieyi);
        tvKuaidiChangjianwenti = (TextView) mainContent.findViewById(R.id.tv_kuaidi_changjianwenti);
        tvKuaidiJubao = (TextView) mainContent.findViewById(R.id.tv_kuaidi_jubao);
        llJingpaiUser = (LinearLayout) mainContent.findViewById(R.id.ll_jingpai_user);
        llUserCenter = (LinearLayout) mainContent.findViewById(R.id.ll_user_center);
        ll_user_comment = (LinearLayout) mainContent.findViewById(R.id.ll_user_comment);
        rl_top_images = (RelativeLayout) mainContent.findViewById(R.id.rl_top_images);
        ll_jubao = (LinearLayout) mainContent.findViewById(R.id.ll_jubao);
        rl_yikoujia = (RelativeLayout) mainContent.findViewById(R.id.rl_yikoujia);
        //ll_clock = (LinearLayout) mainContent.findViewById(R.id.ll_clock);
        tv_jiage_change = (TextView) mainContent.findViewById(R.id.tv_jiage_change);
        tvYikoujiaTitle = (TextView) mainContent.findViewById(R.id.tv_yikoujia_title);
        ll_ask_quesrtion = (LinearLayout) mainContent.findViewById(R.id.ll_ask_quesrtion);
        llAllCommentLay = (LinearLayout) mainContent.findViewById(R.id.ll_all_comment_lay);
        llGoodCommentLay = (LinearLayout) mainContent.findViewById(R.id.ll_good_comment_lay);
        llBadCommentLay = (LinearLayout) mainContent.findViewById(R.id.ll_bad_comment_lay);
        tvDay = (TextView) mainContent.findViewById(R.id.tv_day);
        tvHour = (TextView) mainContent.findViewById(R.id.tv_hour);
        tvMintue = (TextView) mainContent.findViewById(R.id.tv_mintue);
        tvSec = (TextView) mainContent.findViewById(R.id.tv_sec);
        ivHammer = (ImageView) mainContent.findViewById(R.id.iv_hammer);
        tvCount = (TextView) mainContent.findViewById(R.id.tv_count);

        mScrollView.setView(mainContent);

    }

    TextView tvYikoujiaTitle;
    LinearLayout ll_ask_quesrtion;
    LinearLayout llAllCommentLay;
    LinearLayout llGoodCommentLay;
    LinearLayout llBadCommentLay;


    private PaiPinDetailBean paiPinDetailBean;
    private boolean isFinish = false;

    private int product_id;
    private ACache aCache;

    Tencent mTencent;
    private IWeiboShareAPI mWeiboShareAPI;
    private Bitmap shareBitmap;
    private Bitmap shareBitmap2;
    private String shareImagePath = "";
    private String shareImagePath2 = "";

    //一般拍品
    private void getShareBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GlideUrl glideUrl = new GlideUrl(paiPinDetailBean.getCover_pic(), new LazyHeaders.Builder()
                            .build());
                    shareBitmap = Glide.with(mActivity).load(glideUrl).asBitmap().into(150, 150).get();
                    shareBitmap = Bitmap.createScaledBitmap(shareBitmap, 100, 100, true);
                    shareImagePath = saveLocalImg(shareBitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //拍卖会图片分享到qq
    private void getShareBitmap2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GlideUrl glideUrl = new GlideUrl(paiPinDetailBean.getCover_pic(), new LazyHeaders.Builder()
                            .build());
                    shareBitmap2 = Glide.with(mActivity).load(glideUrl).asBitmap().into(100,50).get();
                    //shareBitmap = Bitmap.createScaledBitmap(shareBitmap, 100, 100, true);
                    shareImagePath2 = saveLocalImg2(shareBitmap2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //为QQ存储分享一般图片
    private String saveLocalImg(Bitmap bitmap) {
        String serverPath = paiPinDetailBean.getCover_pic();
        String name = serverPath.substring(serverPath.lastIndexOf("/") + 1, serverPath.length());
        String filePath = FileUtil.getShareCathe() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveLocalImg2(Bitmap bitmap) {
        String serverPath = paiPinDetailBean.getCover_pic();
        String name = serverPath.substring(serverPath.lastIndexOf("/") + 1, serverPath.length());
        String filePath = FileUtil.getShareCathe() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //登录触发
    public void onEventMainThread(LoginEvent event) {
        mScrollView.autoRefresh();
        loadData(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paipin_detail_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        aCache = ACache.get(this);
//        myHeader.init("拍品详情", new MyHeader.Action() {
//            @Override
//            public void leftActio() {
//                finish();
//            }
//        });
        myHeader.initPaiPinDetai("拍品详情", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        if (_Bundle != null) {
            isFinish = _Bundle.getBoolean("isFinish", false);
            isEvent = _Bundle.getBoolean("isEvent", false);
            product_id = _Bundle.getInt("product_id");
        }

        if (_Bundle2 != null){
            if(_Bundle2.getBoolean("isPush")){
                //关注拍品剩余30分钟的推送界面的跳转
                if (_Bundle2.getInt("type") == 13 ||_Bundle2.getInt("type") == 5){
                    Log.i("type-----", _Bundle.getInt("type") + "");
                    isFinish = _Bundle.getBoolean("isFinish", false);
                    isEvent = _Bundle.getBoolean("isEvent", false);
                    product_id = _Bundle2.getInt("id");
                }
                if(_Bundle2.getInt("type") == 1){
                    Log.i("type-----", _Bundle.getInt("type") + "");
                    isFinish = _Bundle.getBoolean("isFinish", false);
                    isEvent = _Bundle.getBoolean("isEvent", false);
                    product_id = _Bundle2.getInt("id");
                    UpdateRedTipByTuiSong();
                }
            }
        }

        mTencent = Tencent.createInstance(Constans.APP_TX_KEY, this.getApplicationContext());

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constans.APP_KEY);
        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }


        initView();


        initListener();

        tvKuaidiPaimaixieyi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tvKuaidiJubao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tvKuaidiChangjianwenti.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        /***
         * add by :胡峰
         * 文字的平滑处理
         */
        tvKuaidiPaimaixieyi.getPaint().setAntiAlias(true);
        tvKuaidiJubao.getPaint().setAntiAlias(true);
        tvKuaidiChangjianwenti.getPaint().setAntiAlias(true);
        loadData(false);
        //获取举报类型
        loadJuBao();
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    private void initListener() {
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareDialog(mActivity, R.style.bottom_dialog).setListner(new ShareDialog.OnPicChooserListener() {
                    @Override
                    public void share(int position) {
                        switch (position) {
                            /***
                             * change by :胡峰
                             * 分享接口的修改
                             */
                            case 1:
                                if (paiPinDetailBean.getEnt_id() == 1){
                                    ShareUtil.shareToWeChat(mActivity, shareBitmap, Constans.SHARE_URL + "/item.html?" + paiPinDetailBean.getProduct_id()+"z", Constans.shareTitle_Type_2, paiPinDetailBean.getProduct_title());
                                    break;
                                }else {
                                    ShareUtil.shareToWeChat(mActivity, shareBitmap2, Constans.SHARE_URL + "/event.html?" +paiPinDetailBean.getProduct_id()+"z", Constans.shareTitle_Type_2, paiPinDetailBean.getProduct_title());
                                    break;
                                }

                            case 2:
                                if (paiPinDetailBean.getEnt_id() == 1){
                                    ShareUtil.shareToCicle(mActivity, shareBitmap, Constans.SHARE_URL + "/item.html?" + paiPinDetailBean.getProduct_id()+"z", Constans.shareTitle_Type_2,paiPinDetailBean.getProduct_title());
                                    break;
                                }else {
                                    ShareUtil.shareToCicle(mActivity, shareBitmap2, Constans.SHARE_URL + "/event.html?" + paiPinDetailBean.getProduct_id()+"z", Constans.shareTitle_Type_2,paiPinDetailBean.getProduct_title());
                                    break;
                                }
                            case 3:
                                if (paiPinDetailBean.getEnt_id() == 1){
                                    ShareUtil.shareToQQ(mActivity, mTencent, Constans.SHARE_URL + "/item.html?" + product_id+"z", Constans.shareTitle_Type_2, paiPinDetailBean.getProduct_title(), shareImagePath, new BaseUiListener());
                                    break;
                                }else {
                                    ShareUtil.shareToQQ(mActivity, mTencent, Constans.SHARE_URL + "/event.html?" + product_id+"z", Constans.shareTitle_Type_2, paiPinDetailBean.getProduct_title(), shareImagePath2, new BaseUiListener());
                                    break;
                                }
                            case 4:
                                if (paiPinDetailBean.getEnt_id() == 1){
                                    ShareUtil.shareToSina(mActivity, mWeiboShareAPI, shareBitmap, Constans.SHARE_URL + "/item.html?"+ product_id+"z", paiPinDetailBean.getProduct_title());
                                    break;
                                }else {
                                    ShareUtil.shareToSina(mActivity, mWeiboShareAPI, shareBitmap2, Constans.SHARE_URL + "/event.html?" + product_id+"z", paiPinDetailBean.getProduct_title());
                                    break;
                                }
                        }
                    }
                }).show();
            }
        });


        tvKuaidiPaimaixieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 交易流程
                UIHelper.jump(mActivity, PaiMaiJiaoYiLiuChengActivity.class);
            }
        });
        tvKuaidiJubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 举报
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", paiPinDetailBean);
                    UIHelper.jump(mActivity, ReportActivity.class, bundle);
                } else {
                    gotoLogin();
                }
            }
        });
        tvKuaidiChangjianwenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 常见问题
                UIHelper.jump(mActivity, ChangJianQuestionActivity.class);
            }
        });
        llJingpaiUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 竞拍
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", paiPinDetailBean.getProduct_id());
                UIHelper.jump(mActivity, JingPaiJiLuActivity.class, bundle);
            }
        });
        tvShangpinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 搜索分类那边
                Bundle bundle = new Bundle();
                bundle.putBoolean("ishot", false);
                bundle.putBoolean("isDetail", true);
                bundle.putString("key_word", "");
                bundle.putInt("cate_id", paiPinDetailBean.getP_cate_id());
                UIHelper.jump(mActivity, SearchPaiPinActivity.class, bundle);

            }
        });
        tvGotoMoreAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 更多问答
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", paiPinDetailBean);
                    UIHelper.jump(mActivity, AskAndQuestionActivity.class, bundle);
                } else {
                    gotoLogin();
                }
            }
        });
        llUserCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/3/10 用户中心
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", paiPinDetailBean.getSeller_user_id());
                    UIHelper.jump(mActivity, MainUserCenterActivity.class, bundle);
                } else {
                    gotoLogin();
                }
            }
        });


        tvMaimaimai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maimamai();
            }
        });
        llAllCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentIndex", 0);
                    bundle.putInt("userid", paiPinDetailBean.getSeller_user_id());
                    UIHelper.jump(mActivity, MainComentActivity.class, bundle);
                } else {
                    gotoLogin();
                }

            }
        });
        llGoodCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentIndex", 1);
                    bundle.putInt("userid", paiPinDetailBean.getSeller_user_id());
                    UIHelper.jump(mActivity, MainComentActivity.class, bundle);
                } else {
                    gotoLogin();
                }

            }
        });
        llBadCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentIndex", 2);
                    bundle.putInt("userid", paiPinDetailBean.getSeller_user_id());
                    UIHelper.jump(mActivity, MainComentActivity.class, bundle);
                } else {
                    gotoLogin();
                }

            }
        });


    }


    YiKouJiaDialog yiKouJiaDialog;
    JingPaiDialog jingpaiDialog;
    JingPaiSureNoticeDialog jingPaiSureNoticeDialog;

    /**
     * 竞拍
     */
    private void maimamai() {
        if (mApplication.isLogin()) {
            if (paiPinDetailBean.getOpen_but_it() == 0) {//没有开启一口价
                String valuue;
                boolean isHasPai = false;
                if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric())) >= Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()))) {
                    isHasPai = true;//已经有人拍了
                    valuue = StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric());
                } else {
                    isHasPai = false;
                    valuue = StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price());
                }
                jingpaiDialog = new JingPaiDialog(mContext, "竞拍", valuue, isHasPai).setCallBack(new JingPaiDialog.CallBack() {
                    @Override
                    public void ok(String value) {
                        jingPaiSureNoticeDialog = new JingPaiSureNoticeDialog(mContext, value, 1).setCallBack(new JingPaiSureNoticeDialog.CallBack() {
                            @Override
                            public void ok(String content, int type) {
                                productjpfollow_submitbyapp(content, type);
                                jingpaiDialog.dismiss();
                            }

                            @Override
                            public void cancle() {

                            }
                        });
                        jingPaiSureNoticeDialog.show();
                    }

                    @Override
                    public void cancle() {

                    }
                });
                jingpaiDialog.show();
            } else {
                String valuue;
                boolean isHasPai = false;
                if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric())) >= Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()))) {
                    valuue = StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric());
                    isHasPai = true;
                } else {
                    isHasPai = false;
                    valuue = StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price());
                }
                yiKouJiaDialog = new YiKouJiaDialog(mContext, "竞拍", valuue, StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBut_it_price()), isHasPai).setCallBack(new YiKouJiaDialog.CallBack() {
                    @Override
                    public void ok(String value, int type) {
                        jingPaiSureNoticeDialog = new JingPaiSureNoticeDialog(mContext, value, type).setCallBack(new JingPaiSureNoticeDialog.CallBack() {
                            @Override
                            public void ok(String content, int type) {
                                yiKouJiaDialog.dismiss();
                                if (type == 1) {//竞价
                                    productjpfollow_submitbyapp(content, type);
                                } else {//一口价
                                    productjpfollow_submitbyapp(content, type);
                                }
                            }

                            @Override
                            public void cancle() {

                            }
                        });
                        jingPaiSureNoticeDialog.show();
                    }

                    @Override
                    public void cancle() {

                    }
                });
                yiKouJiaDialog.show();
            }
        } else {
            UIHelper.jump(this, LoginActivity.class);
        }
    }

    private void loadData(final boolean isAction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getpreauctdetailidbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isAction) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        paiPinDetailBean = new Gson().fromJson(userInfoObj.toString(), PaiPinDetailBean.class);
                        //获取需要分享的bitmap
                        if (paiPinDetailBean.getEnt_id() == 1){
                            getShareBitmap();
                        }else {
                            getShareBitmap2();
                        }
                        initData();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!isAction) {
                    dismissLoadingDialog();
                }
                mScrollView.stopRefresh();
                mScrollView.stopLoadMore();
                mScrollView.setRefreshTime(getTime());
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                if (!isAction) {
                    dismissLoadingDialog();
                }
                mScrollView.stopRefresh();
                mScrollView.stopLoadMore();
                mScrollView.setRefreshTime(getTime());
            }
        });
    }

    private void setTime() {
        if (paiPinDetailBean.isFinish()) {
            //tvPaipinTime.setText("已结束");
            tvDay.setText("拍");
            tvHour.setText("卖");
            tvMintue.setText("结");
            tvSec.setText("束");
            tvMaimaimai.setVisibility(View.GONE);
        } else {
            tvMaimaimai.setVisibility(View.VISIBLE);
            String[] timess = StringFormatUtil.getTimeFromIntNew(paiPinDetailBean.getTime() / 1000);
            //tvPaipinTime.setText(timess[0] + "天" + timess[1] + "时" + timess[2] + "分" + timess[3] + "秒");
            tvDay.setText(timess[0]);
            tvHour.setText(timess[1]);
            tvMintue.setText(timess[2]);
            tvSec.setText(timess[3]);
        }
    }

    private void initData() {

        if (paiPinDetailBean.getEnt_id() == 1) {// 1表示是普通卖家发布的，需要显示卖家头像信息
            llUserCenter.setVisibility(View.VISIBLE);
            isEvent = false;
        } else {//2表示拍卖会拍品，不需要显示发布者信息
            llUserCenter.setVisibility(View.GONE);
            isEvent = true;
        }

        if (isEvent) {
            rl_top_images.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PhoneUtil.getWindowWidth(mContext) / 2));
        } else {
            rl_top_images.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PhoneUtil.getWindowWidth(mContext)));
        }

        if (paiPinDetailBean.getCol_id() > 0) {
            ivStore.setImageResource(R.drawable.collection_02);
        } else {
            ivStore.setImageResource(R.drawable.collection_01);
        }


        if (paiPinDetailBean.getOpen_but_it() == 1) {//0：否 1：是
            if (paiPinDetailBean.getMax_pric().equals(paiPinDetailBean.getBut_it_price())) {
                isFinish = true;
                paiPinDetailBean.setFinish(true);
                setTime();
            } else {
                paiPinDetailBean.setFinish(false);
                paiPinDetailBean.setTime(paiPinDetailBean.getPm_end_remain_second() * 1000);
                setTime();
                if (!isHandRun) {
                    handler.sendEmptyMessage(1);
                }
            }
        } else {
            if (paiPinDetailBean.getPm_end_remain_second() == 0) {
                paiPinDetailBean.setFinish(true);
                setTime();
            } else {
                paiPinDetailBean.setFinish(false);
                paiPinDetailBean.setTime(paiPinDetailBean.getPm_end_remain_second() * 1000);
                setTime();
                if (!isHandRun) {
                    handler.sendEmptyMessage(1);
                }
            }
        }

        //广告轮播图
        final ArrayList<String> paths = new ArrayList<>();
        paths.add(paiPinDetailBean.getCover_pic());
        if (!TextUtils.isEmpty(paiPinDetailBean.getCycle_pic())) {
            if (paiPinDetailBean.getCycle_pic().indexOf(",") > 0) {
                String[] imagesPths = paiPinDetailBean.getCycle_pic().split(",");
                for (String imagesPth : imagesPths) {
                    paths.add(imagesPth);
                }
            } else {
                paths.add(paiPinDetailBean.getCycle_pic());
            }
        }
        ivPageCount.setText("1/" + paths.size());
        viewPage.setAdapter(new ImageViewerAdapter(this, paths, isEvent));
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ivPageCount.setText((position + 1) + "/" + paths.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         * change by :胡峰
         * 对星级图片的处理
         */
        if (isEvent){
            ViewGroup.LayoutParams layoutParams = ivRange.getLayoutParams();
            layoutParams.width = 79;
            layoutParams.height = 40;
            ivRange.setLayoutParams(layoutParams);
            mApplication.setImages(paiPinDetailBean.getRaretag_icon(), ivRange);
        }else {
            ViewGroup.LayoutParams layoutParams = ivRange.getLayoutParams();
            layoutParams.width = 63;
            layoutParams.height = 37;
            ivRange.setLayoutParams(layoutParams);
            mApplication.setImages(paiPinDetailBean.getRaretag_icon(), ivRange);
        }


        tvPaipinName.setText(paiPinDetailBean.getProduct_title());
        tvPaipinDesc.setText(paiPinDetailBean.getSt_name());
        tvShangpinValue.setText(paiPinDetailBean.getAll_cate_name());
        //tvPaipinPaiCount.setText("" + paiPinDetailBean.getJp_count());
        tvCount.setText("" + paiPinDetailBean.getJp_count());
        if (paiPinDetailBean.getJp_count()>=0 && paiPinDetailBean.getJp_count()<20){
            ivHammer.setImageResource(R.drawable.hammer01);
        }else if (paiPinDetailBean.getJp_count() >= 20 && paiPinDetailBean.getJp_count()<50){
            ivHammer.setImageResource(R.drawable.hammer02);
        }else {
            ivHammer.setImageResource(R.drawable.hammer03);
        }



        if (Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric())) > Double.valueOf(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()))) {
            tvPaipinValueNow.setText(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric()));
        } else {
            tvPaipinValueNow.setText(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()));
        }
        if (!isFinish) {
            ll_jubao.setVisibility(View.VISIBLE);
            if (paiPinDetailBean.getOpen_but_it() == 0) {//不开启一口价
                llMaimaimai.setVisibility(View.VISIBLE);
                if (paiPinDetailBean.getJp_count() > 0) {
                    tv_jiage_change.setText("现价");
                    rl_yikoujia.setVisibility(View.GONE);
                    tvYikoujiaTitle.setText("起拍价");
                    //tvPaipinValueYikoujia.setText("￥" + StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()));
                    tvPaipinValueYikoujia.setText(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBegin_auct_price()));
                } else {
                    tv_jiage_change.setText("起拍价");
                    rl_yikoujia.setVisibility(View.GONE);
                }
            } else {
                if (StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBut_it_price()).equals(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getMax_pric()))) {
                    llMaimaimai.setVisibility(View.GONE);
                } else {
                    llMaimaimai.setVisibility(View.VISIBLE);
                }
                if (paiPinDetailBean.getJp_count() > 0) {
                    tv_jiage_change.setText("现价");
                } else {
                    tv_jiage_change.setText("起拍价");
                }
                tvYikoujiaTitle.setText("一口价");
                tvPaipinValueYikoujia.setText(StringFormatUtil.getDoubleFormatNew(paiPinDetailBean.getBut_it_price()));

            }
        } else {
            ll_jubao.setVisibility(View.GONE);
            rl_yikoujia.setVisibility(View.GONE);
            llMaimaimai.setVisibility(View.GONE);
            tv_jiage_change.setText("成交价");
        }

        /**
         * 自己不能拍
         */
        if (paiPinDetailBean.getSeller_user_id() == LoginConfig.getUserInfo(mContext).getUser_id()) {
            KLog.d("自己----");
            llMaimaimai.setVisibility(View.GONE);
            ll_jubao.setVisibility(View.GONE);
        }

        tvPaipinDescDetail.setText(paiPinDetailBean.getProduct_desc());
        tvPaipinDescDetail.setOnStateChangeListener(new MyExpandTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isExpand) {
                tvPaipinDescDetail.setIsExpand(isExpand);
            }
        });
        tvPaipinZhuangtai.setText(paiPinDetailBean.getSt_name());
        if (paiPinDetailBean.getExpress_out_type() == 1) {
            tvKuaidiValue.setText("到付");
        } else {
            tvKuaidiValue.setText("包邮");
        }
        tvKuaidiFahuoAddress.setText(paiPinDetailBean.getFahou_province() + paiPinDetailBean.getFahou_city());
        if ("1".equals(paiPinDetailBean.getFahuo_time_type())) {
            tvKuaidiFahuoTime.setText("当天发货");
        } else if ("2".equals(paiPinDetailBean.getFahuo_time_type())) {
            tvKuaidiFahuoTime.setText("1-3天");
        } else if ("3".equals(paiPinDetailBean.getFahuo_time_type())) {
            tvKuaidiFahuoTime.setText("1周以内");
        } else if ("4".equals(paiPinDetailBean.getFahuo_time_type())) {
            tvKuaidiFahuoTime.setText("2-3周以内");
        }

        ll_ask_quesrtion.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(paiPinDetailBean.getFaq().get(0).getQuestion())) {
            tvQuestion.setText("现在似乎还没有人有什么疑问");
            tvQuestion.setTextColor(ContextCompat.getColor(mContext, R.color.gray_tv));
        } else {
            tvQuestion.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
            tvQuestion.setText(SmileUtils.getSmiledSubCommentText(mContext, paiPinDetailBean.getFaq().get(0).getQuestion()));
        }
        if (TextUtils.isEmpty(paiPinDetailBean.getFaq().get(0).getAnswer())) {
            tvAsw.setText("卖家还没有出现");
            tvAsw.setTextColor(ContextCompat.getColor(mContext, R.color.gray_tv));
        } else {
            tvAsw.setText(SmileUtils.getSmiledSubCommentText(mContext, paiPinDetailBean.getFaq().get(0).getAnswer()));
            tvAsw.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
        }

        //出品人信息
        //change by :胡峰，头像的处理
        mApplication.setTouImages(paiPinDetailBean.getUser_avatar(),profileImage);
        tvKuaidiUsername.setText(paiPinDetailBean.getUser_name());
        tvKuaidiUserCommentGood.setText(paiPinDetailBean.getGood_comment() + "");
        tvKuaidiUserCommentBad.setText(paiPinDetailBean.getBad_comment() + "");
        tvKuaidiUserComment.setText(paiPinDetailBean.getTotal_comment() + "");

        ivStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    if (likeActionFlag) {
                        likeActionFlag = false;
                        likeUnlike(paiPinDetailBean);
                    } else {
                        if (likeActionShowToUserFlag) {
                            likeActionShowToUserFlag = false;
                            UIHelper.t(mContext, R.string.comment_action_more);
                        }
                    }
                } else {
                    UIHelper.jump(mActivity, LoginActivity.class);
                }

            }
        });


        if (isEvent) {
            ll_jubao.setVisibility(View.GONE);
        }


        if (isFinish) {
            if (paiPinDetailBean.getJp_count() == 0) {
                tv_jiage_change.setText("起拍价");
            } else {
                tv_jiage_change.setText("成交价");
            }
        }

    }

    private boolean likeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean likeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void likeUnlike(final PaiPinDetailBean myShowPassBean) {
        if (mApplication.isLogin()) {

            if (isFinish) {
                UIHelper.t(mContext, "拍卖已结束，不能关注");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                if (myShowPassBean.getCol_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", myShowPassBean.getProduct_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colproduct(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            if (myShowPassBean.getCol_id() > 0) {
                                myShowPassBean.setCol_id(0);
                                UIHelper.t(mContext, "取消关注成功");
                            } else {
                                myShowPassBean.setCol_id(1);
                                UIHelper.t(mContext, "关注成功");
                            }
                            if (paiPinDetailBean.getCol_id() > 0) {
                                ivStore.setImageResource(R.drawable.collection_02);
                            } else {
                                ivStore.setImageResource(R.drawable.collection_01);
                            }
                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    UIHelper.t(mContext, "操作失败");
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }

    boolean isNeedCountTime = false;
    boolean isHandRun = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    long time = paiPinDetailBean.getTime();
                    if (time > 1000) {
                        isNeedCountTime = true;
                        paiPinDetailBean.setTime(time - 1000);
                    } else {
                        isNeedCountTime = false;
                        paiPinDetailBean.setFinish(true);
                        paiPinDetailBean.setTime(0);
//                        loadData(true);
                    }
                    setTime();
                    if (isNeedCountTime) {
                        isHandRun = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        isHandRun = false;
                    }
                    break;
            }
        }
    };


    /**
     * 竞拍
     *
     * @param value
     * @param type  1：竞拍 2：一口价 默认为1
     */
    private void productjpfollow_submitbyapp(String value, final int type) {
        KLog.d("传过来的值----", value + "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("price", value);
            jsonObject.put("type", type);
            jsonObject.put("product_id", product_id);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.productjpfollow_submitbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        loadData(true);
                        //出价事件
                        EventBus.getDefault().post(new ChuJiaEvent(paiPinDetailBean.getProduct_id()));
                        if (type == 1) {//1：竞拍 2：一口价 默认为1
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", 1);
                            bundle.putLong("time", paiPinDetailBean.getTime());
                            bundle.putInt("id", paiPinDetailBean.getProduct_id());
                            UIHelper.jump(mActivity, JingjiaAgainFinishActivity.class, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", 2);
                            bundle.putLong("time", paiPinDetailBean.getTime());
                            bundle.putInt("id", paiPinDetailBean.getProduct_id());
                            UIHelper.jump(mActivity, JingjiaAgainFinishActivity.class, bundle);

                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        if (resultObj.getString("status").equals("666")) {//已经有新的出价 需要重新出价 刷新界面
                            mScrollView.autoRefresh();
                            loadData(false);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 获取拍品举报类型
     */
    private void loadJuBao() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.dirtionary_getproductwftypelistbyapp(jsonObject, new ApiCallback() {
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
                        aCache.put(Constans.TAG_REPORT_TYPE, resultObj);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void UpdateRedTipByTuiSong(){
        if(!LoginConfig.isLogin(mContext)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",1);
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_updateredtipbyapp(jsonObject, new ApiCallback() {
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
