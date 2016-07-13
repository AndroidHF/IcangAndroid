package com.buycolle.aicang.ui.activity.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.AddressBean;
import com.buycolle.aicang.event.EditAddressEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.Validator;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by joe on 16/3/4.
 */
public class EditAddressActivity extends BaseActivity {


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.et_address_detail)
    EditText etAddressDetail;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    private String province = "";
    private String city = "";
    private String district = "";
    OptionsPickerView pvOptions;
    NoticeSingleDialog noticeSingleDialog;

    AddressBean addressBean;

    private boolean hasInit = false;

    int selectIndex_1 = 0;
    int selectIndex_2 = 0;
    int selectIndex_3 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_addres);
        ButterKnife.bind(this);
        myHeader.init("修改收货地址", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        //选项选择器
        pvOptions = new OptionsPickerView(mActivity);
        initCity();
        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.setPicker(options1Items, options2Items, options3Items, true);
                pvOptions.setCyclic(false, false, false);

                KLog.d("初始化城市---",province+"--"+city+"--"+district);

                if (!TextUtils.isEmpty(province)) {
                    for (int i = 0; i < options1Items.size(); i++) {
                        if (province.equals(options1Items.get(i))) {
                            selectIndex_1 = i;
                            break;
                        }
                    }

                    ArrayList<String> selectList_2 = options2Items.get(selectIndex_1);
                    for (int i = 0; i < selectList_2.size(); i++) {
                        if (city.equals(selectList_2.get(i))) {
                            selectIndex_2 = i;
                            break;
                        }
                    }

                    ArrayList<String> selectList_3 = options3Items.get(selectIndex_1).get(selectIndex_2);
                    for (int i = 0; i < selectList_3.size(); i++) {
                        if (district.equals(selectList_3.get(i))) {
                            selectIndex_3 = i;
                            break;
                        }
                    }
                    pvOptions.setSelectOptions(selectIndex_1, selectIndex_2, selectIndex_3);
                } else {
                    pvOptions.setSelectOptions(0, 0, 0);
                }
                pvOptions.show();
            }
        });
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1);
                city = options2Items.get(options1).get(option2);
                district = options3Items.get(options1).get(option2).get(options3);
                String tx = province + city + district;
                tvCity.setText(tx);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    showNotice("提示", "请填写姓名", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }else if (etName.getText().toString().trim().length() > 40){
                    showNotice("提示", "您输入的姓名过长", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    showNotice("提示", "请填写联系方式", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }
                if (!Validator.isMobile(etPhone.getText().toString().trim())) {
                    showNotice("提示", "请填写正确的联系号码格式", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    showNotice("提示", "请选择省市区", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etAddressDetail.getText().toString().trim())) {
                    showNotice("提示", "请收货地址信息", "我知道了");
                    btnSave.setEnabled(true);
                    return;
                }
                submit();
            }
        });
        loadData();
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.userreceipt_getlistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    addressBean = new Gson().fromJson(resultObj.toString(), AddressBean.class);
                    if (addressBean.getRows().size() > 0) {
                        hasInit = true;
                        initview();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    private void initview() {
        AddressBean.RowsEntity rowsEntity = addressBean.getRows().get(0);
        etName.setText(rowsEntity.getReceipt_name());
        etPhone.setText(rowsEntity.getReceipt_tel());
        etCode.setText(rowsEntity.getZip_code());
        tvCity.setText(rowsEntity.getProvince() + rowsEntity.getCity() + rowsEntity.getDistrict());
        etAddressDetail.setText(rowsEntity.getReceipt_address());
        city = rowsEntity.getCity();
        province = rowsEntity.getProvince();
        district = rowsEntity.getDistrict();
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("district", district);
            jsonObject.put("province", province);
            jsonObject.put("receipt_address", etAddressDetail.getText().toString());
            jsonObject.put("receipt_name", etName.getText().toString());
            jsonObject.put("receipt_tel", etPhone.getText().toString());
            jsonObject.put("zip_code", etCode.getText().toString());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.userreceipt_saveorupdate(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "更新收货地址成功！");
                        EventBus.getDefault().post(new EditAddressEvent(0));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.t(mContext, R.string.net_error);
            }
        });


    }

    private void initCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator iter = mApplication.mCitisDatasMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String province = (String) entry.getKey();
                    options1Items.add(province);
                    String[] citys = (String[]) entry.getValue();
                    ArrayList<String> options2Item = new ArrayList<String>();
                    ArrayList<ArrayList<String>> options3Item = new ArrayList<>();
                    for (String city : citys) {
                        options2Item.add(city);
                        Iterator dstr = mApplication.mDistrictDatasMap.entrySet().iterator();
                        while (dstr.hasNext()) {
                            Map.Entry cityentry = (Map.Entry) dstr.next();
                            String city_d = (String) cityentry.getKey();
                            if (city_d.equals(city)) {
                                String[] dists = (String[]) cityentry.getValue();
                                ArrayList<String> options3Item_dis = new ArrayList<>();
                                for (String dist : dists) {
                                    options3Item_dis.add(dist);
                                }
                                options3Item.add(options3Item_dis);
                                break;
                            }
                        }
                    }
                    options3Items.add(options3Item);
                    options2Items.add(options2Item);
                }
            }
        }).start();
    }
}
