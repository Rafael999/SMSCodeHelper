package me.gitai.smscodehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import me.gitai.library.utils.StringUtils;
import me.gitai.smscodehelper.R;
import me.gitai.smscodehelper.bundle.BundleScrubber;
import me.gitai.smscodehelper.bundle.PluginBundleManager;
import me.gitai.smscodehelper.utils.TaskerPlugin;

/**
 * Created by gitai on 16-2-4.
 */
public class TaskerActivity extends AbstractPluginActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.layout_tasker);

        if (null == savedInstanceState)
        {
            if (PluginBundleManager.isBundleValid(localeBundle))
            {
                final String address =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_MSG_ADDRESS);
                ((EditText) findViewById(R.id.rf)).setText(address);
                final String body =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_MSG_BODY);
                ((EditText) findViewById(R.id.rb)).setText(body);
            }
        }
    }

    @Override
    public void finish() {
        if (!isCanceled())
        {
            String address = ((EditText) findViewById(R.id.rf)).getText().toString();
            String body = ((EditText) findViewById(R.id.rb)).getText().toString();

            if (!StringUtils.isEmpty(body))
            {
                Intent resultIntent = new Intent();

                resultIntent.putExtra(
                        com.twofortyfouram.locale.Intent.EXTRA_BUNDLE,
                        PluginBundleManager.generateSMSBundle(getApplicationContext(), address, body));

                resultIntent.putExtra(TaskerPlugin.Setting.BUNDLE_KEY_VARIABLE_REPLACE_STRINGS,
                        PluginBundleManager.BUNDLE_EXTRA_MSG_ADDRESS + " " + PluginBundleManager.BUNDLE_EXTRA_MSG_BODY);

                resultIntent.putExtra(
                        com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB,
                        generateBlurb(getApplicationContext(), getString(R.string.prefs_cats_parse)));

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }

    public String generateBlurb(Context context, String message)
    {
        final int maxBlurbLength =
                context.getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length);

        if (message.length() > maxBlurbLength)
        {
            message = message.substring(0, maxBlurbLength);
        }

        return String.format("%s - %s", getTitle(), message);
    }
}
