package com.buycolle.aicang.ui.activity.infomation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MyTradeListAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.infomationbean.MyTradeListBean;
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

/**
 * Created by hufeng on 2016/10/9.
 */
public class MyTradeActivity extends BaseActivity {
    @Bind(R.id.my_trade_title)
    MyHeader myTradeTitle;//顶部标题

    @Bind(R.id.list_trade)
    XListView listTrade;//交易信息列表

    @Bind(R.id.info_trade)
    FrameLayout infoDelete;//清空按钮

    @Bind(R.id.ll_notice_no)
    LinearLayout llNotice_no;

    //我的交易列表内容
   ArrayList<MyTradeListBean> myTradeListBeans;

    //我的交易列表适配器
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
        setContentView(R.layout.activity_my_trade);
        ButterKnife.bind(this);
        myTradeTitle.initInfo("我的交易", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        type = _Bundle.getInt("type");
        LoadMyTradeDate(true);
        myTradeListBeans = new ArrayList<MyTradeListBean>();
        myTradeListAdapter = new MyTradeListAdapter(mActivity,mContext,myTradeListBeans,mApplication,type);
        listTrade.setAdapter(myTradeListAdapter);

        listTrade.setXListViewListener(new XListView.IXListViewListener() {
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

        /***
         * 清除按钮的监听
         */
        infoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoticeDialog(mContext,"清空确认","您即将清空该目录下的所有消\n息，消息删除后将无法恢复。\n\n是否确认提交？").setCallBack(new NoticeDialog.CallBack() {
                    @Override
                    public void ok() {
                        llNotice_no.setVisibility(View.VISIBLE);
                        deleteMessages();
                    }

                    @Override
                    public void cancle() {

                    }
                }).show();
            }
        });

    }

    /**
     * 获取我的交易列表接口内容
     */
    private void LoadMyTradeDate(final boolean isloadMore) {
        isRun = true;
        llNotice_no.setVisibility(View.GONE);
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
                return;
            }

            @Override
            public void onApiSuccess(String response) {
                Log.i("resopose--------", response.toString());
                try {
                    JSONObject resultObj = new JSONObject(response);
                    Log.i("resultObj----------", resultObj.toString());
                    if (JSONUtil.isOK(resultObj)) {
                        if (JSONUtil.isHasData(resultObj)) {
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
                                listTrade.isShowFoot(true);
                            } else {
                                hasMoreData = false;
                                listTrade.isShowFoot(false);
                            }
                        } else {
                            llNotice_no.setVisibility(View.VISIBLE);
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
                    listTrade.onRefreshComplete();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }

                if (!isloadMore) {
                    listTrade.onRefreshComplete();
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
            jsonObject.put("type",type);
            jsonObject.put("id",myTradeListBeans.get(0).getId());
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

}
