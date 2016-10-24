package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.InfoCentAdapterNew;
import com.buycolle.aicang.adapter.InfoNoticeAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.infomationbean.InfoNoticeBean;
import com.buycolle.aicang.bean.infomationbean.MessageCenterHomeBean;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.event.UpdateInfoAnimationEvent;
import com.buycolle.aicang.event.UpdateInfoCenterEvent;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.xlistview.XListView;
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
import de.greenrobot.event.EventBus;

/**
 * Created by hufeng on 2016/10/8.
 */
public class InfoCenterActivity extends BaseActivity {
    @Bind(R.id.info_title)
    MyHeader infoTitle;//消息中心顶部标题
    @Bind(R.id.iv_info)
    ImageView ivInfo;//消息框
    @Bind(R.id.iv_notice)
    ImageView ivNotice;//公告框
    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;//用于切换消息和公告的控件
    @Bind(R.id.list_info)
    ListView listInfo;//消息列表
    @Bind(R.id.list_notice)
    XListView listNotice;//公告列表


    //消息列表内容
    ArrayList<MessageCenterHomeBean> messageCenterHomeBeans;
    //消息列表适配器
    InfoCentAdapterNew infoCentAdapter;
    //公告内容
    ArrayList<InfoNoticeBean> infoNoticeBeans;
    //公告适配器
    InfoNoticeAdapter infoNoticeAdapter;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;
    private boolean hasMoreData = false;

    private String  business_id ;//交易信息id
    private String common_id ;//交流信息id
    private String other_id ;//其他信息id

    //登录触发
    public void onEventMainThread(LoginEvent event) {
        if (mApplication.isLogin()) {
           LoadInfoData();
            //listInfo.setAdapter(infoCentAdapter);
        }
    }

    //我的交易、我的私信、活动信息，系统消息界面返回触发
    public void onEventMainThread(UpdateInfoCenterEvent event){
        if (event.getStatus() == 0 || event.getStatus() == 1||event.getStatus() == 2 || event.getStatus() == 3){
            LoadInfoData();
            //listInfo.setAdapter(infoCentAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_center);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        loadData(false);
        LoadInfoData();
        infoNoticeBeans = new ArrayList<>();
        messageCenterHomeBeans = new ArrayList<>();
        infoNoticeAdapter = new InfoNoticeAdapter(mActivity,mContext,infoNoticeBeans);
        infoCentAdapter = new InfoCentAdapterNew(mActivity,mContext,messageCenterHomeBeans,mApplication);
        //初始化消息中心顶部
        infoTitle.init("消息中心", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
                EventBus.getDefault().post(new UpdateInfoAnimationEvent(0));
            }
        });

        //设置消息框的监听
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivInfo.setImageResource(R.drawable.info_pic_select);
                ivNotice.setImageResource(R.drawable.notice_pic);
                viewFlipper.setDisplayedChild(0);//加载消息列表
            }
        });

        //设置公告框的监听
        ivNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivInfo.setImageResource(R.drawable.info_pic);
                ivNotice.setImageResource(R.drawable.notice_pic_select);
                viewFlipper.setDisplayedChild(1);//加载公告列表界面
            }
        });

        listInfo.setAdapter(infoCentAdapter);
        listNotice.setAdapter(infoNoticeAdapter);
        viewFlipper.setDisplayedChild(0);
        listNotice.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                loadData(false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(false);
                }
            }
        });

        listNotice.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (hasMoreData) {
                                if (!isRun) {
                                    pageIndex++;
                                    loadData(false);
                                }
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

//
    }


    /**
     * 获取公告信息
     * @param isloadMore
     */
    private void loadData(final boolean isloadMore) {
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.publicnotice_getlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore){
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (JSONUtil.isHasData(resultObj)) {
                            if (pageIndex == 1) {
                                infoNoticeBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            ArrayList<InfoNoticeBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<InfoNoticeBean>>() {
                            }.getType());
                            infoNoticeBeans.addAll(allDataArrayList);
                            Log.i("公告信息列表",infoNoticeBeans.toString());
                            infoNoticeAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                hasMoreData = true;
                                listNotice.isShowFoot(true);
                            } else {
                                hasMoreData = false;
                                listNotice.isShowFoot(false);
                            }
                        } else {
                            if (pageIndex == 1) {
                                infoNoticeBeans.clear();
                            }
                            infoNoticeAdapter.notifyDataSetChanged();
                            hasMoreData = false;
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isloadMore){
                    dismissLoadingDialog();
                    listNotice.onRefreshComplete();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }

                if (!isloadMore){
                    listNotice.onRefreshComplete();
                }

                isRun = false;
            }
        });

    }

    /***
     * 获取消息首页信息
     */
    private void LoadInfoData(){
        JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("user_id",LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("business_id","");
                jsonObject.put("common_id","");
                jsonObject.put("other_id","");

                mApplication.apiClient.messageCenter_getNewMessagesByApp(jsonObject, new ApiCallback() {
                    @Override
                    public void onApiStart() {
                        return;
                    }

                    @Override
                    public void onApiSuccess(String response) {
                        try {
                            JSONObject resultObj = new JSONObject(response);
                            if (JSONUtil.isOK(resultObj)) {
                                if (JSONUtil.isHasData(resultObj)) {
                                    if (pageIndex == 1) {
                                        messageCenterHomeBeans.clear();
                                    }
                                    JSONArray rows = resultObj.getJSONArray("rows");
                                    ArrayList<MessageCenterHomeBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MessageCenterHomeBean>>() {
                                    }.getType());
                                    messageCenterHomeBeans.addAll(allDataArrayList);
                                    infoCentAdapter.notifyDataSetChanged();
                                } else {
                                    if (pageIndex == 1) {
                                        messageCenterHomeBeans.clear();
                                    }
                                    infoCentAdapter.notifyDataSetChanged();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }
}
