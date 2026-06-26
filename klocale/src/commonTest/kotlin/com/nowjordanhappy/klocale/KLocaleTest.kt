package com.nowjordanhappy.klocale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KLocaleTest {
    private val supported = listOf("es", "en", "pt", "fr", "it")

    @Test
    fun set_updatesCurrentValue() {
        val klocale = KLocale(supported, fallback = "en")
        klocale.set("es")
        assertEquals("es", klocale.current.value)
    }

    @Test
    fun set_multipleTimes_lastWins() {
        val klocale = KLocale(supported, fallback = "en")
        klocale.set("es")
        klocale.set("fr")
        assertEquals("fr", klocale.current.value)
    }

    @Test
    fun set_invokesOnChanged() {
        var received: String? = null
        val klocale = KLocale(supported, fallback = "en", onChanged = { received = it })
        klocale.set("pt")
        assertEquals("pt", received)
    }

    @Test
    fun set_invokesOnChanged_eachCall() {
        val calls = mutableListOf<String>()
        val klocale = KLocale(supported, fallback = "en", onChanged = { calls += it })
        klocale.set("es")
        klocale.set("pt")
        assertEquals(listOf("es", "pt"), calls)
    }

    @Test
    fun set_nullOnChanged_doesNotCrash() {
        val klocale = KLocale(supported, fallback = "en", onChanged = null)
        klocale.set("es")
        assertEquals("es", klocale.current.value)
    }

    @Test
    fun initialValue_isInSupportedOrFallback() {
        val klocale = KLocale(supported, fallback = "en")
        // mapToSupported always returns a value from supported or the fallback.
        // "en" (fallback) is also in supported, so this covers both cases.
        assertTrue(klocale.current.value in supported)
    }

    @Test
    fun getSystemDefault_isInSupported() {
        val klocale = KLocale(supported, fallback = "en")
        assertTrue(klocale.getSystemDefault() in supported)
    }

    @Test
    fun getSystemDefault_matchesInitialCurrentValue() {
        val klocale = KLocale(supported, fallback = "en")
        assertEquals(klocale.getSystemDefault(), klocale.current.value)
    }
}
