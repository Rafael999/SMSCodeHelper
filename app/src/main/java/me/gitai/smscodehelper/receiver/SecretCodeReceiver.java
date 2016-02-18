package me.gitai.smscodehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.gitai.library.utils.L;
import me.gitai.library.utils.PackageUtils;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.ui.MainPreferences;

/**
 * Created by gitai on 16-1-2.
 */
public class SecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageUtils.enableComponent(context, MainPreferences.class);
        L.d(intent.getAction());
        if (intent.getAction().equals(Constant.SECRET_CODE_ACTION)){
            L.i("Start via SECRET_CODE");
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, MainPreferences.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
