package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.ShangPinStatusBean;
import com.buycolle.aicang.ui.view.expandableLayout.FilterExpandableLayout;
import com.buycolle.aicang.ui.view.expandableLayout.SortExpandableLayout;
import com.buycolle.aicang.util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/3/7.
 */
public class MainFilterDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private LinearLayout rl_cancle_lay;



    private TextView tv_finish;
    private TextView tv_reset;
    private FilterExpandableLayout expand_filter;
    private SortExpandableLayout expand_sort;

    private ACache aCache;

    private ArrayList<ShangPinStatusBean> shangPinTypeBeens;


    public MainFilterDialog(Context context, //
                            boolean isSelectCate_Id, //
                            int cate_id, //
                            String st_id
                           ) {
        super(context, R.style.search_menu_dialog);
        this.mContext = context;
        this.mActivity = (Activity) context;
        init(context);

        aCache = ACache.get(mContext);

        this.isSelectCate_Id = isSelectCate_Id;
        this.cate_id = cate_id;
        this.st_id = st_id;

        JSONObject resultObj = aCache.getAsJSONObject(Constans.TAG_GOOD_STATUS);
        shangPinTypeBeens = new ArrayList<>();
        if (resultObj != null) {
            try {
                JSONArray rows = resultObj.getJSONArray("rows");
                ArrayList<ShangPinStatusBean> allDataArrayList = new Gson().fromJson(rows.toString(), new TypeToken<List<ShangPinStatusBean>>() {
                }.getType());
                shangPinTypeBeens.addAll(allDataArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(st_id)) {
            for (ShangPinStatusBean shangPinTypeBeen : shangPinTypeBeens) {
                if (shangPinTypeBeen.getDir_id().equals(st_id)) {
                    expand_filter.initSelectStatus(shangPinTypeBeen.getItem_name());
                    expand_sort.initSelectStatus(shangPinTypeBeen.getItem_name());
                }
            }
        }

    }

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类
    private String st_id = "";//状态ID集

    private void init(Context context) {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.RIGHT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View view = View.inflate(context, R.layout.dialog_paipin_search_filter_new, null);

        rl_cancle_lay = (LinearLayout) view.findViewById(R.id.rl_cancle_lay);
        tv_finish = (TextView) view.findViewById(R.id.tv_finish);
        tv_reset = (TextView) view.findViewById(R.id.tv_reset);
        expand_filter = (FilterExpandableLayout) view.findViewById(R.id.expand_filter);
        expand_sort = (SortExpandableLayout) view.findViewById(R.id.expand_sort);
        this.setContentView(view);

        rl_cancle_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dismiss();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/4/12 重置
                reset();
            }
        });

    }

    private void reset() {
        // TODO: 16/4/12 状态的保留
        expand_filter.reSet();
        expand_sort.reSet();
    }

    @Override
    public void onClick(View view) {


    }

    private CallBack callBack;

    public MainFilterDialog setCallBack(CallBack call) {
        this.callBack = call;
        return this;
    }

    public interface CallBack {
        void action(boolean isSelectCate_Id, int cate_id,String st_id);
    }
}
