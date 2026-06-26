package com.nowjordanhappy.klocale

import kotlin.test.Test
import kotlin.test.assertEquals

class MapToSupportedTest {
    private val supported = listOf("es", "en", "pt", "fr", "it")

    @Test
    fun exactMatch() = assertEquals("es", mapToSupported("es", supported, "en"))

    @Test
    fun regionStripped() = assertEquals("fr", mapToSupported("fr-CA", supported, "en"))

    @Test
    fun uppercaseInput() = assertEquals("es", mapToSupported("ES", supported, "en"))

    @Test
    fun fallbackWhenNotSupported() = assertEquals("en", mapToSupported("zh", supported, "en"))

    @Test
    fun regionAndUppercase() = assertEquals("en", mapToSupported("EN-US", supported, "en"))

    @Test
    fun fullTagMatch() = assertEquals("pt", mapToSupported("pt-BR", supported, "en"))

    @Test
    fun emptyInput() = assertEquals("en", mapToSupported("", supported, "en"))

    @Test
    fun emptySupportedList() = assertEquals("en", mapToSupported("es", emptyList(), "en"))

    @Test
    fun multipleHyphens() = assertEquals("en", mapToSupported("zh-Hans-CN", supported, "en"))

    @Test
    fun multipleHyphensMatchFound() = assertEquals("pt", mapToSupported("pt-Latn-BR", supported, "en"))

    @Test
    fun uppercaseSupportedList() = assertEquals("ES", mapToSupported("es", listOf("ES", "EN"), "EN"))
}
