package com.nowjordanhappy.klocale

expect fun getSystemLanguage(): String
expect fun syncToSystem(code: String)

internal fun mapToSupported(raw: String, supported: List<String>, fallback: String): String {
    val lang = raw.split("-").first().lowercase()
    return supported.firstOrNull { it.lowercase() == lang } ?: fallback
}
