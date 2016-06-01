package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.AppConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.SmileGVAdapter;
import com.buycolle.aicang.util.CommonUtils;
import com.buycolle.aicang.util.SmileUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/4/20.
 */
public class MyCommentLay extends RelativeLayout {


    private Context context;
    private EditText _commentEdit;
    private Activity mActivity;
    private ViewPager vp_add_smile;
    public boolean isSmileActive = false;//表情布局是否显示
    //表情相关
    private List<List<View>> smileList;
    private LinearLayout ll_root_view;

    private FrameLayout fl_smile_lay;
    private TextView tv_send;
    private LinearLayout ll_smail_container;
    private FrameLayout ll_smail_container_main;

    public MyCommentLay(Context context) {
        super(context);
        this.context = context;
        this.mActivity = (Activity) context;
        init();
    }

    public MyCommentLay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.mActivity = (Activity) context;
        init();
    }

    public MyCommentLay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.mActivity = (Activity) context;
        init();
    }

    private void init() {

        LayoutInflater.from(context).inflate(R.layout.view_comment_lay, this);
        vp_add_smile = (ViewPager) findViewById(R.id.vp_smile_page);
        fl_smile_lay = (FrameLayout) findViewById(R.id.fl_smile_lay);
        _commentEdit = (EditText) findViewById(R.id.et_input);
        tv_send = (TextView) findViewById(R.id.tv_send);
        ll_smail_container = (LinearLayout) findViewById(R.id.ll_smail_container);
        ll_smail_container_main = (FrameLayout) findViewById(R.id.ll_smail_container_main);

        fl_smile_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSmileActive){
                    hideInputMethodWindow();
                    ll_smail_container_main.setVisibility(VISIBLE);
                    ll_smail_container.setVisibility(VISIBLE);
                    isSmileActive = true;
                }else{
                    isSmileActive = false;
                }
            }
        });

        _commentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ll_smail_container_main.setVisibility(View.INVISIBLE);
                    ll_smail_container.setVisibility(View.INVISIBLE);
                    isSmileActive = false;
                }
                return false;
            }
        });
        initSmilList();

        FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) ll_smail_container.getLayoutParams();
        int ConfigHeight = AppConfig.getAppConfig(mActivity).getPhoneKeyBordHeight();
        if (ConfigHeight != -1) {
            lps.height = ConfigHeight;
            ll_smail_container.setLayoutParams(lps);
        } else {
            lps.height = CommonUtils.dp2Px(mActivity, 210);
            ll_smail_container.setLayoutParams(lps);
        }
    }



    //初始化表情数据
    private void initSmilList() {
        smileList = new ArrayList<List<View>>();
        initInnerSmile();
        initSmileViewPager();
    }

    //初始化内置表情
    private void initInnerSmile() {
        smileList.add(getChildGridView(getSmileRes(20), 20));
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
                View view = View.inflate(mActivity, R.layout.smile_vp_item, null);
                MeasuredGridView gv = (MeasuredGridView) view.findViewById(R.id.mgv_smile_vp_item);
                gv.setHorizontalSpacing(0);
                gv.setVerticalSpacing(0);
                ArrayList<String> childList = new ArrayList<>();
                if (i == size) {
                    childList.addAll(list.subList(showNums * i, list.size()));
                } else {
                    childList.addAll(list.subList(showNums * i, showNums * (i + 1)));
                }
                childList.add("delete_expression");
                if (childList.size() > 1) {
                    final SmileGVAdapter smileAdater = new SmileGVAdapter(mActivity, childList);
                    gv.setAdapter(smileAdater);
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        vp_add_smile.setAdapter(mPagerAdapter);
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

    private InputMethodManager mInputMethodManager;

    private void hideInputMethodWindow() {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        try {
            mInputMethodManager.hideSoftInputFromWindow(_commentEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
