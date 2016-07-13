package com.buycolle.aicang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;


/**
 * Created by joean on 16/2/28.
 */
public class MyExpandTextView extends FrameLayout {

    TextView contentView_tv;
    LinearLayout openViewLay;
    ImageView openView;
    protected boolean isExpand = false; // 显示或收起标记
    private int defaultLine = 10; // 显示的行数,超过就隐藏
    private boolean isHasExpand = false; //是否需要判断显示更多的标示

    public MyExpandTextView(Context context) {
        super(context);
        initView(context);
    }

    public MyExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_expand_text_layout, this);
        contentView_tv = (TextView) findViewById(R.id.content_text);
        openViewLay = (LinearLayout) findViewById(R.id.ll_expand_lay);
        openView = (ImageView) findViewById(R.id.open_view);
        // 监听显示或收起按钮事件
        openViewLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(isExpand);
                }
                if (isExpand) {
                    contentView_tv.setLines(contentView_tv.getLineCount());
                    openView.setImageResource(R.drawable.usercenter_notice_arrow_up);
                } else {
                    contentView_tv.setLines(defaultLine);
                    openView.setImageResource(R.drawable.usercenter_notice_arrow_down);
                }
            }
        });
    }

    public void setText(String str) {
        contentView_tv.setText(str);
        int count = contentView_tv.getLayout() == null ? getLineNumber()
                : contentView_tv.getLineCount();
        if (count > defaultLine) {
            isHasExpand = true;
            contentView_tv.setLines(defaultLine);
            openViewLay.setVisibility(View.VISIBLE);
        } else {
            isHasExpand = false;
            contentView_tv.setLines(count);
            openViewLay.setVisibility(View.GONE);
        }
    }

    // 文本框的占用的行数
    private int getLineNumber() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        contentView_tv.measure(widthMeasureSpec, heightMeasureSpec);
        int lineHeight = contentView_tv.getLineHeight();
        int lineNumber = contentView_tv.getMeasuredHeight() / lineHeight;
        return lineNumber;
    }

    public interface OnStateChangeListener {
        void onStateChange(boolean isExpand);
    }

    public OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(
            OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public TextView getContentView_tv() {
        return contentView_tv;
    }

    //状态
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (isHasExpand) {
            if (isExpand) {
                contentView_tv.setLines(contentView_tv.getLineCount());
                openView.setImageResource(R.drawable.usercenter_notice_arrow_up);
            } else {
                contentView_tv.setLines(defaultLine);
                openView.setImageResource(R.drawable.usercenter_notice_arrow_down);
            }
        }
    }
}
