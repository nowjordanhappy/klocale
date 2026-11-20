package com.nowjordanhappy.klocale

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** The platform backend on a real device: run on an API < 33 and an API 33+ emulator. */
@RunWith(AndroidJUnit4::class)
class PlatformBackendTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val backend = PlatformBackend(context)

    @Before
    @After
    fun reset() {
        backend.apply(null)
        context.getSharedPreferences(PlatformBackend.PREFS, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun aPickIsKept_andFollowingThePhoneClearsIt() {
        backend.apply("es")
        assertEquals("es", backend.chosen())
        if (Build.VERSION.SDK_INT >= 33) {
            assertEquals("es", context.getSystemService(LocaleManager::class.java).applicationLocales[0].toLanguageTag())
        }
        backend.apply(null)
        assertNull(backend.chosen())
    }

    @Test
    fun wrap_appliesThePickBelow13_andLeavesTheContextAloneOtherwise() {
        assertSame(context, KLocale.wrap(context))
        backend.apply("fr")
        val wrapped = KLocale.wrap(context)
        if (Build.VERSION.SDK_INT < 33) {
            assertEquals("fr", wrapped.resources.configuration.primaryLocale().language)
            // Compose Multiplatform resources and formatting read the JVM default.
            assertEquals("fr", java.util.Locale.getDefault().language)
            backend.apply(null)
            KLocale.wrap(context)
            assertEquals(android.content.res.Resources.getSystem().configuration.primaryLocale().language, java.util.Locale.getDefault().language)
        } else {
            assertSame(context, wrapped)
        }
    }

    @Test
    fun belowThirteen_aPickStoredByAppCompatIsMigratedOnce() {
        if (Build.VERSION.SDK_INT >= 33) return
        val file = File(context.filesDir, "androidx.appcompat.app.AppCompatDelegate.application_locales_record_file")
        file.writeText("""<?xml version='1.0' encoding='utf-8' standalone='yes' ?><locales application_locales="it" />""")
        try {
            assertEquals("it", PlatformBackend.storedTag(context))
        } finally {
            file.delete()
        }
    }

    @Test
    fun languageNames_inTheirOwnLanguage() {
        assertEquals("Español", languageName("es"))
        assertEquals("Português (Brasil)", languageName("pt-BR"))
        assertEquals("Türkçe", languageName("tr"))
    }

    @Test
    fun aPickMadeBelow13_survivesThePhonesUpdateTo13() {
        if (Build.VERSION.SDK_INT < 33) return
        // As left by Android 12: our stored tag, nothing in the system yet.
        context.getSharedPreferences(PlatformBackend.PREFS, Context.MODE_PRIVATE).edit().putString(PlatformBackend.KEY, "it").commit()
        assertEquals("it", backend.chosen())
        assertEquals("it", context.getSystemService(LocaleManager::class.java).applicationLocales[0].toLanguageTag())
        assertNull(context.getSharedPreferences(PlatformBackend.PREFS, Context.MODE_PRIVATE).getString(PlatformBackend.KEY, null))
    }
}
