package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.ConnectionBean;
import com.buycolle.aicang.ui.fragment.SmileFragment;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.xlistview.XListView;
import com.buycolle.aicang.util.SmileUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.UIUtil;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/8/25.
 */
public class ConectionActivity extends BaseActivity implements SmileFragment.OnFragmentInteractionListener{
    @Bind(R.id.my_header)
    MyHeader myHeader;//顶部标题
    @Bind(R.id.iv_edit_input)
    ImageView ivEditInput;//表情和内容的切换图标
    @Bind(R.id.et_input)
    EditText etInput;//交流输入框
    private int product_id;//当前交流的商品id
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;//所交流商品的标题
    @Bind(R.id.tv_pay_status)
    TextView tvPayStatus;//该商品的支付状态
    @Bind(R.id.tv_wuliu_st)
    TextView tvWuliuStatus;//该商品的物流状态
    @Bind(R.id.tv_count_left)
    TextView tvCountLeft;//剩余询问消息的条数
    @Bind(R.id.sendMsgLayout)
    LinearLayout sendMsgLayout;//整体的输入发送框
    @Bind(R.id.fl_chat_activity_container)
    FrameLayout fragContainer;
    @Bind(R.id.list)
    XListView list;//联系的问答列表
    @Bind(R.id.ib_float_btn)
    ImageButton  ibFloatBtn;//回到顶部按钮
    @Bind(R.id.ll_to_paipin_detai)
    RelativeLayout rlToPaiPinDtai;//顶部标题整体，跳转到拍品详情页
    @Bind(R.id.rl_top_maincontent)
    RelativeLayout rlTopMaincontent;//整体交互界面的布局
    @Bind(R.id.tv_send)
    TextView tvSend;//发送监听

    private boolean isSaller = false;//是否是卖家
    boolean isShow = true;//是否显现
    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;
    private InputMethodManager manager;

    public int remain_count;//交流剩余条数
    public String product_title;//商品名称
    public int pay_status;//付款状态
    public int order_status;//运输状态
    private boolean isloadMore = false;

    public ConnectionBean connectionBean;
    private List<ConnectionBean> connectionBeans;
    private MyAdapter myAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        ButterKnife.bind(this);
        myAdapter = new MyAdapter();
        connectionBeans = new ArrayList<>();
        product_id = _Bundle.getInt("product_id");
        if (_Bundle!= null){
            if (_Bundle.getBoolean("isPush")){
                product_id = _Bundle.getInt("id");
            }
        }
        Log.i("product_id瞎BB的商品----", product_id + "");
        loadConnectionData(true);
        init();

    }

    /**
     * 初始化数据
     */
    public void init() {
        //初始化顶部标题数据
        myHeader.init("售后交流", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        //列表页的监听
        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > Constans.MAX_SHOW_FLOAT_BTN) {
                    ibFloatBtn.setVisibility(View.VISIBLE);
                } else {
                    ibFloatBtn.setVisibility(View.GONE);
                }
            }
        });

        rlTopMaincontent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideComment();
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideComment();
            }
        });
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                loadConnectionData(false);
            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadConnectionData(true);
                }
            }
        });
        ibFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setSelection(0);
            }
        });
        loadConnectionData(isloadMore);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //表情和文字切换图标的监听
            getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_activity_container, new SmileFragment(), "smile").commit();
            ivEditInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) fragContainer.getLayoutParams();

                    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                        if (getCurrentFocus() != null) {
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }

                    if (isShow) {
                        isShow = false;
                        lps.height = UIUtil.dip2px(mContext, 220);
                        fragContainer.setLayoutParams(lps);
                        fragContainer.setVisibility(View.VISIBLE);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    } else {
                        isShow = true;
                        lps.height = UIUtil.dip2px(mContext, 0);
                        fragContainer.setLayoutParams(lps);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        fragContainer.setVisibility(View.INVISIBLE);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    if (fragContainer.getVisibility() == View.VISIBLE) {
                        ivEditInput.setImageResource(R.drawable.key_bord);
                    } else {
                        ivEditInput.setImageResource(R.drawable.smile_icon);
                    }

                }
            });
            isSaller = false;

            etInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 100) {
                        UIHelper.t(mActivity, "您输入的字数过多");
                        etInput.getEditableText().delete(s.length() - 1, s.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        list.setAdapter(myAdapter);
        loadConnectionData(false);

        /***
         * 顶部标题跳转的监听
         */
        rlToPaiPinDtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", product_id);
                UIHelper.jump(mActivity, PaiPinDetailActivity.class, bundle);
            }
        });

        /**
         * 发送按钮的监听
         */
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入提问内容");
                    return;
                }
                tvSend.setEnabled(false);
                submit_conect_context();

            }
        });

    }

    /***
     *
     * 顶部的商品信息初始化
     */
    public void initTop(){
        tvGoodTitle.setText(product_title);
        if (pay_status == 0){
            tvPayStatus.setText("未付款");
        }else if (pay_status == 1){
            tvPayStatus.setText("已支付");
        }else if (pay_status == 2){
            tvPayStatus.setText("付款超时");
        }

        if (order_status == 0){
            tvWuliuStatus.setText("未发货");
        }else if (order_status == 1){
            tvWuliuStatus.setText("已发货");
        }else if (order_status == 2){
            tvWuliuStatus.setText("已收货");
        }else if (order_status == 3){
            tvWuliuStatus.setText("已结算");
        }

        tvCountLeft.setText(remain_count+"");
    }
    /**
     * 获取瞎BB的交流内容列表界面
     */

    public void loadConnectionData(final boolean isloadMore){
        isRun = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("product_id",product_id);
            jsonObject.put("page", pageIndex);
            jsonObject.put("rows", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.commonAftersales_getListByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isloadMore){
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()){
                    return;
                }
                //开始解析数据
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)){
                        if (pageIndex == 1) {
                            connectionBeans.clear();
                        }
                        //解析数组中的具体内容
                        JSONArray jsonArrayResult  = resultObj.getJSONArray("rows");
                        ArrayList<ConnectionBean> allDataArrayList = new Gson().fromJson(jsonArrayResult.toString(), new TypeToken<List<ConnectionBean>>() {
                        }.getType());
                        connectionBeans.addAll(allDataArrayList);
//                        for (Iterator iterator = allDataArrayList.iterator(); iterator.hasNext(); ) {
//                            connectionBean = (ConnectionBean) iterator.next();
//                        }
                        //加载标题
                        product_title = resultObj.getString("product_title");
                        Log.i("hhhh_product_title",product_title);
                        //加载支付状态
                        pay_status = resultObj.getInt("pay_status");
                        Log.i("hhhh_pay_status",pay_status+"");
                        //加载物流状态
                        order_status = resultObj.getInt("order_status");
                        Log.i("hhhh_order_status",order_status+"");
                        //加载交流信息剩余条数
                        remain_count = resultObj.getInt("remain_count");
                        Log.i("hhhh_remain_count",remain_count+"");
                        initTop();
                        //剩余交流条数的限制
                        if (remain_count == 0){
                            sendMsgLayout.setVisibility(View.GONE);
                        }else {
                            sendMsgLayout.setVisibility(View.VISIBLE);
                        }
                        //这里要添加adapter，加载布局
                        myAdapter.notifyDataSetChanged();
                        if (JSONUtil.isCanLoadMore(resultObj)) {
                            list.isShowFoot(true);
                        } else {
                            list.isShowFoot(false);
                        }
                    }else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isRun = false;
                list.onRefreshComplete();
                if (!isloadMore){
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                isRun = false;
                list.onRefreshComplete();
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }
            }
        });

    }


    /***
     * 提交即时交流的内容
     */
    public void submit_conect_context(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("context", etInput.getText().toString());
            jsonObject.put("product_id", product_id);
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.commonAftersales_submitCommonByApp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        etInput.setText("");
                        hideComment();
                        UIUtil.hideInputMethod(mActivity);
                        pageIndex = 1;
                        loadConnectionData(false);
                        if (fragContainer.getVisibility() == View.VISIBLE) {
                            ivEditInput.setImageResource(R.drawable.key_bord);
                        } else {
                            ivEditInput.setImageResource(R.drawable.smile_icon);
                        }

                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvSend.setEnabled(true);
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                tvSend.setEnabled(true);
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    @Override
    public void onSmileClick(String filename) {
        if (etInput.getText().toString().length() >= 95) {
            //change by ：胡峰
            UIHelper.t(mActivity, "您输入的字数过多");
            return;
        }
        try {
            if (filename != "delete_expression") { // 不是删除键，显示表情
                // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                Class clz = Class.forName("com.buycolle.aicang.util.SmileUtils");
                Field field = clz.getField(filename);
                int iCursorStart = Selection.getSelectionStart((etInput.getText()));
                int iCursorEnd = Selection.getSelectionEnd((etInput.getText()));
                if (iCursorStart != iCursorEnd) {
                    ((Editable) etInput.getText()).replace(iCursorStart, iCursorEnd, "");
                }
                int iCursor = Selection.getSelectionEnd((etInput.getText()));
                ((Editable) etInput.getText()).insert(iCursor, SmileUtils.getSmiledText(mActivity,
                        (String) field.get(null)));
            } else { // 删除文字或者表情
                if (!TextUtils.isEmpty(etInput.getText())) {
                    int selectionStart = etInput.getSelectionStart();// 获取光标的位置
                    if (selectionStart > 0) {
                        String body = etInput.getText().toString();
                        String tempStr = body.substring(0, selectionStart);
                        int i = tempStr.lastIndexOf("^");// 获取最后一个表情的位置
                        if (i != -1) {
                            CharSequence cs = tempStr.substring(i, selectionStart);
                            if (SmileUtils.containsKey(cs.toString()) && tempStr.endsWith("^"))
                                etInput.getEditableText().delete(i, selectionStart);
                            else
                                etInput.getEditableText().delete(selectionStart - 1,
                                        selectionStart);
                        } else {
                            etInput.getEditableText().delete(selectionStart - 1, selectionStart);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void hideComment() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) fragContainer.getLayoutParams();
        if (fragContainer.getVisibility() == View.VISIBLE) {
            isShow = true;
            lps.height = UIUtil.dip2px(mContext, 0);
            fragContainer.setLayoutParams(lps);
            fragContainer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 布局加载适配器
     */
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return connectionBeans == null? 0:connectionBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return connectionBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            connectionBean = connectionBeans.get(position);
            if (connectionBean.getUser_id() == LoginConfig.getUserInfo(mContext).getUser_id()){
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.connction_mybuy_qa,null);
                ImageView iv_profile_iamge_buy = (ImageView) view1.findViewById(R.id.iv_profile_image_buy);
                TextView tv_time_buy = (TextView) view1.findViewById(R.id.tv_time_buy);
                TextView tv_content_buy = (TextView) view1.findViewById(R.id.tv_context_buy);
                mApplication.setTouImages(connectionBean.getUser_avatar(), iv_profile_iamge_buy);
                tv_time_buy.setText(connectionBean.getCreate_date());
                tv_content_buy.setText(SmileUtils.getSmiledCommentText(mContext, connectionBean.getContext()));
                return view1;
            }else {
                View view2 = LayoutInflater.from(mContext).inflate(R.layout.connection_ans_qa,null);
                ImageView iv_profile_iamge_ans = (ImageView) view2.findViewById(R.id.iv_profile_image_ans);
                TextView tv_time_ans = (TextView) view2.findViewById(R.id.tv_time_ans);
                TextView tv_context_ans = (TextView) view2.findViewById(R.id.tv_context_ans);
                mApplication.setTouImages(connectionBean.getUser_avatar(),iv_profile_iamge_ans);
                tv_time_ans.setText(connectionBean.getCreate_date());
                tv_context_ans.setText(SmileUtils.getSmiledCommentText(mContext, connectionBean.getContext()));
                return view2;
            }

        }
    }

}
