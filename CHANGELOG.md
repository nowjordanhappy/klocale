# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
