package me.gitai.smscodehelper.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bean.MSG;
import me.gitai.smscodehelper.utils.Captchas;

/**
 * Created by Rikka on 2015/12/7.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getInstence(null);

        if (!sharedPreferences.getBoolean(Constant.KEY_GENERAL_RUN, false)){
            return;
        }

        if (!sharedPreferences.getBoolean(Constant.KEY_TASK_COPY, false)
                && !sharedPreferences.getBoolean(Constant.KEY_TASK_NOTIFICATION, false)
                /*&& !sharedPreferences.getBoolean(Constant.KEY_TASK_INTERCEPT, false)*/){
            return;
        }

        Object[] pdus = (Object[]) intent.getExtras().get(Constant.KEY_SMS_PDUS);

        for (Object p : pdus){
            MSG msg = MSG.createFromPdu((byte[]) p);

            if (msg == null){
                return;
            }

            int parseType = Captchas.PARSE_TYPE_AUTO/*Integer.parseInt(
                    SharedPreferencesUtil.getInstence(null).getString(
                            Constant.KEY_PARSE_TYPE,
                            String.valueOf(Captchas.PARSE_TYPE_AUTO)))*/;

            msg = new Captchas(msg, parseType).parseAuto();
            if (msg == null || StringUtils.isEmpty(msg.getCode())){
                return;
            }

            if (sharedPreferences.getBoolean(Constant.KEY_TASK_COPY, false)){
                // 复制到剪贴板
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (sharedPreferences.getBoolean(Constant.KEY_TASK_CLIPBOARD_CHECK,true) && clipboardManager.hasPrimaryClip()){
                    notification(context, msg);
                    ToastUtil.show(R.string.toast_clipboard_not_empty);
                }else{
                    ClipData clipData = ClipData.newPlainText("SMS Code", msg.getCode());
                    clipboardManager.setPrimaryClip(clipData);

                    // toast
                    ToastUtil.showId(R.string.toast_copy_success_format,msg.getCode());
                }
            }

            if (sharedPreferences.getBoolean(Constant.KEY_TASK_NOTIFICATION, false)){
                notification(context, msg);
            }

            if (sharedPreferences.getBoolean(Constant.KEY_TASK_INTERCEPT, false)){
                //4.4<
                this.abortBroadcast();
            }

            if (!StringUtils.isEmpty(msg.getSender())){
                String provider = String.format("%s(%s)", msg.getSender(), msg.getAddress());
                Set<String> providers = sharedPreferences.getStringSet(Constant.KEY_GENERAL_GUESS, new HashSet<String>());
                if (!providers.contains(provider)){
                    providers.add(provider);
                    sharedPreferences.edit()
                            .putStringSet(Constant.KEY_GENERAL_GUESS, providers)
                            .apply();
                }
            }
        }
    }

    public void notification(Context ctx, MSG msg){
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle
                .setBigContentTitle(ctx.getString(R.string.app_name))
                .setSummaryText(msg.getCode())
                .bigText(msg.getBody());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(
                                BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_notify))
                        .setAutoCancel(true)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(msg.getCode())
                        .setContentInfo(msg.getSender())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(
                                PendingIntent.getBroadcast(
                                    ctx, 0 ,
                                    new Intent("SMS_CODE_COPY").putExtra(Constant.KEY_BUNDLE_SMS_CODE, msg.getCode()), 0))
                        .setStyle(textStyle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setCategory(Notification.CATEGORY_ALARM);
        }

        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }


}
