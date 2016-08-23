package com.buycolle.aicang.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.bean.CityModel;
import com.buycolle.aicang.bean.DistrictModel;
import com.buycolle.aicang.bean.ProvinceModel;
import com.buycolle.aicang.util.PhoneUtil;
import com.buycolle.aicang.util.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by joe on 16/3/2.
 */
public class BaseFragment extends Fragment {


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
     * 当前地区的名称
     */
    protected String mCurrentDistrictName;

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    protected Activity mActivity;
    protected Fragment mFragment;
    protected MainApplication mApplication;
    protected Context mContext;
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragment = this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = getActivity();
        mApplication = (MainApplication) getActivity().getApplication();
    }

    public void refreshByState(int state) {
    }
    public void refreshByState(int state,boolean isAction) {

    }

    public boolean showLoadingDialog(String str) {
        if (!PhoneUtil.isNetworkAvailable(mActivity)) {
//            Toast.makeText(mActivity, "网络异常，请检查网络!", Toast.LENGTH_SHORT).initDialog();
            return false;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
        }
        mProgressDialog.setMessage(str);
        mProgressDialog.show();
        return true;
    }

    public boolean showLoadingDialog() {
        if (!PhoneUtil.isNetworkAvailable(mActivity)) {
//            Toast.makeText(mActivity, "网络异常，请检查网络!", Toast.LENGTH_SHORT).initDialog();
            return false;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setMessage("拼命加载中...");
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.show();

        return true;
    }

    public void dismissLoadingDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
            }
        }, 100);

    }


    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                            for (int k=0; k<districtList.size(); k++) {
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
        }).start();

    }

}
