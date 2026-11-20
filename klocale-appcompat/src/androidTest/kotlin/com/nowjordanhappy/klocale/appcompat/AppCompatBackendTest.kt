package com.nowjordanhappy.klocale.appcompat

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppCompatBackendTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val backend = AppCompatBackend(instrumentation.targetContext)

    @After
    fun reset() = instrumentation.runOnMainSync { backend.apply(null) }

    @Test
    fun aPickIsKept_andFollowingThePhoneClearsIt() {
        instrumentation.runOnMainSync { backend.apply("es") }
        // From 13 through the system (works with no AppCompatActivity: 1.x's set() did nothing here).
        if (Build.VERSION.SDK_INT >= 33) {
            val system = instrumentation.targetContext.getSystemService(android.app.LocaleManager::class.java).applicationLocales
            assertEquals("es", system[0].toLanguageTag())
        } else {
            assertEquals("es", backend.chosen())
        }
        instrumentation.runOnMainSync { backend.apply(null) }
        if (Build.VERSION.SDK_INT < 33) assertNull(backend.chosen())
        if (Build.VERSION.SDK_INT < 33) instrumentation.runOnMainSync { AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList()) }
    }
}
