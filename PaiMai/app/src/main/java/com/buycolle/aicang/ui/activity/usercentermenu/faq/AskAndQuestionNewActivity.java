package com.buycolle.aicang.ui.activity.usercentermenu.faq;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.buycolle.aicang.bean.PaiPinAskAndQsqBean;
import com.buycolle.aicang.bean.PaiPinDetailBean;
import com.buycolle.aicang.event.ReplyEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.fragment.SmileFragment;
import com.buycolle.aicang.ui.view.MyCommentPop;
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
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/10.
 */
public class AskAndQuestionNewActivity extends BaseActivity implements SmileFragment.OnFragmentInteractionListener {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.iv_paimai)
    ImageView ivPaimai;
    @Bind(R.id.iv_range)
    ImageView ivRange;
    @Bind(R.id.tv_good_title)
    TextView tvGoodTitle;
    @Bind(R.id.tv_good_status)
    TextView tvGoodStatus;
    @Bind(R.id.tv_good_desc)
    TextView tvGoodDesc;
    @Bind(R.id.ll_top_detail)
    LinearLayout llTopDetail;
    @Bind(R.id.list)
    XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;
    @Bind(R.id.iv_edit_input)
    ImageView ivEditInput;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.fl_chat_activity_container)
    FrameLayout fragContainer;
    @Bind(R.id.sendMsgLayout)
    LinearLayout sendMsgLayout;
    @Bind(R.id.rl_parent)
    RelativeLayout rlParent;
    @Bind(R.id.rl_top_maincontent)
    RelativeLayout rlTopMaincontent;

    private boolean isSaller = false;//是否是卖家

    private PaiPinDetailBean paiPinDetailBean;
    private ArrayList<PaiPinAskAndQsqBean> paiPinAskAndQsqBeens;
    private MyAdapter myAdapter;


    MyCommentPop myCommentPop;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    private int product_id;


    private InputMethodManager manager;
    boolean isShow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);
        product_id = _Bundle.getInt("product_id");
        paiPinAskAndQsqBeens = new ArrayList<>();
        myAdapter = new MyAdapter();
        myHeader.init("商品的问与答", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        list.setPullRefreshEnable(false);
        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > Constans.MAX_SHOW_FLOAT_BTN) {
                    ibFloatBtn.setVisibility(View.VISIBLE);
                } else {
                    ibFloatBtn.setVisibility(View.GONE);
                }

            }
        });
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLastItemVisible() {
                if (!isRun) {
                    pageIndex++;
                    loadData(true);
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

        ibFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setSelection(0);
            }
        });
        list.setPullRefreshEnable(false);
        list.setAdapter(myAdapter);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入内容");
                    return;
                }
                tvSend.setEnabled(false);
                submit();
            }
        });
        loadDetail();
    }

    private void loadDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.product_getpreauctdetailidbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        paiPinDetailBean = new Gson().fromJson(userInfoObj.toString(), PaiPinDetailBean.class);
                        initTop();
                        //加载信息之后再加载评论
                        loadData(false);
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                dismissLoadingDialog();
            }
        });
    }


    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("context", etInput.getText().toString());
            jsonObject.put("root_id", paiPinDetailBean.getProduct_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("owner_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.commoncomment_submitforproductbyapp(jsonObject, new ApiCallback() {
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
                        hideComment();
                        etInput.setText("");
                        UIUtil.hideInputMethod(mActivity);
                        pageIndex = 1;
                        loadData(false);
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

    private void initTop() {
        mApplication.setImages(paiPinDetailBean.getCover_pic(), ivPaimai);
        mApplication.setImages(paiPinDetailBean.getRaretag_icon(), ivRange);
        tvGoodTitle.setText(paiPinDetailBean.getProduct_title());
        tvGoodStatus.setText(paiPinDetailBean.getSt_name());
        tvGoodDesc.setText(paiPinDetailBean.getProduct_desc());
        if (paiPinDetailBean.getSeller_user_id() == LoginConfig.getUserInfo(mContext).getUser_id()) {
            isSaller = true;
            sendMsgLayout.setVisibility(View.GONE);
        } else {
            getSupportFragmentManager().
                    beginTransaction().add(R.id.fl_chat_activity_container, new SmileFragment(), "smile")
                    .commit();
            manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
            etInput.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        isShow = true;
                        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) fragContainer.getLayoutParams();
                        lps.height = UIUtil.dip2px(mContext, 0);
                        fragContainer.setLayoutParams(lps);
                        etInput.requestFocus();
                        fragContainer.setVisibility(View.INVISIBLE);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        //如果软键盘被隐藏，则显示
                        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        if (fragContainer.getVisibility() == View.VISIBLE) {
                            ivEditInput.setImageResource(R.drawable.key_bord);
                        } else {
                            ivEditInput.setImageResource(R.drawable.smile_icon);
                        }
                    }
                    return false;
                }
            });

            etInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 30) {
                        UIHelper.t(mActivity, "输入字数超出30个限制");
                        etInput.getEditableText().delete(s.length() - 1, s.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            sendMsgLayout.setVisibility(View.VISIBLE);
            isSaller = false;
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

    private void loadData(final boolean isloadMore) {
        isRun = true;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", paiPinDetailBean.getProduct_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.product_getcurandtotalcommentinfosbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog();
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing()) {
                    return;
                }
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (pageIndex == 1) {
                            paiPinAskAndQsqBeens.clear();
                        }
                        JSONArray jsonArrayResult = resultObj.getJSONArray("rows");
                        ArrayList<PaiPinAskAndQsqBean> allDataArrayList = new Gson().fromJson(jsonArrayResult.toString(), new TypeToken<List<PaiPinAskAndQsqBean>>() {
                        }.getType());
                        paiPinAskAndQsqBeens.addAll(allDataArrayList);
                        myAdapter.notifyDataSetChanged();
                        if (JSONUtil.isCanLoadMore(resultObj)) {
                            list.isShowFoot(true);
                        } else {
                            list.isShowFoot(false);
                        }
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoadingDialog();
                isRun = false;
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing()) {
                    return;
                }
                isRun = false;
                dismissLoadingDialog();
                UIHelper.t(mContext, R.string.net_error);
                if (isloadMore) {
                    pageIndex--;
                }
            }
        });

    }

    @Override
    public void onSmileClick(String filename) {
        if (etInput.getText().toString().length() >= 25) {
            UIHelper.t(mActivity, "输入字数超出30个限制");
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


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return paiPinAskAndQsqBeens.size();
        }

        @Override
        public Object getItem(int i) {
            return paiPinAskAndQsqBeens.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_ask_question_item, null);
                holder.ll_other_huida = (LinearLayout) convertView.findViewById(R.id.ll_other_huida);
                holder.ll_maijia_lay = (LinearLayout) convertView.findViewById(R.id.ll_maijia_lay);

                holder.tv_qst_content = (TextView) convertView.findViewById(R.id.tv_qst_content);
                holder.tv_qst_user_name = (TextView) convertView.findViewById(R.id.tv_qst_user_name);
                holder.tv_qst_time = (TextView) convertView.findViewById(R.id.tv_qst_time);

                holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);

                holder.tv_aws_content = (TextView) convertView.findViewById(R.id.tv_aws_content);
                holder.tv_aws_user_name = (TextView) convertView.findViewById(R.id.tv_aws_user_name);
                holder.tv_aws_time = (TextView) convertView.findViewById(R.id.tv_aws_time);

                holder.tv_maijia = (TextView) convertView.findViewById(R.id.tv_maijia);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final PaiPinAskAndQsqBean bean = paiPinAskAndQsqBeens.get(position);

            holder.tv_qst_content.setText(SmileUtils.getSmiledCommentText(mContext, bean.getContext()));

            holder.tv_qst_user_name.setText(bean.getC_user_nick());
            holder.tv_qst_time.setText(bean.getCreate_date());

            if (bean.getSubVec().size() == 0) {
                holder.ll_other_huida.setVisibility(View.GONE);
                if (isSaller) {
                    holder.ll_maijia_lay.setVisibility(View.VISIBLE);
                    holder.tv_maijia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 16/3/29 回复
                            showCommentPop(bean);
                        }
                    });
                } else {
                    holder.ll_maijia_lay.setVisibility(View.GONE);
                }
            } else {
                holder.ll_other_huida.setVisibility(View.VISIBLE);
                holder.ll_maijia_lay.setVisibility(View.GONE);
                PaiPinAskAndQsqBean.SubVecEntity subEntity = bean.getSubVec().get(0);

                holder.tv_aws_content.setText(SmileUtils.getSmiledCommentText(mContext, subEntity.getContext()));
                holder.tv_aws_user_name.setText(subEntity.getC_user_nick());
                holder.tv_aws_time.setText(subEntity.getCreate_date());
                //mApplication.setImages(subEntity.getC_user_avatar(), holder.profile_image);
                //change by :胡峰
                mApplication.setTouImages(subEntity.getC_user_avatar(),holder.profile_image);
            }
            return convertView;
        }

        public class ViewHolder {
            TextView tv_qst_content;
            TextView tv_qst_user_name;
            TextView tv_qst_time;
            TextView tv_aws_content;

            LinearLayout ll_other_huida;
            CircleImageView profile_image;
            TextView tv_aws_user_name;
            TextView tv_aws_time;

            LinearLayout ll_maijia_lay;
            TextView tv_maijia;

        }

    }

    private void showCommentPop(final PaiPinAskAndQsqBean bean) {
        myCommentPop = new MyCommentPop(this, rlParent);
        myCommentPop.setCallBack(new MyCommentPop.CallBack() {
            @Override
            public void send(final TextView sendTv, EditText content) {
                sendTv.setEnabled(false);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("pid", bean.getId());
                    jsonObject.put("context", content.getText().toString());
                    jsonObject.put("root_id", paiPinDetailBean.getProduct_id());
                    jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                    jsonObject.put("owner_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mApplication.apiClient.commoncomment_submitforproductbyapp(jsonObject, new ApiCallback() {
                    @Override
                    public void onApiStart() {

                    }

                    @Override
                    public void onApiSuccess(String response) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoadingDialog();
                        try {
                            JSONObject resultObj = new JSONObject(response);
                            if (JSONUtil.isOK(resultObj)) {
                                pageIndex = 1;
                                loadData(false);
                                sendTv.setEnabled(true);
                                myCommentPop.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIUtil.hideInputMethod(mActivity);
                                    }
                                }, 100);
                                EventBus.getDefault().post(new ReplyEvent(0));
                            } else {
                                UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sendTv.setEnabled(true);
                        }
                        sendTv.setEnabled(true);
                    }

                    @Override
                    public void onApiFailure(Request request, Exception e) {
                        if (isFinishing())
                            return;
                        sendTv.setEnabled(true);
                        UIHelper.t(mContext, R.string.net_error);
                    }
                });
            }
        });
        myCommentPop.showPop();
    }
}
