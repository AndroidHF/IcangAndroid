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

import com.buycolle.aicang.R;
import com.buycolle.aicang.util.AnimationHelper;

import java.util.ArrayList;


/**
 * 分类展开自定义
 */
public class TypesExpandableLayout extends RelativeLayout implements View.OnClickListener {
    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private Integer duration;
    private FrameLayout contentLayout;
    private FrameLayout headerLayout;
    private Animation animation;
    private Activity mContext;

    private ImageView iv_expand_icon;

    private ImageView iv_type_1;
    private ImageView iv_type_2;
    private ImageView iv_type_3;
    private ImageView iv_type_4;
    private ImageView iv_type_5;
    private ImageView iv_type_6;
    private ImageView iv_type_7;
    private ImageView iv_type_8;
    private ImageView iv_type_9;

    private ArrayList<ImageView> typesImages;

    private int currentSelectIndex = 9;
    private int currentSelectValue = 0;
    private boolean isHasSelect = false;


    public TypesExpandableLayout(Context context) {
        super(context);
        this.mContext = (Activity) context;
    }

    public TypesExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    public TypesExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = (Activity) context;
        init(context, attrs);
    }

    public int getCurrentSelectValue() {
        return currentSelectValue;
    }

    public boolean getIsSelect() {
        return isHasSelect;
    }

    /**
     * 重置
     */
    public void reSetStatus() {
        currentSelectIndex = 9;
        currentSelectValue = 0;
        isHasSelect = false;

        int[] defout = new int[]{R.drawable.show_menu_1,//
                R.drawable.show_menu_2,//
                R.drawable.show_menu_3,//
                R.drawable.show_menu_4,//
                R.drawable.show_menu_6,//
                R.drawable.show_menu_5,//
                R.drawable.show_menu_7,//
                R.drawable.show_menu_8,//
                R.drawable.another_icon,//
        };
        for (int i = 0; i < defout.length; i++) {
            typesImages.get(i).setImageResource(defout[i]);
        }
    }

    /**
     * 选中状态
     *
     * @param index
     */
    public void selectIndex(int index) {
        int[] defout = new int[]{R.drawable.show_menu_1,//
                R.drawable.show_menu_2,//
                R.drawable.show_menu_3,//
                R.drawable.show_menu_4,//
                R.drawable.show_menu_6,//
                R.drawable.show_menu_5,//
                R.drawable.show_menu_7,//
                R.drawable.show_menu_8,//
                R.drawable.another_icon,//
        };
        int[] select = new int[]{R.drawable.show_menu_1_sel,//
                R.drawable.show_menu_2_sel,//
                R.drawable.show_menu_3_sel,//
                R.drawable.show_menu_4_sel,//
                R.drawable.show_menu_6_sel,//
                R.drawable.show_menu_5_sel,//
                R.drawable.show_menu_7_sel,//
                R.drawable.show_menu_8_sel,//
                R.drawable.another_icon_sel,//
        };

        if (currentSelectIndex == index) {
            if (isHasSelect) {
                isHasSelect = false;
                currentSelectValue = 0;
                for (int i = 0; i < defout.length; i++) {
                    typesImages.get(i).setImageResource(defout[i]);
                }
            } else {
                isHasSelect = true;
                currentSelectValue = currentSelectIndex + 1;
                for (int i = 0; i < defout.length; i++) {
                    if (i == index) {
                        typesImages.get(i).setImageResource(select[i]);
                    } else {
                        typesImages.get(i).setImageResource(defout[i]);
                    }
                }
            }
        } else {
            isHasSelect = true;
            currentSelectIndex = index;
            currentSelectValue = currentSelectIndex + 1;

            for (int i = 0; i < defout.length; i++) {
                if (i == index) {
                    typesImages.get(i).setImageResource(select[i]);
                } else {
                    typesImages.get(i).setImageResource(defout[i]);
                }
            }
        }
    }

    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.view_expandable_types, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout_types);
        contentLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_contentLayout_types);

        typesImages = new ArrayList<>();
        iv_type_1 = (ImageView) rootView.findViewById(R.id.iv_type_1);
        iv_type_2 = (ImageView) rootView.findViewById(R.id.iv_type_2);
        iv_type_3 = (ImageView) rootView.findViewById(R.id.iv_type_3);
        iv_type_4 = (ImageView) rootView.findViewById(R.id.iv_type_4);
        iv_type_5 = (ImageView) rootView.findViewById(R.id.iv_type_5);
        iv_type_6 = (ImageView) rootView.findViewById(R.id.iv_type_6);
        iv_type_7 = (ImageView) rootView.findViewById(R.id.iv_type_7);
        iv_type_8 = (ImageView) rootView.findViewById(R.id.iv_type_8);
        iv_type_9 = (ImageView) rootView.findViewById(R.id.iv_type_9);
        iv_type_1.setOnClickListener(this);
        iv_type_2.setOnClickListener(this);
        iv_type_3.setOnClickListener(this);
        iv_type_4.setOnClickListener(this);
        iv_type_5.setOnClickListener(this);
        iv_type_6.setOnClickListener(this);
        iv_type_7.setOnClickListener(this);
        iv_type_8.setOnClickListener(this);
        iv_type_9.setOnClickListener(this);


        typesImages.add(iv_type_1);
        typesImages.add(iv_type_2);
        typesImages.add(iv_type_3);
        typesImages.add(iv_type_4);
        typesImages.add(iv_type_6);
        typesImages.add(iv_type_5);
        typesImages.add(iv_type_7);
        typesImages.add(iv_type_8);
        typesImages.add(iv_type_9);


        duration = 200;
        iv_expand_icon = (ImageView) rootView.findViewById(R.id.iv_expand_icon);
        contentLayout.setVisibility(GONE);
        iv_expand_icon.setOnClickListener(new OnClickListener() {
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
                AnimationHelper.getRotationPositive180Animator(iv_expand_icon).start();
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
                AnimationHelper.getRotatioNegative180Animator(iv_expand_icon).start();
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
            case R.id.iv_type_1:
                selectIndex(0);
                break;
            case R.id.iv_type_2:
                selectIndex(1);
                break;
            case R.id.iv_type_3:
                selectIndex(2);
                break;
            case R.id.iv_type_4:
                selectIndex(3);
                break;
            case R.id.iv_type_5:
                selectIndex(5);
                break;
            case R.id.iv_type_6:
                selectIndex(4);
                break;
            case R.id.iv_type_7:
                selectIndex(6);
                break;
            case R.id.iv_type_8:
                selectIndex(7);
                break;
            case R.id.iv_type_9:
                selectIndex(8);
                break;
        }
    }
}
