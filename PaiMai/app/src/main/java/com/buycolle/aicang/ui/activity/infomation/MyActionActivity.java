package com.buycolle.aicang.ui.activity.infomation;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MyActionAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.infomationbean.MyActionListBean;
import com.buycolle.aicang.event.UpdateInfoCenterEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
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
 * Created by hufeng on 2016/10/9.
 */
public class MyActionActivity extends BaseActivity {

    @Bind(R.id.info_activity)
    MyHeader myActivityTitle;//活动信息标题

    @Bind(R.id.ll_notice_no)
    LinearLayout llNoticeNo;//没有活动信息显示的控件

    @Bind(R.id.list_action)
    XListView listAction;//活动列表

    //活动信息列表
    private ArrayList<MyActionListBean> myActionListBeans;

    //活动信息列表适配器
    private MyActionAdapter myActionAdapter;


    //交易类型
    private String type = "";

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;
    private boolean hasMoreData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action);
        ButterKnife.bind(this);
        myActivityTitle.init("活动信息", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
                EventBus.getDefault().post(new UpdateInfoCenterEvent(2));
            }
        });
        LoadActionDate(false);
        myActionListBeans = new ArrayList<>();
        myActionAdapter = new MyActionAdapter(mActivity,mContext,myActionListBeans,mApplication);
        listAction.setAdapter(myActionAdapter);

        listAction.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                LoadActionDate(false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    LoadActionDate(false);
                }
            }
        });

        listAction.setOnScrollListener(new XListView.OnXScrollListener() {
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
                                    LoadActionDate(false);
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

    }


    /**
     * 获取活动列表接口内容
     */
    private void LoadActionDate(final boolean isloadMore) {
        isRun = true;
        llNoticeNo.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       mApplication.apiClient.messageCenter_getEventBannerByApp(jsonObject, new ApiCallback() {
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
                   if (JSONUtil.isOK(resultObj)){
                       if (JSONUtil.isHasData(resultObj)){
                           if (pageIndex == 1) {
                               myActionListBeans.clear();
                           }

                           JSONArray rows = resultObj.getJSONArray("rows");
                           ArrayList<MyActionListBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<MyActionListBean>>() {
                           }.getType());

                           myActionListBeans.addAll(allDataArrayList);
                           myActionAdapter.notifyDataSetChanged();

                           if (JSONUtil.isCanLoadMore(resultObj)) {
                               hasMoreData = true;
                               listAction.isShowFoot(true);
                           } else {
                               hasMoreData = false;
                               listAction.isShowFoot(false);
                           }
                       }else {
                           if (pageIndex == 1) {
                               myActionListBeans.clear();
                           }
                           myActionAdapter.notifyDataSetChanged();
                           hasMoreData = false;
                       }
                   }else {
                       UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

               isRun = false;
               if (!isloadMore){
                   dismissLoadingDialog();
                   listAction.onRefreshComplete();
               }
           }

           @Override
           public void onApiFailure(Request request, Exception e) {
               UIHelper.t(mContext, R.string.net_error);
               if (isloadMore) {
                   pageIndex--;
                   listAction.onRefreshComplete();
               }
               isRun = false;
           }
       });

    }
}
