package com.nowjordanhappy.klocale

import platform.Foundation.NSUserDefaults
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocaleHelperIosTest {
    private val backend = IosBackend()

    @AfterTest
    fun reset() = backend.apply(null)

    @Test
    fun systemLanguage_isNonEmpty() {
        assertTrue(backend.systemLanguage().isNotEmpty())
    }

    @Test
    fun apply_writesAppleLanguages_andIsChosen() {
        backend.apply("es")
        val stored = NSUserDefaults.standardUserDefaults.objectForKey("AppleLanguages") as? List<*>
        assertEquals("es", stored?.firstOrNull())
        assertEquals("es", backend.chosen())
    }

    @Test
    fun apply_null_followsThePhoneAgain() {
        backend.apply("fr")
        backend.apply(null)
        assertNull(backend.chosen())
    }

    @Test
    fun languageName_inItsOwnLanguage() {
        assertEquals("Español", languageName("es"))
    }
}
