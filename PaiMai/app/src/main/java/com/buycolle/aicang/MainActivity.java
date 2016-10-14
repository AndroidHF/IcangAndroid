package com.buycolle.aicang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.buycolle.aicang.adapter.MainPagerAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.UpdateInfo;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.MyBuyActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mysale.MySaleActivity;
import com.buycolle.aicang.ui.fragment.BaseFragment;
import com.buycolle.aicang.ui.fragment.MainNewFragment;
import com.buycolle.aicang.ui.fragment.PostFragment;
import com.buycolle.aicang.ui.fragment.ShowOffFragment;
import com.buycolle.aicang.ui.fragment.UserCenterFragment;
import com.buycolle.aicang.ui.fragment.event.EventFragment;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.UpdateDialog;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.DoubleClickExitHelper;
import com.buycolle.aicang.util.DownLoadManager;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.ToastUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.UpdataInfoParser;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.squareup.okhttp.Request;
import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private UserBean userBean;

    private final String TAG = this.getClass().getName();
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    private UpdateInfo info;
    private String localVersion;

    //登录触发
    public void onEventMainThread(LoginEvent event) {
        if (mApplication.isLogin()) {
            mApplication.updatePushId();
        }


    }

    //登出触发
    public void onEventMainThread(LogOutEvent event) {
        menuHomeClick();
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
        TestinAgent.init(this,"1e0bc70c1b8bbccb62730c990d39b817","荟玩崩溃测试");
        TestinAgentConfig config = new TestinAgentConfig.Builder(mContext)
                .withAppKey("1e0bc70c1b8bbccb62730c990d39b817")            // Appkey of your appliation, required
                .withDebugModel(true)       // Output the crash log in local if you open debug mode
                .withErrorActivity(true)    // Output the activity info in crash or error log
                .withCollectNDKCrash(true)  // Collect NDK crash or not if you use our NDK
                .withOpenCrash(true)        // Monitor crash if true
                .withReportOnlyWifi(true)   // Report data only on wifi mode
                .withReportOnBack(true)     // allow to report data when application in background
                .build();
        TestinAgent.init(config);
        TestinAgent.setLocalDebug(true);//设置为true，则在log中打印崩溃堆栈
        try {
            localVersion = getVersionName();
            Log.i("versionname",localVersion);
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (_Bundle.getInt("type") == 9 || _Bundle.getInt("type") == 10) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isPush", true);
                    bundle.putInt("type", _Bundle.getInt("type"));
                    UIHelper.jump(mActivity, MySaleActivity.class, bundle);
                }

                if (_Bundle.getInt("type") == 11){
                    currentIndex = 2;
                    initStatus(2);
                    mainViewPager.setCurrentItem(currentIndex, false);
                }

                if (_Bundle.getInt("type") == 12){
                    currentIndex = 0;
                    initStatus(0);
                    mainViewPager.setCurrentItem(currentIndex, false);
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
    }

    @OnClick(R.id.iv_main_menu_4)
    public void menuShowclick() {
        currentIndex = 3;
        initStatus(3);
        mainViewPager.setCurrentItem(currentIndex, false);
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
        //homeFrag = new MainFragment();
        //homeFrag = new MainFragmentNew();
        homeFrag = new MainNewFragment();
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
        //mainViewPager.setIsScrollabe(true);
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

    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        Log.i("name",getPackageName());
        return packInfo.versionName;
    }

    public class CheckVersionTask implements Runnable {
        InputStream is;
        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                    Log.i("hufeng", is.toString());
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                Log.i("info", info.toString());
                if (info.getVersion().compareTo(localVersion) > 0) {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                    // LoginMain();
                } else {
                    Log.i("info版本号",info.getVersion());
                    Log.i("local版本",localVersion);
                    Log.i(TAG, "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATA_NONEED;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
//                    Toast.makeText(getApplicationContext(), "当前已经是最新版本，不需要更新",
//                            Toast.LENGTH_SHORT).initDialog();
                    break;
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    //Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
                    Toast toast = Toast.makeText(getApplicationContext(), "                     怂那！更新失败了！\n" + "您可以尝试从官网或各大安卓市场下载更新。", Toast.LENGTH_LONG);
                    ToastUtil.showMyToast(toast, 3000);
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    //Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    Toast toast1 = Toast.makeText(getApplicationContext(), "                    怂那！更新失败了！\n" + "您可以尝试从官网或各大安卓市场下载更新。", Toast.LENGTH_LONG);
                    ToastUtil.showMyToast(toast1, 3000);
                    break;
            }
        }
    };

    /*
	 *
	 * 弹出对话框通知用户更新程序
	 *
	 * 弹出对话框的步骤：
	 *  1.创建alertDialog的builder.
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮
	 *  3.通过builder 创建一个对话框
	 *  4.对话框show()出来
	 */
    protected void showUpdataDialog() {
        new UpdateDialog(mContext,"版本升级",info.getContext(),info.getVersion()).setCallBack(new UpdateDialog.CallBack() {
            @Override
            public void ok() {
                downLoadApk();
            }

            @Override
            public void cancle() {

            }
        }).show();
    }


    /*
	 * 从服务器中下载APK
	 */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    Log.i("url2", info.getUrl());
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }


    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
