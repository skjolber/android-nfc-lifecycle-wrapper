

# android-nfc-lifecycle-wrapper
Library for NFC on Android using androidx lifecycle extensions and functional interfaces.

Features:

 * per-application or per-activity NFC behaviour
 * simple functional interfaces for 
    * reader-callback and  foreground-dispatch NFC events
      * tag scanned
      * tag removed
    * NFC adapter settings 
 * programmatically enable/disable and optionally ignore NFC events

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].

## License
[Apache 2.0]

## Obtain
The project is built with [Gradle] and is available on the central Maven repository. 

### Maven

Add the property
```xml
<android-nfc-lifecycle-wrapper.version>1.0.0</android-nfc-lifecycle-wrapper>
```

then add

```xml
<dependency>
    <groupId>com.github.skjolber.android-nfc-lifecycle-wrapper</groupId>
    <artifactId>android</artifactId>
    <version>${android-nfc-lifecycle-wrapper.version}</version>
</dependency>
```

or

```xml
<dependency>
    <groupId>com.github.skjolber.android-nfc-lifecycle-wrapper</groupId>
    <artifactId>androidx</artifactId>
    <version>${android-nfc-lifecycle-wrapper.version}</version>
</dependency>
```

### Gradle
For

```groovy
ext {
  jsonLogFilterVersion = '1.0.0'
}
```

add

```groovy
api("com.github.skjolber.android-nfc-lifecycle-wrapper:android:${jsonLogFilterVersion}")
```

or

```groovy
api("com.github.skjolber.android-nfc-lifecycle-wrapper:androidx:${jsonLogFilterVersion}")
```

## Usage
If you prefer a working appliction, see the [example]. 

The library relies on registering an `Application.ActivityLifecycleCallbacks` instance in the main `Application` (see further down). 

Activities wanting to interact with NFC must add the interface `NfcActivity` or `NfcCompatActivity` and use the `NfcFactory` passed in the`onPreCreated` or `onPostCreated` methods to configure the desired NFC nature:

```java
public void onPreCreated(NfcFactory nfcFactory) {
   nfcFactory.newForegroundDispatchBuilder()
     .withTagDiscovered( (tag, intent) -> {
        // your code here
      })
     .build();
}
```

Builders are available for foreground dispatch, reader callback and settings (NFC on/off on the device). Keep a reference to the resulting instance to programmatically enable/disable or ignore the resulting events.

### Foreground dispatch
Create a `NfcForegroundDispatchBuilder` and pass in the desired (lambda) functions:

```java
nfcFactory.newForegroundDispatchBuilder()
    .withTagDiscovered( (tag, intent) -> {
        Log.d(TAG, "Tag scanned");

        // your code here
    })
    .withTagRemoved( () -> {
        Log.d(TAG, "Tag removed");

        // your code here
    })
    .build();
```

and keep the resulting built `NfcForegroundDispatch` instance if you want to enable/disable or ignore NFC events.

### Reader callback
Create a `ReaderCallbackBuilder` and pass in the desired (lambda) functions:

```java
nfcFactory.newReaderCallbackBuilder()
    .withTagDiscovered( tag -> {
        Log.d(TAG, "Tag discovered    ");

        // your code here
    })
    .withTagRemoved( () -> {
        Log.d(TAG, "Tag removed");

        // your code here
    })
    .withMainThread()
    .withAllTagTechnologies()
    .build();
```

and keep the resulting built `NfcReaderCallback` instance if you want to enable/disable or ignore NFC events.

### Settings
The current NFC settings is detected via `NfcAdapter` as the `Activity`'s `onResume(..)` is called. 

Create a `SettingsBuilder` and pass in the desired (lambda) functions:

```java
factory.newSettingsBuilder()
    .withEnabled( (transition) -> {
        if (transition) {
            Log.d(TAG, "NFC setting transitioned to enabled.");
        } else {
            Log.d(TAG, "NFC setting is still enabled.");
        }
    })
    .withDisabled((transition, available) -> {
        if(available) {
            if (transition) {
                Log.d(TAG, "NFC setting transitioned to disabled.");
            } else {
                Log.d(TAG, "NFC setting is still disabled.");
            }
        } else {
            if (transition) {
                Log.d(TAG, "NFC is not available on this device.");
            } else {
                Log.d(TAG, "NFC is still not available on this device.");
            }
        }
    })
    .build();
```
and keep the resulting built `NfcSettings` instance if you want to enable/disable or ignore NFC Adapter events.

The `transition` passed to the lambda functions is true only on the inital call and when NFC is turned on or off on the device. This simplifies user notification if NFC functionality is optional.

### Application setup
Wire up `NfcActivityLifecycleCallbacks` or (preferably) `NfcCompatActivityLifecycleCallbacks` with `registerActivityLifecycleCallbacks`and `unregisterActivityLifecycleCallbacks`:

```java
public class NfcApplication extends Application {  
  
    protected NfcCompatActivityLifecycleCallbacks callbacks;  
  
    public void onCreate() {  
        super.onCreate();  
  
        callbacks = NfcCompatActivityLifecycleCallbacks.newBuilder().withApplication(this).build();  
        registerActivityLifecycleCallbacks(callbacks);  
    }  
  
    @Override  
    public void onTerminate() {  
        super.onTerminate();  
  
        unregisterActivityLifecycleCallbacks(callbacks);  
    }  
}
```

If using `AppCompatActivity`, prefer `NfcCompatActivityLifecycleCallbacks`, as it is better than `NfcActivityLifecycleCallbacks` because it can hook the NFC `onPause(..)` and `onResume(..)` wireing directly into the `Activity`'s `lifecycle` instance, which removes 'manual' lookup of activity behaviour as the activity stack push / pop.

# History
- 1.0.0: Initial version.

[Apache 2.0]:           https://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:        https://github.com/skjolber/android-nfc-lifecycle-wrapper/issues
[Gradle]:                   https://gradle.org/
[example]:                example
