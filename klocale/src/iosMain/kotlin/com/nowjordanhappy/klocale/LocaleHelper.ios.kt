package com.nowjordanhappy.klocale

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual fun getSystemLanguage(): String =
    (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"

actual fun syncToSystem(code: String) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(code), forKey = "AppleLanguages")
}
