/***********************************************************************************
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Robin Chutaux
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package com.buycolle.aicang.ui.view.expandableLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ShangPinStatusBean;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.AnimationHelper;
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

/**
 * 分类展开自定义
 */
public class StatusExpandableLayout extends RelativeLayout implements View.OnClickListener {
    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private Integer duration;
    private FrameLayout contentLayout;
    private FrameLayout headerLayout;
    private Animation animation;
    private Activity mContext;
    private ACache aCache;
    private MainApplication mApplication;
    private ArrayList<ShangPinStatusBean> shangPinStatusBeens;

    private ImageView iv_expand_icon_status;

    private TextView tv_status_1;
    private TextView tv_status_2;
    private TextView tv_status_3;
    private TextView tv_status_4;
    private TextView tv_status_5;
    private TextView tv_status_6;
    //private TextView tv_status_7;
    //private TextView tv_status_8;

    private ArrayList<TextView> textViews;

    public StatusExpandableLayout(Context context) {
        super(context);
        this.mContext = (Activity) context;
    }

    public StatusExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    public StatusExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.view_expandable_status, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout_status);
        contentLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_contentLayout_status);
        aCache = ACache.get(mContext);
        mApplication = (MainApplication) mContext.getApplication();
        shangPinStatusBeens = new ArrayList<>();
        loadData();


        tv_status_1 = (TextView) rootView.findViewById(R.id.tv_status_1);
        tv_status_2 = (TextView) rootView.findViewById(R.id.tv_status_2);
        tv_status_3 = (TextView) rootView.findViewById(R.id.tv_status_3);
        tv_status_4 = (TextView) rootView.findViewById(R.id.tv_status_4);
        tv_status_5 = (TextView) rootView.findViewById(R.id.tv_status_5);
        tv_status_6 = (TextView) rootView.findViewById(R.id.tv_status_6);
        //tv_status_7 = (TextView) rootView.findViewById(R.id.tv_status_7);
        //tv_status_8 = (TextView) rootView.findViewById(R.id.tv_status_8);

        tv_status_1.setOnClickListener(this);
        tv_status_2.setOnClickListener(this);
        tv_status_3.setOnClickListener(this);
        tv_status_4.setOnClickListener(this);
        tv_status_5.setOnClickListener(this);
        tv_status_6.setOnClickListener(this);
        //tv_status_7.setOnClickListener(this);
        //tv_status_8.setOnClickListener(this);

        textViews = new ArrayList<>();
        textViews.add(tv_status_1);
        textViews.add(tv_status_2);
        textViews.add(tv_status_3);
        textViews.add(tv_status_4);
        textViews.add(tv_status_5);
        textViews.add(tv_status_6);
        //textViews.add(tv_status_7);
        //textViews.add(tv_status_8);

        duration = 200;
        iv_expand_icon_status = (ImageView) rootView.findViewById(R.id.iv_expand_icon_status);
        contentLayout.setVisibility(GONE);
        iv_expand_icon_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimationRunning) {
                    if (contentLayout.getVisibility() == VISIBLE)
                        collapse(contentLayout);
                    else
                        expand(contentLayout);
                    isAnimationRunning = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationRunning = false;
                        }
                    }, duration);
                }
            }
        });
    }

    private int currentSelectIndex = 8;// 0 - 7
    private boolean isHasSelect = false;

    public boolean isSelect() {
        return isHasSelect;
    }

    public String getSelectVuew() {
        return textViews.get(currentSelectIndex).getText().toString().trim();
    }

    public void initSelectStatus(String value) {
        for (int i = 0; i < textViews.size(); i++) {
            if (value.endsWith(textViews.get(i).getText().toString().trim())) {
                selectIndex(i);
                break;
            }
        }
    }

    public void reSet() {
        currentSelectIndex =8;
        isHasSelect = false;
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setBackgroundResource(R.color.white);
        }
    }


    /**
     * 选中状态
     *
     * @param index
     */
    public void selectIndex(int index) {
        if (currentSelectIndex == index) {
            if (isHasSelect) {
                isHasSelect = false;
                for (TextView textView : textViews) {
                    textView.setBackgroundResource(R.color.white);
                }
            } else {
                isHasSelect = true;
                for (int i = 0; i < textViews.size(); i++) {
                    if (i == index) {
                        textViews.get(i).setBackgroundResource(R.color.orange);
                    } else {
                        textViews.get(i).setBackgroundResource(R.color.white);
                    }
                }
            }
        } else {
            isHasSelect = true;
            currentSelectIndex = index;
            for (int i = 0; i < textViews.size(); i++) {
                if (i == index) {
                    textViews.get(i).setBackgroundResource(R.color.orange);
                } else {
                    textViews.get(i).setBackgroundResource(R.color.white);
                }
            }
        }
    }

    private void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(VISIBLE);

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1)
                    isOpened = true;
                v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(duration);
        v.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelper.getRotationPositive180Animator(iv_expand_icon_status).start();
            }
        }, duration);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    isOpened = false;
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(duration);
        v.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelper.getRotatioNegative180Animator(iv_expand_icon_status).start();
            }
        }, duration);
    }

    public Boolean isOpened() {
        return isOpened;
    }

    public void show() {
        if (!isAnimationRunning) {
            expand(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    public FrameLayout getHeaderLayout() {
        return headerLayout;
    }

    public FrameLayout getContentLayout() {
        return contentLayout;
    }

    public void hide() {
        if (!isAnimationRunning) {
            collapse(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_status_1:
                selectIndex(0);
                break;
            case R.id.tv_status_2:
                selectIndex(1);
                break;
            case R.id.tv_status_3:
                selectIndex(2);
                break;
            case R.id.tv_status_4:
                selectIndex(3);
                break;
            case R.id.tv_status_5:
                selectIndex(4);
                break;
            case R.id.tv_status_6:
                selectIndex(5);
                break;
//            case R.id.tv_status_7:
//                selectIndex(6);
//                break;
//            case R.id.tv_status_8:
//                selectIndex(7);
//                break;

        }
    }

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.dirtionary_getproductstatuslistbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        aCache.put(Constans.TAG_GOOD_STATUS, resultObj);
                        JSONArray rows = resultObj.getJSONArray("rows");
                        ArrayList<ShangPinStatusBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ShangPinStatusBean>>() {
                        }.getType());
                        shangPinStatusBeens.addAll(allDataArrayList);
                        tv_status_1.setText(shangPinStatusBeens.get(0).getItem_name());
                        tv_status_2.setText(shangPinStatusBeens.get(1).getItem_name());
                        tv_status_3.setText(shangPinStatusBeens.get(2).getItem_name());
                        tv_status_4.setText(shangPinStatusBeens.get(3).getItem_name());
                        tv_status_5.setText(shangPinStatusBeens.get(4).getItem_name());
                        tv_status_6.setText(shangPinStatusBeens.get(5).getItem_name());
                        //tv_status_7.setText(shangPinStatusBeens.get(6).getItem_name());
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                UIHelper.t(mContext, R.string.net_error);
            }
        });
    }
}
