package com.buycolle.aicang.ui.activity.usercentermenu.faq;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.UpdateTanNoticeEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/4.
 */
public class MyFAQActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_my_ask)
    TextView tvMyAsk;
    @Bind(R.id.tv_my_showdao_ask)
    TextView tvMyShowdaoAsk;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    @Bind(R.id.iv_myask_notice_icon)
    ImageView point_myask;
    @Bind(R.id.iv_shoudao_notice_icon)
    ImageView point_shoudao;
    private ArrayList<TextView> tvArrayList;

    private BaseFragment myAskFrag, myShowDaoFrag;
    private List<BaseFragment> fragList;
    private MainPagerAdapter pagerAdapter;

    private boolean isShowTotal = false;
    private int my_qa_q;
    private int my_qa_a;


    @OnClick(R.id.tv_my_ask)
    public void paiMainIng() {
        initStatus(0);
    }

    @OnClick(R.id.tv_my_showdao_ask)
    public void paiMainFinish() {
        initStatus(1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_faq);
        ButterKnife.bind(this);
        tvArrayList = new ArrayList<>();
        myHeader.init("Q&A", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        fragList = new ArrayList<>();
        my_qa_a = _Bundle.getInt("my_qa_a");
        my_qa_q = _Bundle.getInt("my_qa_q");
        tvArrayList.add(tvMyAsk);
        tvArrayList.add(tvMyShowdaoAsk);
        myAskFrag = new MyAskFragment();
        myShowDaoFrag = new MyShouDaoAskFragment();
        fragList.add(myAskFrag);
        fragList.add(myShowDaoFrag);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragList);
        vpMainContainer.setAdapter(pagerAdapter);
        vpMainContainer.setOffscreenPageLimit(fragList.size() - 1);
        vpMainContainer.setCurrentItem(0);
        initStatus(0);

        //推送跳转
        if (_Bundle != null) {
            if (_Bundle2.getBoolean("isPush")) {
                if (_Bundle.getInt("type") == 15) {
                    initStatus(1);//跳转到我收到的问询
                }

                if (_Bundle.getInt("type") == 16) {
                    initStatus(0);//跳转到我的问询
                }
            }
        }

    }

    private void initStatus(int index) {
        for (int i = 0; i < tvArrayList.size(); i++) {
            if (index == i) {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_orange_black);
                if (i == 0){//表示拍卖中的被选中
                    point_myask.setVisibility(View.GONE);
                    my_qa_q = 0;
                    if (my_qa_a > 0){
                        point_shoudao.setVisibility(View.VISIBLE);
                    }else {
                        point_shoudao.setVisibility(View.GONE);
                    }
                    UpdateRedTipNotice("my_qa_q");
                }else if (i == 1){
                    point_shoudao.setVisibility(View.GONE);
                    my_qa_a = 0;
                    if (my_qa_q > 0){
                        point_myask.setVisibility(View.VISIBLE);
                    }else {
                        point_myask.setVisibility(View.GONE);
                    }
                    UpdateRedTipNotice("my_qa_a");
                }else {
                    if (my_qa_q > 0){
                        point_myask.setVisibility(View.VISIBLE);
                    }else {
                        point_myask.setVisibility(View.GONE);
                    }

                    if (my_qa_a > 0){
                        point_shoudao.setVisibility(View.VISIBLE);
                    }else {
                        point_shoudao.setVisibility(View.GONE);
                    }
                }
            } else {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }

        vpMainContainer.setCurrentItem(index);
        fragList.get(index).refreshByState(0,true);
    }

    private void UpdateRedTipNotice(String clean_tip){
        if(!LoginConfig.isLogin(mContext)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clean_tip",clean_tip);
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_deleteredtipbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        EventBus.getDefault().post(new UpdateTanNoticeEvent(2));
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
