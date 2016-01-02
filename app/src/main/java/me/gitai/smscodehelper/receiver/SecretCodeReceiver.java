package me.gitai.smscodehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.MainPreferences;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.utils.CommonUtil;

/**
 * Created by gitai on 16-1-2.
 */
public class SecretCodeReceiver extends BroadcastReceiver {
    private final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtil.showId(R.string.app_name);
        CommonUtil.hideLauncher(context, new ComponentName(context.getPackageName(), "me.gitai.smscodehelper.MainPreferences"), false);
        if (intent.getAction().equals(SECRET_CODE_ACTION)){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, MainPreferences.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
