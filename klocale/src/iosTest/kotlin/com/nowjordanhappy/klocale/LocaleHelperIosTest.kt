package com.nowjordanhappy.klocale

import platform.Foundation.NSUserDefaults
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LocaleHelperIosTest {

    @Test
    fun getSystemLanguage_returnsNonEmpty() {
        assertTrue(getSystemLanguage().isNotEmpty())
    }

    @Test
    fun getSystemLanguage_returnsValidTag() {
        assertTrue(getSystemLanguage().length >= 2)
    }

    @Test
    fun syncToSystem_writesToNSUserDefaults() {
        syncToSystem("es")
        val stored = NSUserDefaults.standardUserDefaults.objectForKey("AppleLanguages") as? List<*>
        assertNotNull(stored)
        assertEquals("es", stored.firstOrNull())
    }

    @Test
    fun syncToSystem_overwritesPreviousValue() {
        syncToSystem("es")
        syncToSystem("fr")
        val stored = NSUserDefaults.standardUserDefaults.objectForKey("AppleLanguages") as? List<*>
        assertEquals("fr", stored?.firstOrNull())
    }
}
