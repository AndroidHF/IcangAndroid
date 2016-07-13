package com.buycolle.aicang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeGoodsBean;
import com.buycolle.aicang.bean.HomeGoodsChildBean;
import com.buycolle.aicang.ui.view.PaiPinSearchDialog;
import com.buycolle.aicang.ui.view.mainScrole.MyHomeAdapter;
import com.buycolle.aicang.ui.view.xlistview.XListView;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/11.
 */
public class SearchPaiPinActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_input)
    TextView etInput;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @Bind(R.id.iv_current_price)
    ImageView ivCurrentPrice;
    @Bind(R.id.ll_current_price)
    LinearLayout llCurrentPrice;
    @Bind(R.id.tv_jingpai_user)
    TextView tvJingpaiUser;
    @Bind(R.id.iv_haoping)
    ImageView iv_haoping;
    @Bind(R.id.iv_jingpai_user)
    ImageView ivJingpaiUser;
    @Bind(R.id.ll_jingpai_user)
    LinearLayout llJingpaiUser;
    @Bind(R.id.tv_lost_time)
    TextView tvLostTime;
    @Bind(R.id.iv_lost_time)
    ImageView ivLostTime;
    @Bind(R.id.ll_lost_time)
    LinearLayout llLostTime;
    @Bind(R.id.ll_haoping)
    LinearLayout llHaoping;
    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.tv_haoping)
    TextView tvHaoping;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;

    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<TextView> titles;
    private ArrayList<ImageView> titleImages;


    private ArrayList<HomeGoodsBean> homeGoodsBeanArrayList;
    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 20;


    MyHomeAdapter myAdapter;


    /*
   搜索过来的相关
    */
    private String search_text = "";


    /*
    排序相关
     */

    private int sort_type = 0;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private int sort_value = 1;// 1:升序 2：降序

    private int sort_price;//价格排序  1:升序 2：降序
    private int sort_count;//竞拍人数排序  1:升序 2：降序
    private int sort_time;//剩余时间排序  1:升序 2：降序
    private int sort_eval;//卖家好评排序  1:升序 2：降序


    /*
  条件搜索相关
   */

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类

    private String st_id = "";//状态ID集
    private String start_price = "";//起始价格
    private String end_price = "";//最高价格

    private boolean isSelectExpress_Out_Type = false;
    private int express_out_type;//1 不包邮 2 包邮

    private boolean isSelectOpen_But_It = false;
    private int open_but_it;//0 无 1 有

    private boolean isDetail = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_paipin);
        ButterKnife.bind(this);

        homeGoodsBeanArrayList = new ArrayList<>();
        search_text = _Bundle.getString("key_word");
        isDetail = _Bundle.getBoolean("isDetail", false);
        if(isDetail){
            cate_id = _Bundle.getInt("cate_id");
            isSelectCate_Id = true;
        }
        etInput.setText(search_text);

        initlistener();
        titles = new ArrayList<>();
        titleImages = new ArrayList<>();

        titles.add(tvCurrentPrice);
        titles.add(tvJingpaiUser);
        titles.add(tvLostTime);
        titles.add(tvHaoping);

        titleImages.add(ivCurrentPrice);
        titleImages.add(ivJingpaiUser);
        titleImages.add(ivLostTime);
        titleImages.add(iv_haoping);
        myAdapter = new MyHomeAdapter(mActivity, homeGoodsBeanArrayList);
        list.setPullRefreshEnable(false);
        list.setAdapter(myAdapter);
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(true);
                }
            }
        });
        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                if (firstVisibleItem > Constans.MAX_SHOW_FLOAT_BTN) {
                    ibFloatBtn.setVisibility(View.VISIBLE);
                } else {
                    ibFloatBtn.setVisibility(View.GONE);
                }
            }
        });
        ibFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setSelection(0);
            }
        });
        etInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", 1);
                if(isDetail){
                    bundle.putBoolean("isDetail",isDetail);
                    bundle.putInt("cate_id",cate_id);
                }
                UIHelper.jump(mActivity, SearchActivity.class, bundle);
                finish();
            }
        });
        aCache = ACache.get(mContext);
        JSONObject resultObj = aCache.getAsJSONObject(Constans.TAG_GOOD_STATUS);
        if (resultObj == null) {
            dirtionary_getproductstatuslistbyapp();
        }
        loadData(false);


    }

    private ACache aCache;

    /**
     * 获取物品状态 比如九成新，破损等等
     */
    private void dirtionary_getproductstatuslistbyapp() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.dirtionary_getproductstatuslistbyapp(jsonObject, new ApiCallback() {
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
                        aCache.put(Constans.TAG_GOOD_STATUS, resultObj);
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

    private void initlistener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llCurrentPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(1);
                iniTitleImagesBySelect(0);
            }
        });
        llJingpaiUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(2);
                iniTitleImagesBySelect(1);
            }
        });
        llLostTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(3);
                iniTitleImagesBySelect(2);
            }
        });
        llHaoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(4);
                iniTitleImagesBySelect(3);
            }
        });
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PaiPinSearchDialog(mActivity, isSelectCate_Id, cate_id, st_id, start_price, end_price, isSelectExpress_Out_Type, express_out_type, isSelectOpen_But_It, open_but_it).setCallBack(new PaiPinSearchDialog.CallBack() {
                    @Override
                    public void action(boolean ISSELECTCATE_ID, int CATE_ID, String ST_ID, String START_PRICE, String END_PRICE, boolean ISSELECTEXPRESS_OUT_TYPE, int EXPRESS_OUT_TYPE, boolean ISSELECTOPEN_BUT_IT, int OPEN_BUT_IT) {
                        KLog.d("返回来的数据---", "isSelectCate_Id==" + ISSELECTCATE_ID + ",  " +
                                "cate_id==" + CATE_ID + ",  " +//
                                "st_id==" + ST_ID + ",  " +//
                                "start_price==" + START_PRICE + ",  " +//
                                "end_price==" + END_PRICE + ",  " +//
                                "isSelectExpress_Out_Type==" + ISSELECTEXPRESS_OUT_TYPE + ",  " +//
                                "express_out_type==" + EXPRESS_OUT_TYPE + ",  " +//
                                "isSelectOpen_But_It==" + ISSELECTOPEN_BUT_IT + ",  " +//
                                "open_but_it==" + OPEN_BUT_IT
                        );


                        isSelectCate_Id = ISSELECTCATE_ID;
                        cate_id = CATE_ID;
                        st_id = ST_ID;
                        start_price = START_PRICE;
                        end_price = END_PRICE;
                        isSelectExpress_Out_Type = ISSELECTEXPRESS_OUT_TYPE;
                        express_out_type = EXPRESS_OUT_TYPE;
                        isSelectOpen_But_It = ISSELECTOPEN_BUT_IT;
                        open_but_it = OPEN_BUT_IT;

                        pageIndex = 1;
                        loadData(false);

                    }
                }).show();
            }
        });


    }

    private int currentPosition = 0;
    private boolean isUp = false;

    private void iniTitleImagesAllDefaut() {
        for (int i = 0; i < titleImages.size(); i++) {
            titleImages.get(i).setVisibility(View.GONE);
        }
        sort_type = 4;
        sort_value = 1;
        pageIndex = 1;
        loadData(false);
    }

    private void initTitleImagesAllDefaut() {
        for (int i = 0; i < titleImages.size(); i++) {
            titleImages.get(i).setImageResource(R.drawable.filter_defuat);
        }
    }

    private void initDefoutSortTextView() {
        for (int i = 0; i < titles.size(); i++) {
            titles.get(i).setTextColor(getResources().getColor(R.color.black_tv));
        }
    }

    private void iniTitleImagesBySelect(int index) {
        for (int i = 0; i < titleImages.size(); i++) {
            if (index == i) {
                titleImages.get(i).setVisibility(View.VISIBLE);
                if (currentPosition == index) {
                    if (isUp) {
                        titleImages.get(i).setImageResource(R.drawable.filter_yellow_down);
                        isUp = false;
                    } else {
                        titleImages.get(i).setImageResource(R.drawable.filter_yellow_up);
                        isUp = true;
                    }
                } else {
                    currentPosition = index;
                    isUp = true;
                    titleImages.get(i).setImageResource(R.drawable.filter_yellow_up);
                }
            } else {
                titleImages.get(i).setVisibility(View.GONE);
            }
        }
        if (index == 0) {
            sort_type = 1;
        } else if (index == 1) {
            sort_type = 2;
        } else if (index == 2) {
            sort_type = 3;
        } else if (index == 3) {
            sort_type = 4;
        }
        if(index==3){
            titleImages.get(3).setVisibility(View.GONE);
            sort_value =2;
        }else{
            sort_value = isUp ? 1 : 2;
        }
        pageIndex = 1;
        loadData(false);
    }

    private void iniTitleTvColor(int index) {
        for (int i = 0; i < titles.size(); i++) {
            if (index == i) {
                titles.get(i).setTextColor(getResources().getColor(R.color.orange));
            } else {
                titles.get(i).setTextColor(getResources().getColor(R.color.black_tv));
            }
        }
    }

    private void initStatus(int index) {
        if (index == 1) {
            iniTitleTvColor(0);
        } else if (index == 2) {
            iniTitleTvColor(1);
        } else if (index == 3) {
            iniTitleTvColor(2);
        } else {
            iniTitleTvColor(3);
        }


    }

    private void loadData(final boolean isLoadMore) {
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {

              /*
            排序筛选
             */
            if (sort_type == 0) {//  0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
            } else if (sort_type == 1) {
                jsonObject.put("sort_price", sort_value);//1:升序 2：降序
            } else if (sort_type == 2) {
                jsonObject.put("sort_count", sort_value);//1:升序 2：降序
            } else if (sort_type == 3) {
                jsonObject.put("sort_time", sort_value);//1:升序 2：降序
            } else if (sort_type == 4) {
                jsonObject.put("sort_eval", sort_value);//1:升序 2：降序
            }
            jsonObject.put("search_text", search_text);//

            /*
            条件筛选
             */
            //分类
            if (isSelectCate_Id) {
                jsonObject.put("cate_id", cate_id);
            }
            //是否包邮
            if (isSelectExpress_Out_Type) {
                jsonObject.put("express_out_type", express_out_type);
            }
            //是否一口价
            if (isSelectOpen_But_It) {
                jsonObject.put("open_but_it", open_but_it);
            }

            //状态ID集
            if (!TextUtils.isEmpty(st_id)) {
                jsonObject.put("st_id", st_id);
            }

            if (!TextUtils.isEmpty(start_price)) {
                jsonObject.put("start_price", start_price);
            }
            if (!TextUtils.isEmpty(end_price)) {
                jsonObject.put("end_price", end_price);
            }

            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getnormallistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore) {
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
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            ArrayList<HomeGoodsChildBean> resultArray = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeGoodsChildBean>>() {
                            }.getType());
                            if (pageIndex == 1) {
                                homeGoodsBeanArrayList.clear();
                            }
                            ArrayList<HomeGoodsBean> resultFormatS = formatData(resultArray);
                            homeGoodsBeanArrayList.addAll(resultFormatS);
                            if (pageIndex == 1 && resultArray.size() > 0 && isHandRun == false) {
                                handler.sendEmptyMessage(1);
                            }
                            myAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                list.isShowFoot(true);
                            } else {
                                list.isShowFoot(false);
                            }
                        } else {
                            if (pageIndex == 1) {
                                //UIHelper.t(mContext, "您当前搜索的内容没有结果");
                                tv_null.setVisibility(View.VISIBLE);
                                homeGoodsBeanArrayList.clear();
                                myAdapter.notifyDataSetChanged();
                                list.isShowFoot(false);
                            }
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isLoadMore) {
                    dismissLoadingDialog();
                }



            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
                if (isLoadMore) {
                    pageIndex--;
                }
                isRun = false;
            }
        });

    }

    boolean isNeedCountTime = false;
    boolean isHandRun = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isNeedCountTime = false;
                    //①：其实在这块需要精确计算当前时间
                    for (int index = 0; index < homeGoodsBeanArrayList.size(); index++) {
                        HomeGoodsBean goods = homeGoodsBeanArrayList.get(index);
                        if (goods.isSingle()) {
                            long time = goods.getHomeGoodsChildBeens().get(0).getTime();
                            if (time > 1000) {
                                isNeedCountTime = true;
                                goods.getHomeGoodsChildBeens().get(0).setTime(time - 1000);
                            } else {
                                goods.getHomeGoodsChildBeens().get(0).setFinish(true);
                                goods.getHomeGoodsChildBeens().get(0).setTime(0);
                            }

                        } else {
                            long time = goods.getHomeGoodsChildBeens().get(0).getTime();
                            if (time > 1000) {
                                isNeedCountTime = true;
                                goods.getHomeGoodsChildBeens().get(0).setTime(time - 1000);
                            } else {
                                goods.getHomeGoodsChildBeens().get(0).setFinish(true);
                                goods.getHomeGoodsChildBeens().get(0).setTime(0);
                            }

                            long time_1 = goods.getHomeGoodsChildBeens().get(1).getTime();
                            if (time_1 > 1000) {
                                isNeedCountTime = true;
                                goods.getHomeGoodsChildBeens().get(1).setTime(time_1 - 1000);
                            } else {
                                goods.getHomeGoodsChildBeens().get(1).setFinish(true);
                                goods.getHomeGoodsChildBeens().get(1).setTime(0);
                            }
                        }
                    }
                    myAdapter.notifyDataSetChanged();
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

    private ArrayList<HomeGoodsBean> formatData(ArrayList<HomeGoodsChildBean> resultArray) {
        ArrayList<HomeGoodsBean> homeGoodsBeens = new ArrayList<>();
        if (resultArray.size() > 0) {
            if (resultArray.size() % 2 == 0) {
                ArrayList<HomeGoodsChildBean> homeGoodsChildBeen = new ArrayList<>();
                for (int i = 0; i < resultArray.size(); i++) {
                    resultArray.get(i).setTime(StringFormatUtil.getDaoJiShiTime(resultArray.get(i).getPm_end_time()));
                    homeGoodsChildBeen.add(resultArray.get(i));
                    if ((i + 1) % 2 == 0) {
                        HomeGoodsBean homeGoodsBean = new HomeGoodsBean();
                        ArrayList<HomeGoodsChildBean> items = new ArrayList<>();
                        items.add(homeGoodsChildBeen.get(i - 1));
                        items.add(homeGoodsChildBeen.get(i));
                        homeGoodsBean.setHomeGoodsChildBeens(items);
                        homeGoodsBeens.add(homeGoodsBean);
                    }
                }
            } else {
                ArrayList<HomeGoodsChildBean> homeGoodsChildBeen = new ArrayList<>();
                for (int i = 0; i < resultArray.size(); i++) {
                    resultArray.get(i).setTime(StringFormatUtil.getDaoJiShiTime(resultArray.get(i).getPm_end_time()));
                    homeGoodsChildBeen.add(resultArray.get(i));
                    if ((i + 1) % 2 == 0) {
                        HomeGoodsBean homeGoodsBean = new HomeGoodsBean();
                        ArrayList<HomeGoodsChildBean> items = new ArrayList<>();
                        items.add(homeGoodsChildBeen.get(i - 1));
                        items.add(homeGoodsChildBeen.get(i));
                        homeGoodsBean.setHomeGoodsChildBeens(items);
                        homeGoodsBeens.add(homeGoodsBean);
                    }

                    if (i == resultArray.size() - 1) {
                        HomeGoodsBean homeGoodsBean = new HomeGoodsBean();
                        homeGoodsBean.setSingle(true);
                        ArrayList<HomeGoodsChildBean> items = new ArrayList<>();
                        items.add(homeGoodsChildBeen.get(i));
                        homeGoodsBean.setHomeGoodsChildBeens(items);
                        homeGoodsBeens.add(homeGoodsBean);
                    }
                }
            }
        }
        return homeGoodsBeens;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
