package me.gitai.smscodehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.smscodehelper.Constant;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bean.MSG;
import me.gitai.smscodehelper.bundle.BundleScrubber;
import me.gitai.smscodehelper.bundle.PluginBundleManager;
import me.gitai.smscodehelper.ui.TaskerActivity;
import me.gitai.smscodehelper.utils.TaskerPlugin;

/**
 * Created by gitai on 16-2-4.
 */
public final class QueryReceiver extends BroadcastReceiver{
    protected static final Intent INTENT_REQUEST_REQUERY =
            new Intent(com.twofortyfouram.locale.Intent.ACTION_REQUEST_QUERY).putExtra(com.twofortyfouram.locale.Intent.EXTRA_ACTIVITY,
                                                                                       TaskerActivity.class.getName());
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION.equals(intent.getAction())){
            L.d(String.format("Received unexpected Intent action %s", intent.getAction()));
            return;
        }

        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        if (PluginBundleManager.isBundleValid(bundle)){
        	L.d(String.format("Screen state is b and condition state is b"));
        	/*if ( messageID == -1 ){
        		setResultCode( com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN );
        	}*/
	TaskerPlugin.Event.addPassThroughMessageID( INTENT_REQUEST_REQUERY );
        }
    }
}