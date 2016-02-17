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

package com.yourcompany.yourcondition.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.text.format.DateUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.yourcompany.yourcondition.Constants;
import com.yourcompany.yourcondition.bundle.PluginBundleManager;

/**
 * Tests the {@link QueryReceiver}
 */
public final class ReceiverTest extends AndroidTestCase
{
    /**
     * Amount of time to wait for threaded queries to complete. Android gives BroadcastReceivers 10 seconds to
     * complete their work, however plug-ins should be much faster than that.
     */
    private static final long LATCH_WAIT_TIME = 3 * DateUtils.SECOND_IN_MILLIS;

    /**
     * Initialize a looper to be used across all test cases. This is required, as ConditionFactory guarantees
     * that the methods will be consistently called from the same thread.
     */
    private Looper mLooper;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        final HandlerThread h = new HandlerThread(getClass().getSimpleName());
        h.setUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
        h.start();
        mLooper = h.getLooper();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        mLooper.quit();
    }

    /**
     * Tests sending an Intent with no Action, which should simply return.
     */
    @MediumTest
    public void testNoAction()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        final Intent intent = new Intent(getContext(), QueryReceiver.class);
        final Bundle extras = new Bundle();
        extras.putBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE, false);
        intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, extras);

        getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
        {
            @Override
            public void onReceive(final Context context, final Intent i)
            {
                assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, getResultCode());

                latch.countDown();
            }
        }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

        try
        {
            assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
        }
        catch (final InterruptedException e)
        {
            fail();
        }
    }

    /**
     * Tests sending an Intent with no Bundle, which should simply return.
     */
    @MediumTest
    public void testNoBundle()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        final Intent intent = new Intent(com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION).setClass(getContext(), QueryReceiver.class);

        getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
        {
            @Override
            public void onReceive(final Context context, final Intent i)
            {
                assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, getResultCode());

                latch.countDown();
            }
        }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

        try
        {
            assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
        }
        catch (final InterruptedException e)
        {
            fail();
        }
    }

    /**
     * Tests sending an Intent with a null Bundle, which should simply return.
     */
    @MediumTest
    public void testNullBundle()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        final Intent intent = new Intent(com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION).setClass(getContext(), QueryReceiver.class)
                                                                                                 .putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, (Bundle) null);

        getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
        {
            @Override
            public void onReceive(final Context context, final Intent i)
            {
                assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, getResultCode());

                latch.countDown();
            }
        }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

        try
        {
            assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
        }
        catch (final InterruptedException e)
        {
            fail();
        }
    }

    /**
     * Tests sending an Intent with a bundle of the wrong type Bundle, which should simply return.
     */
    @MediumTest
    public void testBundleWrongType()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        final Intent intent = new Intent(com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION).setClass(getContext(), QueryReceiver.class)
                                                                                                 .putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, "test"); //$NON-NLS-1$

        getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
        {
            @Override
            public void onReceive(final Context context, final Intent i)
            {
                assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, getResultCode());

                latch.countDown();
            }
        }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

        try
        {
            assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
        }
        catch (final InterruptedException e)
        {
            fail();
        }
    }

    /**
     * Tests sending an Intent that is satisfied when the display is on.
     */
    @MediumTest
    public void testSatisfiedDisplayOn()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        /*
         * Force the display on for this test
         */
        @SuppressWarnings("deprecation")
        final WakeLock wakeLock = ((PowerManager) getContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "test"); //$NON-NLS-1$
        try
        {
            wakeLock.acquire();

            final Intent intent = new Intent(com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION).setClass(getContext(), QueryReceiver.class);
            final Bundle extras = new Bundle();
            extras.putBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE, true);
            extras.putInt(PluginBundleManager.BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(mContext));
            intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, extras);

            getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
            {
                @Override
                public void onReceive(final Context context, final Intent i)
                {
                    assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED, getResultCode());

                    latch.countDown();
                }
            }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

            try
            {
                assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
            }
            catch (final InterruptedException e)
            {
                fail();
            }
        }
        finally
        {
            wakeLock.release();
        }
    }

    /**
     * Tests sending an Intent that is satisfied when the display is on.
     */
    @MediumTest
    public void testUnsatisfiedDisplayOn()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        /*
         * Force the display on for this test
         */
        @SuppressWarnings("deprecation")
        final WakeLock wakeLock = ((PowerManager) getContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "test"); //$NON-NLS-1$
        try
        {
            wakeLock.acquire();

            final Intent intent = new Intent(com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION).setClass(getContext(), QueryReceiver.class);
            final Bundle extras = new Bundle();
            extras.putBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE, false);
            extras.putInt(PluginBundleManager.BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(mContext));
            intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, extras);

            getContext().sendOrderedBroadcast(intent, null, new BroadcastReceiver()
            {
                @Override
                public void onReceive(final Context context, final Intent i)
                {
                    assertEquals(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED,
                                 getResultCode());

                    latch.countDown();
                }
            }, new Handler(mLooper), com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNKNOWN, null, null);

            try
            {
                assertTrue(latch.await(LATCH_WAIT_TIME, TimeUnit.MILLISECONDS));
            }
            catch (final InterruptedException e)
            {
                fail();
            }
        }
        finally
        {
            wakeLock.release();
        }
    }
}