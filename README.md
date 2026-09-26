# Reader

A personal Android app for reading fanfiction on [Archive of Our Own](https://archiveofourown.org). See [docs/PLAN.md](docs/PLAN.md) for the plan.

## Getting the app

Every push is built by GitHub Actions. Open the repository's **Actions** tab, pick the latest successful **Build** run, and download the `reader-debug-apk` artifact. It is a zip containing the APK; install that on the phone. Android asks once to allow installs from that source.

Every build is signed with the same key (`keystore/debug.keystore`), so a new build installs over the old one and keeps your library.

## Building locally

This requires JDK 17+ and the Android SDK. Run:

```
./gradlew test assembleDebug
```
