package com.github.skjolber.nfc.lifecycle.utility;

public interface NfcCompatActivity {

    default void onPreCreated(NfcFactory factory) {
    }

    default void onPostCreated(NfcFactory factory) {
    }

}
