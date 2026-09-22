# DSA Tracker

Minimal Mimo-style progress tracker for your 90-day Samsung prep plan.
Home screen shows Day X/90, current week's topic, a streak counter, and a
button to schedule a daily 8 PM local reminder notification.

## Why no .apk in this download
Building an .apk needs the Android SDK + Google's Maven repo, which the
sandbox this was built in can't reach. This is the full source project instead —
getting an APK from it takes about 2 minutes.

## Build it (2 min)
1. Install Android Studio (free): developer.android.com/studio
2. Open Android Studio → Open → select the `DsaTracker` folder
3. Let Gradle sync (auto-downloads what it needs, first time takes a bit)
4. Build → Build Bundle(s)/APK(s) → Build APK(s)
5. APK lands in `app/build/outputs/apk/debug/app-debug.apk` — copy it to
   your phone and install (enable "install unknown apps" once, if prompted)

## Extending it
Weeks array in `MainActivity.kt` mirrors your study plan — edit freely if
the plan shifts. Everything is stored locally via SharedPreferences, no
backend, no accounts.
