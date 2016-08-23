package com.buycolle.aicang.ui.activity.usercentermenu.mysale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.EditPaiPinDetailBean;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.bean.PostImageBean;
import com.buycolle.aicang.event.EditPostEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.ChangJianQuestionActivity;
import com.buycolle.aicang.ui.activity.FaHuoTimeActivity;
import com.buycolle.aicang.ui.activity.PaiMaiEndTimeActivity;
import com.buycolle.aicang.ui.activity.PaiMaiFaHuoZhiDaoActivity;
import com.buycolle.aicang.ui.activity.PaiMaiJiaoYiLiuChengActivity;
import com.buycolle.aicang.ui.activity.post.YunFeiActivity;
import com.buycolle.aicang.ui.activity.shangpintypes.ShangPinStatusActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeDialogPost;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/6.
 */
public class MySaleReShanjiaActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_tobe_maijia)
    TextView tvTobeMaijia;
    @Bind(R.id.ll_not_maijia_lay)
    LinearLayout llNotMaijiaLay;
    @Bind(R.id.iv_fisrt)
    ImageView firstimg;
    @Bind(R.id.iv_fisrt_status)
    ImageView ivFisrtStatus;
    @Bind(R.id.iv_main_close)
    ImageView ivMainClose;
    @Bind(R.id.rv_imgs)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_image_count)
    TextView tvImageCount;
    @Bind(R.id.et_input_good_title)
    EditText etInputGoodTitle;
    @Bind(R.id.et_input_good_desc)
    EditText etInputGoodDesc;
    @Bind(R.id.tv_goods_type_title)
    TextView tvGoodsTypeTitle;
    @Bind(R.id.tv_goods_type_value)
    TextView tvGoodsTypeValue;
    @Bind(R.id.iv_goods_type_arrow)
    ImageView ivGoodsTypeArrow;
    @Bind(R.id.rl_goods_type)
    RelativeLayout rlGoodsType;
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
    @Bind(R.id.tv_end_time_value)
    TextView tvEndTimeValue;
    @Bind(R.id.iv_end_time_arrow)
    ImageView ivEndTimeArrow;
    @Bind(R.id.rl_end_time)
    RelativeLayout rlEndTime;
    @Bind(R.id.tv_yunfei_status_title)
    TextView tvYunfeiStatusTitle;
    @Bind(R.id.tv_yunfei_status_value)
    TextView tvYunfeiStatusValue;
    @Bind(R.id.iv_yunfei_status_arrow)
    ImageView ivYunfeiStatusArrow;
    @Bind(R.id.rl_yunfei_status)
    RelativeLayout rlYunfeiStatus;
    @Bind(R.id.tv_addres_title)
    TextView tvAddresTitle;
    @Bind(R.id.tv_addres_value)
    TextView tvAddresValue;
    @Bind(R.id.iv_addres_arrow)
    ImageView ivAddresArrow;
    @Bind(R.id.rl_addres)
    RelativeLayout rlAddres;
    @Bind(R.id.tv_fahuo_time_title)
    TextView tvFahuoTimeTitle;
    @Bind(R.id.tv_fahuo_time_value)
    TextView tvFahuoTimeValue;
    @Bind(R.id.iv_fahuo_time_arrow)
    ImageView ivFahuoTimeArrow;
    @Bind(R.id.rl_fahuo_time)
    RelativeLayout rlFahuoTime;
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
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.ll_maijia)
    LinearLayout llMaijia;

    @Bind(R.id.tv_jiaoyi_liucheng)
    TextView tv_jiaoyi_liucheng;//交易流程
    @Bind(R.id.tv_fahuo_zhidao)
    TextView tv_fahuo_zhidao;//发货指导
    @Bind(R.id.tv_changjianwenti)
    TextView tv_changjianwenti;//常见问题
    @Bind(R.id.tv_feilv)
    TextView tv_fee_rate;

    private ArrayList<PostImageBean> postImageBeans;
    private PostImageBean mainImageBean = new PostImageBean();

    private GalleryAdapter mAdapter;

    private final int REQUEST_GOOD_STATUS = 2000;
    private final int REQUEST_GOOD_YUNFEI_CHENGDAN = 3000;
    private final int REQUEST_GOOD_FAHUO_TIME = 4000;
    private final int REQUEST_GOOD_PAIMAI_END_TIME = 5000;//拍卖结束时间标识

    TimePickerView pvTime;

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    OptionsPickerView pvOptions;

    private String good_type = "";//商品类型
    private String good_status = "";//商品状态 id
    private String good_end_time = "";//结束时间
    private String good_yunfei = "";//承担运费方
    private String fahou_city = "";//发货地址 市
    private String fahou_province = "";//发货地址 省
    private String good_fahuo_time = "";//发货时间


    private int productId;
    EditPaiPinDetailBean paiPinDetailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_post_again);
        ButterKnife.bind(this);
        myHeader.init("再次上架", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        aCache = ACache.get(mContext);
        postImageBeans = new ArrayList<>();
        productId = _Bundle.getInt("productId");
        //初始化城市信息
        initCity();
        //加载详情数据
        loadData();
        //加载倍率
        loadTopAds();
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

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", productId);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_gethissubmitinfosbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("初始化数据...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        paiPinDetailBean = new Gson().fromJson(userInfoObj.toString(), EditPaiPinDetailBean.class);
                        initData();
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
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    /**
     * 初始化布局界面
     */
    private void initData() {

        //add by hufeng:三个按钮的下滑线
        tv_jiaoyi_liucheng.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_jiaoyi_liucheng.getPaint().setAntiAlias(true);//抗锯齿处理
        tv_fahuo_zhidao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_fahuo_zhidao.getPaint().setAntiAlias(true);
        tv_changjianwenti.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_changjianwenti.getPaint().setAntiAlias(true);

        // 图片处理
        //主图
        mainImageBean.setEmpty(false);
        mainImageBean.setLocalPath("");
        mainImageBean.setServer(true);
        mainImageBean.setServerPath(paiPinDetailBean.getCover_pic());
        mApplication.setImages(mainImageBean.getServerPath(), firstimg);
        ivMainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageBean.setServerPath("");
                firstimg.setImageResource(R.color.transparent);
                ivMainClose.setVisibility(View.GONE);
                ivFisrtStatus.setVisibility(View.GONE);
            }
        });


        int listImagesSize = 0;
        if (!TextUtils.isEmpty(paiPinDetailBean.getCycle_pic())) {
            if (paiPinDetailBean.getCycle_pic().indexOf(",") < 0) {//一张
                listImagesSize = 1;
                PostImageBean postImageBean_1 = new PostImageBean();
                postImageBean_1.setStatus(PostImageBean.Status.DONE);
                postImageBean_1.setEmpty(false);
                postImageBean_1.setServer(true);
                postImageBean_1.setServerPath(paiPinDetailBean.getCycle_pic());
                postImageBeans.add(postImageBean_1);
            } else {
                String[] paths = paiPinDetailBean.getCycle_pic().split(",");
                listImagesSize = paths.length;
                for (String path : paths) {
                    PostImageBean postImageBean_1 = new PostImageBean();
                    postImageBean_1.setStatus(PostImageBean.Status.DONE);
                    postImageBean_1.setEmpty(false);
                    postImageBean_1.setServer(true);
                    postImageBean_1.setServerPath(path);
                    postImageBeans.add(postImageBean_1);
                }
            }

            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            //设置适配器
            mAdapter = new GalleryAdapter(mContext);
            mRecyclerView.setAdapter(mAdapter);

        }
        tvImageCount.setText((listImagesSize+1) + "/10");


        //标题
        etInputGoodTitle.setText(paiPinDetailBean.getProduct_title());
        //描述
        etInputGoodDesc.setText(paiPinDetailBean.getProduct_desc());

        //商品类型
        good_type = paiPinDetailBean.getCate_id() + "";
        tvGoodsTypeValue.setText(paiPinDetailBean.getCate_name());
        tvGoodsTypeValue.setTextColor(getResources().getColor(R.color.black_tv));

        //商品状态
        good_status = paiPinDetailBean.getSt_id() + "";
        tvGoodsStatusValue.setText(paiPinDetailBean.getSt_name());
        rlGoodsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResult(mActivity, ShangPinStatusActivity.class, REQUEST_GOOD_STATUS);
            }
        });
        tvGoodsStatusValue.setTextColor(getResources().getColor(R.color.black_tv));
        //change by :胡峰，拍品结束时间的逻辑处理
        good_end_time = "";
        tvEndTimeValue.setText("请选择");
        tvEndTimeValue.setTextColor(mContext.getResources().getColor(R.color.gray_tv));
        rlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jumpForResult(mActivity, PaiMaiEndTimeActivity.class,REQUEST_GOOD_PAIMAI_END_TIME);
            }
        });


        //运费承担方
        if (paiPinDetailBean.getExpress_out_type() == 1) {
            good_yunfei = "1";//买方
            tvYunfeiStatusValue.setText("到付");
        } else {
            good_yunfei = "2";//卖方
            tvYunfeiStatusValue.setText("包邮");
        }
        tvYunfeiStatusValue.setTextColor(getResources().getColor(R.color.black_tv));

        rlYunfeiStatus.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  UIHelper.jumpForResult(mActivity, YunFeiActivity.class, REQUEST_GOOD_YUNFEI_CHENGDAN);
                                              }
                                          }

        );

        //发货地
        fahou_city = paiPinDetailBean.getFahou_city();
        fahou_province = paiPinDetailBean.getFahou_province();
        tvAddresValue.setText(fahou_province + fahou_city);
        tvAddresValue.setTextColor(getResources().getColor(R.color.black_tv));

        pvOptions = new OptionsPickerView(mActivity);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                fahou_city = options2Items.get(options1).get(option2);
                fahou_province = options1Items.get(options1);
                tvAddresValue.setText(tx);
            }
        });
        rlAddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.setPicker(options1Items, options2Items, true);
                pvOptions.setCyclic(false, false, false);
                pvOptions.setSelectOptions(0, 0, 0);
                pvOptions.show();
            }
        });



        //发货时间
        good_fahuo_time = paiPinDetailBean.getExpress_out_type() + "";
        if ("1".equals(good_fahuo_time)) {
            tvFahuoTimeValue.setText("当天发货");
        } else if ("2".equals(good_fahuo_time)) {
            tvFahuoTimeValue.setText("1-3天");
        } else if ("3".equals(good_fahuo_time)) {
            tvFahuoTimeValue.setText("1周以内");
        } else if ("4".equals(good_fahuo_time)) {
            tvFahuoTimeValue.setText("2-3周以内");
        }
        tvFahuoTimeValue.setTextColor(getResources().getColor(R.color.black_tv));

        rlFahuoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jumpForResult(mActivity, FaHuoTimeActivity.class, REQUEST_GOOD_FAHUO_TIME);
            }
        });

        //是否使用相同的物流信息
        if (paiPinDetailBean.getIs_same_express() == 1) {//是
            cbWuliu.setChecked(true);
        } else {
            cbWuliu.setChecked(false);
        }

        //起拍价
        tvStartPriceValue.setText(paiPinDetailBean.getBegin_auct_price());


        //一口价
        if (paiPinDetailBean.getOpen_but_it() == 1) {//开启一口价
            cbYikoujiaStatus.setChecked(true);
            rlYikouPrice.setVisibility(View.VISIBLE);
            tvYikouPriceValue.setText(paiPinDetailBean.getBut_it_price());
        } else {
            rlYikouPrice.setVisibility(View.GONE);
            cbYikoujiaStatus.setChecked(false);
            tvYikouPriceValue.setText("");
        }
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submint();
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

    private String end_price = "";
    /**
     * 提交数据
     */
    private void submint() {
        if (TextUtils.isEmpty(mainImageBean.getServerPath())) {
            UIHelper.t(mContext, "请选择一张拍品封面的主图");
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
        if (TextUtils.isEmpty(good_end_time)) {
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
        }
        if (cbYikoujiaStatus.isChecked()) {
            if (TextUtils.isEmpty(tvYikouPriceValue.getText().toString().trim())) {
                UIHelper.t(mContext, "你已开启一口价，请填写一口价");
                return;
            }
            if (Integer.valueOf(tvYikouPriceValue.getText().toString().trim()) < Integer.valueOf(tvStartPriceValue.getText().toString().trim())) {
                UIHelper.t(mContext, "一口价应该大于或等于起拍价");
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

                    jsonObject.put("product_id", productId);
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
                    jsonObject.put("pm_end_type", good_end_time);
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
                mApplication.apiClient.product_reupstorebyapp(jsonObject, new ApiCallback() {
                    @Override
                    public void onApiStart() {
                        showLoadingDialog("发布中...");
                    }

                    @Override
                    public void onApiSuccess(String response) {
                        if (isFinishing())
                            return;
                        try {
                            JSONObject resultObj = new JSONObject(response);
                            if (JSONUtil.isOK(resultObj)) {
                                new NoticeSingleDialog(mContext, "温馨提示", "您的商品已上架", "我知道了").setCallBack(new NoticeSingleDialog.CallBack() {
                                    @Override
                                    public void ok() {
                                        finish();
                                        EventBus.getDefault().post(new EditPostEvent(0));
                                    }

                                    @Override
                                    public void cancle() {

                                    }
                                }).show();
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
                        if (isFinishing()) {
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

//        JSONObject jsonObject = new JSONObject();
//        try {
//            //起拍价格
//            jsonObject.put("begin_auct_price", tvStartPriceValue.getText().toString().trim());
//            //一口价
//            if (cbYikoujiaStatus.isChecked()) {
//                jsonObject.put("but_it_price", tvYikouPriceValue.getText().toString().trim());
//            }
//
//
//            //拍品分类
//            jsonObject.put("cate_id", good_type);
//            //拍品封面
//            jsonObject.put("cover_pic", mainImageBean.getServerPath());
//
//            if (postImageBeans.size() > 0) {
//                String paths = "";
//                StringBuffer stringBuffer = new StringBuffer();
//                boolean isOK = false;
//                for (PostImageBean postImageBean : postImageBeans) {
//                    if (!postImageBean.isEmpty()) {
//                        isOK = true;
//                        stringBuffer.append(postImageBean.getServerPath() + ",");
//                    }
//                }
//                if (isOK) {
//                    paths = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
//                    //轮播图
//                    jsonObject.put("cycle_pic", paths);
//                }
//            }
//
//            jsonObject.put("product_id", productId);
//            //承担运费方
//            jsonObject.put("express_out_type", good_yunfei);//必填 1 买家 2 卖家
//            //发货城市
//            jsonObject.put("fahou_city", fahou_city);
//            //发货地省份
//            jsonObject.put("fahou_province", fahou_province);
//            //发货时间
//            jsonObject.put("fahuo_time_type", good_fahuo_time);//必填 1：当天发货 2：1-3天 3 ：1周内 4:2-3周内）
//            //下次使用相同物流
//            jsonObject.put("is_same_express", cbWuliu.isChecked() ? 1 : 0);//0 否 1 是
//            //是否开启一口价
//            jsonObject.put("open_but_it", cbYikoujiaStatus.isChecked() ? 1 : 0);//0：否 1：是
//            //结束时间
//            jsonObject.put("pm_end_type", good_end_time);
//            //拍品介绍
//            jsonObject.put("product_desc", etInputGoodDesc.getText().toString());
//            //标题
//            jsonObject.put("product_title", etInputGoodTitle.getText().toString());
//            //拍品商品状态ID
//            jsonObject.put("st_id", good_status);
//
//            //自身用户ID
//            jsonObject.put("seller_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
//            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mApplication.apiClient.product_reupstorebyapp(jsonObject, new ApiCallback() {
//            @Override
//            public void onApiStart() {
//                showLoadingDialog("发布中...");
//            }
//
//            @Override
//            public void onApiSuccess(String response) {
//                if (isFinishing())
//                    return;
//                try {
//                    JSONObject resultObj = new JSONObject(response);
//                    if (JSONUtil.isOK(resultObj)) {
//                        new NoticeSingleDialog(mContext, "温馨提示", "您的商品已上架", "我知道了").setCallBack(new NoticeSingleDialog.CallBack() {
//                            @Override
//                            public void ok() {
//                                finish();
//                                EventBus.getDefault().post(new EditPostEvent(0));
//                            }
//
//                            @Override
//                            public void cancle() {
//
//                            }
//                        }).initDialog();
//                    } else {
//                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                dismissLoadingDialog();
//            }
//
//            @Override
//            public void onApiFailure(Request request, Exception e) {
//                if (isFinishing()) {
//                    return;
//                }
//                UIHelper.t(mContext, R.string.net_error);
//            }
//        });
    }


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
            viewHolder.iv_close.setVisibility(View.GONE);
            viewHolder.iv_menu_icon_1.setOnClickListener(null);
            viewHolder.ll_add_item.setBackgroundResource(R.drawable.shape_white_black);
            mApplication.setImages(postImageBean.getServerPath(), viewHolder.iv_menu_icon_1);
        }

    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
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
            tvYunfeiStatusValue.setText(data.getStringExtra("type"));
        }
        //物品状态返回
        if (requestCode == REQUEST_GOOD_STATUS && resultCode == mActivity.RESULT_OK) {
            good_status = data.getStringExtra("dir_id");
            tvGoodsStatusValue.setText(data.getStringExtra("item_name"));
        }
        //物品发货时间返回
        if (requestCode == REQUEST_GOOD_FAHUO_TIME && resultCode == mActivity.RESULT_OK) {
            good_fahuo_time = data.getStringExtra("value");
            tvFahuoTimeValue.setText(data.getStringExtra("name"));
        }

        //add by :胡峰，拍品结束时间的获取逻辑
        if (requestCode == REQUEST_GOOD_PAIMAI_END_TIME && resultCode == mActivity.RESULT_OK){
            good_end_time = data.getStringExtra("value");
            Log.i("paimai_end_time--", data.getStringExtra("value"));
            tvEndTimeValue.setText(data.getStringExtra("time"));
            Log.i("tv_end_time_value--",data.getStringExtra("time"));
            tvEndTimeValue.setTextColor(mActivity.getResources().getColor(R.color.black_tv));
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
                if (isFinishing()) {
                    return;
                }else {
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
}
