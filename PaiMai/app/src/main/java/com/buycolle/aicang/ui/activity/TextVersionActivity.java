package com.buycolle.aicang.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.UpdateInfo;
import com.buycolle.aicang.ui.view.MyHeader;
import com.buycolle.aicang.ui.view.UpdateDialog;
import com.buycolle.aicang.util.DownLoadManager;
import com.buycolle.aicang.util.UpdataInfoParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hufeng on 2016/9/5.
 */
public class TextVersionActivity extends BaseActivity {
    @Bind(R.id.tv_version)
    TextView tvVersion;//版本信息
    @Bind(R.id.my_header)
    MyHeader myHeader;

    private final String TAG = this.getClass().getName();
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    private UpdateInfo info;
    private String localVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_text_version);
        ButterKnife.bind(this);
        myHeader.init("检测新版本", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        try {
            //检测新版本
            localVersion = getVersionName();
            Log.i("versionname",localVersion);
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
            //获取当前版本的值
            tvVersion.setText("当前版本："+getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前版本号
     * @return
     * @throws Exception
     */
    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        Log.i("name", getPackageName());
        return packInfo.versionName;
    }

    public class CheckVersionTask implements Runnable {
        InputStream is;
        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                    Log.i("hufeng", is.toString());
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                Log.i("info",info.toString());
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATA_NONEED;
                    handler.sendMessage(msg);
                    // LoginMain();
                } else {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
                    Toast.makeText(getApplicationContext(), "当前已经是最新版本，不需要更新",
                            Toast.LENGTH_SHORT).show();
                    break;
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /*
	 *
	 * 弹出对话框通知用户更新程序
	 *
	 * 弹出对话框的步骤：
	 *  1.创建alertDialog的builder.
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮
	 *  3.通过builder 创建一个对话框
	 *  4.对话框show()出来
	 */
    protected void showUpdataDialog() {
        new UpdateDialog(mContext,"版本升级",info.getContext(),info.getVersion()).setCallBack(new UpdateDialog.CallBack() {
            @Override
            public void ok() {
                downLoadApk();
            }

            @Override
            public void cancle() {

            }
        }).show();
    }


    /*
	 * 从服务器中下载APK
	 */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    Log.i("url2", info.getUrl());
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }


    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
