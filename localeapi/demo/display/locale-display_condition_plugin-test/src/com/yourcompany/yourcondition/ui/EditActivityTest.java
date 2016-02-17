/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.yourcompany.yourcondition.ui;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.twofortyfouram.locale.BreadCrumber;
import com.yourcompany.yourcondition.R;
import com.yourcompany.yourcondition.bundle.PluginBundleManager;
import com.yourcompany.yourcondition.test.BundleTestHelper;

/**
 * Tests the {@link EditActivity}.
 */
public final class EditActivityTest extends ActivityInstrumentationTestCase2<EditActivity>
{
    /**
     * Context of the target application. This is initialized in {@link #setUp()}.
     */
    private Context mTargetContext;

    /**
     * Instrumentation for the test. This is initialized in {@link #setUp()}.
     */
    private Instrumentation mInstrumentation;

    /**
     * Constructor for the test class; required by Android.
     */
    public EditActivityTest()
    {
        super(EditActivity.class);
    }

    /**
     * Setup that executes before every test case
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        mInstrumentation = getInstrumentation();
        mTargetContext = mInstrumentation.getTargetContext();

        /*
         * Perform test case specific initialization. This is required to be set up here because
         * setActivityIntent has no effect inside a method annotated with @UiThreadTest
         */
        if ("testNewConditionWithoutSelection".equals(getName())) //$NON-NLS-1$
        {
            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation")); //$NON-NLS-1$
        }
        else if ("testNewConditionSaveOff".equals(getName())) //$NON-NLS-1$
        {
            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation")); //$NON-NLS-1$
        }
        else if ("testNewConditionSaveOn".equals(getName())) //$NON-NLS-1$
        {
            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation")); //$NON-NLS-1$
        }
        else if ("testEditOldConditionOff".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = PluginBundleManager.generateBundle(mTargetContext, false);

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
        else if ("testEditOldConditionOff_screen_rotation".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = PluginBundleManager.generateBundle(mTargetContext, false);

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
        else if ("testEditOldConditionOn".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = PluginBundleManager.generateBundle(mTargetContext, true);

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
        else if ("testEditOldConditionOn_screen_rotation".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = PluginBundleManager.generateBundle(mTargetContext, true);

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
        else if ("testMissingBreadcrumb".equals(getName())) //$NON-NLS-1$
        {
            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION));
        }
        else if ("testBadBundleMissingExtra".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = new Bundle();

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
        else if ("testBadBundleWrongType".equals(getName())) //$NON-NLS-1$
        {
            final Bundle bundle = new Bundle();
            bundle.putString(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE, "test"); //$NON-NLS-1$

            setActivityIntent(new Intent(com.twofortyfouram.locale.Intent.ACTION_EDIT_CONDITION).putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BREADCRUMB, "Locale > Edit Situation").putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, bundle)); //$NON-NLS-1$
        }
    }

    /**
     * Verifies the Activity class name hasn't been accidentally changed.
     */
    @SmallTest
    public static void testActivityName()
    {
        /*
         * NOTE: This test is expected to fail initially when you are adapting this example to your own
         * plug-in. Once you've settled on a name for your Activity, go ahead and update this test case.
         * 
         * The goal of this test case is to prevent accidental renaming of the Activity. Once a plug-in is
         * published to the app store, the Activity shouldn't be renamed because that will break the plug-in
         * for users who had the old version of the plug-in. If you ever find yourself really needing to
         * rename the Activity after the plug-in has been published, take a look at using an activity-alias
         * entry in the Android Manifest.
         */

        assertEquals("com.yourcompany.yourcondition.ui.EditActivity", EditActivity.class.getName()); //$NON-NLS-1$
    }

    /**
     * Tests {@link EditActivity#generateBlurb(Context, boolean)}.
     */
    @SmallTest
    public void testGetBlurb()
    {
        assertEquals(mTargetContext.getString(R.string.blurb_on), EditActivity.generateBlurb(mTargetContext, true));
        assertEquals(mTargetContext.getString(R.string.blurb_off), EditActivity.generateBlurb(mTargetContext, false));
    }

    /**
     * Tests creation of a new condition, that the UI is initialized to the right state, and that nothing is
     * saved.
     */
    @MediumTest
    @UiThreadTest
    public void testNewCondition_cancel_result() throws Throwable
    {
        final Activity activity = getActivity();

        assertTitle();
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        activity.finish();

        assertEquals(Activity.RESULT_CANCELED, getActivityResultCode(activity));
    }

    /**
     * Tests editing a new condition with screen rotations.
     */
    @MediumTest
    public void testNewCondition_screen_rotation() throws Throwable
    {
        /*
         * At this point, nothing is selected. Rotate the screen to make sure that this state is preserved.
         */
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        /*
         * Select something and rotate the screen to make sure that this state is preserved.
         */
        setSelectedPositionAutoSync(R.string.list_off);
        assertSelectedPositionAutoSync(R.string.list_off);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_off);

        /*
         * Select something and rotate the screen to make sure that this state is preserved.
         */
        setSelectedPositionAutoSync(R.string.list_on);
        assertSelectedPositionAutoSync(R.string.list_on);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_on);
    }

    /**
     * Tests creation of a new condition, that the UI is initialized to the right state, and that changes are
     * properly saved
     */
    @MediumTest
    @UiThreadTest
    public void testNewConditionSaveOff() throws Throwable
    {
        assertTitle();
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        setSelectedPositionAutoSync(R.string.list_off);

        assertActivityResultAutoSync(false);
    }

    /**
     * Tests creation of a new condition, that the UI is initialized to the right state, and that changes are
     * properly saved
     */
    @MediumTest
    @UiThreadTest
    public void testNewConditionSaveOn() throws Throwable
    {
        assertTitle();
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        setSelectedPositionAutoSync(R.string.list_on);

        assertActivityResultAutoSync(true);
    }

    /**
     * Tests editing an old condition, that the UI is initialized to the right state, and that the Activity
     * result is correct
     */
    @MediumTest
    @UiThreadTest
    public void testEditOldConditionOff() throws Throwable
    {
        assertTitle();

        assertSelectedPositionAutoSync(R.string.list_off);

        // Note: It is also valid for the Activity to cancel the result if there are no differences.
        assertActivityResultAutoSync(false);
    }

    /**
     * Tests editing an old condition with screen rotations.
     */
    @MediumTest
    public void testEditOldConditionOff_screen_rotation() throws Throwable
    {
        /*
         * Make sure that the initial state is preserved.
         */
        assertSelectedPositionAutoSync(R.string.list_off);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_off);

        /*
         * Select something and rotate the screen to make sure that this state is preserved.
         */
        setSelectedPositionAutoSync(R.string.list_on);
        assertSelectedPositionAutoSync(R.string.list_on);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_on);
    }

    /**
     * Tests editing an old condition, that the UI is initialized to the right state, and that the Activity
     * result is correct
     */
    @MediumTest
    @UiThreadTest
    public void testEditOldConditionOn() throws Throwable
    {
        assertTitle();
        assertSelectedPositionAutoSync(R.string.list_on);

        // Note: It is also valid for the Activity to cancel the result if there are no differences.
        assertActivityResultAutoSync(true);
    }

    /**
     * Tests editing an old condition with screen rotations.
     */
    @MediumTest
    public void testEditOldConditionOn_screen_rotation() throws Throwable
    {
        /*
         * Make sure that the initial state is preserved.
         */
        assertSelectedPositionAutoSync(R.string.list_on);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_on);

        /*
         * Select something and rotate the screen to make sure that this state is preserved.
         */
        setSelectedPositionAutoSync(R.string.list_off);
        assertSelectedPositionAutoSync(R.string.list_off);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setActivityOrientationSync(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertSelectedPositionAutoSync(R.string.list_off);
    }

    /**
     * Verifies the Activity properly handles a missing breadcrumb
     */
    @MediumTest
    @UiThreadTest
    public void testMissingBreadcrumb()
    {
        final Activity activity = getActivity();

        assertEquals(mTargetContext.getString(R.string.plugin_name), activity.getTitle());
    }

    /**
     * Verifies the Activity properly handles a bundle with a bad value embedded in it.
     */
    @MediumTest
    @UiThreadTest
    public void testBadBundleMissingExtra() throws Throwable
    {
        final Activity activity = getActivity();

        assertTitle();
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        activity.finish();
        assertEquals(Activity.RESULT_CANCELED, getActivityResultCode(activity));
    }

    /**
     * Verifies the Activity properly handles a bundle with a bad value embedded in it.
     */
    @MediumTest
    @UiThreadTest
    public void testBadBundleWrongType() throws Throwable
    {
        final Activity activity = getActivity();

        assertTitle();
        assertSelectedPositionAutoSync(AdapterView.INVALID_POSITION);

        activity.finish();
        assertEquals(Activity.RESULT_CANCELED, getActivityResultCode(activity));
    }

    /**
     * Asserts the Activity title equals expected values.
     */
    private void assertTitle()
    {
        final CharSequence expected = BreadCrumber.generateBreadcrumb(mTargetContext, getActivity().getIntent(), mTargetContext.getString(R.string.plugin_name));
        final CharSequence actual = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? getTitleHoneycomb()
                : getActivity().getTitle();

        assertEquals(expected, actual);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private CharSequence getTitleHoneycomb()
    {
        return getActivity().getActionBar().getSubtitle();
    }

    /**
     * Asserts the Activity result contains the expected values for the given display state.
     * 
     * @param isDisplayOn True if the display is on, false if the display is off.
     */
    private void assertActivityResultAutoSync(final boolean isDisplayOn) throws Throwable
    {
        final Activity activity = getActivity();

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                /*
                 * Verify finishing with text entry is saved
                 */
                activity.finish();

                assertEquals(Activity.RESULT_OK, getActivityResultCode(activity));

                final Intent result = getActivityResultData(activity);
                assertNotNull(result);

                final Bundle extras = result.getExtras();
                assertNotNull(extras);
                assertEquals(String.format("Extras should only contain %s and %s but actually contain %s", com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, extras.keySet()), 2, extras.keySet() //$NON-NLS-1$
                                                                                                                                                                                                                                          .size());

                assertFalse(TextUtils.isEmpty(extras.getString(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB)));
                assertEquals(EditActivity.generateBlurb(mTargetContext, isDisplayOn), extras.getString(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB));
                // BundleTestHelper.assertSerializable(extras);

                final Bundle pluginBundle = extras.getBundle(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
                assertNotNull(pluginBundle);

                /*
                 * Verify the Bundle can be serialized
                 */
                BundleTestHelper.assertSerializable(pluginBundle);

                /*
                 * The following are tests specific to this plug-in's bundle
                 */
                assertTrue(PluginBundleManager.isBundleValid(pluginBundle));
                assertEquals(isDisplayOn, pluginBundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE));
            }
        };
        
        if (getActivity().getMainLooper() == Looper.myLooper())
        {
            runnable.run();
        }
        else
        {
            runTestOnUiThread(runnable);
        }
    }

    /**
     * Asserts provided item is selected in the list.
     * 
     * @param position one of {@link R.string#list_off}, {@link R.string#list_on}, or
     *            {@link AdapterView#INVALID_POSITION}.
     */
    private void assertSelectedPositionAutoSync(final int position) throws Throwable
    {
        final int actualPosition;
        if (AdapterView.INVALID_POSITION == position)
        {
            actualPosition = AdapterView.INVALID_POSITION;
        }
        else
        {
            actualPosition = EditActivity.getPositionForIdInArray(mTargetContext, R.array.display_states, position);
        }

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                assertEquals(actualPosition, ((ListView) getActivity().findViewById(android.R.id.list)).getCheckedItemPosition());
            }
        };

        if (getActivity().getMainLooper() == Looper.myLooper())
        {
            runnable.run();
        }
        else
        {
            runTestOnUiThread(runnable);
        }
    }

    /**
     * Sets the given position in the list.
     * 
     * @param position one of {@link R.string#list_off}, {@link R.string#list_on}, or
     *            {@link AdapterView#INVALID_POSITION}.
     */
    private void setSelectedPositionAutoSync(final int position) throws Throwable
    {
        final int actualPosition;
        if (AdapterView.INVALID_POSITION == position)
        {
            actualPosition = AdapterView.INVALID_POSITION;
        }
        else
        {
            actualPosition = EditActivity.getPositionForIdInArray(mTargetContext, R.array.display_states, position);
        }

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                final ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
                listView.setItemChecked(actualPosition, true);
            }
        };

        if (getActivity().getMainLooper() == Looper.myLooper())
        {
            runnable.run();
        }
        else
        {
            runTestOnUiThread(runnable);
        }
    }

    /**
     * Helper to get the Activity result code via reflection.
     * 
     * @param activity Activity whose result code is to be obtained.
     * @return Result code of the Activity.
     */
    private static int getActivityResultCode(final Activity activity)
    {
        if (null == activity)
        {
            throw new IllegalArgumentException("activity cannot be null"); //$NON-NLS-1$
        }

        /*
         * This is a hack to verify the result code. There is no official way to check this using the Android
         * testing frameworks, so accessing the internals of the Activity object is the only way. This could
         * break on newer versions of Android.
         */

        try
        {
            final Field resultCodeField = Activity.class.getDeclaredField("mResultCode"); //$NON-NLS-1$
            resultCodeField.setAccessible(true);
            return ((Integer) resultCodeField.get(activity)).intValue();
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to set the Activity's orientation. When this method completes, the Activity will have
     * finished its Activity lifecycle events.
     * <p>
     * This method must not be called from the UI thread.
     */
    private void setActivityOrientationSync(final int orientation)
    {
        mInstrumentation.runOnMainSync(new Runnable()
        {
            public void run()
            {
                getActivity().setRequestedOrientation(orientation);
            }
        });
    }

    /**
     * Helper to get the Activity result Intent via reflection.
     * 
     * @param activity Activity whose result Intent is to be obtained. Cannot be null.
     * @return Result Intent of the Activity
     * @throws IllegalArgumentException if {@code activity} is null
     */
    private static Intent getActivityResultData(final Activity activity)
    {
        if (null == activity)
        {
            throw new IllegalArgumentException("activity cannot be null"); //$NON-NLS-1$
        }

        /*
         * This is a hack to verify the result code. There is no official way to check this using the Android
         * testing frameworks, so accessing the internals of the Activity object is the only way. This could
         * break on newer versions of Android.
         */

        try
        {
            final Field resultIntentField = Activity.class.getDeclaredField("mResultData"); //$NON-NLS-1$
            resultIntentField.setAccessible(true);
            return ((Intent) resultIntentField.get(activity));
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}