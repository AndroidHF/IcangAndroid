package com.buycolle.aicang.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.buycolle.aicang.bean.FileInfo;

/**
 * Created by joe on 16/3/16.
 */
public class VedioUtil {

    /**
     * 获取视频媒体信息
     */
    public static FileInfo getVideoMediaInfo(Context context, String videoPath) {
        String[] projection = {MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                MediaStore.Video.Media.DATA + " = '" + videoPath + "'", null, null);
        FileInfo mediaInfo = null;
        if (cursor.moveToFirst()) {
            mediaInfo = new FileInfo();
            mediaInfo.fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            mediaInfo.filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            mediaInfo.thumb = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC));
            mediaInfo.time = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
        }
        cursor.close();
        return mediaInfo;
    }
}
