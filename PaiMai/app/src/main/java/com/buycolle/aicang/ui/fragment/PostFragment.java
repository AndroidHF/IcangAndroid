package com.buycolle.aicang.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.api.AppUrl;
import com.buycolle.aicang.api.callback.ResultCallback;
import com.buycolle.aicang.api.request.OkHttpRequest;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.bean.PostAddressInfo;
import com.buycolle.aicang.bean.PostImageBean;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.event.EditPostEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.event.PublicGoodEvent;
import com.buycolle.aicang.event.TobeSallerEvent;
import com.buycolle.aicang.ui.activity.ChangJianQuestionActivity;
import com.buycolle.aicang.ui.activity.FaHuoTimeActivity;
import com.buycolle.aicang.ui.activity.PaiMaiEndTimeActivity;
import com.buycolle.aicang.ui.activity.PaiMaiFaHuoZhiDaoActivity;
import com.buycolle.aicang.ui.activity.PaiMaiJiaoYiLiuChengActivity;
import com.buycolle.aicang.ui.activity.ToBeSallerActivity;
import com.buycolle.aicang.ui.activity.comment.CommentCropImage2Activity;
import com.buycolle.aicang.ui.activity.comment.CommentCropImageActivity;
import com.buycolle.aicang.ui.activity.post.YunFeiActivity;
import com.buycolle.aicang.ui.activity.shangpintypes.ShangPinStatusActivity;
import com.buycolle.aicang.ui.activity.shangpintypes.ShangPinTypesActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeDialog;
import com.buycolle.aicang.ui.view.NoticeDialogPost;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.ui.view.SelectPicDialog;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.ImageUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/2.
 */
public class PostFragment extends BaseFragment {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.rv_imgs)
    RecyclerView mRecyclerView;
    @Bind(R.id.iv_fisrt)
    ImageView firstimg;
    @Bind(R.id.rl_goods_type)
    RelativeLayout rl_goods_type;

    @Bind(R.id.rl_yunfei_status)
    RelativeLayout rl_yunfei_status;
    @Bind(R.id.tv_yunfei_status_value)
    TextView tv_yunfei_status_value;
    @Bind(R.id.rl_end_time)
    RelativeLayout rl_end_time;
    @Bind(R.id.rl_addres)
    RelativeLayout rl_addres;
    @Bind(R.id.rl_fahuo_time)
    RelativeLayout rl_fahuo_time;
    @Bind(R.id.tv_end_time_value)
    TextView tv_end_time_value;
    @Bind(R.id.tv_addres_value)
    TextView tv_addres_value;
    @Bind(R.id.btn_save)
    Button btn_save;
    @Bind(R.id.tv_tobe_maijia)
    TextView tvTobeMaijia;
    @Bind(R.id.ll_not_maijia_lay)
    LinearLayout llNotMaijiaLay;
    @Bind(R.id.tv_goods_type_value)
    TextView tvGoodsTypeValue;
    @Bind(R.id.tv_goods_status_title)
    TextView tvGoodsStatusTitle;
    @Bind(R.id.tv_goods_status_value)
    TextView tvGoodsStatusValue;
    @Bind(R.id.iv_goods_status_arrow)
    ImageView ivGoodsStatusArrow;
    @Bind(R.id.rl_goods_status)
    RelativeLayout rlGoodsStatus;
    @Bind(R.id.tv_end_time_title)
    TextView tvEndTimeTitle;
    @Bind(R.id.iv_end_time_arrow)
    ImageView ivEndTimeArrow;
    @Bind(R.id.tv_yunfei_status_title)
    TextView tvYunfeiStatusTitle;
    @Bind(R.id.iv_yunfei_status_arrow)
    ImageView ivYunfeiStatusArrow;
    @Bind(R.id.tv_addres_title)
    TextView tvAddresTitle;
    @Bind(R.id.iv_addres_arrow)
    ImageView ivAddresArrow;
    @Bind(R.id.tv_fahuo_time_title)
    TextView tvFahuoTimeTitle;
    @Bind(R.id.tv_fahuo_time_value)
    TextView tvFahuoTimeValue;
    @Bind(R.id.iv_fahuo_time_arrow)
    ImageView ivFahuoTimeArrow;
    @Bind(R.id.cb_wuliu)
    CheckBox cbWuliu;
    @Bind(R.id.rl_wuliu_status)
    RelativeLayout rlWuliuStatus;
    @Bind(R.id.tv_start_price_title)
    TextView tvStartPriceTitle;
    @Bind(R.id.tv_start_price_value)
    EditText tvStartPriceValue;
    @Bind(R.id.rl_start_price)
    RelativeLayout rlStartPrice;
    @Bind(R.id.tv_yikou_price_title)
    TextView tvYikouPriceTitle;
    @Bind(R.id.tv_yikou_price_value)
    EditText tvYikouPriceValue;
    @Bind(R.id.rl_yikou_price)
    RelativeLayout rlYikouPrice;
    @Bind(R.id.cb_yikoujia_status)
    CheckBox cbYikoujiaStatus;
    @Bind(R.id.rl_yikoujia_status)
    RelativeLayout rlYikoujiaStatus;
    @Bind(R.id.ll_maijia)
    LinearLayout llMaijia;
    @Bind(R.id.tv_goods_type_title)
    TextView tvGoodsTypeTitle;
    @Bind(R.id.iv_goods_type_arrow)
    ImageView ivGoodsTypeArrow;
    @Bind(R.id.tv_image_count)
    TextView tv_image_count;
    //主图
    @Bind(R.id.iv_fisrt_status)
    ImageView ivFisrtStatus;
    @Bind(R.id.et_input_good_title)
    EditText etInputGoodTitle;
    @Bind(R.id.et_input_good_desc)
    EditText etInputGoodDesc;
    //add by hufeng:顶部的图片
    @Bind(R.id.iv_top_icon)
    ImageView iv_top_icon;
    //add by hufeng :出品界面增加的三个按钮：交易流程、发货指导、常见问题
    @Bind(R.id.tv_jiaoyi_liucheng)
    TextView tv_jiaoyi_liucheng;//交易流程
    @Bind(R.id.tv_fahuo_zhidao)
    TextView tv_fahuo_zhidao;//发货指导
    @Bind(R.id.tv_changjianwenti)
    TextView tv_changjianwenti;//常见问题
    @Bind(R.id.iv_delete)
    ImageView iv_delete;
    @Bind(R.id.tv_feilv)
    TextView tv_fee_rate;
    private GalleryAdapter mAdapter;
    private ArrayList<PostImageBean> postImageBeans;
    private PostImageBean mainImageBean = new PostImageBean();
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    OptionsPickerView pvOptions;

    private String good_type = "";//商品类型
    private String good_status = "";//商品状态 id
    //private String good_end_time = "";//结束时间
    private String good_yunfei = "";//承担运费方
    private String fahou_city = "";//发货地址 市
    private String fahou_province = "";//发货地址 省
    private String good_fahuo_time = "";//发货时间
    private String paimai_end_time = "";//拍卖结束时间

    private Uri photoUri;
    private static String recentPicPath;//当前拍照的路径

    private UserBean userBean;
    //封面图  大图
    private final int RESULT_PIC_1_GALLARY = 100;
    private final int RESULT_PIC_1_CAMERA = 200;
    private final int REQUEST_CROP_IMAGE = 300;

    private final int RESULT_PIC_2_GALLARY = 400;
    private final int RESULT_PIC_2_CAMERA = 500;

    private final int REQUEST_GOOD_TYPE = 1000;
    private final int REQUEST_GOOD_STATUS = 2000;
    private final int REQUEST_GOOD_YUNFEI_CHENGDAN = 3000;
    private final int REQUEST_GOOD_FAHUO_TIME = 4000;
    private final int REQUEST_GOOD_PAIMAI_END_TIME = 5000;


    //登录触发
    public void onEventMainThread(LoginEvent event) {
        if (mApplication.isLogin()&& mApplication.isSaller()) {
            initSallerView(true);
        } else {
            initSallerView(false);
        }
        initAddressInfo();
        loadTopAds();

    }



    //发布拍品触发 更新发布的 默认地址信息
    public void onEventMainThread(PublicGoodEvent event) {
        initAddressInfo();
        loadTopAds();
    }

    //发布拍品触发 更新发布的 默认地址信息
    public void onEventMainThread(EditPostEvent event) {
        initAddressInfo();
        loadTopAds();
    }


    //成为卖家事件
    public void onEventMainThread(TobeSallerEvent event) {
        KLog.d("极光推送---成为卖家", (llNotMaijiaLay.getVisibility() == View.VISIBLE) + "");
        KLog.d("极光推送---成为卖家", mApplication.isLogin() + "");
        if (llNotMaijiaLay.getVisibility() == View.VISIBLE && mApplication.isLogin()) {
            initSallerView(true);
            LoginConfig.updateUserType(mContext, 2);
        }
    }

    private PostAddressInfo postAddressInfo;

    private void initAddressInfo() {
        if (mApplication.isLogin() && LoginConfig.getUserInfo(mContext).getUser_type() == 2) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.sellerfahuo_getsamefahuo(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    JSONObject resultObj = null;
                    try {
                        resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            JSONObject userInfoObj = resultObj.getJSONObject("infos");
                            if (!TextUtils.isEmpty(userInfoObj.getString("fahou_city"))) {
                                postAddressInfo = new Gson().fromJson(userInfoObj.toString(), PostAddressInfo.class);
                                if (!TextUtils.isEmpty(postAddressInfo.getFahou_city())) {
                                    cbWuliu.setChecked(true);
                                    //运费承担方 1 买家 2 卖家
                                    if (postAddressInfo.getExpress_out_type() == 1) {
                                        good_yunfei = "1";
                                        tv_yunfei_status_value.setText("到付");
                                    } else {
                                        good_yunfei = "2";
                                        tv_yunfei_status_value.setText("包邮");
                                    }
                                    tv_yunfei_status_value.setTextColor(mActivity.getResources().getColor(R.color.black_tv));


                                    fahou_city = postAddressInfo.getFahou_city();
                                    fahou_province = postAddressInfo.getFahou_province();
                                    tv_addres_value.setText(fahou_province + fahou_city);

                                    tv_addres_value.setTextColor(mActivity.getResources().getColor(R.color.black_tv));


                                    //1：当天发货 2：1-3天 3 ：1周内 4:2-3周内
                                    if (postAddressInfo.getFahuo_time_type() == 1) {
                                        good_fahuo_time = "1";
                                        tvFahuoTimeValue.setText("当天发货");
                                    } else if (postAddressInfo.getFahuo_time_type() == 2) {
                                        good_fahuo_time = "2";
                                        tvFahuoTimeValue.setText("1-3天");
                                    } else if (postAddressInfo.getFahuo_time_type() == 3) {
                                        good_fahuo_time = "3";
                                        tvFahuoTimeValue.setText("1周以内");
                                    } else if (postAddressInfo.getFahuo_time_type() == 4) {
                                        good_fahuo_time = "4";
                                        tvFahuoTimeValue.setText("2-3周以内");
                                    }
                                    tvFahuoTimeValue.setTextColor(mActivity.getResources().getColor(R.color.black_tv));
                                } else {
                                    cbWuliu.setChecked(false);
                                }
                            } else {
                                cbWuliu.setChecked(false);

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

                }
            });

        }
    }

    /**
     * add by 胡峰
     * banner图的获取
     * @param savedInstanceState
     *
     *
     */

    private ACache aCache;
    private ArrayList<HomeTopAddBeanNew> homeTopAddBeens;
    private HomeTopAddBeanNew homeTopAddBeanNew;
    private void loadTopAds() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                //Log.i("sessionid_banner_icon",LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appbanner_getsellerlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isAdded()) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            aCache.put(Constans.TAG_TOBE_SALLER_TOP_ADS, resultObj);
                            ArrayList<HomeTopAddBeanNew> homarrays = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                            }.getType());
                            for (Iterator iterator = homarrays.iterator(); iterator.hasNext(); ) {
                                HomeTopAddBeanNew homeTopAddBeanNew = (HomeTopAddBeanNew) iterator.next();
                                //Log.i("banner_icon", homeTopAddBeanNew.getBanner_icon());
                                mApplication.setShowImages(homeTopAddBeanNew.getBanner_icon(), iv_top_icon);
                            }

                            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                            int width = windowManager.getDefaultDisplay().getWidth();
                            Log.i("width", width + "");
                            ViewGroup.LayoutParams layoutParams = iv_top_icon.getLayoutParams();//获取当前的控件的参数
                            layoutParams.height = width / 3;//将高度设置为宽度的三分之一
                            //Log.i("height",layoutParams.height + "");
                            iv_top_icon.setLayoutParams(layoutParams);
                            Log.i("fee_rate--", resultObj.getString("fee_rate"));
                            tv_fee_rate.setText(resultObj.getString("fee_rate"));
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
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    @OnClick(R.id.iv_fisrt)
    public void selectFirstImg() {
        new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
            @Override
            public void select(int position) {
                if (position == 1) {//照相
                    photo(RESULT_PIC_1_CAMERA);
                }
                if (position == 2) {//相册
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(i, RESULT_PIC_1_GALLARY);
                }
            }
        }).show();
    }

    public void photo(int resultCode) {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
            }
            if (file != null) {
                recentPicPath = file.getPath();
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, resultCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        postImageBeans = new ArrayList<>();
        aCache = ACache.get(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_post, container, false);
        ButterKnife.bind(this, view);
        /**
         * add by :胡峰
         * 对顶部图片的宽度做修改
         */
        loadTopAds();
        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_TOBE_SALLER_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                JSONArray adsArray = topaAdsObj.getJSONArray("rows");
                if (adsArray.length() > 0) {
                    aCache.put(Constans.TAG_TOBE_SALLER_TOP_ADS, adsArray);
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                    }.getType());

                    WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    int width = windowManager.getDefaultDisplay().getWidth();
                    ViewGroup.LayoutParams layoutParams = iv_top_icon.getLayoutParams();//获取当前的控件的参数
                    layoutParams.height = width/3;//将高度设置为宽度的三分之一
                    for (Iterator iterator = homeTopAddBeens.iterator(); iterator.hasNext();) {
                        homeTopAddBeanNew = (HomeTopAddBeanNew) iterator.next();
                        Log.i("banner_icon", homeTopAddBeanNew.getBanner_icon());

                    }
                    mApplication.setImages(homeTopAddBeanNew.getBanner_icon(), iv_top_icon);
                    iv_top_icon.setLayoutParams(layoutParams);//使得设置好的参数应用到控件中
                    Log.i("feeeeeeee", topaAdsObj.getString("fee_rate"));
                    tv_fee_rate.setText(topaAdsObj.getString("fee_rate"));
                    Log.i("tv_fee_rate", tv_fee_rate.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            loadTopAds();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //myHeader.init("出品");
        myHeader.initPost("出品");
        mainImageBean.setServerPath("");
        mainImageBean.setLocalPath("");
        postImageBeans.clear();
        PostImageBean postImageBean_1 = new PostImageBean();
        postImageBean_1.setStatus(PostImageBean.Status.INIT);
        postImageBean_1.setEmpty(true);
        postImageBeans.add(postImageBean_1);
        PostImageBean postImageBean_2 = new PostImageBean();
        postImageBean_2.setStatus(PostImageBean.Status.INIT);
        postImageBean_2.setEmpty(true);
        postImageBeans.add(postImageBean_2);
        //refreshByState(2);
        initCity();
        loadTopAds();
        if (mApplication.isLogin()&& mApplication.isSaller()) {
            initSallerView(true);
        } else {
            initSallerView(false);
        }
        initAddressInfo();
    }

    private void initListener() {

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoticeDialog(mContext,"清除确认","确认清除当前编辑的出品\n    信息么？").setCallBack(new NoticeDialog.CallBack() {
                    @Override
                    public void ok() {
                        initSuccessView();
                    }

                    @Override
                    public void cancle() {

                    }
                }).show();
            }
        });

        ivFisrtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMainImages(mainImageBean);
            }
        });

        /**
         * add by :胡峰
         * 下划线和文字平滑处理
         */
        //add by hufeng:三个按钮的下滑线
        tv_jiaoyi_liucheng.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_jiaoyi_liucheng.getPaint().setAntiAlias(true);//抗锯齿处理
        tv_fahuo_zhidao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_fahuo_zhidao.getPaint().setAntiAlias(true);
        tv_changjianwenti.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_changjianwenti.getPaint().setAntiAlias(true);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        //商品类型
        rl_goods_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResultByFragment(mFragment, ShangPinTypesActivity.class, REQUEST_GOOD_TYPE);
            }
        });
        //商品状态
        rlGoodsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResultByFragment(mFragment, ShangPinStatusActivity.class, REQUEST_GOOD_STATUS);
            }
        });
        //change by ：胡峰，拍卖结束时间的修改
        rl_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jumpForResultByFragment(mFragment, PaiMaiEndTimeActivity.class,REQUEST_GOOD_PAIMAI_END_TIME);
            }
        });

        pvOptions = new OptionsPickerView(mActivity);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                fahou_city = options2Items.get(options1).get(option2);
                fahou_province = options1Items.get(options1);
                tv_addres_value.setText(tx);
                tv_addres_value.setTextColor(mActivity.getResources().getColor(R.color.black_tv));
            }
        });

        rl_addres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.setPicker(options1Items, options2Items, true);
                pvOptions.setCyclic(false, false, false);
                pvOptions.setSelectOptions(0, 0, 0);
                pvOptions.show();
            }
        });

        //运费承担方
        rl_yunfei_status.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    UIHelper.jumpForResultByFragment(PostFragment.this, YunFeiActivity.class, REQUEST_GOOD_YUNFEI_CHENGDAN);
                                                }
                                            }

        );

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submint();
            }
        });

        rl_fahuo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResultByFragment(mFragment, FaHuoTimeActivity.class, REQUEST_GOOD_FAHUO_TIME);
            }
        });


        cbYikoujiaStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlYikouPrice.setVisibility(View.VISIBLE);
                } else {
                    rlYikouPrice.setVisibility(View.GONE);
                }
            }
        });

        tvStartPriceValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable edt) {


                String temp = edt.toString();


                if (TextUtils.isEmpty(temp)) {
                    return;
                }

                if (temp.startsWith(".")) {
                    tvStartPriceValue.setText(tvStartPriceValue.getText().toString().replace(".", ""));
                    return;
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) {//没有.
                    return;
                } else {
                    if (temp.length() - posDot - 1 > 2) {
                        edt.delete(posDot + 3, posDot + 4);
                    }
                }

            }
        });
        tvYikouPriceValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable edt) {


                String temp = edt.toString();


                if (TextUtils.isEmpty(temp)) {
                    return;
                }

                if (temp.startsWith(".")) {
                    tvYikouPriceValue.setText(tvYikouPriceValue.getText().toString().replace(".", ""));
                    return;
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) {//没有.
                    return;
                } else {
                    if (temp.length() - posDot - 1 > 2) {
                        edt.delete(posDot + 3, posDot + 4);
                    }
                }
            }
        });

        /**
         * add by hufeng :设置三个按钮的设置监听
         * 交易流程监听
         */
        tv_jiaoyi_liucheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity, PaiMaiJiaoYiLiuChengActivity.class);
            }
        });

        //发货指导监听
        tv_fahuo_zhidao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity, PaiMaiFaHuoZhiDaoActivity.class);
            }
        });

        //常见问题监听
        tv_changjianwenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity, ChangJianQuestionActivity.class);
            }
        });
    }


    private void initSallerView(boolean isSeller) {
        if (mApplication.isLogin() && isSeller) {
                llNotMaijiaLay.setVisibility(View.GONE);
                llMaijia.setVisibility(View.VISIBLE);
                iv_delete.setVisibility(View.VISIBLE);
                loadTopAds();
                initListener();
        } else {
            llNotMaijiaLay.setVisibility(View.VISIBLE);
            llMaijia.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
            tvTobeMaijia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mApplication.isLogin()) {
                        UIHelper.jump(mActivity, ToBeSallerActivity.class);
                    } else {
                        mApplication.gotoLogInByFrag(mFragment);
                    }
                }
            });
        }
    }


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private int currentIndex = 0;

    public class GalleryAdapter extends
            RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private LayoutInflater mInflater;

        public GalleryAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_menu_icon_1;
            ImageView iv_menu_icon_status;
            ImageView iv_close;
            LinearLayout ll_add_item;

            public ViewHolder(View view) {
                super(view);
                iv_menu_icon_1 = (ImageView) view.findViewById(R.id.iv_menu_icon_1);
                iv_menu_icon_status = (ImageView) view.findViewById(R.id.iv_menu_icon_status);
                iv_close = (ImageView) view.findViewById(R.id.iv_close);
                ll_add_item = (LinearLayout) view.findViewById(R.id.ll_add_item);
            }

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }


        @Override
        public int getItemCount() {
            return postImageBeans.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.row_post_img_item_new,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final PostImageBean postImageBean = postImageBeans.get(position);
            if (postImageBeans.size() == 2) {
                boolean hasEmpty = false;
                int emptyCount = 0;
                for (PostImageBean bean : postImageBeans) {
                    if (bean.isEmpty()) {
                        emptyCount++;
                        hasEmpty = true;
                    }
                }
                if (hasEmpty) {
                    if (emptyCount == 1) {
                        if (postImageBean.isEmpty()) {
                            viewHolder.iv_close.setVisibility(View.GONE);
                            viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                            viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                            viewHolder.iv_menu_icon_1.setImageResource(R.color.transparent);
                            viewHolder.iv_menu_icon_1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentIndex = position;
                                    new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                                        @Override
                                        public void select(int position) {
                                            if (position == 1) {//照相
                                                photo(RESULT_PIC_2_CAMERA);
                                            }
                                            if (position == 2) {//相册
                                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                                startActivityForResult(i, RESULT_PIC_2_GALLARY);
                                            }
                                        }
                                    }).show();
                                }
                            });
                        } else {
                            viewHolder.iv_close.setVisibility(View.VISIBLE);
                            viewHolder.iv_menu_icon_1.setOnClickListener(null);
                            viewHolder.ll_add_item.setBackgroundResource(R.drawable.shape_white_black);
                            mApplication.setImages("file://" + postImageBean.getLocalPath(), viewHolder.iv_menu_icon_1);
                            viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                                    postImageBeans.remove(postImageBean);
                                    PostImageBean emptyImageBean = new PostImageBean();
                                    emptyImageBean.setEmpty(true);
                                    if (position == 0) {
                                        postImageBeans.add(0, emptyImageBean);
                                    }
                                    if (position == 1) {
                                        postImageBeans.add(emptyImageBean);
                                    }
                                    notifyDataSetChanged();
                                    tv_image_count.setText(getImageCount() + "/10");

                                }
                            });
                            if (postImageBean.getStatus() == PostImageBean.Status.FAIL) {
                                viewHolder.iv_menu_icon_status.setVisibility(View.VISIBLE);
                                viewHolder.iv_menu_icon_status.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        currentIndex = position;
                                        uploadListmages(postImageBean);
                                    }
                                });
                            } else {
                                viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (emptyCount == 2) {
                        viewHolder.iv_close.setVisibility(View.GONE);
                        viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                        viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                        viewHolder.iv_menu_icon_1.setImageResource(R.color.transparent);
                        viewHolder.iv_menu_icon_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentIndex = position;
                                new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                                    @Override
                                    public void select(int position) {
                                        if (position == 1) {//照相
                                            photo(RESULT_PIC_2_CAMERA);
                                        }
                                        if (position == 2) {//相册
                                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                            startActivityForResult(i, RESULT_PIC_2_GALLARY);
                                        }
                                    }
                                }).show();
                            }
                        });
                    }

                } else {
                    viewHolder.iv_close.setVisibility(View.VISIBLE);
                    viewHolder.iv_menu_icon_1.setOnClickListener(null);
                    viewHolder.ll_add_item.setBackgroundResource(R.drawable.shape_white_black);
                    mApplication.setImages("file://" + postImageBean.getLocalPath(), viewHolder.iv_menu_icon_1);
                    viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                            postImageBeans.remove(postImageBean);
                            PostImageBean emptyImageBean = new PostImageBean();
                            emptyImageBean.setEmpty(true);
                            if (position == 0) {
                                postImageBeans.add(0, emptyImageBean);
                            }
                            if (position == 1) {
                                postImageBeans.add(emptyImageBean);
                            }
                            notifyDataSetChanged();
                            tv_image_count.setText(getImageCount() + "/10");

                        }
                    });
                    if (postImageBean.getStatus() == PostImageBean.Status.FAIL) {
                        viewHolder.iv_menu_icon_status.setVisibility(View.VISIBLE);
                        viewHolder.iv_menu_icon_status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentIndex = position;
                                uploadListmages(postImageBean);
                            }
                        });
                    } else {
                        viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                    }
                }
            } else {
                if (postImageBeans.size() == 9) {
                    if (postImageBean.isEmpty()) {
                        viewHolder.iv_close.setVisibility(View.GONE);
                        viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                        viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                        viewHolder.iv_menu_icon_1.setImageResource(R.color.transparent);
                        viewHolder.iv_menu_icon_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentIndex = position;
                                new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                                    @Override
                                    public void select(int position) {
                                        if (position == 1) {//照相
                                            photo(RESULT_PIC_2_CAMERA);
                                        }
                                        if (position == 2) {//相册
                                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                            startActivityForResult(i, RESULT_PIC_2_GALLARY);
                                        }
                                    }
                                }).show();
                            }
                        });
                    } else {
                        viewHolder.iv_close.setVisibility(View.VISIBLE);
                        viewHolder.iv_menu_icon_1.setOnClickListener(null);
                        viewHolder.ll_add_item.setBackgroundResource(R.drawable.shape_white_black);
                        mApplication.setImages("file://" + postImageBean.getLocalPath(), viewHolder.iv_menu_icon_1);
                        viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                                postImageBeans.remove(postImageBean);
                                boolean isEmptys = false;
                                for (PostImageBean imageBean : postImageBeans) {
                                    if (imageBean.isEmpty()) {
                                        isEmptys = true;
                                    }
                                }
                                if (!isEmptys) {
                                    PostImageBean postImageBean1 = new PostImageBean();
                                    postImageBean1.setEmpty(true);
                                    postImageBeans.add(postImageBean1);
                                }
                                notifyDataSetChanged();
                                tv_image_count.setText(getImageCount() + "/10");

                            }
                        });
                        if (postImageBean.getStatus() == PostImageBean.Status.FAIL) {
                            viewHolder.iv_menu_icon_status.setVisibility(View.VISIBLE);
                            viewHolder.iv_menu_icon_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentIndex = position;
                                    uploadListmages(postImageBean);
                                }
                            });
                        } else {
                            viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if (postImageBean.isEmpty()) {
                        viewHolder.iv_close.setVisibility(View.GONE);
                        viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                        viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                        viewHolder.iv_menu_icon_1.setImageResource(R.color.transparent);
                        viewHolder.iv_menu_icon_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentIndex = position;
                                new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                                    @Override
                                    public void select(int position) {
                                        if (position == 1) {//照相
                                            photo(RESULT_PIC_2_CAMERA);
                                        }
                                        if (position == 2) {//相册
                                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                            startActivityForResult(i, RESULT_PIC_2_GALLARY);
                                        }
                                    }
                                }).show();
                            }
                        });
                    } else {
                        viewHolder.iv_close.setVisibility(View.VISIBLE);
                        viewHolder.iv_menu_icon_1.setOnClickListener(null);
                        viewHolder.ll_add_item.setBackgroundResource(R.drawable.shape_white_black);
                        mApplication.setImages("file://" + postImageBean.getLocalPath(), viewHolder.iv_menu_icon_1);
                        viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewHolder.ll_add_item.setBackgroundResource(R.drawable.post_addpic_small_bg);
                                postImageBeans.remove(postImageBean);
                                notifyDataSetChanged();
                                tv_image_count.setText(getImageCount() + "/10");
                            }
                        });
                        if (postImageBean.getStatus() == PostImageBean.Status.FAIL) {
                            viewHolder.iv_menu_icon_status.setVisibility(View.VISIBLE);
                            viewHolder.iv_menu_icon_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentIndex = position;
                                    uploadListmages(postImageBean);
                                }
                            });
                        } else {
                            viewHolder.iv_menu_icon_status.setVisibility(View.GONE);
                        }

                    }
                }
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //运费承担方 返回
        if (requestCode == REQUEST_GOOD_YUNFEI_CHENGDAN && resultCode == mActivity.RESULT_OK) {
            if ("到付".equals(data.getStringExtra("type"))) {
                good_yunfei = "1";
            } else {
                good_yunfei = "2";
            }
            tv_yunfei_status_value.setText(data.getStringExtra("type"));
            tv_yunfei_status_value.setTextColor(mActivity.getResources().getColor(R.color.black_tv));
        }
        //物品类型返回
        if (requestCode == REQUEST_GOOD_TYPE && resultCode == mActivity.RESULT_OK) {
            int level = data.getIntExtra("level", 0);
            good_type = data.getStringExtra("cate_id");
            if (level == 1) {
                tvGoodsTypeValue.setText(data.getStringExtra("cate_name"));
            } else if (level == 2) {
                tvGoodsTypeValue.setText(data.getStringExtra("p_cate_name") + "/" + data.getStringExtra("cate_name"));
            } else {
                tvGoodsTypeValue.setText(data.getStringExtra("p_cate_name") + "/" + data.getStringExtra("p_1_cate_name") + "/" + data.getStringExtra("cate_name"));
            }
            tvGoodsTypeValue.setTextColor(mActivity.getResources().getColor(R.color.black_tv));

        }
        //物品状态返回
        if (requestCode == REQUEST_GOOD_STATUS && resultCode == mActivity.RESULT_OK) {
            good_status = data.getStringExtra("dir_id");
            tvGoodsStatusValue.setText(data.getStringExtra("item_name"));
            tvGoodsStatusValue.setTextColor(mActivity.getResources().getColor(R.color.black_tv));

        }
        //物品发货时间返回
        if (requestCode == REQUEST_GOOD_FAHUO_TIME && resultCode == mActivity.RESULT_OK) {
            good_fahuo_time = data.getStringExtra("value");
            tvFahuoTimeValue.setText(data.getStringExtra("name"));
            tvFahuoTimeValue.setTextColor(mActivity.getResources().getColor(R.color.black_tv));

        }

        //add by :胡峰，拍品结束时间的获取逻辑
        if (requestCode == REQUEST_GOOD_PAIMAI_END_TIME && resultCode == mActivity.RESULT_OK){
            paimai_end_time = data.getStringExtra("value");
            tv_end_time_value.setText(data.getStringExtra("time"));
            tv_end_time_value.setTextColor(mActivity.getResources().getColor(R.color.black_tv));
        }

        //封面图片返回来  相册
        if (requestCode == RESULT_PIC_1_GALLARY && resultCode == mActivity.RESULT_OK) {
            Uri uri = data.getData();
            String pathLocal = ImageUtils.getPath(mContext, uri);
            KLog.d("返回的本地图片路径", pathLocal);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", pathLocal);
            UIHelper.jumpForResultByFragment(mFragment, CommentCropImageActivity.class, bundle, CommentCropImageActivity.COROP_REQUEST);
        }
        //封面图片返回来  相机
        if (requestCode == RESULT_PIC_1_CAMERA && resultCode == mActivity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", recentPicPath);
            UIHelper.jumpForResultByFragment(mFragment, CommentCropImageActivity.class, bundle, CommentCropImageActivity.COROP_REQUEST);
        }
        //列表返回来  相册
        if (requestCode == RESULT_PIC_2_GALLARY && resultCode == mActivity.RESULT_OK) {
            Uri uri = data.getData();
            String pathLocal = ImageUtils.getPath(mContext, uri);
            KLog.d("返回的本地图片路径", pathLocal);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", pathLocal);
            UIHelper.jumpForResultByFragment(mFragment, CommentCropImage2Activity.class, bundle, CommentCropImage2Activity.COROP_REQUEST);
//            String path = ImageUtils.getSmallBitmap(data.getStringExtra(CommentCropImage2Activity.RERULT_PATH));
//
//            int emptyCount = 0;
//            for (PostImageBean bean : postImageBeans) {
//                if (bean.isEmpty()) {
//                    emptyCount++;
//                }
//            }
//            if (postImageBeans.size() >= 2 && postImageBeans.size() < 9 && emptyCount < 2) {
//                PostImageBean postImageBean = new PostImageBean();
//                postImageBean.setEmpty(true);
//                postImageBeans.add(postImageBean);
//            }
//            postImageBeans.get(currentIndex).setLocalPath(path);
//            postImageBeans.get(currentIndex).setEmpty(false);
//            postImageBeans.get(currentIndex).setStatus(PostImageBean.Status.INIT);
//            mAdapter.notifyDataSetChanged();
//            uploadListmages(postImageBeans.get(currentIndex));
//            tv_image_count.setText(getImageCount() + "/10");
        }
        //列表图片返回来  相机
        if (requestCode == RESULT_PIC_2_CAMERA && resultCode == mActivity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", recentPicPath);
            UIHelper.jumpForResultByFragment(mFragment, CommentCropImage2Activity.class, bundle, CommentCropImage2Activity.COROP_REQUEST);
//            String path = ImageUtils.getSmallBitmap(data.getStringExtra(CommentCropImage2Activity.RERULT_PATH));
//            int emptyCount = 0;
//            for (PostImageBean bean : postImageBeans) {
//                if (bean.isEmpty()) {
//                    emptyCount++;
//                }
//            }
//            if (postImageBeans.size() >= 2 && postImageBeans.size() < 9 && emptyCount < 2) {
//                PostImageBean postImageBean = new PostImageBean();
//                postImageBean.setEmpty(true);
//                postImageBeans.add(postImageBean);
//            }
//            postImageBeans.get(currentIndex).setLocalPath(path);
//            postImageBeans.get(currentIndex).setEmpty(false);
//            postImageBeans.get(currentIndex).setStatus(PostImageBean.Status.INIT);
//            mAdapter.notifyDataSetChanged();
//            uploadListmages(postImageBeans.get(currentIndex));
//            tv_image_count.setText(getImageCount() + "/10");
        }

        if (requestCode == CommentCropImageActivity.COROP_REQUEST && resultCode == CommentCropImageActivity.COROP_RESULT) {
            String path = data.getStringExtra(CommentCropImageActivity.RERULT_PATH);
            if (path != null) {
                mainImageBean.setLocalPath(path);
                mainImageBean.setStatus(PostImageBean.Status.INIT);
                mApplication.setImages("file://" + mainImageBean.getLocalPath(), firstimg);
                uploadMainImages(mainImageBean);
                tv_image_count.setText(getImageCount() + "/10");
            }
        }

        if (requestCode == CommentCropImage2Activity.COROP_REQUEST && resultCode == CommentCropImage2Activity.COROP_RESULT){
            //String path = ImageUtils.getSmallBitmap(data.getStringExtra(CommentCropImage2Activity.RERULT_PATH));
            String path = data.getStringExtra(CommentCropImage2Activity.RERULT_PATH);
            if (path != null){
                int emptyCount = 0;
                for (PostImageBean bean : postImageBeans) {
                    if (bean.isEmpty()) {
                        emptyCount++;
                    }
                }
                if (postImageBeans.size() >= 2 && postImageBeans.size() < 9 && emptyCount < 2) {
                    PostImageBean postImageBean = new PostImageBean();
                    postImageBean.setEmpty(true);
                    postImageBeans.add(postImageBean);
                }
                postImageBeans.get(currentIndex).setLocalPath(path);
                postImageBeans.get(currentIndex).setEmpty(false);
                postImageBeans.get(currentIndex).setStatus(PostImageBean.Status.INIT);
                mAdapter.notifyDataSetChanged();
                uploadListmages(postImageBeans.get(currentIndex));
                tv_image_count.setText(getImageCount() + "/10");
            }
        }
    }

    private Handler mainImageHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//失败了
//                ivMainClose.setVisibility(View.VISIBLE);
                ivFisrtStatus.setVisibility(View.VISIBLE);
            }
            if (msg.what == 1) {//成功
//                ivMainClose.setVisibility(View.VISIBLE);
                ivFisrtStatus.setVisibility(View.GONE);
            }
        }
    };
    private Handler listImageHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };

    private void uploadMainImages(final PostImageBean bean) {
        File filename = new File(bean.getLocalPath());
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(filename.getName(), filename))
                .upload(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        mainImageHander.sendEmptyMessage(0);
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.d("拍卖返回---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtil.isOK(jsonObject)) {
                                String path = jsonObject.getJSONObject("resultMap").getString("message");
                                bean.setServerPath(path);
                                mainImageHander.sendEmptyMessage(1);
                            } else {
                                bean.setStatus(PostImageBean.Status.FAIL);
                                listImageHander.sendEmptyMessage(0);
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            bean.setStatus(PostImageBean.Status.FAIL);
                            listImageHander.sendEmptyMessage(0);
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void uploadListmages(final PostImageBean bean) {
        File filename = new File(bean.getLocalPath());
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(filename.getName(), filename))
                .upload(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        KLog.d("拍卖onError返回---", e.getMessage());
                        bean.setStatus(PostImageBean.Status.FAIL);
                        listImageHander.sendEmptyMessage(0);
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.d("拍卖onResponse返回---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtil.isOK(jsonObject)) {
                                bean.setStatus(PostImageBean.Status.DONE);
                                String path = jsonObject.getJSONObject("resultMap").getString("message");
                                bean.setServerPath(path);
                                listImageHander.sendEmptyMessage(1);
                            } else {
                                bean.setStatus(PostImageBean.Status.FAIL);
                                listImageHander.sendEmptyMessage(0);
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            bean.setStatus(PostImageBean.Status.FAIL);
                            listImageHander.sendEmptyMessage(0);
                            e.printStackTrace();
                        }

                    }
                });
    }


    NoticeSingleDialog noticeSingleDialog;
    private String end_price = "";

    /**
     * 提交数据
     */
    private void submint() {

        if (TextUtils.isEmpty(mainImageBean.getServerPath())) {
            UIHelper.t(mContext, "请至少选择一张图片作为封面");
            return;
        }

        boolean isOk = true;
        for (PostImageBean postImageBean : postImageBeans) {
            if (!postImageBean.isEmpty()) {
                if (postImageBean.getStatus() == PostImageBean.Status.FAIL || postImageBean.getStatus() == PostImageBean.Status.INIT) {
                    isOk = false;
                    break;
                }
            }
        }
        if (!isOk) {
            UIHelper.t(mContext, "您有拍品图片还没上传成功");
            return;
        }

        if (TextUtils.isEmpty(etInputGoodTitle.getText().toString().trim())) {
            UIHelper.t(mContext, "请输入商品名称");
            return;
        }
        if (TextUtils.isEmpty(etInputGoodDesc.getText().toString().trim())) {
            UIHelper.t(mContext, "请输入商品描述");
            return;
        }
        if (TextUtils.isEmpty(good_type)) {
            UIHelper.t(mContext, "请选择商品类型");
            return;
        }
        if (TextUtils.isEmpty(good_status)) {
            UIHelper.t(mContext, "请选择商品状态");
            return;
        }
        if (TextUtils.isEmpty(paimai_end_time)) {
            UIHelper.t(mContext, "请选择结束时间");
            return;
        }
        if (TextUtils.isEmpty(good_yunfei)) {
            UIHelper.t(mContext, "请选择运费承担方");
            return;
        }
        if (TextUtils.isEmpty(fahou_city)) {
            UIHelper.t(mContext, "请选择发货地");
            return;
        }
        if (TextUtils.isEmpty(good_fahuo_time)) {
            UIHelper.t(mContext, "请选择发货时间");
            return;
        }
        if (TextUtils.isEmpty(tvStartPriceValue.getText().toString().trim())) {
            UIHelper.t(mContext, "请填写起拍价格");
            return;
        }else if (tvStartPriceValue.getText().toString().trim().startsWith("0")){
            UIHelper.t(mContext,"请输入正确的起拍价");
            return;
        }
        if (cbYikoujiaStatus.isChecked()) {
            if (TextUtils.isEmpty(tvYikouPriceValue.getText().toString().trim())) {
                UIHelper.t(mContext, "您已开启一口价，请输入一口价");
                return;
            }
            if (tvYikouPriceValue.getText().toString().trim().startsWith("0")){
                UIHelper.t(mContext,"请输入正确的一口价");
                return;
            }
            if (Integer.valueOf(tvYikouPriceValue.getText().toString().trim()) < Integer.valueOf(tvStartPriceValue.getText().toString().trim())) {
                UIHelper.t(mContext, "一口价应该大于或等于起拍价");
                Log.i("interger--",Integer.MAX_VALUE+"");
                return;
            }
        }

        if (cbYikoujiaStatus.isChecked()){
            end_price = tvYikouPriceValue.getText().toString().trim();
        }else {
            end_price = "无";
        }

        new NoticeDialogPost(mContext,"出品确认",tvStartPriceValue.getText().toString().trim(),end_price).setCallBack(new NoticeDialogPost.CallBack() {
            @Override
            public void ok() {
                JSONObject jsonObject = new JSONObject();
                try {
                    //起拍价格
                    jsonObject.put("begin_auct_price", tvStartPriceValue.getText().toString().trim());
                    //一口价
                    if (cbYikoujiaStatus.isChecked()) {
                        jsonObject.put("but_it_price", tvYikouPriceValue.getText().toString().trim());
                    }


                    //拍品分类
                    jsonObject.put("cate_id", good_type);
                    //拍品封面
                    jsonObject.put("cover_pic", mainImageBean.getServerPath());

                    if (postImageBeans.size() > 0) {
                        String paths = "";
                        StringBuffer stringBuffer = new StringBuffer();
                        boolean isOK = false;
                        for (PostImageBean postImageBean : postImageBeans) {
                            if (!postImageBean.isEmpty()) {
                                isOK = true;
                                stringBuffer.append(postImageBean.getServerPath() + ",");
                            }
                        }
                        if (isOK) {
                            paths = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                            //轮播图
                            jsonObject.put("cycle_pic", paths);
                        }
                    }
                    //承担运费方
                    jsonObject.put("express_out_type", good_yunfei);//必填 1 买家 2 卖家
                    //发货城市
                    jsonObject.put("fahou_city", fahou_city);
                    //发货地省份
                    jsonObject.put("fahou_province", fahou_province);
                    //发货时间
                    jsonObject.put("fahuo_time_type", good_fahuo_time);//必填 1：当天发货 2：1-3天 3 ：1周内 4:2-3周内）
                    //下次使用相同物流
                    jsonObject.put("is_same_express", cbWuliu.isChecked() ? 1 : 0);//0 否 1 是
                    //是否开启一口价
                    jsonObject.put("open_but_it", cbYikoujiaStatus.isChecked() ? 1 : 0);//0：否 1：是
                    //结束时间
                    //jsonObject.put("pm_end_time", good_end_time);
                    //change by :胡峰，拍卖结束时间上传
                    jsonObject.put("pm_end_type",paimai_end_time);
                    //拍品介绍
                    jsonObject.put("product_desc", etInputGoodDesc.getText().toString());
                    //标题
                    jsonObject.put("product_title", etInputGoodTitle.getText().toString());
                    //拍品商品状态ID
                    jsonObject.put("st_id", good_status);

                    //自身用户ID
                    jsonObject.put("seller_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                    jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mApplication.apiClient.product_submitByApp(jsonObject, new ApiCallback() {
                    @Override
                    public void onApiStart() {
                        showLoadingDialog("发布中...");
                    }

                    @Override
                    public void onApiSuccess(String response) {
                        if (!isAdded())
                            return;
                        try {
                            JSONObject resultObj = new JSONObject(response);
                            if (JSONUtil.isOK(resultObj)) {
                                noticeSingleDialog = new NoticeSingleDialog(mContext, "温馨提示", "您已成功发布\n请耐心等待平台审核", "我知道了").setCallBack(new NoticeSingleDialog.CallBack() {
                                    @Override
                                    public void ok() {

                                    }

                                    @Override
                                    public void cancle() {

                                    }
                                });
                                noticeSingleDialog.show();
                                noticeSingleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                    }
                                });
                                initSuccessView();
                                EventBus.getDefault().post(new PublicGoodEvent(0));
                            } else {
                                UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onApiFailure(Request request, Exception e) {
                        if (!isAdded()) {
                            return;
                        }
                        UIHelper.t(mContext, R.string.net_error);
                    }
                });
            }

            @Override
            public void cancle() {

            }
        }).show();


    }

    private PostImageBean getEmptyBean() {
        PostImageBean postImageBean = new PostImageBean();
        postImageBean.setEmpty(true);
        return postImageBean;
    }

    private int getImageCount() {
        int count = 0;
        for (PostImageBean postImageBean : postImageBeans) {
            if (!postImageBean.isEmpty()) {
                count++;
            }
        }

        if (mainImageBean.getLocalPath() != null) {
            if (!TextUtils.isEmpty(mainImageBean.getLocalPath())) {
                count++;
            }
        }

        return count;
    }


    private void initSuccessView() {
        //主图
        mainImageBean.setServerPath("");
        firstimg.setImageResource(R.color.transparent);
        ivFisrtStatus.setVisibility(View.GONE);

        postImageBeans.clear();
        postImageBeans.add(getEmptyBean());
        postImageBeans.add(getEmptyBean());
        mAdapter.notifyDataSetChanged();
        mainImageBean.setLocalPath("");
        tv_image_count.setText(getImageCount() + "/10");

        good_type = "";
        good_status = "";
        good_yunfei = "";
        fahou_city = "";
        fahou_province = "";
        good_fahuo_time = "";
        paimai_end_time = "";

        etInputGoodTitle.setText("");
        etInputGoodDesc.setText("");
        tvGoodsTypeValue.setText("请选择");
        tvGoodsStatusValue.setText("请选择");
        tv_end_time_value.setText("请选择");
        tv_yunfei_status_value.setText("请选择");
        tv_addres_value.setText("请选择");
        tvFahuoTimeValue.setText("请选择");

        tvGoodsTypeValue.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        tvGoodsStatusValue.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        tv_end_time_value.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        tv_yunfei_status_value.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        tv_addres_value.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        tvFahuoTimeValue.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));


        cbWuliu.setChecked(true);
        cbYikoujiaStatus.setChecked(true);
        rlYikouPrice.setVisibility(View.VISIBLE);

        cbYikoujiaStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlYikouPrice.setVisibility(View.VISIBLE);
                } else {
                    rlYikouPrice.setVisibility(View.GONE);
                }
            }
        });

        tvStartPriceValue.setText("");
        tvYikouPriceValue.setText("");

    }

    private void initCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator iter = mApplication.mCitisDatasMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String province = (String) entry.getKey();
                    options1Items.add(province);
                    String[] citys = (String[]) entry.getValue();
                    ArrayList<String> options2Item = new ArrayList<String>();
                    for (String city : citys) {
                        options2Item.add(city);
                    }
                    options2Items.add(options2Item);
                }
            }
        }).start();
    }
}
