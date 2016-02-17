package me.gitai.smscodehelper;

import android.app.Application;

import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.ToastUtil;

/**
 * Created by gitai on 15-12-12.
 */
public class SMSApp extends Application implements Thread.UncaughtExceptionHandler{
    @Override
    public void onCreate() {
        super.onCreate();
        L.setLogcatEnable(this, true);
        L.setLogToFileEnable(true, this);
        SharedPreferencesUtil.initialize(this);
        ToastUtil.initialize(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        L.e(ex);
        System.exit(1);
    }
}
