package com.buycolle.aicang.ui.activity.infomation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MyTradeListAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.infomationbean.MyTradeListBean;
import com.buycolle.aicang.event.UpdateInfoCenterEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeDialog;
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
 * Created by hufeng on 2016/10/9.
 */
public class MyDirectActivity extends BaseActivity {

    @Bind(R.id.info_direct)
    MyHeader myDirectTitle;//我的私信顶部标题
    @Bind(R.id.info_trade)
    FrameLayout InfoDelete;//清空按钮
    @Bind(R.id.ll_notice_no)
    LinearLayout llNoticeno;//没有消息显示的控件
    @Bind(R.id.list_direct)
    XListView listDirect;//我的私信列表

    //存放我的私信内容的列表
    ArrayList<MyTradeListBean> myTradeListBeans;
    //我的私信的适配器
    MyTradeListAdapter myTradeListAdapter;

    //交易类型
    private int type ;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;
    private boolean hasMoreData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_direct);
        ButterKnife.bind(this);
        myDirectTitle.initInfo("我的私信", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
                EventBus.getDefault().post(new UpdateInfoCenterEvent(1));
            }
        });

        type = _Bundle.getInt("type");
        LoadMyTradeDate(true);
        myTradeListBeans = new ArrayList<MyTradeListBean>();
        myTradeListAdapter = new MyTradeListAdapter(mActivity,mContext,myTradeListBeans,mApplication,type);
        listDirect.setAdapter(myTradeListAdapter);

        listDirect.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                LoadMyTradeDate(false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    LoadMyTradeDate(false);
                }
            }
        });

        //一键已读和一键删除的点击事件
        InfoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewInfoDelete) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.menu_poup_window, null);
                TextView tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
                TextView tvDelete = (TextView) view.findViewById(R.id.tv_delete);
                final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                /**
                 * 一键已读点击事件
                 */
                tvEmpty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateReadMessages();
                        LoadMyTradeDate(false);
                        listDirect.setAdapter(myTradeListAdapter);
                        popupWindow.dismiss();
                    }
                });

                /**
                 * 一键删除监听事件
                 */
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NoticeDialog(mContext, "清空确认", "您即将清空该目录下的所有消\n息，消息删除后将无法恢复。\n\n是否确认提交？").setCallBack(new NoticeDialog.CallBack() {
                            @Override
                            public void ok() {
                                llNoticeno.setVisibility(View.VISIBLE);
                                deleteMessages();
                                popupWindow.dismiss();
                            }

                            @Override
                            public void cancle() {
                                popupWindow.dismiss();
                            }
                        }).show();
                    }
                });

                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_info_bg));

                popupWindow.showAsDropDown(viewInfoDelete);
            }
        });
    }

    /**
     * 获取我的交易列表接口内容
     */
    private void LoadMyTradeDate(final boolean isloadMore) {
        isRun = true;
        llNoticeno.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("business_id", "");
            jsonObject.put("common_id", "");
            jsonObject.put("other_id", "");
            jsonObject.put("type", type);
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.messageCenter_getMessageListByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                Log.i("resopose--------", response.toString());
                try {
                    JSONObject resultObj = new JSONObject(response);
                    Log.i("resultObj----------", resultObj.toString());
                    if (JSONUtil.isOK(resultObj)) {
                        if (!resultObj.getString("cur_page").equals("0")) {
                            if (pageIndex == 1) {
                                myTradeListBeans.clear();
                            }
                            JSONArray rows = resultObj.getJSONArray("rows");
                            Log.i("rows---", rows.toString());
                            ArrayList<MyTradeListBean> allDateList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyTradeListBean>>() {
                            }.getType());
                            Log.i("allDateList----", allDateList.toString());
                            myTradeListBeans.addAll(allDateList);
                            Log.i("myTradeList-----", myTradeListBeans.toString());
                            myTradeListAdapter.notifyDataSetChanged();
                            if (JSONUtil.isCanLoadMore(resultObj)) {
                                hasMoreData = true;
                                listDirect.isShowFoot(true);
                            } else {
                                hasMoreData = false;
                                listDirect.isShowFoot(false);
                            }
                        } else {
                            llNoticeno.setVisibility(View.VISIBLE);
                            if (pageIndex == 1) {
                                myTradeListBeans.clear();
                            }
                            myTradeListAdapter.notifyDataSetChanged();
                            hasMoreData = false;
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isRun = false;
                if (!isloadMore) {
                    dismissLoadingDialog();
                    listDirect.onRefreshComplete();
                }

            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }

                if (!isloadMore) {
                    listDirect.onRefreshComplete();
                }
                isRun = false;
            }
        });

    }

    /**
     * 清空消息中心的消息列表
     */
    public void deleteMessages(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid",LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id",LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("type", type);
            if (myTradeListBeans.size() != 0){
                jsonObject.put("max_id",myTradeListBeans.get(0).getId());
                jsonObject.put("min_id",myTradeListBeans.get(myTradeListBeans.size() - 1).getId());
            }else {
                jsonObject.put("max_id","");
                jsonObject.put("min_id","");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.messageCenter_deleteMessagesByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {

            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
    }

    /***
     * 消息中心列表一键已读接口
     */
    public void updateReadMessages(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid",LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id",LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("type", type);
            if (myTradeListBeans.size() != 0){
                jsonObject.put("max_id", myTradeListBeans.get(0).getId());
                jsonObject.put("min_id",myTradeListBeans.get(myTradeListBeans.size() - 1).getId());
            }else {
                jsonObject.put("max_id","");
                jsonObject.put("min_id","");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.messageCenter_updateMessagesReadByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {

            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
    }
}
