package com.nowjordanhappy.klocale.sample

import androidx.compose.ui.window.ComposeUIViewController
import com.nowjordanhappy.klocale.KLocale
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

// ponytail: top-level val — no DI needed in sample
private val klocale = KLocale(
    supported = listOf("es", "en", "pt", "fr", "it"),
    fallback = "en"
)

fun MainViewController() = ComposeUIViewController {
    App(klocale, onOpenSettings = {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let { url ->
            UIApplication.sharedApplication.openURL(
                url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = null
            )
        }
    })
}
