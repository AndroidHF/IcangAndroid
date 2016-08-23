package com.buycolle.aicang.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.SubjectPagerAdapter;
import com.buycolle.aicang.bean.CityModel;
import com.buycolle.aicang.bean.DistrictModel;
import com.buycolle.aicang.bean.ProvinceModel;
import com.buycolle.aicang.ui.fragment.SubjectFragment;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;
import com.buycolle.aicang.ui.view.mainScrole.ScrollableLayout;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.XmlParserHandler;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by joe on 16/3/2.
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;
    protected Context mContext;
    protected ProgressDialog mProgressDialog;
    private MyHandler mHandler = null;
    protected MainApplication mApplication;
    protected View statusView = null;
    public FragmentManager mFragmentManager;
    protected Bundle _Bundle;
    protected Bundle _Bundle2;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();


    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();


    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前地区的名称
     */
    protected String mCurrentDistrictName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        mApplication = (MainApplication) getApplication();
        mApplication.addActivity(this);
        mFragmentManager = getSupportFragmentManager();
        _Bundle = getIntent().getExtras();
        _Bundle2 = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //针对Android 4.4到5.X之间设置statusbar背景色
        int versionCode = Build.VERSION.SDK_INT;
        if (versionCode >= Build.VERSION_CODES.KITKAT && versionCode < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) this.findViewById(android.R.id.content);
            statusView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(this));
            statusView.setBackgroundColor(getResources().getColor(R.color.black));
            contentView.addView(statusView, lp);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    protected boolean showLoadingDialog(String str) {
        if (!PhoneUtil.isNetworkAvailable(this)) {
//            Toast.makeText(this, "网络异常，请检查网络!", Toast.LENGTH_SHORT).initDialog();
            return false;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(str);
        mProgressDialog.show();
        return true;
    }

    protected boolean showLoadingDialog() {
        if (!PhoneUtil.isNetworkAvailable(this)) {
//            Toast.makeText(this, "网络异常，请检查网络!", Toast.LENGTH_SHORT).initDialog();
            return false;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        return true;
    }

    protected void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            if (mHandler == null) {
                mHandler = new MyHandler(this);
            }
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (mHandler != null)
            mHandler.clear();
        super.onDestroy();
    }

    static class MyHandler extends Handler {
        private WeakReference<BaseActivity> weakReference;

        public MyHandler(BaseActivity activity) {
            weakReference = new WeakReference<BaseActivity>(activity);
        }

        public void clear() {
            weakReference.clear();
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity mActivity = weakReference.get();
            if (mActivity != null) {
                mActivity.mProgressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mActivity.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();

                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }

                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

    }

    NoticeSingleDialog baseNotice;
    public void showNotice(String title, String content,String sure) {
        baseNotice = new NoticeSingleDialog(mContext, title, content, sure);
        baseNotice.setCallBack(new NoticeSingleDialog.CallBack() {
            @Override
            public void ok() {
            }

            @Override
            public void cancle() {

            }
        }).show();
    }
    protected void gotoLogin() {
        UIHelper.jump(this, LoginActivity.class);
    }

    final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
    protected ScrollAbleFragment subjectFragment;
    public void initSubFragmentPager( ViewPager viewPager,ScrollableLayout mScrollLayout,int index) {
        subjectFragment = SubjectFragment.newInstance(index);
        fragmentList.add(subjectFragment);
        viewPager.setAdapter(new SubjectPagerAdapter(getSupportFragmentManager(), fragmentList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        viewPager.setCurrentItem(0);
    }

}