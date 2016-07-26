package com.buycolle.aicang.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.api.AppUrl;
import com.buycolle.aicang.api.callback.ResultCallback;
import com.buycolle.aicang.api.request.OkHttpRequest;
import com.buycolle.aicang.bean.PostShowBean;
import com.buycolle.aicang.bean.ShowDetailBean;
import com.buycolle.aicang.event.EditShowEvent;
import com.buycolle.aicang.ui.activity.comment.CommentShow2CropImageActivity;
import com.buycolle.aicang.ui.activity.comment.CommentShowCropImageActivity;
import com.buycolle.aicang.ui.activity.shangpintypes.ShangPinTypesActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeDialog;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.ui.view.PromotedActionsLibrary;
import com.buycolle.aicang.ui.view.SelectPicDialog;
import com.buycolle.aicang.ui.view.draglist.DragSortController;
import com.buycolle.aicang.ui.view.draglist.DragSortListView;
import com.buycolle.aicang.util.ImageUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 发布show
 * <p/>
 * Created by joe on 16/3/12.
 */
public class EditPublicShowActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.ds_list)
    DragSortListView dsList;
    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.tv_save_draft)
    TextView tvSaveDraft;
    @Bind(R.id.tv_publish)
    TextView tvPublish;
    @Bind(R.id.fl_back)
    FrameLayout fl_back;

    private ArrayList<PostShowBean> postShowBeans;
    private PostShowBean mainImgBean = new PostShowBean();



    private MyAdapter myAdapter;
    private String mainLoalPath = "";
    private String mainServerPath = "";
    private String cate_id;
    private int show_id;
    private LinearLayout ll_add;
    private FrameLayout iv_add_item;
    private EditText et_input_link;
    private EditText et_input_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_show);
        ButterKnife.bind(this);
        postShowBeans = new ArrayList<>();
//        myHeader.init("我要晒物", new MyHeader.Action() {
//            @Override
//            public void leftActio() {
//                finish();
//            }
//        });
        myHeader.initShow("我要晒物");
        show_id = _Bundle.getInt("show_id");
        initHeader();
        DragSortController controller = new DragSortController(dsList);
        controller.setDragHandleId(R.id.iv_drag);
        controller.setClickRemoveId(R.id.iv_delete);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DRAG);
        controller.setRemoveMode(DragSortController.CLICK_REMOVE);
        dsList.setFloatViewManager(controller);
        dsList.setOnTouchListener(controller);
        dsList.setDragEnabled(true);
        dsList.setDropListener(onDrop);
        dsList.setRemoveListener(onRemove);
        myAdapter = new MyAdapter();
        dsList.setAdapter(myAdapter);

        container = (FrameLayout) findViewById(R.id.container);

        final PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();
        promotedActionsLibrary.setup(getApplicationContext(), container);
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.public_text_icon), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (promotedActionsLibrary.isable) {
                    PostShowBean postShowBean = new PostShowBean();
                    postShowBean.setType(1);
                    postShowBeans.add(postShowBean);
                    myAdapter.notifyDataSetChanged();
                    dsList.setSelection(postShowBeans.size() - 1);
                }
            }
        });
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.public_image_icon), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (promotedActionsLibrary.isable) {
                    PostShowBean postShowBean = new PostShowBean();
                    postShowBean.setType(2);
                    postShowBeans.add(postShowBean);
                    myAdapter.notifyDataSetChanged();
                    dsList.setSelection(postShowBeans.size() - 1);
                }

            }
        });
        promotedActionsLibrary.addMainItem(getResources().getDrawable(R.drawable.public_show_add));


        tvSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDraft();
            }
        });
        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPublish();
            }
        });

        loadData();

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoticeDialog(mContext,"返回确认","确认放弃当前编辑的晒物信息\n并返回么？").setCallBack(new NoticeDialog.CallBack() {
                    @Override
                    public void ok() {
                        finish();
                    }

                    @Override
                    public void cancle() {

                    }
                }).show();
            }
        });
    }

    ShowDetailBean showDetailBean;

    private void loadData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("show_id", show_id);
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.show_getdetailidbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("加载数据中...");
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
                        initData();
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
                UIHelper.t(mContext, "初始化失败，退出重试...");
                finish();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initData() {

        //主图
        mainLoalPath = "";
        mainServerPath = showDetailBean.getCover_pic();
        if (!TextUtils.isEmpty(mainServerPath)){
            iv_main_close.setVisibility(View.VISIBLE);
            iv_fisrt_status.setVisibility(View.GONE);
            ll_add.setVisibility(View.GONE);
            mApplication.setShowImages(mainServerPath, iv_fisrt);
        }else {
            iv_main_close.setVisibility(View.GONE);
            iv_fisrt_status.setVisibility(View.GONE);
            ll_add.setVisibility(View.VISIBLE);
        }

        //mApplication.setImages(mainServerPath, iv_fisrt);
        //change by :胡峰
        //mApplication.setShowImages(mainServerPath, iv_fisrt);

        iv_main_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_fisrt_status.setVisibility(View.GONE);
                iv_main_close.setVisibility(View.GONE);
                ll_add.setVisibility(View.VISIBLE);
                iv_fisrt.setImageResource(R.color.transparent);
                mainLoalPath = "";
                mainServerPath = "";
            }
        });


        //类型
        tv_goods_type_value.setText(showDetailBean.getCate_name());
        cate_id = showDetailBean.getCate_id() + "";
        //标题
        et_title_top.setText(showDetailBean.getTitle());
        //描述
        et_desc_top.setText(showDetailBean.getIntro());


        String context = showDetailBean.getContext();
        if (!TextUtils.isEmpty(context)) {
            ArrayList<PostShowBean> beans = new Gson().fromJson(context.toString(), new TypeToken<List<PostShowBean>>() {
            }.getType());
            if (beans.size() > 0) {
                for (PostShowBean bean : beans) {
                    bean.setServer(true);
                    bean.setStatus(PostShowBean.Status.DONE);
                    postShowBeans.add(bean);
                }
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    NoticeSingleDialog noticeSingleDialog;

    /**
     * 发布
     */
    private void submitPublish() {
        if (TextUtils.isEmpty(mainServerPath)) {
            UIHelper.t(mContext, "请上传一张图片做为晒物封面");
            return;
        }

        if (TextUtils.isEmpty(cate_id)) {
            UIHelper.t(mContext, "请选择物品类型");
            return;
        }
        if (TextUtils.isEmpty(et_title_top.getText().toString())) {
            UIHelper.t(mContext, "请填写标题");
            return;
        }
        if (TextUtils.isEmpty(et_desc_top.getText().toString())) {
            UIHelper.t(mContext, "请填写文字描述");
            return;
        }

        new NoticeDialog(mContext,"发布确认","确认发布这篇晒物么？").setCallBack(new NoticeDialog.CallBack() {
            @Override
            public void ok() {
                boolean isOK = true;
                if (postShowBeans.size() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (PostShowBean postShowBean : postShowBeans) {
                        if (postShowBean.getType() == 2 && postShowBean.getStatus() != PostShowBean.Status.DONE) {
                            isOK = false;
                        }
                    }
                }
                if (!isOK) {
                    UIHelper.t(mContext, "还有图片尚未上传成功");
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    if (postShowBeans.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (PostShowBean postShowBean : postShowBeans) {
                            JSONObject dingyiObj = new JSONObject();
                            dingyiObj.put("type", postShowBean.getType());
                            dingyiObj.put("content", postShowBean.getContent());
                            jsonArray.put(dingyiObj);
                        }
                        jsonObject.put("context", jsonArray);
                    }
                    jsonObject.put("cate_id", cate_id);
                    jsonObject.put("title", et_title_top.getText().toString());
                    jsonObject.put("cover_pic", mainServerPath);
                    jsonObject.put("show_id", show_id);
                    jsonObject.put("intro", et_desc_top.getText().toString());
                    jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
                    jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mApplication.apiClient.show_resubmitbyapp(jsonObject, new ApiCallback() {
                    @Override
                    public void onApiStart() {
                        showLoadingDialog("提交数据中...");
                    }

                    @Override
                    public void onApiSuccess(String response) {
                        if (isFinishing())
                            return;
                        dismissLoadingDialog();
                        try {
                            JSONObject resultObj = new JSONObject(response);
                            if (JSONUtil.isOK(resultObj)) {
                                EventBus.getDefault().post(new EditShowEvent(1));
                                noticeSingleDialog = new NoticeSingleDialog(mContext, "温馨提示", "您已成功发布\n请耐心等待平台审核", "我知道了").setCallBack(new NoticeSingleDialog.CallBack() {
                                    @Override
                                    public void ok() {

                                    }

                                    @Override
                                    public void cancle() {

                                    }
                                });
                                noticeSingleDialog.show();
                                noticeSingleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //EventBus.getDefault().post(new EditShowEvent(2));
                                        finish();
                                    }
                                });
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
            }

            @Override
            public void cancle() {

            }
        }).show();

    }


    ImageView iv_fisrt;
    ImageView iv_fisrt_status;
    ImageView iv_main_close;
    RelativeLayout rl_goods_type;
    TextView tv_goods_type_value;
    EditText et_title_top;
    EditText et_desc_top;

    /**
     * 保存草稿
     */
    private void submitDraft() {
        boolean isOK = true;
        if (postShowBeans.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (PostShowBean postShowBean : postShowBeans) {
                if (postShowBean.getType() == 2 && postShowBean.getStatus() != PostShowBean.Status.DONE) {
                    isOK = false;
                }
            }
        }
        if (!isOK) {
            UIHelper.t(mContext, "还有图片尚未上传成功");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (postShowBeans.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (PostShowBean postShowBean : postShowBeans) {
                    JSONObject dingyiObj = new JSONObject();
                    dingyiObj.put("type", postShowBean.getType());
                    dingyiObj.put("content", postShowBean.getContent());
                    jsonArray.put(dingyiObj);
                }
                jsonObject.put("context", jsonArray);
            }
            jsonObject.put("cate_id", cate_id);
            jsonObject.put("show_id", show_id);
            jsonObject.put("title", et_title_top.getText().toString());
            jsonObject.put("cover_pic", mainServerPath);
            jsonObject.put("intro", et_desc_top.getText().toString());
            jsonObject.put("ower_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mApplication.apiClient.show_submittoreadybyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("保存数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                dismissLoadingDialog();
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        UIHelper.t(mContext, "草稿保存成功");
                        EventBus.getDefault().post(new EditShowEvent(0));
                        finish();
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
    }

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        PostShowBean item = myAdapter.getItem(from);
                        myAdapter.remove(item);
                        myAdapter.insert(item, to);
                    }
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    myAdapter.remove(myAdapter.getItem(which));
                }
            };


    private final int RESULT_PIC_1_GALLARY = 100;
    private final int RESULT_PIC_1_CAMERA = 200;

    private final int RESULT_PIC_2_GALLARY = 400;
    private final int RESULT_PIC_2_CAMERA = 500;

    private final int REQUEST_GOOD_TYPE = 1000;


    private void initHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.view_publish_show_header, null);

        iv_fisrt = (ImageView) header.findViewById(R.id.iv_fisrt);
        iv_fisrt_status = (ImageView) header.findViewById(R.id.iv_fisrt_status);
        iv_main_close = (ImageView) header.findViewById(R.id.iv_main_close);
        ll_add = (LinearLayout) header.findViewById(R.id.ll_add);

        iv_fisrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                    @Override
                    public void select(int position) {
                        if (position == 1) {//照相
                            photo(RESULT_PIC_1_CAMERA);
                        }
                        if (position == 2) {//相册
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(i, RESULT_PIC_1_GALLARY);
                        }
                    }
                }).show();
            }
        });

        rl_goods_type = (RelativeLayout) header.findViewById(R.id.rl_goods_type);
        tv_goods_type_value = (TextView) header.findViewById(R.id.tv_goods_type_value);
        et_title_top = (EditText) header.findViewById(R.id.et_title_top);
        et_desc_top = (EditText) header.findViewById(R.id.et_desc_top);

        dsList.addHeaderView(header);

        rl_goods_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromShow", true);
                UIHelper.jumpForResult(mActivity, ShangPinTypesActivity.class, bundle,REQUEST_GOOD_TYPE);
            }
        });

    }

    private Uri photoUri;
    private String recentPicPath;//当前拍照的路径

    public void photo(int resultCode) {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
            }
            if (file != null) {
                recentPicPath = file.getPath();
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, resultCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int currentPosition = 0;

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return postShowBeans.size();
        }

        @Override
        public PostShowBean getItem(int i) {
            return postShowBeans.get(i);
        }

        public void remove(PostShowBean item) {
            postShowBeans.remove(item);
            notifyDataSetChanged();
        }

        public void insert(PostShowBean item, int i) {
            postShowBeans.add(i, item);
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_public_show_item, null);
            iv_add_item = (FrameLayout) view.findViewById(R.id.iv_add_item);
            ImageView iv_main = (ImageView) view.findViewById(R.id.iv_main);
            ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
            LinearLayout ll_add1 = (LinearLayout) view.findViewById(R.id.ll_add1);
            ImageView iv_status = (ImageView) view.findViewById(R.id.iv_status);
            et_input_link = (EditText) view.findViewById(R.id.et_input_link);
            et_input_content = (EditText) view.findViewById(R.id.et_input_content);
            final PostShowBean postShowBean = postShowBeans.get(position);
            if (postShowBean.getType() == 1) {//文字
                et_input_content.setText(postShowBean.getContent());
                iv_add_item.setVisibility(View.GONE);
                et_input_link.setVisibility(View.GONE);
                et_input_content.setVisibility(View.VISIBLE);
                et_input_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        postShowBean.setContent(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            } else if (postShowBean.getType() == 2) {//图片
                iv_add_item.setVisibility(View.VISIBLE);
                et_input_link.setVisibility(View.GONE);
                et_input_content.setVisibility(View.GONE);
                if (TextUtils.isEmpty(postShowBean.getImageLocal()) && !postShowBean.isServer()) {
                    iv_status.setVisibility(View.GONE);
                    ll_add1.setVisibility(View.VISIBLE);
                    iv_add.setVisibility(View.VISIBLE);
                    iv_main.setImageResource(R.color.transparent);
                    iv_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentPosition = position;
                            new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                                @Override
                                public void select(int position) {
                                    if (position == 1) {//照相
                                        photo(RESULT_PIC_2_CAMERA);
                                    }
                                    if (position == 2) {//相册
                                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                        startActivityForResult(i, RESULT_PIC_2_GALLARY);
                                    }
                                }
                            }).show();
                        }
                    });
                } else {
                    if (postShowBean.isServer()) {
                        iv_status.setVisibility(View.GONE);
                        iv_add.setVisibility(View.GONE);
                        ll_add1.setVisibility(View.GONE);
                        iv_add_item.setBackgroundResource(R.drawable.shape_white_black);
                        //mApplication.setImages(postShowBean.getContent(), iv_main);
                        //change by hufeng:
                        mApplication.setShowImages(postShowBean.getContent(), iv_main);
                    } else {
                        if (postShowBean.getStatus() == PostShowBean.Status.FAIL) {
                            iv_status.setVisibility(View.VISIBLE);
                            iv_add.setVisibility(View.GONE);
                            ll_add1.setVisibility(View.GONE);
                            iv_add_item.setBackgroundResource(R.drawable.shape_white_black);
                            iv_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    uploadListmages(postShowBean);
                                }
                            });
                        } else if (postShowBean.getStatus() == PostShowBean.Status.INIT) {
                            iv_status.setVisibility(View.GONE);
                            iv_add.setVisibility(View.GONE);
                            ll_add1.setVisibility(View.GONE);
                            iv_add_item.setBackgroundResource(R.drawable.shape_white_black);
                            //change by hufeng
                            mApplication.setShowImages("file://" + postShowBean.getImageLocal(), iv_main);
                        } else {
                            iv_status.setVisibility(View.GONE);
                            iv_add.setVisibility(View.GONE);
                            ll_add1.setVisibility(View.GONE);
                            iv_add_item.setBackgroundResource(R.drawable.shape_white_black);
                            //mApplication.setImages("file://" + postShowBean.getImageLocal(), iv_main);
                            //change by hufeng:
                            mApplication.setShowImages("file://" + postShowBean.getImageLocal(), iv_main);
                        }
                    }

                }
            }
//            else {//链接
//                et_input_link.setText(postShowBean.getContent());
//                iv_add_item.setVisibility(View.GONE);
//                et_input_link.setVisibility(View.VISIBLE);
//                et_input_content.setVisibility(View.GONE);
//                et_input_link.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        postShowBean.setContent(charSequence.toString());
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });
//            }

            return view;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //封面图片返回来  相册
        if (requestCode == RESULT_PIC_1_GALLARY && resultCode == mActivity.RESULT_OK) {
            Uri uri = data.getData();
            String pathLocal = ImageUtils.getPath(mContext, uri);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", pathLocal);
            UIHelper.jumpForResult(mActivity, CommentShowCropImageActivity.class, bundle, CommentShowCropImageActivity.COROP_REQUEST);
        }
        //封面图片返回来  相机
        if (requestCode == RESULT_PIC_1_CAMERA && resultCode == mActivity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", recentPicPath);
            UIHelper.jumpForResult(mActivity, CommentShowCropImageActivity.class, bundle, CommentShowCropImageActivity.COROP_REQUEST);
        }

        if (requestCode == RESULT_PIC_2_GALLARY && resultCode == mActivity.RESULT_OK) {
            Uri uri = data.getData();
            String pathLocal = ImageUtils.getPath(mContext, uri);
            KLog.d("返回的本地图片路径", pathLocal);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath",pathLocal);
            UIHelper.jumpForResult(mActivity, CommentShow2CropImageActivity.class, bundle, CommentShow2CropImageActivity.COROP_REQUEST);
//            String path = ImageUtils.getSmallBitmap(pathLocal);
//            PostShowBean postShowBean = postShowBeans.get(currentPosition);
//            postShowBean.setImageLocal(path);
//            postShowBean.setStatus(PostShowBean.Status.INIT);
//            myAdapter.notifyDataSetChanged();
//            uploadListmages(postShowBean);
        }

        if (requestCode == RESULT_PIC_2_CAMERA && resultCode == mActivity.RESULT_OK) {
//            String path = ImageUtils.getSmallBitmap(recentPicPath);
//            PostShowBean postShowBean = postShowBeans.get(currentPosition);
//            postShowBean.setImageLocal(path);
//            postShowBean.setStatus(PostShowBean.Status.INIT);
//            myAdapter.notifyDataSetChanged();
//            uploadListmages(postShowBean);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath",recentPicPath);
            UIHelper.jumpForResult(mActivity,CommentShow2CropImageActivity.class,bundle,CommentShow2CropImageActivity.COROP_REQUEST);
        }

        //物品类型返回
        if (requestCode == REQUEST_GOOD_TYPE && resultCode == mActivity.RESULT_OK) {
            int level = data.getIntExtra("level", 0);
            cate_id = data.getStringExtra("cate_id");
            if (level == 1) {
                tv_goods_type_value.setText(data.getStringExtra("cate_name"));
            } else if (level == 2) {
                tv_goods_type_value.setText(data.getStringExtra("p_cate_name") + "/" + data.getStringExtra("cate_name"));
            } else {
                tv_goods_type_value.setText(data.getStringExtra("p_cate_name") + "/" + data.getStringExtra("p_1_cate_name")+"/"+data.getStringExtra("cate_name"));
            }
        }

        if (requestCode == CommentShowCropImageActivity.COROP_REQUEST && resultCode == CommentShowCropImageActivity.COROP_RESULT) {
            String path = data.getStringExtra(CommentShowCropImageActivity.RERULT_PATH);
            if (path != null) {
                mainLoalPath = path;
                //mApplication.setImages("file://" + path, iv_fisrt);
                //change by hufeng
                mApplication.setShowImages("file://" + path, iv_fisrt);
                ll_add.setVisibility(View.GONE);
                uploadMainImages(mainLoalPath);
            }
        }

        if (requestCode == CommentShow2CropImageActivity.COROP_REQUEST && resultCode == CommentShow2CropImageActivity.COROP_RESULT){
            //String path = ImageUtils.getSmallBitmap(data.getStringExtra(CommentCropImage2Activity.RERULT_PATH));
            String path = data.getStringExtra(CommentShow2CropImageActivity.RERULT_PATH);
            PostShowBean postShowBean = postShowBeans.get(currentPosition);
            postShowBean.setImageLocal(path);
            postShowBean.setStatus(PostShowBean.Status.INIT);
            myAdapter.notifyDataSetChanged();
            uploadListmages(postShowBean);
        }


    }


    private Handler mainImageHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//失败了
                iv_fisrt_status.setVisibility(View.VISIBLE);
                iv_main_close.setVisibility(View.VISIBLE);
                ll_add.setVisibility(View.GONE);
                iv_fisrt_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_add.setVisibility(View.VISIBLE);
                        iv_fisrt_status.setVisibility(View.GONE);
                        uploadMainImages(mainLoalPath);
                    }
                });

            }
            if (msg.what == 1) {//成功
                iv_main_close.setVisibility(View.VISIBLE);
                iv_fisrt_status.setVisibility(View.GONE);
                iv_main_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_fisrt_status.setVisibility(View.GONE);
                        iv_main_close.setVisibility(View.GONE);
                        ll_add.setVisibility(View.VISIBLE);
                        iv_fisrt.setImageResource(R.color.transparent);
                        mainLoalPath = "";
                        mainServerPath = "";
                    }
                });
            }
        }
    };

    private void uploadMainImages(final String mainPath) {
        iv_main_close.setVisibility(View.VISIBLE);
        iv_main_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_fisrt_status.setVisibility(View.GONE);
                iv_main_close.setVisibility(View.GONE);
                iv_fisrt.setImageResource(R.color.transparent);
                mainLoalPath = "";
                mainServerPath = "";
            }
        });

        File filename = new File(mainPath);
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(filename.getName(), filename))
                .upload(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        mainImageHander.sendEmptyMessage(0);

                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.d("上传图片-onResponse-返回---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtil.isOK(jsonObject)) {
                                String path = jsonObject.getJSONObject("resultMap").getString("message");
                                mainServerPath = path;
                                mainImageHander.sendEmptyMessage(1);
                            } else {
                                mainImageHander.sendEmptyMessage(0);
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            mainImageHander.sendEmptyMessage(0);
                            e.printStackTrace();
                        }

                    }
                });
    }

    private Handler listImageHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myAdapter.notifyDataSetChanged();
        }
    };

    private void uploadListmages(final PostShowBean bean) {
        File filename = new File(bean.getImageLocal());
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(filename.getName(), filename))
                .upload(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        bean.setStatus(PostShowBean.Status.FAIL);
                        listImageHander.sendEmptyMessage(0);
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.d("拍卖onResponse返回---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtil.isOK(jsonObject)) {
                                bean.setStatus(PostShowBean.Status.DONE);
                                String path = jsonObject.getJSONObject("resultMap").getString("message");
                                bean.setContent(path);
                                listImageHander.sendEmptyMessage(1);
                            } else {
                                bean.setStatus(PostShowBean.Status.FAIL);
                                listImageHander.sendEmptyMessage(0);
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            bean.setStatus(PostShowBean.Status.FAIL);
                            listImageHander.sendEmptyMessage(0);
                            e.printStackTrace();
                        }

                    }
                });
    }

    /***
     * add by :胡峰，晒物草稿发布成功后，我要晒物界面内容清空
     */
    private void initSuccessView(){
        mainImgBean.setImageLocal("");
        iv_fisrt.setImageResource(R.color.transparent);
        iv_main_close.setVisibility(View.GONE);
        ll_add.setVisibility(View.VISIBLE);
        iv_fisrt_status.setVisibility(View.GONE);
        tv_goods_type_value.setText("请选择");
        tv_goods_type_value.setTextColor(mActivity.getResources().getColor(R.color.gray_tv));
        et_title_top.setText("");
        et_desc_top.setText("");
        iv_add_item.setVisibility(View.GONE);
        et_input_content.setVisibility(View.GONE);
        et_input_link.setVisibility(View.GONE);
        myAdapter.notifyDataSetChanged();
    }

}
