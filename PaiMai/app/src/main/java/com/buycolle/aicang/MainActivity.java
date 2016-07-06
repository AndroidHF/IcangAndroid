package com.buycolle.aicang;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.MyBuyActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mysale.MySaleActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.fragment.MainFragment;
import com.buycolle.aicang.ui.fragment.PostFragment;
import com.buycolle.aicang.ui.fragment.ShowOffFragment;
import com.buycolle.aicang.ui.fragment.UserCenterFragment;
import com.buycolle.aicang.ui.fragment.event.EventFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.DoubleClickExitHelper;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    @Bind(R.id.vp_main_container)
    FixedViewPager mainViewPager;
    @Bind(R.id.iv_main_menu_1)
    ImageView ivMainMenu1;
    @Bind(R.id.iv_main_menu_2)
    ImageView ivMainMenu2;
    @Bind(R.id.iv_main_menu_3)
    ImageView ivMainMenu3;
    @Bind(R.id.iv_main_menu_4)
    ImageView ivMainMenu4;
    @Bind(R.id.iv_main_menu_5)
    ImageView ivMainMenu5;
    private List<BaseFragment> fragList;
    private BaseFragment homeFrag, eventFrag, postFrag, showFrag, usetFrag;
    private MainPagerAdapter pagerAdapter;

    DoubleClickExitHelper doubleClick;
    private int currentIndex = 0;

    private ArrayList<ImageView> menus;

    private ACache aCache;

    //登录触发
    public void onEventMainThread(LoginEvent event) {
        if (mApplication.isLogin()) {
            mApplication.updatePushId();
        }


    }

    //登出触发
    public void onEventMainThread(LogOutEvent event) {
        menuHomeClick();
        menuEventclick();
        menuPostclick();
        menuShowclick();
        menuUserclick();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentIndex = savedInstanceState.getInt("index", 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", currentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        aCache = ACache.get(this);
        EventBus.getDefault().register(this);
        menus = new ArrayList<>();
        fragList = new ArrayList<>();
        doubleClick = new DoubleClickExitHelper(this);
        menus.add(ivMainMenu1);
        menus.add(ivMainMenu2);
        menus.add(ivMainMenu3);
        menus.add(ivMainMenu4);
        menus.add(ivMainMenu5);
        initViewPager();
        //加载商品状态
        dirtionary_getproductstatuslistbyapp();
        if (mApplication.isLogin()) {
            mApplication.updatePushId();
        }

        if (!FileUtil.isLogoExist()) {
            KLog.e("创建logo报错了", "不存存在");
            PhoneUtil.copyBigDataToSD(mContext, FileUtil.APP_DOWNLOAD_LOGO_PATH + FileUtil.APP_DOWNLOAD_LOGO_NAME);
        }

        if (_Bundle != null && mApplication.isLogin()) {
            if (_Bundle.getBoolean("isPush", false)) {
                if (_Bundle.getInt("type") == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", _Bundle.getInt("id"));
                    UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
                }

                if (_Bundle.getInt("type") == 2 || _Bundle.getInt("type") == 3 || _Bundle.getInt("type") == 8) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isPush", true);
                    bundle.putInt("type", _Bundle.getInt("type"));
                    UIHelper.jump(mActivity, MyBuyActivity.class, bundle);
                }

                if (_Bundle.getInt("type") == 6) {
                    currentIndex = 1;
                    initStatus(1);
                    mainViewPager.setCurrentItem(currentIndex, false);
                }
                if (_Bundle.getInt("type") == 5) {
                    UIHelper.t(mContext, "还有20分钟拍卖会就开始");
                }
                if (_Bundle.getInt("type") == 9 || _Bundle.getInt("type") == 10) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isPush", true);
                    bundle.putInt("type", _Bundle.getInt("type"));
                    UIHelper.jump(mActivity, MySaleActivity.class, bundle);
                }

            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_main_menu_1)
    public void menuHomeClick() {
        currentIndex = 0;
        initStatus(0);
        mainViewPager.setCurrentItem(currentIndex, false);
        homeFrag.refreshByState(0);
    }

    @OnClick(R.id.iv_main_menu_2)
    public void menuEventclick() {
        currentIndex = 1;
        initStatus(1);
        mainViewPager.setCurrentItem(currentIndex, false);
        eventFrag.refreshByState(0);
    }

    @OnClick(R.id.iv_main_menu_3)
    public void menuPostclick() {
        currentIndex = 2;
        initStatus(2);
        mainViewPager.setCurrentItem(currentIndex, false);
        postFrag.refreshByState(0);
    }

    @OnClick(R.id.iv_main_menu_4)
    public void menuShowclick() {
        currentIndex = 3;
        initStatus(3);
        mainViewPager.setCurrentItem(currentIndex, false);
        showFrag.refreshByState(0);
    }

    @OnClick(R.id.iv_main_menu_5)
    public void menuUserclick() {
        currentIndex = 4;
        initStatus(4);
        mainViewPager.setCurrentItem(currentIndex, false);
        usetFrag.refreshByState(0);
    }

    private void initStatus(int index) {
        int[] defaut = new int[]{R.drawable.main_menu_1, R.drawable.main_menu_2, R.drawable.main_menu_3, R.drawable.main_menu_4, R.drawable.main_menu_5};
        int[] select = new int[]{R.drawable.main_menu_1_sel, R.drawable.main_menu_2_sel, R.drawable.main_menu_3_sel, R.drawable.main_menu_4_sel, R.drawable.main_menu_5_sel};
        for (int i = 0; i < 5; i++) {
            if (i == index) {
                menus.get(i).setImageResource(select[i]);
            } else {
                menus.get(i).setImageResource(defaut[i]);
            }
        }
    }


    private void initViewPager() {
        fragList = new ArrayList<BaseFragment>();
        homeFrag = new MainFragment();
        eventFrag = new EventFragment();
        postFrag = new PostFragment();
        showFrag = new ShowOffFragment();
        usetFrag = new UserCenterFragment();
        fragList.add(homeFrag);
        fragList.add(eventFrag);
        fragList.add(postFrag);
        fragList.add(showFrag);
        fragList.add(usetFrag);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragList);
        mainViewPager.setAdapter(pagerAdapter);
        mainViewPager.setOffscreenPageLimit(fragList.size() - 1);
        mainViewPager.setCurrentItem(currentIndex);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return doubleClick.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

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
}
