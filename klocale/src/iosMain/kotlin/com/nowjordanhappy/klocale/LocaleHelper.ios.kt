package com.nowjordanhappy.klocale

import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleIdentifier
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

/** iOS: `AppleLanguages` in the app's defaults (applied on the next launch), plus our own record of the pick. */
internal class IosBackend : LocaleBackend {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun systemLanguage(): String =
        (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"

    override fun chosen(): String? = defaults.stringForKey(KEY)

    override fun apply(tag: String?) {
        if (tag == null) {
            defaults.removeObjectForKey(KEY)
            defaults.removeObjectForKey(APPLE_LANGUAGES)
        } else {
            defaults.setObject(tag, forKey = KEY)
            defaults.setObject(listOf(tag), forKey = APPLE_LANGUAGES)
        }
    }

    private companion object {
        const val KEY = "klocale.chosen"
        const val APPLE_LANGUAGES = "AppleLanguages"
    }
}

actual fun defaultBackend(): LocaleBackend = IosBackend()

actual fun languageName(tag: String): String {
    val name = NSLocale(localeIdentifier = tag).displayNameForKey(NSLocaleIdentifier, value = tag) ?: tag
    return name.replaceFirstChar { it.uppercaseChar() }
}
