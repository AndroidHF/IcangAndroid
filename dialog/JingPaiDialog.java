package com.example.hufeng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
public class JingPaiDialog  extends Dialog{
    private Context mContext;
    private CallBack callback;
    private String CurrentValue;
    private boolean isHasPai;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_current_value)
    TextView tv_current_value;
    @Bind(R.id.et_input_value)
    EditText et_input_value;
    @Bind(R.id.tv_plus_1)
    TextView tv_plus_1;
    @Bind(R.id.tv_plus_5)
    TextView tv_plus_5;
    @Bind(R.id.tv_plus_10)
    TextView tv_plus_10;
    @Bind(R.id.tv_cancle)
    Button tv_cancle;
    @Bind(R.id.tv_sure)
    Button tv_sure;
    TextView tv_price_title;
    TextView getTv_price_title_msg;






   public JingPaiDialog(Context context,String title, final String current, final CallBack callback){
       super(context);
       setContentView(R.layout.dialog_paimai);
       ButterKnife.bind(this);
       this.CurrentValue = current;
       this.isHasPai = isHasPai;
       this.callback = callback;
       if (isHasPai){
            tv_price_title.setText("现价");
            getTv_price_title_msg.setText("*您输入的价格必须\n比当前价格更高");
       }else{
           tv_price_title.setText("起拍价");
           getTv_price_title_msg.setText("*您输入的价格必须\n比起拍价格更高");
       }

       tv_title.setText(title);
       tv_current_value.setText(current);
       et_input_value.setText(current);

       tv_cancle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
               if (callback != null) {
                   callback.cancle();
               }
           }
       });

       tv_sure.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (callback != null){
                   if (TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                       UIHelper.ToastShow(mContext,"请填写需要出的价");
                       return;
                   }
                   if (isHasPai){
                       if (Double.valueOf(et_input_value.getText().toString())<= Double.valueOf(current)){
                           UIHelper.ToastShow(mContext,"出价要大于现价哦亲~~");
                       }else {
                           callback.ok(et_input_value.getText().toString().trim());
                       }
                   }else {
                       if (Double.valueOf(et_input_value.getText().toString())<= Double.valueOf(current)){
                           UIHelper.ToastShow(mContext,"出价要大于起拍价哦亲~~");
                       }else {
                           callback.ok(et_input_value.getText().toString().trim());
                       }
                   }
               }
               et_input_value.setText(current);
           }
       });

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
                if (!".".equals(temp)) {
                    if (temp.startsWith(".")) {
                        int posDot = temp.indexOf(".");
                        if (temp.length() - posDot - 1 > 2) {
                            s.delete(posDot + 3, posDot + 4);
                        }
                    }
                }
            }
        });

       tv_plus_1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())) {
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim()) + 1;
                   et_input_value.setText(InputValue + "");
               } else {
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim()) + 1;
                   et_input_value.setText(InputValue + "");
               }
           }
       });

       tv_plus_5.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim())+5;
                   et_input_value.setText(InputValue+"");
               }else {
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim())+5;
                   et_input_value.setText(InputValue+"");
               }
           }
       });

       tv_plus_10.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!TextUtils.isEmpty(et_input_value.getText().toString().trim())){
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim())+10;
                   et_input_value.setText(InputValue+"");
               }else {
                   int InputValue = Integer.valueOf(et_input_value.getText().toString().trim())+10;
                   et_input_value.setText(InputValue+"");
               }
           }
       });

   }

   public interface CallBack{
        void cancle();
        void ok(String trim);
   }

}
