package me.gitai.smscodehelper;

import android.app.Application;
import android.content.Intent;

import me.gitai.library.utils.CrashUtil;
import me.gitai.library.utils.IOUtils;
import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.ui.MainPreferences;

/**
 * Created by gitai on 15-12-12.
 */
public class SMSApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        L.setLogcatEnable(this, true);
        L.setLogToFileEnable(true, this);
        SharedPreferencesUtil.initialize(this);
        ToastUtil.initialize(this);

        CrashUtil.getInstance().init(this, new CrashUtil.OnCrash() {
            @Override
            public boolean onCrash(Thread thread, Throwable th) {
                ToastUtil.show(CrashUtil.collectDeviceInfo(SMSApp.this),th);
                try{
                    IOUtils.writeString(CrashUtil.collectDeviceInfo(SMSApp.this)+th.getLocalizedMessage(),
                            openFileOutput(StringUtils.toSafeFileName("crash-" + System.nanoTime() + ".log"), MODE_WORLD_WRITEABLE));
                }catch (Exception ex){
                    L.e(ex);
                }
                restart();
                return false;
            }
        });
    }

    public void restart(){
        Intent intent = new Intent();
        intent.setClass(this, MainPreferences.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
