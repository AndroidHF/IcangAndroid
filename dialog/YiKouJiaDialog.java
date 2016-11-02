package com.example.hufeng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hufeng.aicangproject.R;
import com.example.hufeng.com.example.hufeng.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/4/19.
 */
public class YiKouJiaDialog extends Dialog{
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_cancle)
    TextView tv_cancle;
    @Bind(R.id.tv_sure)
    TextView tv_sure;
    @Bind(R.id.tv_current_value)
    TextView tv_current_value;
    @Bind(R.id.et_input_value)
    EditText et_input_value;
    @Bind(R.id.tv_plus_1)
    Button tv_plus_1;
    @Bind(R.id.tv_plus_5)
    TextView tv_plus_5;
    @Bind(R.id.tv_plus_10)
    TextView tv_plus_10;
    @Bind(R.id.tv_yikou_value)
    TextView tv_yikou_value;
    @Bind(R.id.tv_yikoujia)
    TextView tv_yikoujia;
    @Bind(R.id.tv_price_title)
    TextView tv_price_title;

    private String CurrentValue;
    private String yikoujia;
    private int type = 1;//1:出价，2：一口价
    private boolean isHasPai=false;
    private Context mContext;
    private CallBack callBack;
    public YiKouJiaDialog(Context context,String titile, final String current, final String yikoujia, final boolean isHasPai){
        super(context);
        setContentView(R.layout.dialog_yikoujia);
        ButterKnife.bind(this);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        this.CurrentValue = current;
        this.isHasPai = isHasPai;
        this.yikoujia = yikoujia;
        if (isHasPai){
            tv_price_title.setText("现价");
        }else {
            tv_price_title.setText("起拍价");
        }

        tv_title.setText(titile);
        tv_current_value.setText("￥"+current);
        tv_yikou_value.setText("￥"+yikoujia);
        et_input_value.setText(current);
        et_input_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (TextUtils.isEmpty(temp)) {
                    return;
                }

                if (temp.startsWith(".")) {
                    et_input_value.setText(et_input_value.getText().toString().replace(".", ""));
                    return;
                }

                int posDot = temp.indexOf(".");
                if (posDot <= 0) {
                    if (Double.valueOf(s.toString()) > Double.valueOf(yikoujia) || s.toString().length() > yikoujia.length()) {
                        et_input_value.setText(s.toString());
                    } else {
                        et_input_value.setText(yikoujia);
                    }
                } else {
                    s.delete(posDot + 3, posDot + 4);
                    et_input_value.setText(s.toString());
                }
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callBack != null) {
                    callBack.cancle();
                }
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null){
                    if (TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                        UIHelper.ToastShow(mContext,"请填写要出的价");
                        return;
                    }
                    if (isHasPai){
                        if (Double.valueOf(et_input_value.getText().toString()) <= Double.valueOf(current)){
                            UIHelper.ToastShow(mContext,"出价要大于现价哦亲");
                        }else {
                            if (Double.valueOf(et_input_value.getText().toString()) <= Double.valueOf(current)){
                                callBack.ok(yikoujia,2);
                            }else {
                                callBack.ok(et_input_value.getText().toString(),1);
                            }
                        }
                    }
                }
            }
        });


        tv_plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                    et_input_value.setText(et_input_value.getText().toString().trim());
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim());
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }else {
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim())+1;
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }
            }
        });

        tv_plus_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                    et_input_value.setText(et_input_value.getText().toString().trim());
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim());
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }else {
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim())+5;
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }
            }
        });

        tv_plus_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                    et_input_value.setText(et_input_value.getText().toString().trim());
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim());
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }else {
                    double inputValue = Double.valueOf(et_input_value.getText().toString().trim())+10;
                    if (inputValue>Double.valueOf(yikoujia)){
                        et_input_value.setText(yikoujia+"");
                    }else {
                        et_input_value.setText(inputValue+"");
                    }
                }
            }
        });

        tv_yikou_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input_value.setText(yikoujia+"");
            }
        });
    }

    public interface CallBack{
        void cancle();
        void ok(String yikoujia, int i);
    }

}
