package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeGoodsBean;
import com.buycolle.aicang.bean.HomeGoodsChildBean;
import com.buycolle.aicang.bean.SallerUserInfoBean;
import com.buycolle.aicang.event.ChuJiaEvent;
import com.buycolle.aicang.ui.activity.comment.MainComentActivity;
import com.buycolle.aicang.ui.view.mainScrole.MyHomeAdapter;
import com.buycolle.aicang.ui.view.xlistview.XListView;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/10.
 */
public class MainUserCenterActivity extends BaseActivity {

    @Bind(R.id.fl_back)
    FrameLayout flBack;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.tv_kuaidi_username)
    TextView tvKuaidiUsername;
    @Bind(R.id.tv_kuaidi_user_comment)
    TextView tvKuaidiUserComment;
    @Bind(R.id.tv_kuaidi_user_comment_good)
    TextView tvKuaidiUserCommentGood;
    @Bind(R.id.tv_kuaidi_user_comment_bad)
    TextView tvKuaidiUserCommentBad;
    @Bind(R.id.ll_user_center)
    LinearLayout llUserCenter;
    @Bind(R.id.tv_ta_chupin)
    TextView tvTaChupin;
    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;

    @Bind(R.id.ll_all_comment_lay)
    LinearLayout llAllCommentLay;
    @Bind(R.id.ll_good_comment_lay)
    LinearLayout llGoodCommentLay;
    @Bind(R.id.ll_bad_comment_lay)
    LinearLayout llBadCommentLay;
    @Bind(R.id.iv_user_status)
    ImageView ivUserStatus;
    @Bind(R.id.tv_user_desc)
    TextView tvUserDesc;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;

    private int userid;

    private boolean isInitUserInfo = false;

    MyHomeAdapter myAdapter;


    private ArrayList<HomeGoodsBean> homeGoodsBeanArrayList;
    private boolean isRun = false;

    private int pageIndex = 1;
    private int pageNum = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usercenter);
        ButterKnife.bind(this);
        homeGoodsBeanArrayList = new ArrayList<>();
        userid = _Bundle.getInt("userid");
        initListener();
        myAdapter = new MyHomeAdapter(mActivity, homeGoodsBeanArrayList);
        list.setAdapter(myAdapter);

        llAllCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("currentIndex", 0);
                bundle.putInt("userid", userid);
                UIHelper.jump(mActivity, MainComentActivity.class, bundle);
            }
        });
        llGoodCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("currentIndex", 1);
                bundle.putInt("userid", userid);
                UIHelper.jump(mActivity, MainComentActivity.class, bundle);
            }
        });
        llBadCommentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("currentIndex", 2);
                bundle.putInt("userid", userid);
                UIHelper.jump(mActivity, MainComentActivity.class, bundle);
            }
        });

        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex=1;
                loadData(false,false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(true,false);
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

        loadData(false,false);

    }

    public void onEventMainThread(ChuJiaEvent event) {
        boolean has = false;
        for (HomeGoodsBean homeGoodsBean : homeGoodsBeanArrayList) {
            for (HomeGoodsChildBean homeGoodsChildBean : homeGoodsBean.getHomeGoodsChildBeens()) {
                if (homeGoodsChildBean.getProduct_id() == event.getStatus()) {
                    has = true;
                    break;
                }
            }
        }
        if (has) {
            pageIndex = 1;
            loadData(false, true);
        }
    }

    SallerUserInfoBean userInfo;


    private void initUserView() {
        //change by :胡峰，头像的处理
        mApplication.setTouImages(userInfo.getUser_avatar(),profileImage);
        tvKuaidiUsername.setText(userInfo.getUser_nick());
        tvKuaidiUserComment.setText(userInfo.getTotal_comment() + "");
        tvKuaidiUserCommentGood.setText(userInfo.getGood_comment() + "");
        tvKuaidiUserCommentBad.setText(userInfo.getBad_comment() + "");
        if (userInfo.getPerson_tip() == null) {
            tvUserDesc.setText("这个用户很懒，什么也没有留下");
        } else {
            if (TextUtils.isEmpty(userInfo.getPerson_tip())) {
                tvUserDesc.setText("这个用户很懒，什么也没有留下");
            } else {
                tvUserDesc.setText(userInfo.getPerson_tip());
            }
        }
        if (userInfo.getCol_id() > 0) {
            ivUserStatus.setImageResource(R.drawable.commen_store_red);
        } else {
            ivUserStatus.setImageResource(R.drawable.commen_store_black);
        }
        ivUserStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeActionFlag) {
                    likeActionFlag = false;
                    likeUnlike(userInfo);
                } else {
                    if (likeActionShowToUserFlag) {
                        likeActionShowToUserFlag = false;
                        UIHelper.t(mContext, R.string.comment_action_more);
                    }
                }
            }
        });
    }

    private boolean likeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean likeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void likeUnlike(final SallerUserInfoBean sallerUserInfoBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (sallerUserInfoBean.getCol_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", userid);
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colseller(jsonObject, new ApiCallback() {
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
                            if (sallerUserInfoBean.getCol_id() > 0) {
                                sallerUserInfoBean.setCol_id(0);
                                UIHelper.t(mContext,"取消关注成功");
                            } else {
                                sallerUserInfoBean.setCol_id(1);
                                UIHelper.t(mContext,"关注成功");
                            }
                            if (sallerUserInfoBean.getCol_id() > 0) {
                                ivUserStatus.setImageResource(R.drawable.commen_store_red);
                            } else {
                                ivUserStatus.setImageResource(R.drawable.commen_store_black);
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


    private void initListener() {
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Myholder myholder = null;
            if (convertView == null) {
                myholder = new Myholder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_home_main, null);
                myholder.iv_1 = (ImageView) convertView.findViewById(R.id.iv_1);
                myholder.ll_item_1 = (LinearLayout) convertView.findViewById(R.id.ll_item_1);
                myholder.ll_item_2 = (LinearLayout) convertView.findViewById(R.id.ll_item_2);
                convertView.setTag(myholder);
            } else {
                myholder = (Myholder) convertView.getTag();

            }
            MainApplication.getInstance().setImages(Constans.canglaoshiPath, myholder.iv_1);
            myholder.ll_item_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.jump(mContext, PaiPinDetailActivity.class);
                }
            });
            myholder.ll_item_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.jump(mContext, PaiPinDetailActivity.class);
                }
            });
            return convertView;
        }

        public class Myholder {

            ImageView iv_1;
            LinearLayout ll_item_1;
            LinearLayout ll_item_2;
        }
    }


    private void loadData(final boolean isLoadMore,final boolean isAction) {
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userid);
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getcursellerpushlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore&&!isAction) {
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
                        if (isInitUserInfo) {
                            JSONArray jsonArray = resultObj.getJSONArray("rows");
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
                            JSONObject userInfoObj = resultObj.getJSONObject("sellerUserInfos");
                            userInfo = new Gson().fromJson(userInfoObj.toString(), SallerUserInfoBean.class);
                            initUserView();
                            JSONArray jsonArray = resultObj.getJSONArray("rows");
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
                if(pageIndex==1){
                    list.onRefreshComplete();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                if(pageIndex==1){
                    list.onRefreshComplete();
                }
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
}
