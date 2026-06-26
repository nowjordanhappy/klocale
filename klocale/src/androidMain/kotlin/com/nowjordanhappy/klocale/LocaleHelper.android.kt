package com.nowjordanhappy.klocale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

actual fun getSystemLanguage(): String {
    val appLocales = AppCompatDelegate.getApplicationLocales()
    return if (!appLocales.isEmpty) {
        appLocales[0]?.toLanguageTag() ?: Locale.getDefault().toLanguageTag()
    } else {
        Locale.getDefault().toLanguageTag()
    }
}

actual fun syncToSystem(code: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
}
