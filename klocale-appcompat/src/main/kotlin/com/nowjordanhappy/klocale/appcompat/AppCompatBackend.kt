package com.nowjordanhappy.klocale.appcompat

import android.app.LocaleManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.nowjordanhappy.klocale.KLocale
import com.nowjordanhappy.klocale.KLocaleContext
import com.nowjordanhappy.klocale.LocaleBackend

/**
 * klocale 1.x's behaviour, fixed: the language through [AppCompatDelegate] (needs `AppCompatActivity` below 13),
 * stored below 13 by the manifest service this artifact ships. From 13 it talks to the system's [LocaleManager]
 * directly, so a pick works even before an AppCompatActivity exists (1.x's `set()` did nothing then).
 */
class AppCompatBackend(context: Context) : LocaleBackend {
    private val app = context.applicationContext

    override fun systemLanguage(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.getSystemService(LocaleManager::class.java).systemLocales[0].toLanguageTag()
        } else {
            Resources.getSystem().configuration.locales[0].toLanguageTag()
        }

    override fun chosen(): String? = AppCompatDelegate.getApplicationLocales().takeUnless { it.isEmpty }?.get(0)?.toLanguageTag()

    override fun apply(tag: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.getSystemService(LocaleManager::class.java).applicationLocales =
                tag?.let { LocaleList.forLanguageTags(it) } ?: LocaleList.getEmptyLocaleList()
        } else {
            // The empty list is AppCompat's own "no choice": following the phone again.
            AppCompatDelegate.setApplicationLocales(tag?.let { LocaleListCompat.forLanguageTags(it) } ?: LocaleListCompat.getEmptyLocaleList())
        }
    }
}

/** KLocale for an app on `AppCompatActivity` (no `wrap` needed: AppCompat applies the language itself). */
fun KLocale.Companion.appCompat(
    supported: List<String>,
    fallback: String = "en",
    onChanged: ((String) -> Unit)? = null,
): KLocale = KLocale(supported, fallback, onChanged, AppCompatBackend(KLocaleContext.app))
