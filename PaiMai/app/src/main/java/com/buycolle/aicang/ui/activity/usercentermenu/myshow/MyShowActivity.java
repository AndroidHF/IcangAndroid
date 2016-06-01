package com.buycolle.aicang.ui.activity.usercentermenu.myshow;

import android.os.Bundle;
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
public class MyShowActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_my_show)
    TextView tvMyShow;
    @Bind(R.id.tv_not_pass)
    TextView tvNotPass;
    @Bind(R.id.tv_caogao)
    TextView tvCaogao;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    private ArrayList<TextView> tvArrayList;

    private BaseFragment myShowFrag, notPassFrag, caoGaoFrag;
    private List<BaseFragment> fragList;
    private MainPagerAdapter pagerAdapter;

    private boolean isShowTotal = false;


    @OnClick(R.id.tv_my_show)
    public void paiMainIng() {
        initStatus(0);
    }

    @OnClick(R.id.tv_not_pass)
    public void paiMainFinish() {
        initStatus(1);
    }

    @OnClick(R.id.tv_caogao)
    public void liuPai() {
        initStatus(2);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshow);
        ButterKnife.bind(this);
        tvArrayList = new ArrayList<>();
        myHeader.init("我的晒物", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        fragList = new ArrayList<>();
        tvArrayList.add(tvMyShow);
        tvArrayList.add(tvNotPass);
        tvArrayList.add(tvCaogao);
        myShowFrag = new MyShowFragment();
        notPassFrag = new ShowNoPassFragment();
        caoGaoFrag = new ShowCaoGaoFragment();
        fragList.add(myShowFrag);
        fragList.add(notPassFrag);
        fragList.add(caoGaoFrag);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragList);
        vpMainContainer.setAdapter(pagerAdapter);
        vpMainContainer.setOffscreenPageLimit(fragList.size() - 1);
        vpMainContainer.setCurrentItem(0);

        setTanNotice();

    }

    private void initStatus(int index) {
        for (int i = 0; i < tvArrayList.size(); i++) {
            if (index == i) {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_orange_black);
            } else {
                tvArrayList.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }
        vpMainContainer.setCurrentItem(index);
    }

    private void setTanNotice() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("my_show", 1);
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_updatetipbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        EventBus.getDefault().post(new UpdateTanNoticeEvent(0));
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
