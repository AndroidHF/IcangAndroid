package com.buycolle.aicang.reciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.PushMessageEntity;
import com.buycolle.aicang.event.TobeSallerEvent;

import java.util.Random;
import java.util.UUID;

import de.greenrobot.event.EventBus;


/**
 * Created by joe on 15/12/6.
 */
public class PushNotificationHelper {


    public static NotificationManager notificationManager = null;
    protected Context appContext;
    protected String packageName;

    public static void cancellAll(){
        notificationManager.cancelAll();
    }

    public PushNotificationHelper init(Context context) {
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        packageName = appContext.getApplicationInfo().packageName;
        return this;
    }

    public void sendNotification(PushMessageEntity message, boolean isForeground) {
        //系统消息和去网页
        int msgId = new Random().nextInt(Integer.MAX_VALUE);
        Drawable drawable = appContext.getResources().getDrawable(R.drawable.ic_launcher);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                .setLargeIcon(bitmap)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(appContext.getResources().getColor(R.color.transparent))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        mBuilder.setContentTitle(message.getTitle());
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        mBuilder.setContentText(message.getContent());
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.getContent()));

        Intent broadcastIntent = new Intent(appContext, NotificationReceiver.class);
        broadcastIntent.setAction("com.aicang.huiwan");
        broadcastIntent.putExtra("type", message.getType());
        broadcastIntent.putExtra("id", message.getKey_id());
        //add by hufeng
        //broadcastIntent.putExtra("ent_id",message.getEnt_id());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, UUID.randomUUID().hashCode(), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //1：出价被超，2：中拍提醒 ，3：付款提醒，4：发货提醒 5:订阅即将开始的竞拍推送提醒 6:冻结用户提醒 7:解锁 8:离付款超时还差3小时提醒 9：卖家拍品被中拍提醒 10买家付款了，给卖家提醒 11:卖家身份审核成功,13:关注的拍品还有30分钟结束
        mBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(msgId, mBuilder.build());
        if (message.getType() == 11) {
            EventBus.getDefault().post(new TobeSallerEvent(0));
        }
    }
}

