package me.gitai.smscodehelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.utils.CommonUtil;
import me.gitai.smscodehelper.widget.TestPreference;


/**
 * Created by gitai on 15-12-12.
 */
public class MainPreferences extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            setFitsSystemWindows(true);
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        if (getActionBar() != null){
            getActionBar().setDisplayShowHomeEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MaterialDialog materialDialog = new MaterialDialog(this);
            materialDialog.setMessage(R.string.tip_m);
            materialDialog.setPositiveButton(R.string.get_permission, new MaterialDialog.OnClickListener() {
                @Override
                public boolean onClick(View v, View MaterialDialog) {
                    getPermission(Manifest.permission.RECEIVE_SMS);
                    return true;
                }
            });
            materialDialog.show();
        }

        if (getIntent()!=null && Intent.ACTION_SEND.equals(getIntent().getAction())){
            ((TestPreference)findPreference(Constant.KEY_GENERAL_TEST))
                    .share(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    @Override
    protected void onDestroy() {
        CommonUtil.hideLauncher(this,
                SharedPreferencesUtil.getInstence(null).getBoolean(Constant.KEY_GENERAL_HIDDEN_ICON, false));
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    MaterialDialog materialDialog = new MaterialDialog(this)
                            .setTitle("OAQ")
                            .setMessage(R.string.permission_denied)
                            .setPositiveButton(R.string.exit, new MaterialDialog.OnClickListener() {
                                @Override
                                public boolean onClick(View v, View MaterialDialog) {
                                    finish();
                                    return false;
                                }
                            });
                    materialDialog.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(14)
    private void setFitsSystemWindows(boolean on) {
        ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(on);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getPermission(String permission)
    {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }
}
