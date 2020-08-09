package com.github.skjolber.nfc.lifecycle.utility;

public interface NfcActivity {

    default void onPreCreated(NfcFactory factory) {
    }

    default void onPostCreated(NfcFactory factory) {
    }

}
