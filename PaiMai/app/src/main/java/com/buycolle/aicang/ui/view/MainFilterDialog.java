package com.buycolle.aicang.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ShangPinFilterBean;
import com.buycolle.aicang.bean.ShangPinStatusBean;
import com.buycolle.aicang.ui.view.expandableLayout.FilterExpandableLayout;
import com.buycolle.aicang.ui.view.expandableLayout.SortExpandableLayout;
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
 * Created by joe on 16/3/7.
 */
public class MainFilterDialog extends Dialog implements View.OnClickListener {

    private Context                       mContext;
    private Activity                      mActivity;
    private LinearLayout                  rl_cancle_lay;
    private MainApplication               mApplication;
    private ArrayList<ShangPinFilterBean> shangPinFilterBeens;

    private TextView               tv_finish;
    private TextView               tv_reset;
    private FilterExpandableLayout expand_filter;
    private SortExpandableLayout   expand_sort;
    private TextView               tv_filter_1;
    private TextView               tv_filter_2;
    private TextView               tv_filter_3;
    private TextView               tv_filter_4;
    private FrameLayout            headerLayout;
    private RelativeLayout         rl_main;
    private ArrayList<TextView>    textViews_filter;//存放筛选的集合
    private ArrayList<Integer>     filter_dir_id;//存放筛选的dir_id;

    private TextView             tv_sort_1;//默认排序
    private TextView             tv_sort_2;//剩余时间
    private TextView             tv_sort_3;//当前价格
    private TextView             tv_sort_4;//竞拍人数
    private TextView             tv_sort_5;//卖家好评
    private ArrayList<TextView>  textViews_sort;//存放排序标题的集合
    private ArrayList<ImageView> imageViews_sort;//存放排序图标的集合
    private ArrayList<Integer>   sort_dir_id;//存放排序的dir_id;

    private ImageView iv_sort_1;
    private ImageView iv_sort_2;
    private ImageView iv_sort_3;
    private ImageView iv_sort_4;
    private ImageView iv_sort_5;

    private boolean isHasSelect = false;

    private int     currentPosition = 0;
    private boolean isUp            = false;

    private boolean isSelectCate_Id = false;
    private int cate_id;//分类
    private String st_id = "";//状态ID集
    private int    sort_item;// 0,无 1，价格排序 2,竞拍人数排序 3,剩余时间排序 4,卖家好评排序
    private String sort_value;// A:升序 B：降序,默认排序的sor_id

    private ACache aCache;

    private ArrayList<ShangPinStatusBean> shangPinTypeBeens;

    public MainFilterDialog(Context context, //
                            boolean isSelectCate_Id, //
                            int cate_id, //
                            String st_id, int sort_item, String sort_value) {
        super(context, R.style.search_menu_dialog);
        this.mContext = context;
        this.mActivity = (Activity) context;
        init(context);
        aCache = ACache.get(mContext);


        this.isSelectCate_Id = isSelectCate_Id;
        this.cate_id = cate_id;
        this.st_id = st_id;
        this.sort_item = sort_item;
        this.sort_value = sort_value;

        JSONObject resultObj = aCache.getAsJSONObject(Constans.TAG_GOOD_FILTER);
        shangPinTypeBeens = new ArrayList<>();
        if (resultObj != null) {
            try {
                JSONArray rows = resultObj.getJSONArray("rows");
                ArrayList<ShangPinStatusBean> allDataArrayList = new Gson().fromJson(rows.toString(),
                                                                                     new TypeToken<List<ShangPinStatusBean>>() {
                                                                                     }.getType());
                shangPinTypeBeens.addAll(allDataArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(st_id)) {
            for (ShangPinStatusBean shangPinTypeBeen : shangPinTypeBeens) {
                if (shangPinTypeBeen.getDir_id().equals(st_id)) {
                    //                    expand_filter.initSelectStatus(shangPinTypeBeen.getItem_name());
                    //                    expand_sort.initSelectStatus(shangPinTypeBeen.getItem_name());
                }
            }
        }

    }

    private void init(Context context) {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.RIGHT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        mApplication = (MainApplication) mActivity.getApplication();
        View view = View.inflate(context, R.layout.dialog_paipin_search_filter_new_hufeng, null);

        rl_cancle_lay = (LinearLayout) view.findViewById(R.id.rl_cancle_lay);
        tv_finish = (TextView) view.findViewById(R.id.tv_finish);
        tv_reset = (TextView) view.findViewById(R.id.tv_reset);
        rl_main = (RelativeLayout) view.findViewById(R.id.rl_dialog_parent);
        //        expand_filter = (FilterExpandableLayout) view.findViewById(R.id.expand_filter);
        //        expand_sort = (SortExpandableLayout) view.findViewById(R.id.expand_sort);

        textViews_filter = new ArrayList<>();
        tv_filter_1 = (TextView) view.findViewById(R.id.tv_filter_1);
        tv_filter_2 = (TextView) view.findViewById(R.id.tv_filter_2);
        tv_filter_3 = (TextView) view.findViewById(R.id.tv_filter_3);
        tv_filter_4 = (TextView) view.findViewById(R.id.tv_filter_4);
        textViews_filter.add(tv_filter_1);
        textViews_filter.add(tv_filter_2);
        textViews_filter.add(tv_filter_3);
        textViews_filter.add(tv_filter_4);

        textViews_sort = new ArrayList<>();
        tv_sort_1 = (TextView) view.findViewById(R.id.tv_sort_1);
        tv_sort_2 = (TextView) view.findViewById(R.id.tv_sort_2);
        tv_sort_3 = (TextView) view.findViewById(R.id.tv_sort_3);
        tv_sort_4 = (TextView) view.findViewById(R.id.tv_sort_4);
        tv_sort_5 = (TextView) view.findViewById(R.id.tv_sort_5);
        textViews_sort.add(tv_sort_1);
        textViews_sort.add(tv_sort_1);
        textViews_sort.add(tv_sort_1);
        textViews_sort.add(tv_sort_1);
        textViews_sort.add(tv_sort_1);

        imageViews_sort = new ArrayList<>();
        iv_sort_1 = (ImageView) view.findViewById(R.id.iv_good_moren);
        iv_sort_2 = (ImageView) view.findViewById(R.id.iv_lost_time);
        iv_sort_3 = (ImageView) view.findViewById(R.id.iv_current_price);
        iv_sort_4 = (ImageView) view.findViewById(R.id.iv_current_user);
        iv_sort_5 = (ImageView) view.findViewById(R.id.iv_good_pin);
        imageViews_sort.add(iv_sort_1);
        imageViews_sort.add(iv_sort_2);
        imageViews_sort.add(iv_sort_3);
        imageViews_sort.add(iv_sort_4);
        imageViews_sort.add(iv_sort_5);

        filter_dir_id = new ArrayList<>();
        sort_dir_id = new ArrayList<>();
        shangPinFilterBeens = new ArrayList<>();

        //设置监听
        tv_filter_1.setOnClickListener(this);
        tv_filter_2.setOnClickListener(this);
        tv_filter_3.setOnClickListener(this);
        tv_filter_4.setOnClickListener(this);

        tv_sort_1.setOnClickListener(this);
        tv_sort_2.setOnClickListener(this);
        tv_sort_3.setOnClickListener(this);
        tv_sort_4.setOnClickListener(this);
        tv_sort_5.setOnClickListener(this);
        //        initStatus(1);
        //        iniTitleImagesBySelect(0);

        loadData();
        this.setContentView(view);

        rl_cancle_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callBack.action(isSelectCate_Id, cate_id, st_id, sort_item, sort_value);
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
        //        expand_filter.reSet();
        //        expand_sort.reSet();
        reSetFilter();
        reSetSort();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    private CallBack callBack;

    public MainFilterDialog setCallBack(CallBack call) {
        this.callBack = call;
        return this;
    }

    public interface CallBack {

        void action(boolean isSelectCate_Id, int cate_id, String st_id, int sort_item, String sort_value);
    }

    public void selectIndex2(int index) {
        if (isHasSelect) {
            isHasSelect = false;
            textViews_filter.get(index).setBackgroundResource(R.drawable.shape_orange_shangpin_state);
            Log.i("id---------", shangPinFilterBeens.get(index).getDir_id() + "");
        } else {
            isHasSelect = true;
            textViews_filter.get(index).setBackgroundResource(R.drawable.shape_black_shangpin_state);
        }
    }

    public void iniTitleTvColor(int index) {
        for (int i = 0; i < textViews_sort.size(); i++) {
            if (index == i) {
                textViews_sort.get(i).setTextColor(mActivity.getResources().getColor(R.color.tv_textview_select));
            } else {
                textViews_sort.get(i).setTextColor(mActivity.getResources().getColor(R.color.tv_textview_noselect));
            }
        }
    }

    public void initStatus(int index) {
        if (index == 0) {
            iniTitleTvColor(0);
        } else if (index == 1) {
            iniTitleTvColor(1);
        } else if (index == 2) {
            iniTitleTvColor(2);
        } else if (index == 3) {
            iniTitleTvColor(3);
        } else {
            iniTitleTvColor(4);
        }
    }

    private void iniTitleImagesBySelect(int index) {
        for (int i = 0; i < imageViews_sort.size(); i++) {
            if (index == i) {
                imageViews_sort.get(i).setVisibility(View.VISIBLE);
                if (currentPosition == index) {
                    if (isUp) {
                        imageViews_sort.get(i).setImageResource(R.drawable.sort_up);
                        isUp = false;
                    } else {
                        imageViews_sort.get(i).setImageResource(R.drawable.sort_low);
                        isUp = true;
                    }
                } else {
                    currentPosition = index;
                    isUp = true;
                    imageViews_sort.get(i).setImageResource(R.drawable.sort_low);
                }

            } else {
                imageViews_sort.get(i).setVisibility(View.GONE);
            }
        }
        if (index == 0) {
            sort_item = 0;
        } else if (index == 1) {
            sort_item = 1;
        } else if (index == 2) {
            sort_item = 2;
        } else if (index == 3) {
            sort_item = 3;
        } else if (index == 4) {
            sort_item = 4;
        }

        if (index == 0) {
            imageViews_sort.get(0).setVisibility(View.GONE);
            sort_value = sort_dir_id.get(0) + "B";
            Log.i("排序的--默认排序", sort_value);
        }
        if (index == 4) {
            imageViews_sort.get(4).setImageResource(R.drawable.sort_low);
            sort_value = sort_dir_id.get(4) + "B";
            Log.i("排序的--卖家好评", sort_value);
        } else {
            sort_value = isUp ? sort_dir_id.get(index) + "B" : sort_dir_id.get(index) + "A";
            Log.i("排序的--其他", sort_value);
        }
        //        if (index == 1){
        //            sort_value = isUp ? sort_dir_id.get(1)+"B" : sort_dir_id.get(1)+"A";
        //            Log.i("排序的--剩余时间",sort_value);
        //        }
        //        if (index == 2){
        //            sort_value = isUp ?  sort_dir_id.get(2)+"B" : sort_dir_id.get(2)+"A";
        //            Log.i("排序的--当前价格",sort_value);
        //        }
        //        if (index == 3){
        //            sort_value = isUp ?  sort_dir_id.get(3)+"B" : sort_dir_id.get(3)+"A";
        //            Log.i("排序的--竞拍人数",sort_value);
        //        }
        //        pageIndex = 1;
        //        loadData(false);
    }

    public void reSetFilter() {
        isHasSelect = false;
        for (int i = 0; i < textViews_filter.size(); i++) {
            textViews_filter.get(i).setBackgroundResource(R.drawable.shape_black_shangpin_state);
        }
    }

    public void reSetSort() {
        initStatus(1);
        iniTitleImagesBySelect(0);
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
                        ArrayList<ShangPinFilterBean> allDataArrayList = new Gson().fromJson(rows.toString(),
                                                                                             new TypeToken<List<ShangPinFilterBean>>() {
                                                                                             }.getType());
                        shangPinFilterBeens.addAll(allDataArrayList);
                        if (shangPinFilterBeens.get(0).getDir_key().equals("filter_type")) {
                            tv_filter_1.setText(shangPinFilterBeens.get(0).getItem_name());
                            filter_dir_id.add(shangPinFilterBeens.get(0).getDir_id());
                        }

                        if (shangPinFilterBeens.get(1).getDir_key().equals("filter_type")) {
                            tv_filter_2.setText(shangPinFilterBeens.get(1).getItem_name());
                            filter_dir_id.add(shangPinFilterBeens.get(1).getDir_id());
                        }

                        if (shangPinFilterBeens.get(2).getDir_key().equals("filter_type")) {
                            tv_filter_3.setText(shangPinFilterBeens.get(2).getItem_name());
                            filter_dir_id.add(shangPinFilterBeens.get(2).getDir_id());
                        }

                        if (shangPinFilterBeens.get(3).getDir_key().equals("filter_type")) {
                            tv_filter_4.setText(shangPinFilterBeens.get(3).getItem_name());
                            filter_dir_id.add(shangPinFilterBeens.get(3).getDir_id());
                        }

                        if (shangPinFilterBeens.get(4).getDir_key().equals("sort_type")) {
                            tv_sort_1.setText(shangPinFilterBeens.get(4).getItem_name());
                            Log.i("id2-----", shangPinFilterBeens.get(4).getDir_id() + "");
                            sort_dir_id.add(shangPinFilterBeens.get(4).getDir_id());
                        }

                        if (shangPinFilterBeens.get(5).getDir_key().equals("sort_type")) {
                            tv_sort_2.setText(shangPinFilterBeens.get(5).getItem_name());
                            Log.i("id2-----", shangPinFilterBeens.get(5).getDir_id() + "");
                            sort_dir_id.add(shangPinFilterBeens.get(5).getDir_id());
                        }

                        if (shangPinFilterBeens.get(6).getDir_key().equals("sort_type")) {
                            tv_sort_3.setText(shangPinFilterBeens.get(6).getItem_name());
                            Log.i("id2-----", shangPinFilterBeens.get(6).getDir_id() + "");
                            sort_dir_id.add(shangPinFilterBeens.get(6).getDir_id());
                        }

                        if (shangPinFilterBeens.get(7).getDir_key().equals("sort_type")) {
                            tv_sort_4.setText(shangPinFilterBeens.get(7).getItem_name());
                            Log.i("id2-----", shangPinFilterBeens.get(7).getDir_id() + "");
                            sort_dir_id.add(shangPinFilterBeens.get(7).getDir_id());
                        }

                        if (shangPinFilterBeens.get(8).getDir_key().equals("sort_type")) {
                            tv_sort_5.setText(shangPinFilterBeens.get(8).getItem_name());
                            Log.i("id2-----", shangPinFilterBeens.get(8).getDir_id() + "");
                            sort_dir_id.add(shangPinFilterBeens.get(8).getDir_id());
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
