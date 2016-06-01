package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.fragment.SmileFragment;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.util.SmileUtils;
import com.buycolle.aicang.util.UIUtil;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/4/19.
 */
public class DemoActivity extends BaseActivity implements SmileFragment.OnFragmentInteractionListener {


    private InputMethodManager manager;


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.iv_edit_input)
    ImageView ivEditInput;
    @Bind(R.id.et_input)
    EditText _commentEdit;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.sendMsgLayout)
    LinearLayout sendMsgLayout;
    @Bind(R.id.fl_chat_activity_container)
    FrameLayout fragContainer;
    @Bind(R.id.rootView)
    RelativeLayout rootView;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        getSupportFragmentManager().
                beginTransaction().add(R.id.fl_chat_activity_container, new SmileFragment(), "smile")
                .commit();

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ivEditInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) fragContainer.getLayoutParams();

                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null) {
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }

                if (isShow) {
                    isShow = false;
                    lps.height= UIUtil.dip2px(mContext,220);
                    fragContainer.setLayoutParams(lps);
                    fragContainer.setVisibility(View.VISIBLE);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } else {
                    isShow = true;
                    lps.height= UIUtil.dip2px(mContext,0);
                    fragContainer.setLayoutParams(lps);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    fragContainer.setVisibility(View.INVISIBLE);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });

        _commentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getActionMasked() == MotionEvent.ACTION_DOWN ) {
                    isShow = true;
                    LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) fragContainer.getLayoutParams();
                    lps.height= UIUtil.dip2px(mContext,0);
                    fragContainer.setLayoutParams(lps);
                    _commentEdit.requestFocus();
                    fragContainer.setVisibility(View.INVISIBLE);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    //如果软键盘被隐藏，则显示
                    if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
                        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

    }

    boolean isShow = true;


    @Override
    public void onSmileClick(String filename) {

        try {
            if (filename != "delete_expression") { // 不是删除键，显示表情
                // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                Class clz = Class.forName("com.buycolle.aicang.util.SmileUtils");
                Field field = clz.getField(filename);
                int iCursorStart = Selection.getSelectionStart((_commentEdit.getText()));
                int iCursorEnd = Selection.getSelectionEnd((_commentEdit.getText()));
                if (iCursorStart != iCursorEnd) {
                    ((Editable) _commentEdit.getText()).replace(iCursorStart, iCursorEnd, "");
                }
                int iCursor = Selection.getSelectionEnd((_commentEdit.getText()));
                ((Editable) _commentEdit.getText()).insert(iCursor, SmileUtils.getSmiledText(mActivity,
                        (String) field.get(null)));
            } else { // 删除文字或者表情
                if (!TextUtils.isEmpty(_commentEdit.getText())) {
                    int selectionStart = _commentEdit.getSelectionStart();// 获取光标的位置
                    if (selectionStart > 0) {
                        String body = _commentEdit.getText().toString();
                        String tempStr = body.substring(0, selectionStart);
                        int i = tempStr.lastIndexOf("^");// 获取最后一个表情的位置
                        if (i != -1) {
                            CharSequence cs = tempStr.substring(i, selectionStart);
                            if (SmileUtils.containsKey(cs.toString()) && tempStr.endsWith("^"))
                                _commentEdit.getEditableText().delete(i, selectionStart);
                            else
                                _commentEdit.getEditableText().delete(selectionStart - 1,
                                        selectionStart);
                        } else {
                            _commentEdit.getEditableText().delete(selectionStart - 1, selectionStart);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
