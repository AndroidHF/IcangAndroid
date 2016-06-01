package com.buycolle.aicang.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.util.superlog.KLog;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by joe on 16/1/4.
 */
public class FileUtil {

    public static final String APP_DOWNLOAD_LOGO_PATH = getRootDir()
            + File.separator + "logo" + File.separator;
    public static final String APP_DOWNLOAD_LOGO_PATH_Check = getRootDir()
            + File.separator + "logo";
    public static final String APP_CROP_PATH = getRootDir()
            + File.separator + "crop";

    public static final String APP_DOWNLOAD_LOGO_NAME = "logo_pm.jpg";


    /**
     * 删除单个缓存文件
     *
     * @param path
     */
    public static void deleteCacheFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists())
                file.delete();
        }
    }

    public static boolean isLogoExist() {
        File file = new File(APP_DOWNLOAD_LOGO_PATH_Check);
        if (!file.exists()) {
            file.mkdirs();
            return false;
        } else {
            File logofile = new File(APP_DOWNLOAD_LOGO_PATH + APP_DOWNLOAD_LOGO_NAME);
            return logofile.exists();
        }
    }

    public static String getCropFilePath() {
        File file = new File(APP_CROP_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
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

    /**
     * 得到头像缓存文件夹地址
     *
     * @return
     */
    public static String getPortraitCathe() {
        String rootDir = getRootDir();
        File file = new File(rootDir + File.separator + "portrait");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsoluteFile().toString();
    }

    public static boolean existSD() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 保存本地头像
     *
     * @param bitmap
     * @return
     */
    public static File saveAvatarLocal(Context mContext, Bitmap bitmap) {
        String suf = "_avatar.jpg";
        String filename = LoginConfig.getUserInfo(mContext).getUser_id() + System.currentTimeMillis() + suf;
        File file = new File(FileUtil.getPortraitCathe() + File.separator + filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
        } catch (Exception e) {
            return null;
        }
        KLog.d("保存头像路径:---" + file.getAbsoluteFile().toString());
        return file;
    }

    public static File saveImageLocal(Context mContext, Bitmap bitmap) {
        String suf = "_crop.jpg";
        String filename = LoginConfig.getUserInfo(mContext).getUser_id() + System.currentTimeMillis() + suf;
        File file = new File(FileUtil.getPortraitCathe() + File.separator + filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
        } catch (Exception e) {
            return null;
        }
        KLog.d("保存的图片路径:---" + file.getAbsoluteFile().toString());
        return file;
    }

    /**
     * 得到分享缓存
     *
     * @return
     */
    public static String getShareCathe() {
        String rootDir = getRootDir();
        File file = new File(rootDir + File.separator + "share");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsoluteFile().toString();
    }

    public static void clearShareCache() {
        String rootDir = getRootDir();
        File file = new File(rootDir + File.separator + "share");
        if (file.exists()) {
            File files[] = file.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }

    public static Uri FilePathToUri(Context context, String path) {
        if (path != null) {
            path = Uri.decode(path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(")
                    .append(MediaStore.Images.ImageColumns.DATA)
                    .append("=")
                    .append("'" + path + "'")
                    .append(")");
            Cursor cur = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns._ID},
                    buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                    .moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                index = cur.getInt(index);
            }
            if (index == 0) {
                //do nothing
            } else {
                Uri uri_temp = Uri
                        .parse("content://media/external/images/media/"
                                + index);
                if (uri_temp != null) {
                    return uri_temp;
                }
            }

        }
        return null;
    }

}
