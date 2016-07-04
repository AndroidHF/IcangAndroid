package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.ShangPinStatusBean;
import com.buycolle.aicang.ui.view.expandableLayout.StatusExpandableLayout;
import com.buycolle.aicang.ui.view.expandableLayout.TypesExpandableLayout;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/3/7.
 */
public class PaiPinSearchDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private LinearLayout rl_cancle_lay;


    private TextView tv_finish;
    private TextView tv_reset;

    private TypesExpandableLayout expand_type;
    private StatusExpandableLayout expand_status;


    private EditText et_input_start_price;
    private EditText et_input_end_price;

    private TextView tv_baoyou;
    private TextView tv_no_baoyou;

    private TextView tv_you_yikoujia;
    private TextView tv_wu_yikoujia;

    private ACache aCache;

    private ArrayList<ShangPinStatusBean> shangPinTypeBeens;


    public PaiPinSearchDialog(Context context, //

                              boolean isSelectCate_Id, //
                              int cate_id, //

                              String st_id,//
                              String start_price, //
                              String end_price,//

                              boolean isSelectExpress_Out_Type, //
                              int express_out_type,//

                              boolean isSelectOpen_But_It, //
                              int open_but_it) {
        super(context, R.style.search_menu_dialog);
        this.mContext = context;
        this.mActivity = (Activity) context;
        init(context);

        aCache = ACache.get(mContext);

        this.isSelectCate_Id = isSelectCate_Id;
        this.cate_id = cate_id;

        this.st_id = st_id;

        this.start_price = start_price;

        this.end_price = end_price;

        this.isSelectExpress_Out_Type = isSelectExpress_Out_Type;
        this.express_out_type = express_out_type;

        this.isSelectOpen_But_It = isSelectOpen_But_It;
        this.open_but_it = open_but_it;

        if (isSelectCate_Id) {
            expand_type.selectIndex(cate_id - 1);
        }

        // TODO: 16/4/12 状态初始化

        JSONObject resultObj = aCache.getAsJSONObject(Constans.TAG_GOOD_STATUS);
        shangPinTypeBeens = new ArrayList<>();
        if (resultObj != null) {
            try {
                JSONArray rows = resultObj.getJSONArray("rows");
                ArrayList<ShangPinStatusBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ShangPinStatusBean>>() {
                }.getType());
                shangPinTypeBeens.addAll(allDataArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(st_id)) {
            for (ShangPinStatusBean shangPinTypeBeen : shangPinTypeBeens) {
                if (shangPinTypeBeen.getDir_id().equals(st_id)) {
                    expand_status.initSelectStatus(shangPinTypeBeen.getItem_name());
                }
            }
        }

        if (!TextUtils.isEmpty(start_price)) {
            et_input_start_price.setText(start_price);
        }
        if (!TextUtils.isEmpty(end_price)) {
            et_input_end_price.setText(end_price);
        }

        if (isSelectExpress_Out_Type) {
            if (express_out_type == 1) {//1 不包邮 2 包邮
                initBaoYouStatus(2);
            } else {
                initBaoYouStatus(1);
            }
        }

        if (isSelectOpen_But_It) {
            if (open_but_it == 0) {//0 无 1 有
                initYiKouJiaStatus(2);
            } else {
                initYiKouJiaStatus(1);
            }
        }

    }

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类

    private String st_id = "";//状态ID集
    private String start_price = "";//起始价格
    private String end_price = "";//最高价格

    private boolean isSelectExpress_Out_Type = false;
    private int express_out_type;//1 不包邮 2 包邮

    private boolean isSelectOpen_But_It = false;
    private int open_but_it;//0 无 1 有


    private void init(Context context) {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.RIGHT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View view = View.inflate(context, R.layout.dialog_paipin_search_filter, null);

        rl_cancle_lay = (LinearLayout) view.findViewById(R.id.rl_cancle_lay);
        tv_finish = (TextView) view.findViewById(R.id.tv_finish);
        tv_reset = (TextView) view.findViewById(R.id.tv_reset);
        expand_type = (TypesExpandableLayout) view.findViewById(R.id.expand_type);
        expand_status = (StatusExpandableLayout) view.findViewById(R.id.expand_status);
        et_input_start_price = (EditText) view.findViewById(R.id.et_input_start_price);
        et_input_end_price = (EditText) view.findViewById(R.id.et_input_end_price);
        tv_baoyou = (TextView) view.findViewById(R.id.tv_baoyou);
        tv_no_baoyou = (TextView) view.findViewById(R.id.tv_no_baoyou);

        tv_you_yikoujia = (TextView) view.findViewById(R.id.tv_you_yikoujia);
        tv_wu_yikoujia = (TextView) view.findViewById(R.id.tv_wu_yikoujia);


        this.setContentView(view);

        rl_cancle_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callBack != null) {

                    //分类
                    if (expand_type.getIsSelect()) {
                        isSelectCate_Id = true;
                        cate_id = expand_type.getCurrentSelectValue();
                    } else {
                        isSelectCate_Id = false;
                        cate_id = 0;
                    }

                    //状态
                    if (expand_status.isSelect()) {
                        for (ShangPinStatusBean shangPinTypeBeen : shangPinTypeBeens) {
                            if (shangPinTypeBeen.getItem_name().equals(expand_status.getSelectVuew())) {
                                st_id = shangPinTypeBeen.getDir_id();
                                break;
                            }
                        }
                    } else {
                        st_id = "";
                    }

                    //开始价格
                    if (!TextUtils.isEmpty(et_input_start_price.getText().toString().trim())) {
                        if (et_input_start_price.getText().toString().trim().startsWith("0")){
                            UIHelper.t(mContext,"请输入正确的开始价格");
                            return;
                        }else{
                            start_price = et_input_start_price.getText().toString().trim();
                        }
                    } else {
                        start_price = "";
                    }
                    //结束价格
                    if (!TextUtils.isEmpty(et_input_end_price.getText().toString().trim())) {
                        if (et_input_end_price.getText().toString().trim().startsWith("0")){
                            UIHelper.t(mContext,"请输入正确的结束价格");
                            return;
                        }else {
                            end_price = et_input_end_price.getText().toString().trim();
                        }
                    } else {
                        end_price = "";
                    }

                    if (!TextUtils.isEmpty(et_input_start_price.getText().toString().trim()) && !TextUtils.isEmpty(et_input_end_price.getText().toString().trim())) {
                        if (Integer.valueOf(et_input_start_price.getText().toString().trim()) >= Integer.valueOf(et_input_end_price.getText().toString().trim())) {
                            UIHelper.t(mContext, "您输入的最高价应该大于最低价");
                            return;
                        }
                    }

                    if (baoyouHasSelect) {
                        isSelectExpress_Out_Type = true;
                    } else {
                        isSelectExpress_Out_Type = false;
                    }

                    if (yikoujiaHasSelect) {
                        isSelectOpen_But_It = true;
                    } else {
                        isSelectOpen_But_It = false;
                    }
                    dismiss();
                    callBack.action(isSelectCate_Id, cate_id,//
                            st_id, //
                            start_price, end_price, //
                            isSelectExpress_Out_Type, express_out_type, //
                            isSelectOpen_But_It, open_but_it);


                }
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12 重置
                reset();
            }
        });
        tv_baoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12 包邮
                initBaoYouStatus(1);
            }
        });
        tv_no_baoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12  不包邮
                initBaoYouStatus(2);
            }
        });
        tv_you_yikoujia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12 一口价
                initYiKouJiaStatus(1);
            }
        });
        tv_wu_yikoujia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12 没有一口价
                initYiKouJiaStatus(2);
            }
        });
    }

    private void reset() {
        expand_type.reSetStatus();

        // TODO: 16/4/12 状态的保留
        expand_status.reSet();

        et_input_start_price.setText("");
        et_input_end_price.setText("");

        baoyouHasSelect = false;
        currentBaoYouIndex = 0;
        express_out_type = 0;
        tv_baoyou.setBackgroundResource(R.color.white);
        tv_no_baoyou.setBackgroundResource(R.color.white);


        open_but_it = 3;// 3 没选择 0，无一口价 1，有一口价
        currentYiKouJiaIndex = 0;
        yikoujiaHasSelect = false;

        tv_you_yikoujia.setBackgroundResource(R.color.white);
        tv_wu_yikoujia.setBackgroundResource(R.color.white);


    }

    int currentBaoYouIndex = 0;
    boolean baoyouHasSelect = false;

    /**
     * 包邮状态
     *
     * @param index 1 不包邮 2 包邮
     */
    private void initBaoYouStatus(int index) {
        if (currentBaoYouIndex == index) {
            if (index == 1) {
                if (baoyouHasSelect) {
                    express_out_type = 0;
                    baoyouHasSelect = false;
                    tv_baoyou.setBackgroundResource(R.color.white);
                } else {
                    baoyouHasSelect = true;
                    express_out_type = 2;
                    tv_baoyou.setBackgroundResource(R.color.orange);
                }
            } else {
                if (baoyouHasSelect) {
                    express_out_type = 0;
                    baoyouHasSelect = false;
                    tv_no_baoyou.setBackgroundResource(R.color.white);
                } else {
                    express_out_type = 1;
                    baoyouHasSelect = true;
                    tv_no_baoyou.setBackgroundResource(R.color.orange);
                }
            }
        } else {
            baoyouHasSelect = true;
            currentBaoYouIndex = index;

            if (index == 1) {
                express_out_type = 2;
                tv_baoyou.setBackgroundResource(R.color.orange);
                tv_no_baoyou.setBackgroundResource(R.color.white);
            } else {
                express_out_type = 1;
                tv_baoyou.setBackgroundResource(R.color.white);
                tv_no_baoyou.setBackgroundResource(R.color.orange);
            }
        }
    }

    int currentYiKouJiaIndex = 0;
    boolean yikoujiaHasSelect = false;


    /**
     * 包邮状态
     *
     * @param index 1 不包邮 2 包邮
     */
    private void initYiKouJiaStatus(int index) {
        if (currentYiKouJiaIndex == index) {
            if (index == 1) {
                if (yikoujiaHasSelect) {
                    open_but_it = 3;
                    yikoujiaHasSelect = false;
                    tv_you_yikoujia.setBackgroundResource(R.color.white);
                } else {
                    open_but_it = 1;
                    yikoujiaHasSelect = true;
                    tv_you_yikoujia.setBackgroundResource(R.color.orange);
                }
            } else {
                if (yikoujiaHasSelect) {
                    open_but_it = 3;
                    yikoujiaHasSelect = false;
                    tv_wu_yikoujia.setBackgroundResource(R.color.white);
                } else {
                    open_but_it = 0;
                    yikoujiaHasSelect = true;
                    tv_wu_yikoujia.setBackgroundResource(R.color.orange);
                }
            }
        } else {
            yikoujiaHasSelect = true;
            currentYiKouJiaIndex = index;

            if (index == 1) {
                open_but_it = 1;
                tv_you_yikoujia.setBackgroundResource(R.color.orange);
                tv_wu_yikoujia.setBackgroundResource(R.color.white);
            } else {
                open_but_it = 0;
                tv_you_yikoujia.setBackgroundResource(R.color.white);
                tv_wu_yikoujia.setBackgroundResource(R.color.orange);
            }
        }
    }

    @Override
    public void onClick(View view) {


    }

    private CallBack callBack;

    public PaiPinSearchDialog setCallBack(CallBack call) {
        this.callBack = call;
        return this;
    }

    public interface CallBack {
        void action(boolean isSelectCate_Id, int cate_id, //
                    String st_id, String start_price, String end_price,//
                    boolean isSelectExpress_Out_Type, int express_out_type, //
                    boolean isSelectOpen_But_It, int open_but_it);
    }
}
