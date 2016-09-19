package com.buycolle.aicang.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.buycolle.aicang.MainActivity;
import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.event.TobeSallerEvent;
import com.buycolle.aicang.ui.activity.ConectionActivity;
import com.buycolle.aicang.ui.activity.PaiPinDetailActivity;
import com.buycolle.aicang.ui.activity.SplashActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.faq.MyFAQActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mybuy.MyBuyActivity;
import com.buycolle.aicang.ui.activity.usercentermenu.mysale.MySaleActivity;
import com.buycolle.aicang.util.ForegroundUtil;
import com.buycolle.aicang.util.superlog.KLog;

import de.greenrobot.event.EventBus;

/**
 * Created by joe on 15/12/8.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private int type = 0;
    private int id = 0;
    private int ent_id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        KLog.d("过来了么----", "-----" + intent.getIntExtra("type", 0));
        KLog.d("MainApp.getInstance()).isForeground()----", "-----" + ForegroundUtil.get(MainApplication.getInstance()).isForeground());
        type = intent.getIntExtra("type", -1);
        id = intent.getIntExtra("id", 0);
        if (type > 0) {
            //1：出价被超，跳转到拍品详情界面
            // 2：中拍提醒 ，跳转到我买到--已落拍
            // 3：买家付款提醒，跳转到我买到--已落拍
            // 4：卖家已经发货，买家收到推送，跳转到我买到--已落拍
            // 5:订阅即将开始的竞拍推送提醒，拍卖会界面
            // 6:冻结用户提醒
            // 7:解锁
            // 8:离付款超时还差3小时提醒，跳转到我买到--已落拍
            // 9：卖家拍品被中拍提醒，跳转到我卖出的--已落拍
            // 10:买家付款了，给卖家提醒，跳转到我卖出的--已落拍
            // 11:卖家身份审核成功，跳转出品界面
            // 12:卖家身份没有通过审核，跳转到首页
            // 13：关注的商品，还有30分钟结束，跳转到拍品详情
            // 14:售后交流
            // 15：拍品QA有买家留言，跳转到我收到的问询
            // 16：拍品QA有卖家回复，跳转到我的问询
            // 17：售后交流界面，跳转到售后交流界面
            if (type == 1){
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, PaiPinDetailActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.putExtra("id",id);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
            if (type == 2 || type == 3 || type == 8 ||type == 4) {
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, MyBuyActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }

            if (type == 5) {
                if (!ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                } else {
                    //UIHelper.t(context, "还有20分钟拍卖会就开始");
                    Intent update = new Intent(context,PaiPinDetailActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.putExtra("id", id);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                }
            }
            if (type == 6) {
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, MainActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
            if (type == 7) {
                if (!ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
            if (type == 11) {
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    EventBus.getDefault().post(new TobeSallerEvent(0));
                } else {
                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }

            if (type == 12) {
                if (!ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }else {
                    Intent update = new Intent(context, MainActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                }
            }

            if (type == 9 || type == 10) {
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, MySaleActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }

            if (type == 13){
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, PaiPinDetailActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.putExtra("id",id);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }

            if (type == 15 || type == 16){
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, MyFAQActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.putExtra("id",id);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }

            if (type == 17){
                if (ForegroundUtil.get(MainApplication.getInstance()).isForeground()) {
                    Intent update = new Intent(context, ConectionActivity.class);
                    update.putExtra("isPush", true);
                    update.putExtra("type", type);
                    update.putExtra("id",id);
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                } else {
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.putExtra("isPush", true);
                    intent1.putExtra("type", type);
                    intent1.putExtra("id", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
        }
    }
}
