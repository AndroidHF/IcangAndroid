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
import android.widget.ImageView;
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
public class SortExpandableLayout extends RelativeLayout implements View.OnClickListener {
    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private Integer duration;
    private FrameLayout headerLayout;
    private Animation animation;
    private Activity mContext;
    private ACache aCache;
    private MainApplication mApplication;
    private ArrayList<ShangPinFilterBean> shangPinFilterBeens;



    private TextView tv_sort_1;//默认排序
    private TextView tv_sort_2;//剩余时间
    private TextView tv_sort_3;//当前价格
    private TextView tv_sort_4;//竞拍人数
    private TextView tv_sort_5;//卖家好评

    private ImageView iv_moren;
    private ImageView iv_lost_time;
    private ImageView iv_current_price;
    private ImageView iv_user_pin;
    private ImageView iv_good_pin;

    private int currentPosition = 0;
    private boolean isUp = false;
    private int sort_item = 0;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private int sort_value = 1;// A:升序 B：降序


    private ArrayList<TextView> textViews;
    private ArrayList<ImageView> imageViews;
    private ArrayList<Integer> Sort_id;//存放sort_id的控件

    public SortExpandableLayout(Context context) {
        super(context);
        this.mContext = (Activity) context;
    }

    public SortExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    public SortExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.view_expandable_sort, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout_status);
        aCache = ACache.get(mContext);
        mApplication = (MainApplication) mContext.getApplication();
        shangPinFilterBeens = new ArrayList<>();
        Sort_id = new ArrayList<>();
        loadData();


        tv_sort_1 = (TextView) rootView.findViewById(R.id.tv_sort_1);
        tv_sort_2 = (TextView) rootView.findViewById(R.id.tv_sort_2);
        tv_sort_3 = (TextView) rootView.findViewById(R.id.tv_sort_3);
        tv_sort_4 = (TextView) rootView.findViewById(R.id.tv_sort_4);
        tv_sort_5 = (TextView) rootView.findViewById(R.id.tv_sort_5);

        iv_moren = (ImageView) rootView.findViewById(R.id.iv_good_moren);
        iv_lost_time = (ImageView) rootView.findViewById(R.id.iv_lost_time);
        iv_current_price = (ImageView) rootView.findViewById(R.id.iv_current_price);
        iv_user_pin = (ImageView) rootView.findViewById(R.id.iv_current_user);
        iv_good_pin = (ImageView) rootView.findViewById(R.id.iv_good_pin);

        tv_sort_1.setOnClickListener(this);
        tv_sort_2.setOnClickListener(this);
        tv_sort_3.setOnClickListener(this);
        tv_sort_4.setOnClickListener(this);
        tv_sort_5.setOnClickListener(this);

        textViews = new ArrayList<>();
        imageViews = new ArrayList<>();
        textViews.add(tv_sort_1);
        textViews.add(tv_sort_2);
        textViews.add(tv_sort_3);
        textViews.add(tv_sort_4);
        textViews.add(tv_sort_5);

        imageViews.add(iv_moren);
        imageViews.add(iv_lost_time);
        imageViews.add(iv_current_price);
        imageViews.add(iv_user_pin);
        imageViews.add(iv_good_pin);
        initStatus(1);
        iniTitleImagesBySelect(0);

        duration = 200;
    }

    private boolean isHasSelect = false;

    public boolean isSelect() {
        return isHasSelect;
    }



    public void reSet() {
        initStatus(1);
        iniTitleImagesBySelect(0);
    }


    public void initSelectStatus(String value) {
        for (int i = 0; i < textViews.size(); i++) {
            if (value.endsWith(textViews.get(i).getText().toString().trim())) {
                initStatus(i);
                iniTitleImagesBySelect(i);
                break;
            }
        }
    }

    public void iniTitleTvColor(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                textViews.get(i).setTextColor(getResources().getColor(R.color.tv_textview_select));
            } else {
                textViews.get(i).setTextColor(getResources().getColor(R.color.tv_textview_noselect));
            }
        }
    }

    public void initStatus(int index) {
        if (index == 1) {
            iniTitleTvColor(0);
        } else if (index == 2) {
            iniTitleTvColor(1);
        } else if (index == 3) {
            iniTitleTvColor(2);
        } else if(index == 4){
            iniTitleTvColor(3);
        }else {
            iniTitleTvColor(4);
        }
    }

    private void iniTitleImagesBySelect(int index) {
        for (int i = 0; i < imageViews.size(); i++) {
            if (index == i) {
                imageViews.get(i).setVisibility(View.VISIBLE);
                if (currentPosition == index) {
                    if (isUp) {
                        imageViews.get(i).setImageResource(R.drawable.sort_up);
                        isUp = false;
                    } else {
                        imageViews.get(i).setImageResource(R.drawable.sort_low);
                        isUp = true;
                    }
                } else {
                    currentPosition = index;
                    isUp = true;
                    imageViews.get(i).setImageResource(R.drawable.sort_low);
                }
            } else {
                imageViews.get(i).setVisibility(View.GONE);
            }
        }
        if (index == 0) {
            sort_item = 1;
        } else if (index == 1) {
            sort_item = 2;
        } else if (index == 2) {
            sort_item = 3;
        } else if (index == 3) {
            sort_item = 4;
        }else if (index == 4){
            sort_item = 5;
        }
        if(index==0){
            imageViews.get(0).setVisibility(View.GONE);
            sort_value = 2;
        }else if (index == 4){
            imageViews.get(4).setImageResource(R.drawable.sort_low);
            sort_value = 2;
        }else {
            sort_value = isUp ? 1 : 2;
        }
//        pageIndex = 1;
//        loadData(false);
        Log.i("sort_value",sort_value+"");
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



    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_sort_1:
                initStatus(1);
                iniTitleImagesBySelect(0);
                break;
            case R.id.tv_sort_2:
                initStatus(2);
                iniTitleImagesBySelect(1);
                break;
            case R.id.tv_sort_3:
                initStatus(3);
                iniTitleImagesBySelect(2);
                break;
            case R.id.tv_sort_4:
                initStatus(4);
                iniTitleImagesBySelect(3);
                break;
            case R.id.tv_sort_5:
                initStatus(5);
                iniTitleImagesBySelect(4);
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
                            if (shangPinFilterBeens.get(4).getDir_key().equals("sort_type")){
                                tv_sort_1.setText(shangPinFilterBeens.get(4).getItem_name());
                                Log.i("id2-----", shangPinFilterBeens.get(4).getDir_id() + "");
                            }

                            if (shangPinFilterBeens.get(5).getDir_key().equals("sort_type")){
                                tv_sort_2.setText(shangPinFilterBeens.get(5).getItem_name());
                                Log.i("id2-----", shangPinFilterBeens.get(5).getDir_id() + "");
                            }

                            if (shangPinFilterBeens.get(6).getDir_key().equals("sort_type")){
                                tv_sort_3.setText(shangPinFilterBeens.get(6).getItem_name());
                                Log.i("id2-----", shangPinFilterBeens.get(6).getDir_id() + "");
                            }

                            if (shangPinFilterBeens.get(7).getDir_key().equals("sort_type")){
                                tv_sort_4.setText(shangPinFilterBeens.get(7).getItem_name());
                                Log.i("id2-----", shangPinFilterBeens.get(7).getDir_id() + "");
                            }

                            if (shangPinFilterBeens.get(8).getDir_key().equals("sort_type")){
                                tv_sort_5.setText(shangPinFilterBeens.get(8).getItem_name());
                                Log.i("id2-----", shangPinFilterBeens.get(8).getDir_id() + "");
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
