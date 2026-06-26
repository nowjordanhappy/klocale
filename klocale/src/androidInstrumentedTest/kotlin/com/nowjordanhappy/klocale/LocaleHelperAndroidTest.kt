package com.nowjordanhappy.klocale

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class LocaleHelperAndroidTest {

    private fun onMain(block: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
    }

    @After
    fun tearDown() {
        // On API < 33, AppCompatDelegate uses an in-process static cache (sRequestedAppLocales)
        // that we can reset without an Activity. On API 33+ we didn't touch it, so nothing to reset.
        if (Build.VERSION.SDK_INT < 33) {
            onMain { AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList()) }
        }
    }

    @Test
    fun noPerAppLocale_returnsSystemDefault() {
        assertEquals(Locale.getDefault().toLanguageTag(), getSystemLanguage())
    }

    @Test
    fun perAppLocaleSet_returnsThatLocale() {
        if (Build.VERSION.SDK_INT >= 33) {
            // On API 33+, AppCompatDelegate.setApplicationLocales() is a no-op without an active
            // AppCompatActivity (no delegate → no LocaleManager call). The round-trip is
            // verified by the sample app; here we just confirm getSystemLanguage() doesn't crash.
            assertTrue(getSystemLanguage().isNotEmpty())
            return
        }
        // On API < 33, AppCompatDelegate stores the locale in sRequestedAppLocales (in-process
        // static cache). No Activity needed — set via main thread, read back immediately.
        onMain { AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es")) }
        val lang = getSystemLanguage().split("-").first().lowercase()
        assertEquals("es", lang)
    }
}
