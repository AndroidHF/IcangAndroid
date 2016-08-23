package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.bean.PostShowBean;
import com.buycolle.aicang.bean.ShowDetailBean;
import com.buycolle.aicang.commen.PhotoViewMultPicActivity;
import com.buycolle.aicang.event.LikeShowEvent;
import com.buycolle.aicang.event.StoreShowEvent;
import com.buycolle.aicang.ui.fragment.SmileFragment;
import com.buycolle.aicang.ui.view.MeasuredListView;
import com.buycolle.aicang.ui.view.MyCommentPop;
import com.buycolle.aicang.ui.view.ShareDialog;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.ShareUtil;
import com.buycolle.aicang.util.SmileUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.UIUtil;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.squareup.okhttp.Request;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by joe on 16/3/13.
 */
public class ShowDetailActivity extends BaseActivity implements SmileFragment.OnFragmentInteractionListener, IWeiboHandler.Response {

    @Bind(R.id.fl_back)
    FrameLayout flBack;
    @Bind(R.id.iv_menu_icon)
    ImageView ivMenuIcon;
    @Bind(R.id.tv_common_topbar_title)
    TextView tvCommonTopbarTitle;
    @Bind(R.id.iv_store)
    ImageView ivStore;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.rl_header_top)
    RelativeLayout rlHeaderTop;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.rl_reply)
    RelativeLayout rlReply;
    @Bind(R.id.list)
    ListView list;

    View headerView;
    @Bind(R.id.rl_parent)
    RelativeLayout rlParent;
    @Bind(R.id.iv_edit_input)
    ImageView ivEditInput;
    @Bind(R.id.fl_smile_lay)
    FrameLayout flSmileLay;
    @Bind(R.id.sendMsgLayout)
    LinearLayout sendMsgLayout;
    @Bind(R.id.fl_chat_activity_container)
    FrameLayout fragContainer;


    private MyAdapter myAdapter;
    private LinearLayout ll_content;
    LayoutInflater inflater;

    ShowDetailBean showDetailBean;
    private InputMethodManager manager;


    private ArrayList<ShowDetailBean.RecomListEntity> recomListEntities;

    private int show_id;
    boolean isShow = true;


    Tencent mTencent;
    private IWeiboShareAPI mWeiboShareAPI;
    private Bitmap shareBitmap;
    private String shareImagePath = "";

    private void getShareBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GlideUrl glideUrl = new GlideUrl(showDetailBean.getCover_pic(), new LazyHeaders.Builder()
                            .build());
                    //shareBitmap = Glide.with(mActivity).load(glideUrl).asBitmap().into(150, 150).get();
                    shareBitmap = Glide.with(mActivity).load(glideUrl).asBitmap().into(100, 50).get();
                    //shareBitmap = Bitmap.createScaledBitmap(shareBitmap, 100, 100, true);
                    shareImagePath = saveLocalImg(shareBitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //为QQ存储分享图片
    private String saveLocalImg(Bitmap bitmap) {
        String serverPath = showDetailBean.getCover_pic();
        String name = serverPath.substring(serverPath.lastIndexOf("/") + 1, serverPath.length());
        String filePath = FileUtil.getShareCathe() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        ButterKnife.bind(this);
        recomListEntities = new ArrayList<>();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        show_id = _Bundle.getInt("show_id");
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.view_show_detail_header, null);
        findHeaderView(headerView);

        list.addHeaderView(headerView);
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);

        mTencent = Tencent.createInstance(Constans.APP_TX_KEY, this.getApplicationContext());

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constans.APP_KEY);
        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }


        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareDialog(mActivity, R.style.bottom_dialog).setListner(new ShareDialog.OnPicChooserListener() {
                    @Override
                    public void share(int position) {
                        switch (position) {
                            /***
                             * cahnge by :胡峰
                             * 分享界面的跳转
                             */
                            case 1:
                                ShareUtil.shareToWeChat(mActivity, shareBitmap, Constans.SHARE_URL + "/initDialog.html?" + show_id+"z", Constans.shareTitle_Type_2, showDetailBean.getTitle());
                                Log.i("show_id-----", showDetailBean.getShow_id() + "");
                                break;
                            case 2:
                                ShareUtil.shareToCicle(mActivity, shareBitmap, Constans.SHARE_URL+"/initDialog.html?"+show_id+"z", Constans.shareTitle_Type_2, showDetailBean.getTitle());
                                break;
                            case 3:
                                ShareUtil.shareToQQ(mActivity, mTencent, Constans.SHARE_URL+"/initDialog.html?"+showDetailBean.getShow_id()+"z", Constans.shareTitle_Type_2, showDetailBean.getTitle(), shareImagePath, new BaseUiListener());
                                break;
                            case 4:
                                ShareUtil.shareToSina(mActivity, mWeiboShareAPI, shareBitmap, Constans.SHARE_URL+"/initDialog.html?"+show_id+"z", showDetailBean.getTitle());
                                break;
                        }
                    }
                }).show();
            }


        });
        loadData(false);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    tvSend.setEnabled(false);
                    if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                        UIHelper.t(mContext, "请输入评论内容");
                        tvSend.setEnabled(true);
                        return;
                    }
                    send();
                } else {
                    UIHelper.jump(mActivity, LoginActivity.class);
                }
            }
        });


        getSupportFragmentManager().
                beginTransaction().add(R.id.fl_chat_activity_container, new SmileFragment(), "smile")
                .commit();
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
                    UIHelper.t(mContext, "您输入的字数过多");
                    etInput.getEditableText().delete(s.length() - 1, s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void send() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("context", etInput.getText().toString());
            jsonObject.put("root_id", showDetailBean.getShow_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("owner_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.commoncomment_submitforshowbyapp(jsonObject, new ApiCallback() {
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
                        if (recomListEntities.size() > 0) {
                            list.setSelection(0);
                        }
                        etInput.setText("");
                        tvSend.setEnabled(true);
                        loadData(false);
                    } else {
                        tvSend.setEnabled(true);
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


    private void findHeaderView(View headerView) {
        ll_content = (LinearLayout) headerView.findViewById(R.id.ll_content);
        iv_cover = (ImageView) headerView.findViewById(R.id.iv_cover);
        iv_goods_type_arrow = (ImageView) headerView.findViewById(R.id.iv_goods_type_arrow);
        iv_like_icon = (ImageView) headerView.findViewById(R.id.iv_like_icon);
        tv_goods_type = (TextView) headerView.findViewById(R.id.tv_goods_type);
        tv_goods_time = (TextView) headerView.findViewById(R.id.tv_goods_time);
        tv_goods_content = (TextView) headerView.findViewById(R.id.tv_goods_content);
        tv_comment_count = (TextView) headerView.findViewById(R.id.tv_comment_count);
        tv_user_name = (TextView) headerView.findViewById(R.id.tv_user_name);
        tv_like_count = (TextView) headerView.findViewById(R.id.tv_like_count);
        tv_user_desc = (TextView) headerView.findViewById(R.id.tv_user_desc);
        tv_comment_count_1 = (TextView) headerView.findViewById(R.id.tv_comment_count_1);
        iv_user_image = (CircleImageView) headerView.findViewById(R.id.iv_user_image);
        ll_like_lay = (LinearLayout) headerView.findViewById(R.id.ll_like_lay);
    }

    private void loadData(final boolean isAction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("show_id", show_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.show_getdetailidbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                if (!isAction) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        showDetailBean = new Gson().fromJson(userInfoObj.toString(), ShowDetailBean.class);
                        getShareBitmap();
                        initData();
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!isAction) {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
                if (!isAction) {
                    dismissLoadingDialog();
                }
            }
        });
    }

    ImageView iv_cover;
    ImageView iv_goods_type_arrow;
    ImageView iv_like_icon;
    TextView tv_goods_type;
    TextView tv_goods_time;
    TextView tv_goods_content;
    TextView tv_user_name;
    TextView tv_comment_count;
    TextView tv_like_count;
    TextView tv_user_desc;
    TextView tv_comment_count_1;
    CircleImageView iv_user_image;
    LinearLayout ll_like_lay;

    private void initData() {
        initStoreStatus(showDetailBean.getCol_id());
        initLikeStatus(showDetailBean.getZ_id());
        ll_like_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    if (likeActionFlag) {
                        likeActionFlag = false;
                        likeUnlike(showDetailBean);
                    } else {
                        if (likeActionShowToUserFlag) {
                            likeActionShowToUserFlag = false;
                            UIHelper.t(mContext, R.string.comment_action_more);
                        }
                    }
                } else {
                    UIHelper.jump(mActivity, LoginActivity.class);
                }
            }
        });

        ivStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplication.isLogin()) {
                    if (storeActionFlag) {
                        storeActionFlag = false;
                        storeUnStore(showDetailBean);
                    } else {
                        if (storeActionShowToUserFlag) {
                            storeActionShowToUserFlag = false;
                            UIHelper.t(mContext, R.string.comment_action_more);
                        }
                    }
                } else {
                    UIHelper.jump(mActivity, LoginActivity.class);
                }
            }
        });

        //change by :胡峰，晒物默认的加载图片的修改
        mApplication.setShowImages(showDetailBean.getCover_pic(), iv_cover);
        //mApplication.setImages(showDetailBean.getCover_pic(), iv_cover);
        iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> paths = new ArrayList<String>();
                paths.add(showDetailBean.getCover_pic());
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                bundle.putBoolean("noShowCount", true);
                bundle.putStringArrayList("path", paths);
                UIHelper.jump(mActivity, PhotoViewMultPicActivity.class, bundle);
            }
        });
        mApplication.setImages(showDetailBean.getCate_icon(), iv_goods_type_arrow);
        //mApplication.setImages(showDetailBean.getUser_avatar(), iv_user_image);
        //change by :胡峰，头像的修改
        mApplication.setTouImages(showDetailBean.getUser_avatar(),iv_user_image);
        tv_user_name.setText(showDetailBean.getUser_nick());
        tv_goods_type.setText(showDetailBean.getCate_name());
        tv_goods_time.setText(showDetailBean.getLast_update_date());
        tv_goods_content.setText(showDetailBean.getTitle());
        tv_like_count.setText(showDetailBean.getZ_count() + "");
        tv_comment_count.setText(showDetailBean.getComment_count() + "");
        tv_user_desc.setText(showDetailBean.getIntro());
        ll_content.removeAllViews();

        final String context = showDetailBean.getContext();
        if (!TextUtils.isEmpty(context)) {
            ArrayList<PostShowBean> postShowBeans = new Gson().fromJson(context.toString(), new TypeToken<List<PostShowBean>>() {
            }.getType());
            if (postShowBeans.size() > 0) {
                for (final PostShowBean ziDingYi : postShowBeans) {
                    if (ziDingYi.getType() == 1) {//1 表示文字，2 表示图  3表示链接
                        View text = inflater.inflate(R.layout.view_show_detail_header_text, null);
                        TextView tv_zidingyi_content = (TextView) text.findViewById(R.id.tv_zidingyi_content);
                        tv_zidingyi_content.setText(ziDingYi.getContent());
                        ll_content.addView(text);
                    }
                    if (ziDingYi.getType() == 2) {//1 表示文字，2 表示图  3表示链接
                        View imges = inflater.inflate(R.layout.view_show_detail_header_image, null);
                        final ImageView iv_zidingyi_image = (ImageView) imges.findViewById(R.id.iv_zidingyi_image);
                        final ImageView iv_invisible = (ImageView) imges.findViewById(R.id.iv_invisible);
                        //图片显示方式不是按照比例来了  全部显示 和 ios 一样
                        iv_invisible.setVisibility(View.GONE);
                        mApplication.setImages(ziDingYi.getContent(), iv_zidingyi_image);
                        KLog.d("--加载图片--getWidth--", "---");
                        iv_zidingyi_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> paths = new ArrayList<String>();
                                paths.add(ziDingYi.getContent());
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", 0);
                                bundle.putBoolean("noShowCount", true);
                                bundle.putStringArrayList("path", paths);
                                UIHelper.jump(mActivity, PhotoViewMultPicActivity.class, bundle);
                            }
                        });
                        ll_content.addView(imges);
                    }
                    if (ziDingYi.getType() == 3) {//1 表示文字，2 表示图  3表示链接
                        View linke = inflater.inflate(R.layout.view_show_detail_header_link, null);
                        TextView tv_zidingyi_link = (TextView) linke.findViewById(R.id.tv_zidingyi_link);
                        tv_zidingyi_link.setText(ziDingYi.getContent());
                        tv_zidingyi_link.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        ll_content.addView(linke);
                    }
                }
            }
        }

        tv_comment_count_1.setText(showDetailBean.getComment_count() + "");
        recomListEntities.clear();
        recomListEntities.addAll(showDetailBean.getRecomList());
        myAdapter.notifyDataSetChanged();


    }

    private void initLikeStatus(int status) {
        if (status > 0) {
            iv_like_icon.setImageResource(R.drawable.myshow_like_red_icon);
        } else {
            iv_like_icon.setImageResource(R.drawable.myshow_like_icon);
        }
    }

    private void initLikeUndComment() {
        tv_like_count.setText(showDetailBean.getZ_count() + "");
        tv_comment_count.setText(showDetailBean.getComment_count() + "");
    }

    private void initStoreStatus(int status) {
        if (status > 0) {
            ivStore.setImageResource(R.drawable.commen_store_red);
        } else {
            ivStore.setImageResource(R.drawable.store_white);
        }
    }


    private boolean likeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean likeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void likeUnlike(final ShowDetailBean myShowPassBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (myShowPassBean.getZ_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", myShowPassBean.getShow_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_clickzshare(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            if (myShowPassBean.getZ_id() > 0) {
                                UIHelper.t(mContext, "取消点赞成功");
                                myShowPassBean.setZ_id(0);
                                myShowPassBean.setZ_count(myShowPassBean.getZ_count() - 1);
                                EventBus.getDefault().post(new LikeShowEvent(myShowPassBean.getShow_id(), false));
                            } else {
                                UIHelper.t(mContext, "点赞成功");
                                myShowPassBean.setZ_id(1);
                                myShowPassBean.setZ_count(myShowPassBean.getZ_count() + 1);
                                EventBus.getDefault().post(new LikeShowEvent(myShowPassBean.getShow_id(), true));
                            }
                            initLikeStatus(showDetailBean.getZ_id());
                            initLikeUndComment();
                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing()) {
                        return;
                    }
                    likeActionFlag = true;
                    likeActionShowToUserFlag = true;
                    UIHelper.t(mContext, "操作失败");
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }


    private boolean storeActionFlag = true;//可以操作点赞，用于防止用户点赞过去频繁
    private boolean storeActionShowToUserFlag = true;//可以操作点赞，用于防止用户点赞频繁显示提示语

    private void storeUnStore(final ShowDetailBean myShowPassBean) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (myShowPassBean.getCol_id() > 0) {//已经点赞-----取消
                    jsonObject.put("type", 2);
                } else {
                    jsonObject.put("type", 1);
                }
                jsonObject.put("aim_id", myShowPassBean.getShow_id());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mApplication.apiClient.commonnote_colshare(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    storeActionFlag = true;
                    storeActionShowToUserFlag = true;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            if (myShowPassBean.getCol_id() > 0) {
                                UIHelper.t(mContext, "取消关注成功");
                                myShowPassBean.setCol_id(0);
                                EventBus.getDefault().post(new StoreShowEvent(myShowPassBean.getShow_id(), false));
                            } else {
                                UIHelper.t(mContext, "关注成功");
                                EventBus.getDefault().post(new StoreShowEvent(myShowPassBean.getShow_id(), true));
                                myShowPassBean.setCol_id(1);
                            }
                            initStoreStatus(showDetailBean.getCol_id());
                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing()) {
                        return;
                    }
                    storeActionFlag = true;
                    storeActionShowToUserFlag = true;
                    UIHelper.t(mContext, "操作失败");
                }
            });
        } else {
            UIHelper.jump(mActivity, LoginActivity.class);
        }
    }

    @Override
    public void onSmileClick(String filename) {
        if (etInput.getText().toString().length() >= 25) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return recomListEntities.size();
        }

        @Override
        public Object getItem(int i) {
            return recomListEntities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_show_detail_item, null);
                holder.ll_comment_1 = (LinearLayout) convertView.findViewById(R.id.ll_comment_1);
                holder.mListView = (MeasuredListView) convertView.findViewById(R.id.mListView);
                holder.iv_user_image = (CircleImageView) convertView.findViewById(R.id.iv_user_image);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_range = (TextView) convertView.findViewById(R.id.tv_range);
                holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
                holder.tv_ding = (TextView) convertView.findViewById(R.id.tv_ding);
                holder.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ShowDetailBean.RecomListEntity recomListEntity = recomListEntities.get(position);

            //mApplication.setImages(recomListEntity.getC_user_avatar(), holder.iv_user_image);
            //change by :胡峰，晒物评论中头像的修改
            mApplication.setTouImages(recomListEntity.getC_user_avatar(),holder.iv_user_image);
            holder.tv_user_name.setText(recomListEntity.getC_user_nick());
            holder.tv_time.setText(recomListEntity.getCreate_date());
            holder.tv_range.setText("#" + (position + 1));
            holder.tv_context.setText(recomListEntity.getContext());

            holder.tv_context.setText(SmileUtils.getSmiledCommentText(mContext, recomListEntity.getContext()));

            holder.tv_ding.setText("顶（" + recomListEntity.getZ_count() + "）");
            holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mApplication.isLogin()) {
                        showCommentPop(recomListEntity);
                    } else {
                        UIHelper.jump(mActivity, LoginActivity.class);
                    }
                }
            });
            if (recomListEntity.getSubVec().size() > 0) {
                holder.ll_comment_1.setVisibility(View.VISIBLE);
                holder.mListView.setAdapter(new MyCommentAdapter(recomListEntity.getSubVec()));
            } else {
                holder.ll_comment_1.setVisibility(View.GONE);

            }

            holder.tv_ding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dingComment(recomListEntity);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            LinearLayout ll_comment_1;
            MeasuredListView mListView;
            CircleImageView iv_user_image;
            TextView tv_user_name;
            TextView tv_time;
            TextView tv_range;
            TextView tv_context;
            TextView tv_ding;
            TextView tv_reply;
        }
    }

    private void dingComment(ShowDetailBean.RecomListEntity recomListEntity) {
        if (mApplication.isLogin()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", recomListEntity.getId());
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mApplication.apiClient.commoncomment_totopshowcommentbyapp(jsonObject, new ApiCallback() {
                @Override
                public void onApiStart() {

                }

                @Override
                public void onApiSuccess(String response) {
                    if (isFinishing())
                        return;
                    try {
                        JSONObject resultObj = new JSONObject(response);
                        if (JSONUtil.isOK(resultObj)) {
                            loadData(true);
                        } else {
                            UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiFailure(Request request, Exception e) {
                    if (isFinishing())
                        return;
                    UIHelper.t(mContext, R.string.net_error);
                }
            });
        } else {
            gotoLogin();
        }
    }


    private class MyCommentAdapter extends BaseAdapter {

        ArrayList<ShowDetailBean.RecomListEntity.SubRecomListEntity> subRecomListEntities;

        public MyCommentAdapter(ArrayList<ShowDetailBean.RecomListEntity.SubRecomListEntity> subRecomListEntities) {
            this.subRecomListEntities = subRecomListEntities;
        }

        @Override
        public int getCount() {
            return subRecomListEntities.size();
        }

        @Override
        public Object getItem(int i) {
            return subRecomListEntities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_comment_child_item, null);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ShowDetailBean.RecomListEntity.SubRecomListEntity entity = subRecomListEntities.get(position);
            holder.tv_user_name.setText(entity.getC_user_nick());

            holder.tv_context.setText(SmileUtils.getSmiledSubCommentText(mContext, entity.getContext()));

            return convertView;
        }

        public class ViewHolder {
            TextView tv_user_name;
            TextView tv_context;
        }
    }

    MyCommentPop myCommentPop;

    private void showCommentPop(final ShowDetailBean.RecomListEntity bean) {
        myCommentPop = new MyCommentPop(this, rlParent);
        myCommentPop.setCallBack(new MyCommentPop.CallBack() {
            @Override
            public void send(final TextView sendTv, EditText content) {
                sendTv.setEnabled(false);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("pid", bean.getId());
                    jsonObject.put("context", content.getText().toString());
                    jsonObject.put("root_id", showDetailBean.getShow_id());
                    jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                    jsonObject.put("owner_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mApplication.apiClient.commoncomment_submitforshowbyapp(jsonObject, new ApiCallback() {
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
                                if (recomListEntities.size() > 0) {
                                    list.setSelection(0);
                                }
                                loadData(false);
                                sendTv.setEnabled(true);
                                myCommentPop.dismiss();
                                UIUtil.hideInputMethod(mActivity);
                            } else {
                                sendTv.setEnabled(true);
                                UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
