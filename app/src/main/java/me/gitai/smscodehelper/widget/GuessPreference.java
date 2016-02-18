package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.utils.ToastUtil;
import me.gitai.library.utils.adapter.BaseAdapterHelper;
import me.gitai.library.utils.adapter.QuickAdapter;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 16-1-2.
 */
public class GuessPreference extends Preference implements Preference.OnPreferenceClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private MaterialDialog licenseDialog;

    List<String> providers = new ArrayList<String>();
    private Context mContext;
    private QuickAdapter<String> mAdapter;

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
        mContext = ctx;
        licenseDialog = new MaterialDialog(ctx)
                .setTitle(getTitle())
                .setNegativeButton(android.R.string.ok, null);

        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        providers = new ArrayList<String>(getSharedPreferences().getStringSet(getKey(), Collections.EMPTY_SET));
        if (StringUtils.isEmpty(providers)){
            ToastUtil.showId(R.string.toast_providers_is_empty);
            return false;
        }
        mAdapter = new QuickAdapter<String>(mContext, R.layout.spinner_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(android.R.id.text1,item);
            }
        };
        mAdapter.addAll(providers);
        licenseDialog
                .setTitle(String.format("%s (%d)", getTitle(), providers.size()))
                .setMessage("Long-Click for remove")
                .setCanceledOnTouchOutside(true)
                .setContentView(new ListView(mContext), new MaterialDialog.OnViewInflateListener() {
                    @Override
                    public boolean onInflate(View v) {
                        ListView view = (ListView)v;
                        view.setOnItemClickListener(GuessPreference.this);
                        view.setOnItemLongClickListener(GuessPreference.this);
                        view.setAdapter(mAdapter);
                        return false;
                    }
                })
                .setNegativeButton(R.string.clear, new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(View v, View MaterialDialog) {
                        providers.clear();
                        mAdapter.clear();
                        notifyDataSetChanged();
                        return true;
                    }
                })
                .show();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        providers.remove(position);
        mAdapter.remove(position);
        notifyDataSetChanged();
        return true;
    }

    private void notifyDataSetChanged(){
        licenseDialog
                .setTitle(String.format("%s (%d)", getTitle(), providers.size()));
        mAdapter.notifyDataSetChanged();
        SharedPreferencesUtil
                .getEditor(null)
                .putStringSet(getKey(), new HashSet<String>(providers))
                .commit();
    }
}
