package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.SmileGVAdapter;
import com.buycolle.aicang.ui.view.keyboard.KeyboardUtil;
import com.buycolle.aicang.ui.view.keyboard.PanelLayout;
import com.buycolle.aicang.util.SmileUtils;
import com.buycolle.aicang.util.UIHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joe on 15/12/20.
 */
public class MyCommentPop extends PopupWindow implements View.OnClickListener {


    private Activity _mActivity;
    private View parent;
    private EditText _commentEdit;
    private TextView commentButton;

    private InputMethodManager manager;
    private RecyclerView mContentRyv;

    private LinearLayout fragContainer;
    private ImageView iv_edit_input;

    private PanelLayout mPanelRoot;


    ViewPager vp_smile_page;
    //表情相关
    private List<List<View>> smileList;

    public MyCommentPop(Context context) {
        super(context);
        _mActivity = (Activity) context;
        initView();
    }

    public MyCommentPop(Context context, View parent) {
        super(context);
        this.parent = parent;
        _mActivity = (Activity) context;
        initView();
    }

    public MyCommentPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyCommentPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    boolean isShow = true;


    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) _mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_comment_pop, null);
        manager = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        _commentEdit = (EditText) view.findViewById(R.id.input_sms);
        iv_edit_input = (ImageView) view.findViewById(R.id.iv_edit_input);
        mContentRyv = (RecyclerView) view.findViewById(R.id.content_ryv);
        vp_smile_page = (ViewPager) view.findViewById(R.id.vp_add_smile);
        mPanelRoot = (PanelLayout) view.findViewById(R.id.panel_root);
        commentButton = (TextView) view.findViewById(R.id.send_sms);
        fragContainer = (LinearLayout) view.findViewById(R.id.ll_smail_container);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        mContentRyv.setLayoutManager(new LinearLayoutManager(_mActivity));
        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(_commentEdit);
                    mPanelRoot.setVisibility(View.GONE);
                    dismiss();
                }

                return false;
            }
        });
        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new ColorDrawable(0xc0000000));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        initSmilList();
        setContentView(view);

        commentButton.setOnClickListener(this);

        iv_edit_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlusIv();
            }
        });


        _commentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    iv_edit_input.setImageResource(R.drawable.smile_icon);
                }
                return false;
            }
        });


        _commentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 30) {
                    //UIHelper.t(_mActivity, "输入字数超出30个限制");
                    //change by :胡峰，提示内容修改
                    UIHelper.t(_mActivity,"您输入的字数过多");
                    _commentEdit.getEditableText().delete(s.length() - 1, s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void onClickPlusIv() {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            KeyboardUtil.showKeyboard(_commentEdit);
            iv_edit_input.setImageResource(R.drawable.smile_icon);
        } else {
            KeyboardUtil.hideKeyboard(_commentEdit);
            mPanelRoot.setVisibility(View.VISIBLE);
            iv_edit_input.setImageResource(R.drawable.key_bord);
        }

    }


    public void setEditHint(String hintStr) {
        _commentEdit.setHint(hintStr);
    }

    public EditText getEdit() {
        return _commentEdit;
    }


    public void showPop() {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        _commentEdit.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) _commentEdit.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_sms://发送评论
                //change by :胡峰，输入内容的判断
                if (TextUtils.isEmpty(_commentEdit.getText().toString().trim())){
                    UIHelper.t(_mActivity,"请输入答复内容");
                    return;
                }
                if (callBack != null)
                    callBack.send(commentButton, _commentEdit);
                break;

        }
    }


    private CallBack callBack;

    public void setCallBack(CallBack back) {
        this.callBack = back;
    }

    public interface CallBack {
        void send(TextView sendTv, EditText content);
    }


    //初始化表情数据
    private void initSmilList() {
        smileList = new ArrayList<List<View>>();
        initInnerSmile();
        initSmileViewPager();
    }

    //初始化内置表情
    private void initInnerSmile() {
        smileList.add(getChildGridView(getSmileRes(18), 18));
    }

    //取得环信表情资源
    public List<String> getSmileRes(int getSum) {
        List<String> resList = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            resList.add(filename);
        }
        return resList;

    }

    //获取表情gridview的子view
    private List<View> getChildGridView(List<String> list, int showNums) {
        List<View> viewList = null;
        if (list != null && list.size() != 0 && showNums > 0) {
            viewList = new ArrayList<>();
            int size = list.size() / showNums;
            for (int i = 0; i <= size; i++) {
                View view = View.inflate(_mActivity, R.layout.smile_vp_item, null);
                MeasuredGridView gv = (MeasuredGridView) view.findViewById(R.id.mgv_smile_vp_item);
                gv.setHorizontalSpacing(0);
                gv.setVerticalSpacing(0);
                ArrayList<String> childList = new ArrayList<>();
                if (i == size) {
                    childList.addAll(list.subList(showNums * i, list.size()));
                } else {
                    childList.addAll(list.subList(showNums * i, showNums * (i + 1)));
                }
                if (childList.size() > 1) {
                    final SmileGVAdapter smileAdater = new SmileGVAdapter(_mActivity, childList);
                    gv.setAdapter(smileAdater);
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (_commentEdit.getText().toString().length() >= 25) {
                                //UIHelper.t(_mActivity, "输入字数超出30个限制");
                                //change by :胡峰，提示内容修改
                                UIHelper.t(_mActivity,"您输入的字数过多");
                                return;
                            }

                            String filename = (String) smileAdater.getItem(position);
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
                                    ((Editable) _commentEdit.getText()).insert(iCursor, SmileUtils.getSmiledText(_mActivity,
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
                    });
                    viewList.add(view);
                }
            }
        }
        return viewList;
    }


    //初始化表情viewpager
    private void initSmileViewPager() {
        SmilePagerAdapter mPagerAdapter = new SmilePagerAdapter(smileList);
        vp_smile_page.setAdapter(mPagerAdapter);
    }

    class SmilePagerAdapter extends PagerAdapter {
        private List<List<View>> views;
        private List<View> mList;

        public SmilePagerAdapter(List<List<View>> views) {
            this.views = views;
            mList = new ArrayList<>();
            for (List<View> list : views) {
                mList.addAll(list);
            }
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mList.get(arg1));
            return mList.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mList.get(arg1));

        }

    }

}
