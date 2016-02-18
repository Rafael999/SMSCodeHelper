package me.gitai.library.utils;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by gitai on 16-2-18.
 */
public class CrashUtil implements Thread.UncaughtExceptionHandler {
    private static CrashUtil mCrashUtil = new CrashUtil();

    private Context mContext;
    private OnCrash mOnCrash;
    private Thread.UncaughtExceptionHandler mDefHandler;

    public static CrashUtil getInstance(){
        return mCrashUtil;
    }

    public void init(Context ctx){
        init(ctx, null);
    }

    public void init(Context ctx, OnCrash onCrash){
        this.mContext = ctx;

        mDefHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        L.e(ex);

        if (mOnCrash != null){
            if (mOnCrash.onCrash(thread,ex)){
                exit();
            }
        }
    }

    public static String collectDeviceInfo(Context ctx){
        List<String> info = new ArrayList<>();
        info.add("================ Device Info ================");
        info.add("SDK: " + AndroidUtils.getSDKIntStr(Build.VERSION.SDK_INT));
        info.add(AndroidUtils.getBatteryInfo(AndroidUtils.getBatteryStatus(ctx)));
        info.add(AndroidUtils.getSignatureInfo(ctx));
        info.add("Locale: " + AndroidUtils.getLocale(Locale.getDefault()));
        info.add("VM: " + AndroidUtils.getVMType(System.getProperty("java.vm.version")));
        info.add("==================== END ====================");
        return StringUtils.join(info, "\n");
    }

    public void exit(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public interface OnCrash{
        boolean onCrash(Thread thread, Throwable ex);
    }
}
