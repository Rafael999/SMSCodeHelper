package me.gitai.smscodehelper.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by gitai on 16-1-2.
 */
public class CommonUtil {
    public static void hideLauncher(Activity act, boolean hidden) {
        hideLauncher(act, act.getComponentName(), hidden);
    }

    public static void hideLauncher(Context ctx, ComponentName cn, boolean hidden) {
        PackageManager p = ctx.getApplicationContext().getPackageManager();
        int state = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        if (hidden){
            state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        }
        if (p.getComponentEnabledSetting(cn) != state){
            p.setComponentEnabledSetting(cn,state, PackageManager.DONT_KILL_APP);
        }
    }
}
