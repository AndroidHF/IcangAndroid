package com.buycolle.aicang.ui.activity.comment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.MyHeaderWithSure;
import com.buycolle.aicang.util.FileUtil;
import com.buycolle.aicang.util.ImageUtils;
import com.buycolle.aicang.util.UIHelper;
import com.hhw.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by joe on 16/4/30.
 */
public class CommentShow2CropImageActivity extends BaseActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener {

    @Bind(R.id.header)
    MyHeaderWithSure header;
    @Bind(R.id.cropImageView)
    CropImageView mCropImageView;

    private String imagePath;
    private Bitmap oricalBitMap;

    public static final int COROP_RESULT = 888;
    public static final int COROP_REQUEST = 987;
    public static final String RERULT_PATH = "rerult_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_crop_image);
        ButterKnife.bind(this);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);
        if (_Bundle != null) {
            imagePath = _Bundle.getString("imagePath");
            if (imagePath == null || TextUtils.isEmpty(imagePath)) {
                UIHelper.t(mContext, "初始化图片失败");
                finish();
            }

            header.init("编辑", "确定", new MyHeaderWithSure.Action() {
                @Override
                public void leftActio() {
                    finish();
                }

                @Override
                public void sureActio() {
                    header.getSureTv().setEnabled(false);
                    showLoadingDialog("处理图片中...");
                    mCropImageView.getCroppedImageAsync();
                }
            });

            showLoadingDialog("初始化图片...");
            Observable.just(true).map(new Func1<Boolean, Bitmap>() {
                @Override
                public Bitmap call(Boolean aBoolean) {
                    //Bitmap bitmap = ImageUtils.getImageBitmap(imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (ImageUtils.getBitmapDegree(imagePath)>0){
                        bitmap = ImageUtils.rotateBitmapByDegree(bitmap,ImageUtils.getBitmapDegree(imagePath));
                        return bitmap;
                    }else {
                        return bitmap;
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bitmap>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoadingDialog();
                            Toast.makeText(mActivity, "初始化图片失败", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onNext(Bitmap path) {
                            dismissLoadingDialog();
                            oricalBitMap = path;
                            mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
                            mCropImageView.setFixedAspectRatio(false);
                            mCropImageView.setAutoZoomEnabled(false);
                            mCropImageView.setShowProgressBar(true);
                            mCropImageView.setImageBitmap(oricalBitMap);
                        }
                    });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (oricalBitMap != null) {
            oricalBitMap.recycle();
        }
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnGetCroppedImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

    }

    File cropFile;

    @Override
    public void onGetCroppedImageComplete(CropImageView view, final Bitmap bitmap, Exception error) {
        if (error == null) {
            Observable.just(true).map(new Func1<Boolean, String>() {
                @Override
                public String call(Boolean aBoolean) {
                    cropFile = new File(FileUtil.getCropFilePath(), System.currentTimeMillis() + ".jpg");
                    if (cropFile.exists()) {
                        cropFile.delete();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(cropFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return cropFile.getAbsolutePath();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoadingDialog();
                            header.getSureTv().setEnabled(true);
                            Toast.makeText(mActivity, "截图失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(String path) {
                            dismissLoadingDialog();
                            Intent intent = new Intent();
                            intent.putExtra(RERULT_PATH, path);
                            setResult(COROP_RESULT, intent);
                            finish();
                        }
                    });
        } else {
            header.getSureTv().setEnabled(true);
            Toast.makeText(this, "截图失败: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
