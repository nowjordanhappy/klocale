package com.nowjordanhappy.klocale

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.io.File
import java.util.Locale

/**
 * The platform backend, no AppCompat needed:
 * - Android 13+: the system's per-app language ([LocaleManager]); the system recreates the activities.
 * - Below 13: the tag is stored here, applied to each Activity by [KLocale.Companion.wrap] in
 *   `attachBaseContext`, and the running activities are recreated after a change.
 */
class PlatformBackend(context: Context) : LocaleBackend {
    private val app = context.applicationContext
    private val prefs = app.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    override fun systemLanguage(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.getSystemService(LocaleManager::class.java).systemLocales[0].toLanguageTag()
        } else {
            Resources.getSystem().configuration.primaryLocale().toLanguageTag()
        }

    override fun chosen(): String? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return storedTag(app)
        val manager = app.getSystemService(LocaleManager::class.java)
        val system = manager.applicationLocales.takeUnless { it.isEmpty }?.get(0)?.toLanguageTag()
        // A pick made below 13 survives the phone's update to 13+: handed to the system once, then ours is dropped.
        val older = prefs.getString(KEY, null)
        if (older != null) {
            if (system == null) manager.applicationLocales = LocaleList.forLanguageTags(older)
            prefs.edit().remove(KEY).commit()
            return system ?: older
        }
        return system
    }

    override fun apply(tag: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.getSystemService(LocaleManager::class.java).applicationLocales =
                tag?.let { LocaleList.forLanguageTags(it) } ?: LocaleList.getEmptyLocaleList()
        } else {
            prefs.edit().apply { if (tag == null) remove(KEY) else putString(KEY, tag) }.commit()
            KLocaleContext.recreateActivities()
        }
    }

    internal companion object {
        const val PREFS = "klocale"
        const val KEY = "tag"
        private const val MIGRATED = "migrated_appcompat"

        /** AppCompat's own record below 13 (autoStoreLocales), read once so a switch from klocale 1.x keeps the pick. */
        private const val APPCOMPAT_FILE = "androidx.appcompat.app.AppCompatDelegate.application_locales_record_file"

        fun storedTag(context: Context): String? {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            if (!prefs.getBoolean(MIGRATED, false)) {
                val old = try {
                    Regex("application_locales=\"([^\"]+)\"").find(File(context.filesDir, APPCOMPAT_FILE).readText())?.groupValues?.get(1)
                } catch (_: Exception) {
                    null
                }
                prefs.edit().apply {
                    putBoolean(MIGRATED, true)
                    if (old != null && !prefs.contains(KEY)) putString(KEY, old.split(",").first())
                }.commit()
            }
            return prefs.getString(KEY, null)
        }
    }
}

internal fun Configuration.primaryLocale(): Locale =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) locales[0] else @Suppress("DEPRECATION") locale

actual fun defaultBackend(): LocaleBackend = PlatformBackend(KLocaleContext.app)

actual fun languageName(tag: String): String {
    val locale = Locale.forLanguageTag(tag)
    return locale.getDisplayName(locale).replaceFirstChar { it.titlecase(locale) }
}
