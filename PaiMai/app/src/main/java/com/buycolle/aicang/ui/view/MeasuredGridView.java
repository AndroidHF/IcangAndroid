package com.buycolle.aicang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Lance on 2015/9/17.
 * 固定长度的GridView，用于listview的嵌套
 * 测试android studio svn commit
 */
public class MeasuredGridView extends GridView{

    public MeasuredGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public MeasuredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MeasuredGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
