package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.api.AppUrl;
import com.buycolle.aicang.api.callback.ResultCallback;
import com.buycolle.aicang.api.request.OkHttpRequest;
import com.buycolle.aicang.bean.HomeTopAddBeanNew;
import com.buycolle.aicang.ui.activity.comment.CommentIdCardCropImageActivity;
import com.buycolle.aicang.ui.activity.comment.CommentShowCropImageActivity;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.NoticeSingleDialog;
import com.buycolle.aicang.ui.view.SelectPicDialog;
import com.buycolle.aicang.ui.view.autoscrollviewpager.AutoScrollViewPager;
import com.buycolle.aicang.ui.view.autoscrollviewpager.HomeAddImagePagerAdapter;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.ImageUtils;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.Validator;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/15.
 */
public class ToBeSallerActivity extends BaseActivity {

    private final int Result_PIC_1 = 100;
    private final int Result_PIC_2 = 300;

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.convenientBanner)
    AutoScrollViewPager viewPager;
    @Bind(R.id.et_input_username)
    EditText etInputUsername;
    @Bind(R.id.et_input_id)
    EditText etInputId;
    @Bind(R.id.et_input_zhifubao)
    EditText etInputZhifubao;
    @Bind(R.id.tv_addres_value)
    TextView tvAddresValue;
    @Bind(R.id.iv_addres_arrow)
    ImageView ivAddresArrow;
    @Bind(R.id.rl_addres)
    RelativeLayout rlAddres;
    @Bind(R.id.tv_jiaoyi_liucheng)
    TextView tvJiaoyiLiucheng;
    @Bind(R.id.tv_kuaidi_jubao)
    TextView tvKuaidiJubao;
    @Bind(R.id.btn_save)
    Button btnSave;
    OptionsPickerView pvOptions;
    @Bind(R.id.tv_goto_image_demo)
    TextView tvGotoImageDemo;
    @Bind(R.id.iv_id_card)
    ImageView ivIdCard;
    @Bind(R.id.iv_add_id_card)
    ImageView ivAddIdCard;
    @Bind(R.id.iv_add_item)
    FrameLayout iv_add_item;
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();


    private String province = "";
    private String city = "";

    private String upPath = "";
    private static String recentPicPath = "";
    private String tempPicPath = "";


    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    private ACache aCache;
    private ArrayList<HomeTopAddBeanNew> homeTopAddBeens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tobe_saller);
        ButterKnife.bind(this);
        aCache = ACache.get(this);
        initProvinceDatas();
        myHeader.init("验证卖家身份", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });


        JSONObject topaAdsObj = aCache.getAsJSONObject(Constans.TAG_TOBE_SALLER_TOP_ADS);
        if (topaAdsObj != null) {
            try {
                JSONArray adsArray = topaAdsObj.getJSONArray("rows");
                if (adsArray.length() > 0) {
                    homeTopAddBeens = new Gson().fromJson(adsArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                    }.getType());
                    /**
                     * add by :胡峰
                     * 验证卖家身份界面中的banner图的比例设置为3：1
                     */
                    WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                    int width = windowManager.getDefaultDisplay().getWidth();
                    Log.i("seller---width", width + "");
                    ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();//获取当前的控件的参数
                    layoutParams.height = width/3;//将高度设置为宽度的三分之一
                    Log.i("seller---height",layoutParams.height+"");
                    viewPager.setLayoutParams(layoutParams);//使得设置好的参数应用到控件中
                    viewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(false));
                    viewPager.setInterval(5000);
                    viewPager.startAutoScroll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadTopAds();
        } else {
            loadTopAds();
        }
        //选项选择器
        pvOptions = new OptionsPickerView(mActivity);
        Iterator iter = mCitisDatasMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String province = (String) entry.getKey();
            options1Items.add(province);
            String[] citys = (String[]) entry.getValue();
            ArrayList<String> options2Item = new ArrayList<String>();
            for (String city : citys) {
                options2Item.add(city);
            }
            options2Items.add(options2Item);
        }

        rlAddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //三级联动效果
                pvOptions.setPicker(options1Items, options2Items, true);
                //设置选择的三级单位
                pvOptions.setCyclic(false, false, false);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(0, 0, 0);
                pvOptions.show();
            }
        });

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1);
                city = options2Items.get(options1).get(option2);
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2);
                tvAddresValue.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv));
                tvAddresValue.setText(tx);
            }
        });

        tvGotoImageDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity, ImageDemoActivity.class);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(etInputUsername.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入您的名字");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etInputId.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入您的身份证号码");
                    btnSave.setEnabled(true);
                    return;
                }
                if (!Validator.isIDCard(etInputId.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入正确的身份证号码格式");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(upPath)) {
                    UIHelper.t(mContext, "请上传您的手持身份证照片");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(etInputZhifubao.getText().toString().trim())) {
                    UIHelper.t(mContext, "请输入您的支付宝账号");
                    btnSave.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    UIHelper.t(mContext, "请选择您所在的区域信息");
                    btnSave.setEnabled(true);
                    return;
                }
                submit();
            }
        });

        ivAddIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectPicDialog(mActivity, R.style.bottom_dialog).setListner(new SelectPicDialog.OnPicChooserListener() {
                    @Override
                    public void select(int position) {
                        if (position == 1) {//照相
                            photo(Result_PIC_2);
                        }
                        if (position == 2) {//相册
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(i, Result_PIC_1);
                        }
                    }
                }).show();
            }
        });
        /**
         * add by :胡峰
         * 添加下划线，和文字平滑处理
         */
        tvKuaidiJubao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下滑线
        tvKuaidiJubao.getPaint().setAntiAlias(true);//抗锯齿处理
        tvJiaoyiLiucheng.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvJiaoyiLiucheng.getPaint().setAntiAlias(true);
        //常见问题
        tvKuaidiJubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity,ChangJianQuestionActivity.class);
            }
        });
        //交易流程
        tvJiaoyiLiucheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.jump(mActivity,PaiMaiJiaoYiLiuChengActivity.class);
            }
        });
    }


    private void loadTopAds() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (mApplication.isLogin()) {
                jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appbanner_getsellerlistbyapp(jsonObject, new ApiCallback() {
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
                        JSONArray jsonArray = resultObj.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            aCache.put(Constans.TAG_TOBE_SALLER_TOP_ADS, resultObj);
                            ArrayList<HomeTopAddBeanNew> homarrays = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeTopAddBeanNew>>() {
                            }.getType());
                            viewPager.setAdapter(new HomeAddImagePagerAdapter(mActivity, homeTopAddBeens).setInfiniteLoop(false));
                            viewPager.setInterval(5000);
                            viewPager.startAutoScroll();
                            homeTopAddBeens.clear();
                            homeTopAddBeens.addAll(homarrays);
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
                if (isFinishing())
                    return;
                UIHelper.t(mContext, R.string.net_error);
            }
        });

    }

    NoticeSingleDialog noticeSingleDialog;

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
            jsonObject.put("alipay_card", etInputZhifubao.getText().toString().trim());
            jsonObject.put("city", city);
            jsonObject.put("province", province);
            jsonObject.put("user_name", etInputUsername.getText().toString().trim());
            jsonObject.put("identity_card", etInputId.getText().toString().trim());
            jsonObject.put("identity_card_cover", upPath);
            jsonObject.put("user_id", LoginConfig.getUserInfo(mContext).getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.appuser_submitsupplysellerbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {
                showLoadingDialog("提交数据中...");
            }

            @Override
            public void onApiSuccess(String response) {
                if (isFinishing())
                    return;
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        noticeSingleDialog = new NoticeSingleDialog(mContext, "温馨提示", "您的验证信息已提交成功\n请耐心等待平台审核！", "我知道了");
                        noticeSingleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        noticeSingleDialog.setCallBack(new NoticeSingleDialog.CallBack() {
                            @Override
                            public void ok() {
                                finish();
                            }

                            @Override
                            public void cancle() {

                            }
                        }).show();
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

    private Uri photoUri;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 16/1/4  个人照片返回
        if (requestCode == Result_PIC_1 && resultCode == RESULT_OK) {//个人照片相册返回
            Uri uri = data.getData();
            if (uri != null) {
                String pathLocal = ImageUtils.getPath(this, uri);
                KLog.d("返回的本地图片路径", pathLocal);
//                String path = ImageUtils.getSmallBitmap(pathLocal);
//                tempPicPath = path;
//                uploadImages(path);
                Bundle bundle = new Bundle();
                bundle.putString("imagePath",pathLocal);
                UIHelper.jumpForResult(mActivity,CommentIdCardCropImageActivity.class,bundle,CommentIdCardCropImageActivity.COROP_REQUEST);
            }

        }
        if (requestCode == Result_PIC_2 && resultCode == RESULT_OK) {//个人照片相机返回
//            String path = ImageUtils.getSmallBitmap(recentPicPath);
//            tempPicPath = path;
//            uploadImages(path);
            Bundle bundle = new Bundle();
            bundle.putString("imagePath",recentPicPath);
            UIHelper.jumpForResult(mActivity, CommentIdCardCropImageActivity.class, bundle, CommentIdCardCropImageActivity.COROP_REQUEST);
        }

        if (requestCode == CommentShowCropImageActivity.COROP_REQUEST && resultCode == CommentIdCardCropImageActivity.COROP_RESULT){
            //String path = ImageUtils.getSmallBitmap(data.getStringExtra(CommentIdCardCropImageActivity.RERULT_PATH));
            String path = data.getStringExtra(CommentIdCardCropImageActivity.RERULT_PATH);
            tempPicPath = path;
            uploadImages(path);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(tempPicPath)) {
            FileUtil.deleteCacheFile(tempPicPath);
        }
    }

    private void uploadImages(String filename) {
        File file = new File(filename);
        new OkHttpRequest.Builder()
                .url(AppUrl.FILEUPLAOD)
                .addParams("req_from", "mj-app")
                .files(new Pair<String, File>(file.getName(), file))
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
                                upPath = path;
                                ivAddIdCard.setVisibility(View.GONE);
                                iv_add_item.setBackgroundResource(R.drawable.shape_white_black);
                                mApplication.setImages(path, ivIdCard);
                            } else {
                                UIHelper.t(mContext, JSONUtil.getServerMessage(jsonObject));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                });
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        viewPager.startAutoScroll();
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }
}
