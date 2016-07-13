package com.buycolle.aicang.ui.activity.userinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.api.AppUrl;
import com.buycolle.aicang.api.callback.ResultCallback;
import com.buycolle.aicang.api.request.OkHttpRequest;
import com.buycolle.aicang.bean.UserBean;
import com.buycolle.aicang.event.EditUserInfoEvent;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.activity.comment.CommentUserCropImageActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.SelectPicDialog;
import com.buycolle.aicang.ui.view.SexSelectDialog;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.ImageUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/4.
 */
public class UserInfoActivity extends BaseActivity {

    private final int Result_PIC_1 = 100;
    private final int Result_PIC_2 = 300;
    private final int REQUEST_CROP_IMAGE = 200;


    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.rl_gexing_lay)
    RelativeLayout rlGexingLay;
    @Bind(R.id.rl_sexy_lay)
    RelativeLayout rlSexyLay;
    @Bind(R.id.rl_phone_lay)
    RelativeLayout rlPhoneLay;
    @Bind(R.id.rl_email_lay)
    RelativeLayout rlEmailLay;
    @Bind(R.id.rl_address_lay)
    RelativeLayout rlAddressLay;
    @Bind(R.id.rl_zhifubao_lay)
    RelativeLayout rlZhifubaoLay;
    @Bind(R.id.rl_editpsw_lay)
    RelativeLayout rlEditpswLay;

    @Bind(R.id.tv_gexing_title)
    TextView tvGexingTitle;
    @Bind(R.id.tv_qianming_value)
    TextView tvQianmingValue;
    @Bind(R.id.iv_gexing_ico)
    ImageView ivGexingIco;
    @Bind(R.id.tv_sex_title)
    TextView tvSexTitle;
    @Bind(R.id.iv_sex_ico)
    ImageView ivSexIco;
    @Bind(R.id.tv_phone_title)
    TextView tvPhoneTitle;
    @Bind(R.id.iv_phone_ico)
    ImageView ivPhoneIco;
    @Bind(R.id.tv_email_title)
    TextView tvEmailTitle;
    @Bind(R.id.iv_email_ico)
    ImageView ivEmailIco;
    @Bind(R.id.tv_address_title)
    TextView tvAddressTitle;
    @Bind(R.id.iv_address_ico)
    ImageView ivAddressIco;
    @Bind(R.id.tv_zhifubao_title)
    TextView tvZhifubaoTitle;
    @Bind(R.id.iv_zhifubao_ico)
    ImageView ivZhifubaoIco;
    @Bind(R.id.tv_editpsw_title)
    TextView tvEditpswTitle;
    @Bind(R.id.iv_editpsw_ico)
    ImageView ivEditpswIco;

    UserBean userBean;
    @Bind(R.id.tv_sex_value)
    TextView tvSexValue;
    @Bind(R.id.tv_phone_value)
    TextView tvPhoneValue;
    @Bind(R.id.tv_email_value)
    TextView tvEmailValue;

    private Uri uri;

    @OnClick(R.id.rl_gexing_lay)
    public void gexinQM() {
        Bundle bundle = new Bundle();
        bundle.putString("gexing", userBean.getPerson_tip());
        UIHelper.jump(mActivity, GeXingQmActivity.class, bundle);
    }

    private static String recentPicPath;//当前拍照的路径

    @OnClick(R.id.profile_image)
    public void updateImage() {
        new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
            @Override
            public void select(int position) {
                if (position == 1) {//照相
                    try {
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String filename = FileUtil.getPortraitCathe() + File.separator + LoginConfig.getUserInfo(mContext).getUser_id() + System.currentTimeMillis() + ".JPEG";
                        File file = new File(filename);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        recentPicPath = file.getAbsolutePath();
                        uri = Uri.fromFile(file);
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(openCameraIntent, Result_PIC_2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (position == 2) {//相册
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(i, Result_PIC_1);
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 16/1/4  个人照片返回
        if (requestCode == Result_PIC_1 && resultCode == RESULT_OK) {//个人照片相册返回
            Uri uri = data.getData();
            String pathLocal = ImageUtils.getPath(mContext, uri);
            KLog.d("返回的本地图片路径", pathLocal);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", pathLocal);
            UIHelper.jumpForResult(mActivity, CommentUserCropImageActivity.class, bundle, CommentUserCropImageActivity.COROP_REQUEST);
        }
        // TODO: 16/1/4  拍照
        if (requestCode == Result_PIC_2 && resultCode == RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", recentPicPath);
            KLog.d("拍照返回的本地图片路径", recentPicPath);
            UIHelper.jumpForResult(mActivity, CommentUserCropImageActivity.class, bundle, CommentUserCropImageActivity.COROP_REQUEST);
        }
        if (requestCode == CommentUserCropImageActivity.COROP_REQUEST && resultCode == CommentUserCropImageActivity.COROP_RESULT) {
            String path = data.getStringExtra(CommentUserCropImageActivity.RERULT_PATH);
            if (path != null) {
                uploadImages(new File(path));
            }
        }


    }


    private void startZoom(Uri uri) {
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        // 宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪时是否保留图片的比例
        intent.putExtra("scale", true);
        //不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        // 去黑边
        intent.putExtra("scaleUpIfNeeded", true);
        //设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }

    private void uploadImages(File filename) {
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(filename.getName(), filename))
                .upload(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        showLoadingDialog("上传中...");
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.d("拍卖返回---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtil.isOK(jsonObject)) {
                                String path = jsonObject.getJSONObject("resultMap").getString("message");
                                updateUserInfo(false, path);
                            } else {
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @OnClick(R.id.rl_sexy_lay)
    public void selectSex() {
        new SexSelectDialog(mContext, R.style.bottom_dialog).setListner(new SexSelectDialog.OnPicChooserListener() {
            @Override
            public void share(int position) {
                if (position == 1) {//男
                    updateUserInfo(true, "1");
                }
                if (position == 2) {//女
                    updateUserInfo(true, "2");
                }
            }
        }).showDialog(userBean.getSex());
    }

    @OnClick(R.id.rl_email_lay)
    public void bindEmail() {
        Bundle bundle = new Bundle();
        bundle.putString("email", userBean.getEmail());
        UIHelper.jump(mActivity, BindEmailActivity.class, bundle);
    }

    @OnClick(R.id.rl_address_lay)
    public void address() {
        UIHelper.jump(mActivity, EditAddressActivity.class);
    }

    @OnClick(R.id.rl_zhifubao_lay)
    public void zhifubao() {
        if (TextUtils.isEmpty(userBean.getAlipay_card())) {
            UIHelper.jump(mActivity, EditZhifubaoInfoActivity.class);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("zhifubao", userBean.getAlipay_card());
            UIHelper.jump(mActivity, ZhifubaoInfoActivity.class, bundle);
        }
    }

    @OnClick(R.id.rl_editpsw_lay)
    public void editPsw() {
        UIHelper.jump(mActivity, EditPswActivity.class);
    }


    //更新个人资料
    public void onEventMainThread(EditUserInfoEvent event) {
        if (event.getStatus() == 1) {//个性签名更新
            String gexing = LoginConfig.getUserInfoGeXing(mContext);
            tvQianmingValue.setText(gexing);
            userBean.setPerson_tip(gexing);
        }
        if (event.getStatus() == 2) {//邮箱更新
            String email = LoginConfig.getUserInfoEmail(mContext);
            tvEmailValue.setText(email);
            userBean.setEmail(email);
        }
        if (event.getStatus() == 3) {//支付宝更新
            String zhifubao = LoginConfig.getUserInfoZhifuBao(mContext);
            userBean.setAlipay_card(zhifubao);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        myHeader.init("个人信息", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appuser_getinfosbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("加载个人信息");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        JSONObject userInfoObj = resultObj.getJSONObject("infos");
                        userBean = new Gson().fromJson(userInfoObj.toString(), UserBean.class);
                        LoginConfig.updateUserInfoByUserCenter(mContext, userBean);
                        initView();
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
                if (isFinishing()) {
                    finish();
                }
            }
        });
    }

    private void updateUserInfo(final boolean isSex, final String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (isSex) {
                jsonObject.put("sex", value);
            } else {
                jsonObject.put("user_avatar", value);

            }
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appuser_updateinfosbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("更新中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        if (isSex) {
                            userBean.setSex(Integer.valueOf(value));
                            LoginConfig.updateUserInfoSex(mContext, Integer.valueOf(value));
                            initView();
                        } else {
                            LoginConfig.updateUserInfoImage(mContext, value);
                            EventBus.getDefault().post(new EditUserInfoEvent(0));
                            getUserInfo();
                        }
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
                if (isFinishing()) {
                    finish();
                }
            }
        });

    }


    private void initView() {
        UserBean userBean = LoginConfig.getUserInfo(mContext);
        tvUserName.setText(userBean.getUser_nick());
        //mApplication.setImages(userBean.getUser_avatar(), profileImage);
        //change by :胡峰，头像的处理
        mApplication.setTouImages(userBean.getUser_avatar(),profileImage);
        tvQianmingValue.setText(userBean.getPerson_tip());
        if (userBean.getSex() == 1) {
            tvSexValue.setText("男");
        } else if (userBean.getSex() == 2) {
            tvSexValue.setText("女");
        } else {
            tvSexValue.setText("");
        }
        tvPhoneValue.setText(userBean.getUser_phone());
        if (TextUtils.isEmpty(userBean.getEmail())) {
            tvEmailValue.setText("未绑定");
        } else {
            tvEmailValue.setText(userBean.getEmail());
        }
    }
}
