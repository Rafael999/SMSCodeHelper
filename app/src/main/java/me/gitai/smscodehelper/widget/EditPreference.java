package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 15-11-5.
 */
public class EditPreference extends Preference implements Preference.OnPreferenceClickListener {
    private MaterialDialog licenseDialog;
    private String def_hint;
    private String def_text;
    private EditText editText;

    public EditPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EditPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.preference);
        def_hint = ta.getString(R.styleable.preference_hint);
        def_text = ta.getString(R.styleable.preference_text);
        init(context);
    }

    private void init(Context context){
        licenseDialog = new MaterialDialog(context)
                .setTitle(getTitle())
                .setContentView(R.layout.layout_editdialog, new MaterialDialog.OnViewInflateListener() {
                    @Override
                    public boolean onInflate(View v) {
                        editText = (EditText)v.findViewById(R.id.editText);
                        return false;
                    }
                })
                .setPositiveButton(android.R.string.ok, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(View v, View MaterialDialog) {
                        String data = editText.getText().toString();
                        SharedPreferences.Editor editor = getSharedPreferences().edit();
                        editor.putString(getKey(), data);
                        editor.commit();
                        return true;
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        editText.setHint(def_hint);
        editText.setText(getSharedPreferences().getString(getKey(), def_text));

        licenseDialog.show();
        return false;
    }
}
