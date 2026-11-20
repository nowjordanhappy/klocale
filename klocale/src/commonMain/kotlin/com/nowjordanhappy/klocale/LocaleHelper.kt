package com.nowjordanhappy.klocale

/**
 * [raw] to one of [supported]: the exact tag first ("pt-BR" → "pt-BR"), then the language alone
 * ("fr-CA" → "fr", "pt" → "pt-BR"), else [fallback].
 */
internal fun mapToSupported(raw: String, supported: List<String>, fallback: String): String {
    if (raw.isEmpty()) return fallback
    supported.firstOrNull { it.equals(raw, ignoreCase = true) }?.let { return it }
    val lang = raw.split("-").first().lowercase()
    return supported.firstOrNull { it.split("-").first().lowercase() == lang } ?: fallback
}
