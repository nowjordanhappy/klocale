package com.nowjordanhappy.klocale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

actual fun getSystemLanguage(): String = Locale.getDefault().toLanguageTag()

actual fun syncToSystem(code: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
}
