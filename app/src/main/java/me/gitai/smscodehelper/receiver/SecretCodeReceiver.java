package me.gitai.smscodehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import me.gitai.library.utils.L;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.ui.MainPreferences;
import me.gitai.smscodehelper.utils.CommonUtil;

/**
 * Created by gitai on 16-1-2.
 */
public class SecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("Start by SECRET_CODE");
        CommonUtil.hideLauncher(context, new ComponentName(context.getPackageName(), Constant.COMPONENTNAME_MAIN), false);
        if (intent.getAction().equals(Constant.SECRET_CODE_ACTION)){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, MainPreferences.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
