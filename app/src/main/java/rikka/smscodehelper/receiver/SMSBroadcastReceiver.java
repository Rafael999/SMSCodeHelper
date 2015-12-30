package rikka.smscodehelper.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.ToastUtil;
import rikka.smscodehelper.R;
import rikka.smscodehelper.bean.SMSInfo;
import rikka.smscodehelper.utils.SMSCode;
import rikka.smscodehelper.utils.StringUtils;

/**
 * Created by Rikka on 2015/12/7.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getInstence(null);

        if (!sharedPreferences.getBoolean("open", false)){
            return;
        }

        if (!sharedPreferences.getBoolean("copy", false) && !sharedPreferences.getBoolean("notification", false) && !sharedPreferences.getBoolean("intercept", false)){
            return;
        }

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");

        for (Object p : pdus){
            byte[] sms = (byte[]) p;

            SmsMessage message = SmsMessage.createFromPdu(sms);

            String content = message.getMessageBody();
            String number = message.getOriginatingAddress();

            if (StringUtils.isEmpty(content)){
                return;
            }

            int parseType = Integer.parseInt(SharedPreferencesUtil.getInstence(null).getString("parse_type", String.valueOf(SMSCode.PARSE_TYPE_V1)));
            SMSInfo smsinfo = SMSCode.parse(content, number, parseType);
            if (smsinfo == null || StringUtils.isEmpty(smsinfo.code)){
                return;
            }

            smsinfo.smsAddress = number;
            smsinfo.smsBody = content;

            if (sharedPreferences.getBoolean("copy", false)){
                // 复制到剪贴板
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (sharedPreferences.getBoolean("clipboard_check",true) && clipboardManager.hasPrimaryClip()){
                    notification(context, smsinfo);
                    ToastUtil.show(R.string.toast_clipboard_not_empty);
                }else{
                    ClipData clipData = ClipData.newPlainText("SMS Code", smsinfo.code);
                    clipboardManager.setPrimaryClip(clipData);

                    // toast
                    ToastUtil.showId(R.string.toast_copy_success_format,smsinfo.code);
                }
            }

            if (sharedPreferences.getBoolean("notification", false)){
                notification(context, smsinfo);
            }

            if (sharedPreferences.getBoolean("intercept", false)){
                //4.4<
                this.abortBroadcast();
            }
        }
    }

    public void notification(Context ctx, SMSInfo smsinfo){
        String msg = "";

        if (!StringUtils.isEmpty(smsinfo.sender)){
            msg = String.format("%s %s", smsinfo.sender, smsinfo.code);
        }else{
            msg=String.format("%s", smsinfo.code);
        }

        // 来条通知吗 0.0
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle
                .setBigContentTitle(ctx.getString(R.string.app_name))
                .setSummaryText(smsinfo.smsAddress)
                .bigText(smsinfo.smsBody);




        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_notify);

        Intent intent = new Intent("SMS_CODE_COPY");

        intent.putExtra("smscode", smsinfo.code);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0 ,intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(largeIcon)
                        .setAutoCancel(true)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(smsinfo.code)
                        .setContentInfo(smsinfo.sender)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
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
        notificationManager.notify(0, builder.build());
    }


}
