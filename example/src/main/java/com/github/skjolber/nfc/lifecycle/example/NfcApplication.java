package com.github.skjolber.nfc.lifecycle.example;

import android.app.Application;

import com.github.skjolber.nfc.lifecycle.utility.NfcActivityLifecycleCallbacks;
import com.github.skjolber.nfc.lifecycle.utility.NfcCompatActivityLifecycleCallbacks;
import android.util.Log;

/**
 *
 * Main application. Normally, one callback would be sufficient.
 *
 */

public class NfcApplication extends Application {

    private static final String TAG = NfcApplication.class.getName();

    protected NfcActivityLifecycleCallbacks callbacks;
    protected NfcCompatActivityLifecycleCallbacks appcompatCallbacks;

    public void onCreate() {
        super.onCreate();

        callbacks = NfcActivityLifecycleCallbacks.newBuilder().withApplication(this).build();
        appcompatCallbacks = NfcCompatActivityLifecycleCallbacks.newBuilder().withApplication(this).build();

        Log.d(TAG, "Register application lifecycle callbacks");
        registerActivityLifecycleCallbacks(callbacks);
        registerActivityLifecycleCallbacks(appcompatCallbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "Unregister application lifecycle callbacks");

        unregisterActivityLifecycleCallbacks(callbacks);
        unregisterActivityLifecycleCallbacks(appcompatCallbacks);
    }
}