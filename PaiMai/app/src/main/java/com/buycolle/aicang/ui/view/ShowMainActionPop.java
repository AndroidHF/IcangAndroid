package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;


/**
 * Created by joean on 16/2/28.
 */
public class ShowMainActionPop {

    protected Activity mContext;
    protected PopupWindow mWindow;
    protected View mRootView;
    protected WindowManager mWindowManager;
    private LayoutInflater inflater;
    private boolean actionTop = false;

    private LinearLayout scroll;
    private RelativeLayout parent;
    private TextView tv_content;

    public ShowMainActionPop(Activity context) {
        mContext = context;
        mWindow = new PopupWindow(context);
        mWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = (ViewGroup) inflater
                .inflate(R.layout.view_show_main_aciton, null);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mWindow.setContentView(mRootView);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        parent = (RelativeLayout) mRootView.findViewById(R.id.parent);
        scroll = (LinearLayout) mRootView.findViewById(R.id.scroll);
        tv_content = (TextView) mRootView.findViewById(R.id.tv_content);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    /**
     * 是否显示在上面
     *
     * @param actionTop
     */
    public ShowMainActionPop setGra(boolean actionTop) {
        this.actionTop = actionTop;
        return this;
    }

    public ShowMainActionPop setType(boolean edit) {
        if (edit) {
            tv_content.setText("修改拍品的标题、\n照片和描述，\n需要重新经过审核");
        } else {
            tv_content.setText("只修改拍品的物流信息\n和价格，无需重新审核");
        }
        return this;
    }

    public ShowMainActionPop setMsg(String content) {
        tv_content.setText(content);
        return this;
    }

    public void show(View anchor) {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());
        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int rootWidth = mRootView.getMeasuredWidth();
        int rootHeight = mRootView.getMeasuredHeight();
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        int xPos = (screenWidth - rootWidth) / 2;
        int yPos = anchorRect.top - rootHeight;
        boolean onTop = true;
        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;
        if (actionTop) {// 尽量显示上面，，分享
            onTop = (dyTop > rootHeight) ? true : false;
        } else {//对应操作按钮享
            onTop = (dyBottom < rootHeight) ? true : false;
        }
        if (!onTop) {
            yPos = anchorRect.bottom;
        }
        showArrow(0, anchorRect.centerX());
        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                : R.style.Animations_PopDownMenu_Center);
        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    private void showArrow(int whichArrow, int requestedX) {
        final int scrollWith = scroll.getMeasuredWidth();
        ViewGroup.MarginLayoutParams scrollparam = (ViewGroup.MarginLayoutParams) scroll
                .getLayoutParams();
        scrollparam.leftMargin = requestedX - scrollWith / 2;
    }


    /**
     * 菜单隐藏
     */
    public void dismiss() {
        if (mWindow != null)
            mWindow.dismiss();
    }

}
