package me.gitai.smscodehelper.widget;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.preference.Preference;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 16-1-2.
 */
public class TestPreference extends Preference implements Preference.OnPreferenceClickListener {
    protected MaterialDialog licenseDialog;
    protected Context ctx;
    protected int title;
    protected String def_hint;
    protected String def_text;
    protected EditText editText;

    private SharedPreferences shared = getSharedPreferences();
    private SharedPreferences.Editor editor = shared.edit();

    public TestPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initTypedArray(attrs);
    }

    public TestPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initTypedArray(attrs);
    }

    public TestPreference(Context context) {
        super(context);
        init(context);
    }
    private void initTypedArray(AttributeSet attrs){
        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.preference);
        def_hint = ta.getString(R.styleable.preference_hint);
        def_text = ta.getString(R.styleable.preference_text);
    }

    private void init(Context context){
        ctx = context;
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
                        editor.putString(getKey(), data);
                        editor.commit();
                        sendSMS(data);
                        return true;
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        editText.setHint(def_hint);
        editText.setText(shared.getString(getKey(), def_text));

        licenseDialog.show();
        return false;
    }

    private void sendSMS(String body){
        ContentValues cv = new ContentValues();
        cv.put("address", "+861069565845264");
        cv.put("date_sent", System.currentTimeMillis());
        cv.put(Telephony.Mms.Inbox.DATE, System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            cv.put(Telephony.Mms.Inbox.DATE_SENT, System.currentTimeMillis());
        }
        cv.put("body", String.format(body, (int)((Math.random()*9+1)*100000)));
        ctx.getContentResolver().insert(Uri.parse("content://sms"), cv);
    }
}
