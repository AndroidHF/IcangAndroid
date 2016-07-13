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
public class JingPaiDialog extends Dialog {

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

    private TextView tv_price_title;
    private TextView tv_price_title_msg;


    private String currentValuee;
    private boolean isHasPai = false;


    public JingPaiDialog(final Context context, String title, String current) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_jingpai);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_value = (TextView) findViewById(R.id.tv_current_value);
        tv_plus_1 = (TextView) findViewById(R.id.tv_plus_1);
        tv_plus_5 = (TextView) findViewById(R.id.tv_plus_5);
        tv_plus_10 = (TextView) findViewById(R.id.tv_plus_10);
        et_input_value = (EditText) findViewById(R.id.et_input_value);
        tv_price_title = (TextView) findViewById(R.id.tv_price_title);
        tv_price_title_msg = (TextView) findViewById(R.id.tv_price_title_msg);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        this.currentValuee = current;

        tv_title.setText(title);
        tv_current_value.setText("￥" + current);
        et_input_value.setText(current);

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
                    callBack.ok(et_input_value.getText().toString().trim());
                }
            }
        });
        tv_plus_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputValue = Integer.valueOf(currentValuee) + 5;
                et_input_value.setText(inputValue + "");
            }
        });
        tv_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputValue = Integer.valueOf(currentValuee) + 1;
                et_input_value.setText(inputValue + "");
            }
        });
        tv_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputValue = Integer.valueOf(currentValuee) + 10;
                et_input_value.setText(inputValue + "");
            }
        });

    }

    public JingPaiDialog(final Context context, String title, String current, final boolean isHasPai) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_jingpai);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_value = (TextView) findViewById(R.id.tv_current_value);
        tv_plus_1 = (TextView) findViewById(R.id.tv_plus_1);
        tv_plus_5 = (TextView) findViewById(R.id.tv_plus_5);
        tv_plus_10 = (TextView) findViewById(R.id.tv_plus_10);
        et_input_value = (EditText) findViewById(R.id.et_input_value);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_price_title = (TextView) findViewById(R.id.tv_price_title);
        tv_price_title_msg = (TextView) findViewById(R.id.tv_price_title_msg);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        this.currentValuee = current;
        this.isHasPai = isHasPai;
        if (isHasPai) {
            tv_price_title.setText("现价");
            tv_price_title_msg.setText("*您输入的价格必须\n比当前价格更高");
        } else {
            tv_price_title.setText("起拍价");
            tv_price_title_msg.setText("*您输入的价格必须\n比起拍价格更高");
        }

        tv_title.setText(title);
        tv_current_value.setText("￥" + current);
        et_input_value.setText(current);

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
                            UIHelper.t(mContext,"您的出价必须高于现价");
                        } else {
                            callBack.ok(et_input_value.getText().toString().trim());
                        }
                    } else {
                        if (Double.valueOf(et_input_value.getText().toString()) < Double.valueOf(currentValuee)) {
                            UIHelper.t(mContext, "出价要大于或者等于起拍价哦亲~~");
                        } else {
                            callBack.ok(et_input_value.getText().toString().trim());
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
                } else {
                    int inputValue = Integer.valueOf(currentValuee) + 5;
                    et_input_value.setText(inputValue + "");
                }
            }
        });
        tv_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                    et_input_value.setText(StringFormatUtil.getPlusValue(et_input_value.getText().toString().trim(), currentValuee, 1));

                } else {
                    int inputValue = Integer.valueOf(currentValuee) + 1;
                    et_input_value.setText(inputValue + "");
                }
            }
        });
        tv_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                    et_input_value.setText(StringFormatUtil.getPlusValue(et_input_value.getText().toString().trim(), currentValuee, 10));

                } else {
                    int inputValue = Integer.valueOf(currentValuee) + 10;
                    et_input_value.setText(inputValue + "");
                }
            }
        });

        et_input_value.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();

                if (!".".equals(temp)) {
                    if (temp.startsWith(".")) {
                        int posDot = temp.indexOf(".");
                        if (temp.length() - posDot - 1 > 2) {
                            edt.delete(posDot + 3, posDot + 4);
                            return;
                        }
                    }
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public JingPaiDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok(String value);

        void cancle();
    }
}
