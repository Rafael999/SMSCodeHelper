package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import java.util.HashSet;
import java.util.Set;

import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 16-1-2.
 */
public class GuessPreference extends Preference implements Preference.OnPreferenceClickListener {
    private MaterialDialog licenseDialog;

    Set<String> providers = new HashSet<String>();

    public GuessPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GuessPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuessPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context ctx){
        licenseDialog = new MaterialDialog(ctx)
                .setTitle(getTitle())
                .setNegativeButton(android.R.string.ok, null);

        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        providers = getSharedPreferences().getStringSet(getKey(),null);
        if (StringUtils.isEmpty(providers)){
            ToastUtil.showId(R.string.toast_providers_is_empty);
            return false;
        }
        licenseDialog
                .setMessage(StringUtils.join(providers,"\n"))
                .show();
        return false;
    }
}
