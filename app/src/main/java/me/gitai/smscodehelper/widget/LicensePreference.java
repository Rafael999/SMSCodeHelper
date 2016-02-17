package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;

import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 15-11-5.
 */
public class LicensePreference extends Preference implements Preference.OnPreferenceClickListener {
    private MaterialDialog licenseDialog;
    private String def_msg;

    public LicensePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LicensePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LicensePreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.preference);
        def_msg = ta.getString(R.styleable.preference_text);
        init(context);
    }

    private void init(Context context){
        licenseDialog = new MaterialDialog(context)
                .setTitle(getTitle())
                .setMessage(def_msg);

        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        licenseDialog.show();
        return false;
    }
}
