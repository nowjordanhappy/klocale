package com.nowjordanhappy.klocale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class LocaleHelperAndroidTest {

    @After
    fun tearDown() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

    @Test
    fun noPerAppLocale_returnsSystemDefault() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        assertEquals(Locale.getDefault().toLanguageTag(), getSystemLanguage())
    }

    @Test
    fun perAppLocaleSet_returnsThatLocale() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
        val lang = getSystemLanguage().split("-").first().lowercase()
        assertEquals("es", lang)
    }
}
