package com.nowjordanhappy.klocale.sample

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nowjordanhappy.klocale.KLocale

class MainActivity : ComponentActivity() {
    private val klocale = KLocale(
        supported = listOf("es", "en", "pt", "fr", "it"),
        fallback = "en"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App(klocale, onOpenSettings = ::openLanguageSettings) }
    }

    private fun openLanguageSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startActivity(
                Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            )
        } else {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}
