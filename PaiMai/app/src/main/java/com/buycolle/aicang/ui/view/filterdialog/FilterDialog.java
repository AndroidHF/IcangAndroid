package com.buycolle.aicang.ui.view.filterdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.ShangPinFilterBean;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public class FilterDialog {

    private static final String[] TAG = new String[]{
            "有一口价", "包邮", "热门拍品", "即将结束"
    };

    private static final String[] ORDER = new String[]{
            "默认排序", "默认排序", "默认排序", "默认排序", "默认排序"
    };

    private Context mContext;

    private static FilterDialog mInstance;

    private Dialog mDialog;

    private DialogConfirmListener mConfirmListener;

    private FilterTagAdapter   mTagAdapter;
    private FilterOrderAdapter mOrderAdapter;

    private boolean mIsFisret = true;

    public interface DialogConfirmListener {

        void onConfirm(String filter_id, String sort_id);
    }

    private FilterDialog() {

    }

    private ArrayList<ShangPinFilterBean> mShangPinFilterBeen;

    private List<ShangPinFilterBean> mTags;
    private List<ShangPinFilterBean> mOrders;

    public static FilterDialog getInstance() {
        if (null == mInstance) {
            mInstance = new FilterDialog();
        }
        return mInstance;
    }

    public void show(Context context, View view, DialogConfirmListener confirmListener) {
        mConfirmListener = confirmListener;

        if (mDialog == null) {
            mContext = context;
            mDialog = new Dialog(context, R.style.OptDialog);
        }
        mDialog.setContentView(R.layout.dialog_home_filter);

        Window window = mDialog.getWindow();
        //        window.setWindowAnimations(R.style.share_animation);
        window.setGravity(Gravity.TOP);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.x = (int) view.getX();
        params.y = (int) view.getY() + view.getHeight();
        window.setAttributes(params);

        if (mIsFisret) {
            init();
        }else {
            mIsFisret = false;
        }

        mDialog.show();
    }

    public void show(){

    }

    private void init() {
        RecyclerView rvTag = getView(R.id.rv_dialog_filter_tag);
        rvTag.setLayoutManager(new GridLayoutManager(mContext, 3));
        mTagAdapter = new FilterTagAdapter(mContext, new ArrayList<String>());
        rvTag.setAdapter(mTagAdapter);

        RecyclerView rvOrder = getView(R.id.rv_dialog_filter_order);
        rvOrder.setLayoutManager(new GridLayoutManager(mContext, 3));
        mOrderAdapter = new FilterOrderAdapter(mContext, new ArrayList<String>());
        rvOrder.setAdapter(mOrderAdapter);

        Button btnReset = getView(R.id.btn_reset);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagAdapter.resetData();
                mOrderAdapter.resetData();
            }
        });

        Button btnConfirm = getView(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    boolean[] isSelcted = mTagAdapter.mIsSelected;
                    StringBuilder sbTag = new StringBuilder();
                    for (int i = 0; i < isSelcted.length; i++) {
                        if (isSelcted[i]) {
                            sbTag.append(mTags.get(i).getDir_id());
                            sbTag.append(",");
                        }
                    }
                    if (!TextUtils.isEmpty(sbTag)) {
                        sbTag.delete(sbTag.length() - 1, sbTag.length());
                    }

                    StringBuilder sbOrder = new StringBuilder();
                    boolean[] isOrderSelcetd = mOrderAdapter.mIsSelected;
                    for (int i = 0; i < isOrderSelcetd.length; i++) {
                        if (isOrderSelcetd[i]) {
                            int id = mOrders.get(i).getDir_id();
                            sbOrder.append(id);
                            if (id == 46 || id == 50) {
                                sbOrder.append("A");
                            } else {
                                sbOrder.append(mOrderAdapter.mIsChecked ? "B" : "A");
                            }
                            break;
                        }
                    }

                    mConfirmListener.onConfirm(sbTag.toString(), sbOrder.toString());
                    dismiss();
                }
            }
        });

    }

    private <T extends View> T getView(int resId) {
        return (T) mDialog.findViewById(resId);
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    public void destory(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void setData(ArrayList<ShangPinFilterBean> allDataArrayList) {
        mShangPinFilterBeen = new ArrayList<>();
        mShangPinFilterBeen.addAll(allDataArrayList);
        mTags = new ArrayList<>();
        mOrders = new ArrayList<>();
        List<String> tag = new ArrayList<>();
        List<String> order = new ArrayList<>();
        for (int i = 0; i < mShangPinFilterBeen.size(); i++) {
            ShangPinFilterBean bean = mShangPinFilterBeen.get(i);
            if (bean.getDir_key().equals("filter_type")) {
                tag.add(bean.getItem_name());
                mTags.add(bean);
            } else if (bean.getDir_key().equals("sort_type")) {
                order.add(bean.getItem_name());
                mOrders.add(bean);
            }
        }
        mTagAdapter.addAll(tag);
        mOrderAdapter.addAll(order, mOrders);
    }

}
