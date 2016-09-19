package com.buycolle.aicang.ui.view.mainScrole;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.HomeGoodsBean;
import com.buycolle.aicang.bean.HomeGoodsChildBean;
import com.buycolle.aicang.event.ChuJiaEvent;
import com.buycolle.aicang.event.HomeBackEvent;
import com.buycolle.aicang.ui.view.xlistview.XListView;
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

import de.greenrobot.event.EventBus;

/**
 * 首页列表数据
 */
public class ListFragment extends ScrollAbleFragment implements ScrollableHelper.ScrollableContainer {

    private XListView mListview;
    List<String>  strlist;
    MyHomeAdapter myAdapter;
    ImageButton   ib_float_btn;
    TextView      tv_null;
    public String index = "0";//全部

    private boolean mIsFilter;

    private ArrayList<HomeGoodsBean> homeGoodsBeanArrayList;
    private boolean isRun = false;

    private boolean mIsFirst = false;

    public static ListFragment newInstance(String index) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position", index);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getString("position");
        homeGoodsBeanArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListview = (XListView) view.findViewById(R.id.listview);
        ib_float_btn = (ImageButton) view.findViewById(R.id.ib_float_btn);
        tv_null = (TextView) view.findViewById(R.id.tv_null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        myAdapter = new MyHomeAdapter(mActivity, homeGoodsBeanArrayList);
        mListview.setAdapter(myAdapter);
        mListview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                loadData(index, false, false);
                mListview.setPullRefreshEnable(true);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(index, true, false);
                }
            }
        });
        mListview.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                if (firstVisibleItem > Constans.MAX_SHOW_FLOAT_BTN) {
                    ib_float_btn.setVisibility(View.VISIBLE);
                } else {
                    ib_float_btn.setVisibility(View.GONE);
                }

            }
        });
        ib_float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new HomeBackEvent(0));
                mListview.setSelection(0);
            }
        });
        loadData(index, false, false);
    }

    public boolean isFirst() {
        if (!mIsFirst) {
            mIsFirst = true;
            return mIsFirst;
        }
        return false;
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
            loadData(index, false, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private int pageIndex = 1;
    private int pageNum   = 20;

    public void filterData(String cate_id,
                            String filter_id,
                            String sort_id,
                            final boolean isLoadMore,
                            final boolean isAction) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            if (!"0".equals(cate_id)) {
                jsonObject.put("cate_id", cate_id);// 1 - 9 写死
            }
            jsonObject.put("filter_id", filter_id);
            jsonObject.put("sort_id", sort_id);
            jsonObject.put("page", 1);
            jsonObject.put("rows", 50);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_filter_list_by_app(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore && !isAction) {
                    tv_null.setVisibility(View.GONE);
                    showLoadingDialog();
                } else {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded()) return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        ArrayList<HomeGoodsChildBean> resultArray = new Gson().fromJson(jsonArray.toString(),
                                                                                        new TypeToken<List<HomeGoodsChildBean>>() {
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
                            mListview.isShowFoot(true);
                        } else {
                            mListview.isShowFoot(false);
                        }

                        if (pageIndex == 1 && resultArray.size() == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                        } else {
                            tv_null.setVisibility(View.GONE);
                        }

                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isLoadMore && !isAction) {
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
                if (!isLoadMore) {
                    mListview.onRefreshComplete();
                }

            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded()) return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
                if (isLoadMore) {
                    pageIndex--;
                }
                if (!isLoadMore) {
                    mListview.onRefreshComplete();
                }
                isRun = false;
            }
        });

    }

    private void loadData(String cate_id, final boolean isLoadMore, final boolean isAction) {
        tv_null.setVisibility(View.GONE);
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            if (!"0".equals(cate_id)) {
                jsonObject.put("cate_id", cate_id);// 1 - 9 写死
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
        mApplication.apiClient.product_filter_list_by_app(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isLoadMore && !isAction) {
                    tv_null.setVisibility(View.GONE);
                    showLoadingDialog();
                } else {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (!isAdded()) return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        ArrayList<HomeGoodsChildBean> resultArray = new Gson().fromJson(jsonArray.toString(),
                                                                                        new TypeToken<List<HomeGoodsChildBean>>() {
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
                            mListview.isShowFoot(true);
                        } else {
                            mListview.isShowFoot(false);
                        }

                        if (pageIndex == 1 && resultArray.size() == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                        } else {
                            tv_null.setVisibility(View.GONE);
                        }

                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isLoadMore && !isAction) {
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
                if (!isLoadMore) {
                    mListview.onRefreshComplete();
                }

            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (!isAdded()) return;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
                if (isLoadMore) {
                    pageIndex--;
                }
                if (!isLoadMore) {
                    mListview.onRefreshComplete();
                }
                isRun = false;
            }
        });

    }

    boolean isNeedCountTime = false;
    boolean isHandRun       = false;
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

        KLog.d("自己设置的时间", "----");

        ArrayList<HomeGoodsBean> homeGoodsBeens = new ArrayList<>();
        if (resultArray.size() > 0) {
            if (resultArray.size() % 2 == 0) {
                ArrayList<HomeGoodsChildBean> homeGoodsChildBeen = new ArrayList<>();
                for (int i = 0; i < resultArray.size(); i++) {
                    KLog.d("自己设置的时间", "----");

                    resultArray.get(i).setTime(resultArray.get(i).getPm_end_remain_second() * 1000);
                    KLog.d("自己设置的时间", "----" + resultArray.get(i).getTime());
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
                    //                    resultArray.get(i).setTime(StringFormatUtil.getDaoJiShiTime(resultArray.get
                    // (i).getPm_end_time()));
                    resultArray.get(i).setTime(resultArray.get(i).getPm_end_remain_second() * 1000);
                    KLog.d("自己设置的时间", "----" + resultArray.get(i).getTime());

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
    public View getScrollableView() {
        return mListview;
    }

    @Override
    public void refresh(int status) {
        if (status == 0) {//首页的刷新数据
            pageIndex = 1;
            loadData(index, false, false);
        }
    }

    /**
     * 用户类别的更新
     *
     * @param state
     */
    @Override
    public void refreshByState(int state) {
        super.refreshByState(state);
        index = state + "";
        pageIndex = 1;
        loadData(index, false, false);
    }

    public void refreshByState(int state, boolean isAction) {
        super.refreshByState(state);
        index = state + "";
        pageIndex = 1;
        loadData(index, false, isAction);
    }
}
