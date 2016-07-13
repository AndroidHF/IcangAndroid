package com.buycolle.aicang.ui.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;

import com.buycolle.aicang.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

/**
 * 练习 测试用例
 *
 * Created by joe on 16/3/31.
 */
public class Demo extends BaseActivity {
    SubsamplingScaleImageView imageView;

    public static final String APP_DOWNLOAD_LOGO_PATH = getRootDir()
            + File.separator ;
    public static final String APP_DOWNLOAD_LOGO_PATH_Check = getRootDir()
            + File.separator + "logo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        imageView.setMinScale(2.0F);
        File logofile = new File(APP_DOWNLOAD_LOGO_PATH + "hx_1459409567986.jpg");
        imageView.setImage(ImageSource.uri(logofile.getAbsolutePath()), new ImageViewState(0.5F, new PointF(0, 0), 0));
    }

    /**
     * 创建文件根目录
     */
    public static String getRootDir() {
        String dir = "";
        if (existSD()) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "paimai");
            if (!file.exists()) {
                file.mkdirs();
            }
            dir = file.getAbsolutePath();
        } else {
            File file = new File(Environment.getDataDirectory() + File.separator + "paimai");
            if (!file.exists()) {
                file.mkdirs();
            }
            dir = file.getAbsolutePath();
        }
        return dir;
    }

    public static boolean existSD() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}
