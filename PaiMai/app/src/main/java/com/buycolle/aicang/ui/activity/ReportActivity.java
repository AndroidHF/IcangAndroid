package com.buycolle.aicang.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PaiPinDetailBean;
import com.buycolle.aicang.bean.ReportTypeBean;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.ReportTypeDialog;
import com.buycolle.aicang.util.ACache;
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
 * Created by joe on 16/3/10.
 */
public class ReportActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_paimai)
    ImageView ivPaimai;
    @Bind(R.id.rl_select_weigui_type)
    RelativeLayout rlSelectWeiguiType;
    @Bind(R.id.ev_input)
    EditText evInput;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.tv_type)
    TextView tv_type;
    @Bind(R.id.iv_range)
    ImageView ivRange;
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;
    @Bind(R.id.tv_good_status)
    TextView tvGoodStatus;
    @Bind(R.id.tv_good_desc)
    TextView tvGoodDesc;

    private PaiPinDetailBean entity;

    private ArrayList<ReportTypeBean> reportTypeBeanArrayList;
    ACache aCache;

    private int wf_type;
    private String wf_type_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        aCache = ACache.get(this);
        myHeader.init("举报", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        entity = (PaiPinDetailBean) _Bundle.getSerializable("entity");
        mApplication.setImages(entity.getCover_pic(), ivPaimai);
        mApplication.setImages(entity.getRaretag_icon(), ivRange);

        tvGoodTitle.setText(entity.getProduct_title());
        tvGoodStatus.setText(entity.getSt_name());
        tvGoodDesc.setText(entity.getProduct_desc());

        initListener();
        JSONObject jsonObject = aCache.getAsJSONObject(Constans.TAG_REPORT_TYPE);
        if (jsonObject != null) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                reportTypeBeanArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ReportTypeBean>>() {
                }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            loadJuBao();
        }


    }

    private void initListener() {
        rlSelectWeiguiType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> strings = new ArrayList<String>();
                for (ReportTypeBean reportTypeBean : reportTypeBeanArrayList) {
                    strings.add(reportTypeBean.getItem_name());
                }
                new ReportTypeDialog(mContext, strings).setCallBack(new ReportTypeDialog.CallBack() {
                    @Override
                    public void ok(int type) {
                        wf_type = reportTypeBeanArrayList.get(type).getDir_id();
                        wf_type_title = reportTypeBeanArrayList.get(type).getItem_name();
                        tv_type.setText(wf_type_title);
                    }
                }).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setEnabled(false);
                if (TextUtils.isEmpty(tv_type.getText().toString().trim())) {
                    UIHelper.t(mContext, "请选择违规类型");
                    btnRegister.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(evInput.getText().toString().trim())) {
                    UIHelper.t(mContext, "请填写举报原因");
                    btnRegister.setEnabled(true);
                    return;
                }

                report();

            }
        });
    }

    private void report() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", entity.getProduct_id());
            jsonObject.put("report_reason", evInput.getText().toString());
            jsonObject.put("wf_type", wf_type);
            jsonObject.put("wf_type_title", wf_type_title);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.proudctreport_submitbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "举报成功");
                        finish();
                    } else {
                        btnRegister.setEnabled(true);
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    btnRegister.setEnabled(true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                btnRegister.setEnabled(true);
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }

    /**
     * 获取拍品举报类型
     */
    private void loadJuBao() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.dirtionary_getproductwftypelistbyapp(jsonObject, new ApiCallback() {
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
                        aCache.put(Constans.TAG_REPORT_TYPE, resultObj);
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        reportTypeBeanArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ReportTypeBean>>() {
                        }.getType());
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
            }
        });
    }
}
