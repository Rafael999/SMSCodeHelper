package me.gitai.smscodehelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.Preference;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.gitai.library.utils.L;
import me.gitai.library.utils.ParseAboutXml;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;
import me.gitai.library.widget.MaterialDialog;
import me.gitai.smscodehelper.BuildConfig;
import me.gitai.smscodehelper.R;

/**
 * Created by gitai on 15-11-5.
 */
public class VersionPreference extends Preference implements Preference.OnPreferenceClickListener {

    private MaterialDialog versionDialog;
    private ParseAboutXml.About about;

    public VersionPreference(Context ctx, AttributeSet attrs, int defStyleAttr) {
        super(ctx, attrs, defStyleAttr);
        init(ctx, attrs);
    }

    public VersionPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init(ctx, attrs);
    }

    public VersionPreference(Context ctx) {
        super(ctx);
        init(ctx);
    }

    private void init(Context ctx, AttributeSet attrs){
        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.version);
        //appid = ta.getString(R.styleable.version_appid);
        init(ctx);
    }

    private void init(final Context ctx){
        try{
            about = ParseAboutXml.Parse(ctx, R.xml.about);
        }catch (Exception ex){
            L.e(ex);
        }

        setSummary(
                String.format(
                        "%s %s-%s(%s)",
                        about.getName(
                                getContext().getString(R.string.app_name)),
                        about.getVersion(BuildConfig.VERSION_NAME),
                        BuildConfig.BUILD_TYPE,
                        about.getCode(
                                String.valueOf(
                                        BuildConfig.VERSION_CODE))));

        versionDialog = new MaterialDialog(ctx)
                .setTitle(about.getName(
                        getContext().getString(R.string.app_name)))
                .setContentView(R.layout.layout_about, new MaterialDialog.OnViewInflateListener() {

                    @Override
                    public boolean onInflate(View v) {
                        setText(v, R.id.version, String.format(
                                "%s(%s)",
                                about.getVersion(BuildConfig.VERSION_NAME),
                                about.getCode(
                                        String.valueOf(
                                                BuildConfig.VERSION_CODE))));

                        String homes = null;
                        for(ParseAboutXml.Url url:about.getHome()){
                            if (!StringUtils.isEmpty(homes)){
                                homes = homes + " | "  + url.genHtml();
                            }else{
                                homes =  "<div>" + url.genHtml();
                            }
                        }
                        homes = homes + "</div>";

                        setText(v, R.id.des, Html.fromHtml(about.getDescription() + homes));

                        LinearLayout root = ((LinearLayout)v.findViewById(R.id.root));
                        for(ParseAboutXml.ChangeLog log: about.getChangelogs()){
                            View view = LayoutInflater.from(ctx).inflate(R.layout.item_log, null);

                            setText(view, R.id.title, log.getName());
                            setText(view, R.id.version, String.valueOf(log.getCode()));
                            setText(view, R.id.content, log.getContent());

                            root.addView(view);
                        }
                        for(ParseAboutXml.Dependencie dep: about.getDependencies()){
                            View view = LayoutInflater.from(ctx).inflate(R.layout.item_log, null);

                            setText(view, R.id.title, ctx.getString(R.string.bullet) + dep.getTitle());
                            setText(view, R.id.version, dep.getVer());
                            setText(view, R.id.content, Html.fromHtml(dep.getLicense().toString() + "<br><br>" + dep.genHtml()));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                                TextView content = ((TextView)view.findViewById(R.id.content));
                                switch (dep.getAlign()){
                                    case "center":
                                        content.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        break;
                                    case "right":
                                        content.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                        break;
                                    default:
                                        content.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                                        break;

                                }
                            }

                            root.addView(view);
                        }

                        return false;
                    }
                });

        // Remove Update task on 15-12-12.
        setOnPreferenceClickListener(this);

        if (!SharedPreferencesUtil.getInstence(null).getBoolean(BuildConfig.VERSION_NAME,false)){
            versionDialog.show();
            SharedPreferencesUtil.getEditor(null).putBoolean(BuildConfig.VERSION_NAME,true).commit();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        versionDialog.show();
        return false;
    }

    private void setText(View perent, int id, String text){
        TextView tv = (TextView)perent.findViewById(id);
        if (StringUtils.isEmpty(text)){
            tv.setVisibility(View.GONE);
            return;
        }
        setText(tv, new SpannedString(text));
    }

    private void setText(View perent, int id, Spanned text){
        TextView tv = (TextView)perent.findViewById(id);
        setText(tv, text);
    }

    private void setText(TextView v, Spanned text){
        if (v == null){
            return;
        }
        if (StringUtils.isEmpty(text)){
            v.setVisibility(View.GONE);
        }else{
            v.setText(text);
        }
        v.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
