package com.nowjordanhappy.klocale

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import java.util.Locale

/**
 * [base] in the language picked in the app — below Android 13 with the platform backend. Call it from every
 * Activity's `attachBaseContext`: `super.attachBaseContext(KLocale.wrap(newBase))`. From 13 (and while
 * following the phone) it returns [base] unchanged: the system applies the per-app language itself.
 *
 * Holds as long as the Activity is recreated on configuration changes (no `configChanges` for locale-bearing
 * changes). A WebView resets the locale, and text built from the application Context needs [localizedContext].
 */
fun KLocale.Companion.wrap(base: Context): Context {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return base
    val tag = PlatformBackend.storedTag(base)
    // The JVM default too: Compose Multiplatform resources and number/date formatting read Locale.getDefault(),
    // not the Activity's configuration (13+ does this itself). Back to the phone's when following it.
    if (tag == null) {
        Locale.setDefault(Resources.getSystem().configuration.primaryLocale())
        return base
    }
    val locale = Locale.forLanguageTag(tag)
    Locale.setDefault(locale)
    val config = Configuration(base.resources.configuration)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) config.setLocales(LocaleList(locale)) else @Suppress("DEPRECATION") { config.locale = locale }
    return base.createConfigurationContext(config)
}

/** A Context in the app's language for text made outside an Activity (notifications, widgets). */
fun KLocale.Companion.localizedContext(context: Context): Context = wrap(context.applicationContext)

/**
 * Opens Android's own app-language screen (13+). False when it isn't there — below 13, or an OEM that strips
 * it: show the app's own list then (never the phone-wide language screen, which changes every app).
 */
fun KLocale.openSystemScreen(activity: Activity): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false
    return try {
        activity.startActivity(Intent(Settings.ACTION_APP_LOCALE_SETTINGS, Uri.fromParts("package", activity.packageName, null)))
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}
