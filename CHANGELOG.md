# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - 2026-11-20

### Added
- Platform mode (default on Android): the system's per-app language on Android 13+, and below 13 a stored pick applied by `KLocale.wrap(context)` in `attachBaseContext` — works with a plain `ComponentActivity`, no AppCompat. KLocale recreates the running activities after a pick below 13. `wrap` also sets `Locale.getDefault()`, so Compose Multiplatform resources and number/date formatting follow the pick.
- The sample shows its own language list where the system screen doesn't exist (below Android 13).
- `klocale-appcompat` artifact: `KLocale.appCompat(…)` for apps on `AppCompatActivity`, shipping the `AppLocalesMetadataHolderService` so the pick survives restarts below 13.
- `chosen: StateFlow<String?>` (null while following the phone) and `followSystem()`.
- `KLocale.openSystemScreen(activity)`, `KLocale.localizedContext(context)`, `languageName(tag)`.
- A language AppCompat stored below 13 (1.x apps) is read once and kept.
- A pick made below 13 is handed to the system once the phone updates to Android 13+.

### Changed
- **Breaking:** the core artifact no longer depends on AppCompat. AppCompat apps add `klocale-appcompat`.
- `set()` works on Android 13+ without an AppCompatActivity (it talks to `LocaleManager` directly).
- Regional tags match exactly first: `pt-BR` in `supported` is picked for `pt-BR` (and for `pt`).

### Removed
- `getSystemLanguage()` and `syncToSystem()` (use `getSystemDefault()`, `set()`, `followSystem()`).

## [1.0.1] - 2026-06-27

### Added
- README with installation, usage, Android and iOS setup instructions, and requirements table
- Demo GIFs for Android API 30, Android API 36, and iOS in README
- Maven Central, License, and Kotlin version badges in README
- MIT License
- Sample app launcher icons (KMP cube) for Android and iOS
- `CHANGELOG.md` — all future releases documented here
- CI now auto-creates a GitHub Release with changelog notes on every `v*` tag push

## [1.0.0] - 2026-06-27

### Added
- `KLocale` class — single entry point for in-app language switching on Android and iOS
- `current: StateFlow<String>` — observable language state, no Compose dependency required
- `set(code: String)` — updates the StateFlow and syncs to the platform language system
- `getSystemDefault()` — maps device locale to the nearest supported language (e.g. `fr-CA` → `fr`)
- `onChanged` callback — optional hook for host app persistence (DataStore, multiplatform-settings, etc.)
- Android implementation via `AppCompatDelegate.setApplicationLocales()` — backports to API 21, delegates to `LocaleManager` on API 33+
- iOS implementation via `NSUserDefaults` (`AppleLanguages` key) — system registers on next launch
- `mapToSupported()` utility — case-insensitive language tag matching with fallback
- Sample app for Android and iOS demonstrating the full integration
- Tests: `KLocaleTest` (8 common cases), Android instrumented tests, iOS simulator tests
- README with Android setup (`localeConfig`), iOS setup (`CFBundleLocalizations`), and usage examples
- MIT License
- GitHub Actions CI — publishes to Maven Central on `v*` tag push
