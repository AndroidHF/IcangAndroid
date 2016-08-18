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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ShangPinFilterBean;
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

/**
 * 分类展开自定义
 */
public class FilterExpandableLayout extends RelativeLayout implements View.OnClickListener {
    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private Integer duration;
    private FrameLayout headerLayout;
    private Animation animation;
    private Activity mContext;
    private ACache aCache;
    private MainApplication mApplication;
    private ArrayList<ShangPinFilterBean> shangPinFilterBeens;


    private TextView tv_filter_1;
    private TextView tv_filter_2;
    private TextView tv_filter_3;
    private TextView tv_filter_4;


    private ArrayList<TextView> textViews;

    public FilterExpandableLayout(Context context) {
        super(context);
        this.mContext = (Activity) context;
    }

    public FilterExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    public FilterExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = (Activity) context;
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.view_expandable_filter, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout_status);
        aCache = ACache.get(mContext);
        mApplication = (MainApplication) mContext.getApplication();
        shangPinFilterBeens = new ArrayList<>();
        loadData();


        tv_filter_1 = (TextView) rootView.findViewById(R.id.tv_filter_1);
        tv_filter_2 = (TextView) rootView.findViewById(R.id.tv_filter_2);
        tv_filter_3 = (TextView) rootView.findViewById(R.id.tv_filter_3);
        tv_filter_4 = (TextView) rootView.findViewById(R.id.tv_filter_4);

        tv_filter_1.setOnClickListener(this);
        tv_filter_2.setOnClickListener(this);
        tv_filter_3.setOnClickListener(this);
        tv_filter_4.setOnClickListener(this);

        textViews = new ArrayList<>();
        textViews.add(tv_filter_1);
        textViews.add(tv_filter_2);
        textViews.add(tv_filter_3);
        textViews.add(tv_filter_4);

        duration = 200;
    }

    private int currentSelectIndex = 4;//0 - 3
    private int currentSelectValue = 0;
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
                selectIndex2(i);
                break;
            }
        }
    }

    public void reSet() {
        currentSelectIndex =4;
        isHasSelect = false;
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setBackgroundResource(R.drawable.shape_black_shangpin_state);
        }
    }


    /**
     * 选中状态
     *
     * @param index
     */
//    public void selectIndex(int index) {
//        if (currentSelectIndex == index) {
//            if (isHasSelect) {
//                isHasSelect = false;
//                for (TextView textView : textViews) {
//                    textView.setBackgroundResource(R.drawable.shape_black_shangpin_state);
//                }
//            } else {
//                isHasSelect = true;
//                for (int i = 0; i < textViews.size(); i++) {
//                    if (i == index) {
//                        textViews.get(i).setBackgroundResource(R.drawable.shape_orange_shangpin_state);
//                    } else {
//                        textViews.get(i).setBackgroundResource(R.drawable.shape_black_shangpin_state);
//                    }
//                }
//            }
//        } else {
//            isHasSelect = true;
//            currentSelectIndex = index;
//            for (int i = 0; i < textViews.size(); i++) {
//                if (i == index) {
//                    textViews.get(i).setBackgroundResource(R.drawable.shape_orange_shangpin_state);
//                } else {
//                    textViews.get(i).setBackgroundResource(R.drawable.shape_black_shangpin_state);
//                }
//            }
//        }
//    }

    public void selectIndex2(int index) {
        if (isHasSelect){
            isHasSelect = false;
            textViews.get(index).setBackgroundResource(R.drawable.shape_orange_shangpin_state);
           Log.i("id---------", shangPinFilterBeens.get(index).getDir_id()+"");
        }else {
            isHasSelect = true;
            textViews.get(index).setBackgroundResource(R.drawable.shape_black_shangpin_state);
        }
    }

//    private void expand(final View v) {
//        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        final int targetHeight = v.getMeasuredHeight();
//        v.getLayoutParams().height = 0;
//        v.setVisibility(VISIBLE);
//
//        animation = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1)
//                    isOpened = true;
//                v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
//                v.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//        animation.setDuration(duration);
//        v.startAnimation(animation);
//    }

//    private void collapse(final View v) {
//        final int initialHeight = v.getMeasuredHeight();
//        animation = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1) {
//                    v.setVisibility(View.GONE);
//                    isOpened = false;
//                } else {
//                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
//                    v.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        animation.setDuration(duration);
//        v.startAnimation(animation);
//    }

//    public Boolean isOpened() {
//        return isOpened;
//    }


    public FrameLayout getHeaderLayout() {
        return headerLayout;
    }



//    @Override
//    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
//        animation.setAnimationListener(animationListener);
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_filter_1:
                selectIndex2(0);
                break;
            case R.id.tv_filter_2:
                selectIndex2(1);
                break;
            case R.id.tv_filter_3:
                selectIndex2(2);
                break;
            case R.id.tv_filter_4:
                selectIndex2(3);
                break;
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
        mApplication.apiClient.dirtionary_getFilterAndSortListByApp(jsonObject, new ApiCallback() {
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
                        ArrayList<ShangPinFilterBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ShangPinFilterBean>>() {
                        }.getType());
                        shangPinFilterBeens.addAll(allDataArrayList);
                            if (shangPinFilterBeens.get(0).getDir_key().equals("filter_type")){
                                tv_filter_1.setText(shangPinFilterBeens.get(0).getItem_name());
                            }

                            if (shangPinFilterBeens.get(1).getDir_key().equals("filter_type")){
                                tv_filter_2.setText(shangPinFilterBeens.get(1).getItem_name());
                            }

                            if (shangPinFilterBeens.get(2).getDir_key().equals("filter_type")){
                                tv_filter_3.setText(shangPinFilterBeens.get(2).getItem_name());
                            }

                            if (shangPinFilterBeens.get(3).getDir_key().equals("filter_type")){
                                tv_filter_4.setText(shangPinFilterBeens.get(3).getItem_name());
                            }

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
