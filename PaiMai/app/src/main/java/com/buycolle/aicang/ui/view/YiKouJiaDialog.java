package com.buycolle.aicang.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.util.StringFormatUtil;
import com.buycolle.aicang.util.UIHelper;


/**
 * Created by joe on 16/1/5.
 */
public class YiKouJiaDialog extends Dialog {

    private Context mContext;

    public CallBack callBack;

    private TextView tv_title;
    private TextView tv_cancle;
    private TextView tv_sure;

    private TextView tv_current_value;
    private EditText et_input_value;
    private TextView tv_plus_1;
    private TextView tv_plus_5;
    private TextView tv_plus_10;
    private TextView tv_yikou_value;
    private TextView tv_yikoujia;
    private TextView tv_price_title;


    private String currentValuee;
    private String yikouJia;

    private int type = 1;// 1 出价  2 一口价
    private boolean isHasPai = false;

    public YiKouJiaDialog(final Context context, String title, final String current, final String yikoujia, final boolean isHasPai) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_yikoujia);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_value = (TextView) findViewById(R.id.tv_current_value);
        tv_plus_1 = (TextView) findViewById(R.id.tv_plus_1);
        tv_plus_5 = (TextView) findViewById(R.id.tv_plus_5);
        tv_price_title = (TextView) findViewById(R.id.tv_price_title);
        tv_plus_10 = (TextView) findViewById(R.id.tv_plus_10);
        tv_yikou_value = (TextView) findViewById(R.id.tv_yikou_value);
        tv_yikoujia = (TextView) findViewById(R.id.tv_yikoujia);
        et_input_value = (EditText) findViewById(R.id.et_input_value);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        this.currentValuee = current;
        this.isHasPai = isHasPai;
        this.yikouJia = yikoujia;

        if (isHasPai) {
            tv_price_title.setText("现价");
        } else {
            tv_price_title.setText("起拍价");
        }

        tv_title.setText(title);
        tv_current_value.setText("￥" + current);
        tv_yikou_value.setText("￥" + yikoujia);
        et_input_value.setText(current);

        et_input_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable edt) {


                String temp = edt.toString();


                if (TextUtils.isEmpty(temp)) {
                    return;
                }

                if (temp.startsWith(".")) {
                    et_input_value.setText(et_input_value.getText().toString().replace(".", ""));
                    return;
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) {//没有.
                    if (Double.valueOf(edt.toString()) > Double.valueOf(yikouJia) || edt.toString().length() > yikoujia.length()) {
                        et_input_value.setText(yikouJia);
                    }
                } else {
                    if (!temp.endsWith(".")) {
                        if (Double.valueOf(temp) > Double.valueOf(yikouJia)) {
                            et_input_value.setText(yikouJia);
                            return;
                        } else {
                            if (temp.length() - posDot - 1 > 2) {
                                edt.delete(posDot + 3, posDot + 4);
                            }
                        }
                    }
                }
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    callBack.cancle();
                }
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callBack != null) {
                    if (TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                        UIHelper.t(mContext, "请填写需要出的价~~");
                        return;
                    }

                    if (isHasPai) {
                        if (Double.valueOf(et_input_value.getText().toString()) <= Double.valueOf(currentValuee)) {
                            UIHelper.t(mContext, "您的出价必须高于现价");
                        } else {
                            if (Double.valueOf(yikouJia) == Double.valueOf(et_input_value.getText().toString())) {
                                callBack.ok(yikouJia, 2);
                            } else {
                                callBack.ok(et_input_value.getText().toString(), 1);
                            }
                        }
                    } else {
                        if (Double.valueOf(et_input_value.getText().toString()) < Double.valueOf(currentValuee)) {
                            UIHelper.t(mContext, "出价要大于或者等于起拍价哦亲~~");
                        } else {
                            if (yikouJia.endsWith(et_input_value.getText().toString())) {
                                callBack.ok(yikouJia, 2);
                            } else {
                                callBack.ok(et_input_value.getText().toString(), 1);
                            }
                        }

                    }

                }
            }
        });
        tv_plus_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                    et_input_value.setText(StringFormatUtil.getPlusValue(et_input_value.getText().toString().trim(), currentValuee, 5));
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim());
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                } else {
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim()) + 5;
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                }
            }
        });
        tv_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                    et_input_value.setText(StringFormatUtil.getPlusValue(et_input_value.getText().toString().trim(), currentValuee, 1));
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim()) ;
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                } else {
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim()) + 1;
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                }
            }
        });
        tv_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                    et_input_value.setText(StringFormatUtil.getPlusValue(et_input_value.getText().toString().trim(), currentValuee, 10));
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim());
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                } else {
                    double inputValue = Double.valueOf(currentValuee) + 10;
                    if (inputValue > Double.valueOf(yikoujia)) {
                        et_input_value.setText(yikoujia + "");
                    } else {
                        et_input_value.setText(StringFormatUtil.getDoubleFormatNew(inputValue+""));
                    }
                }
            }
        });
        tv_yikoujia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_input_value.setText(yikouJia + "");
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public YiKouJiaDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok(String value, int type);

        void cancle();
    }
}
