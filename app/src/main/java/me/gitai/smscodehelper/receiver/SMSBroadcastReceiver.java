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
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bean.MSG;
import me.gitai.smscodehelper.bundle.BundleScrubber;
import me.gitai.smscodehelper.bundle.PluginBundleManager;
import me.gitai.smscodehelper.utils.Captchas;
/**
 * Created by Rikka on 2015/12/7.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences = SharedPreferencesUtil.getInstence(null);

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("SMS Received");
        if (!sharedPreferences.getBoolean(Constant.KEY_TASK_COPY, false)
                && !sharedPreferences.getBoolean(Constant.KEY_TASK_NOTIFICATION, false)
                /*&& !sharedPreferences.getBoolean(Constant.KEY_TASK_INTERCEPT, false)*/){
            L.d("Not working(copy,notification: false)");
            return;
        }

        if (intent == null){
            L.d("Not working(intent: null)");
            return;
        }

        Bundle b1 = intent.getExtras();

        if (b1 != null){
            Object[] pdus = (Object[]) b1.get(Constant.KEY_SMS_PDUS);

            if (pdus != null){
                if (!sharedPreferences.getBoolean(Constant.KEY_GENERAL_RUN, false)){
                    L.d("Not working(run: false)");
                    return;
                }
                L.d("Pdus count: " + pdus.length);
                for (Object p : pdus){
                    MSG msg = MSG.createFromPdu((byte[]) p);

                    if (msg == null){
                        L.d("Not working(msg: null)");
                        return;
                    }

                    L.d("MSG: " + msg.toString());
                    parse(context, msg);
                }
            }else{
                /*
                 * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
                 */

                if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
                {
                    L.e(String.format(Locale.US,
                            "Received unexpected Intent action %s",
                            intent.getAction())); //$NON-NLS-1$
                    return;
                }

                BundleScrubber.scrub(intent);

                Bundle b2 = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
                BundleScrubber.scrub(b2);

                if (PluginBundleManager.isBundleValid(b2))
                {
                    String address = b2.getString(PluginBundleManager.BUNDLE_EXTRA_MSG_ADDRESS);
                    String body = b2.getString(PluginBundleManager.BUNDLE_EXTRA_MSG_BODY);
                    ToastUtil.show(address, body);
                }
            }
        }else{
            L.d("Not working(extras is null)");
        }
    }

    public boolean parse(Context ctx, MSG msg) {
        int parseType = Captchas.PARSE_TYPE_AUTO/*Integer.parseInt(
                    SharedPreferencesUtil.getInstence(null).getString(
                            Constant.KEY_PARSE_TYPE,
                            String.valueOf(Captchas.PARSE_TYPE_AUTO)))*/;

        L.d("parseType:" + parseType);

        msg = new Captchas(ctx, msg, parseType).parseAuto();

        L.d("MSG: " + msg.toString());

        if (msg == null || StringUtils.isEmpty(msg.getCode())){
            L.d("Not working(msg,code: null)");
            return false;
        }

        if (sharedPreferences.getBoolean(Constant.KEY_TASK_COPY, false)){
            L.d("Copyable");
            // 复制到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            if (sharedPreferences.getBoolean(Constant.KEY_TASK_CLIPBOARD_CHECK,true)
                    && clipboardManager.hasPrimaryClip()
                    && !Constant.KEY_CLIP_LABEL
                        .equals(clipboardManager.getPrimaryClip().getDescription().getLabel())){
                notification(ctx, msg);
                ToastUtil.show(R.string.toast_clipboard_not_empty);
            }else{
                ClipData clipData = ClipData.newPlainText(Constant.KEY_CLIP_LABEL, msg.getCode());
                clipboardManager.setPrimaryClip(clipData);

                // toast
                ToastUtil.showId(R.string.format_copy_success,msg.getCode());
            }
        }else{
            L.d("Uncopyable");
        }

        if (sharedPreferences.getBoolean(Constant.KEY_TASK_NOTIFICATION, false)){
            L.d("Noticeable");
            notification(ctx, msg);
        }else{
            L.d("Unnoticeable");
        }

        if (sharedPreferences.getBoolean(Constant.KEY_TASK_INTERCEPT, false)){
            L.d("Interceptable");
            //4.4<
            this.abortBroadcast();
        }else{
            L.d("Uninterceptable");
        }

        if (!StringUtils.isEmpty(msg.getSender()) && msg.getAddress() != "110"){
            L.d("Sender is " + msg.getSender());
            String provider = String.format("%s(%s)", msg.getSender(), msg.getAddress());
            Set<String> providers = sharedPreferences.getStringSet(Constant.KEY_GENERAL_GUESS, new HashSet<String>());
            if (!providers.contains(provider)){
                providers.add(provider);
                sharedPreferences.edit()
                        .putStringSet(Constant.KEY_GENERAL_GUESS, providers)
                        .apply();
            }
        }
        return true;
    }

    public void notification(Context ctx, MSG msg){
        int randomId = new Random().nextInt();
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle
                .setBigContentTitle(ctx.getString(R.string.app_name))
                .setSummaryText(msg.getCode())
                .bigText(msg.getBody());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setLargeIcon(
                                BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(msg.getCode())
                        .setContentInfo(msg.getSender())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(
                                PendingIntent.getBroadcast(
                                    ctx, randomId ,
                                    new Intent("SMS_CODE_COPY")
                                            .putExtra(Constant.KEY_BUNDLE_SMS_CODE, msg.getCode()),
                                        0))
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
        notificationManager.notify(randomId, builder.build());
    }


}
