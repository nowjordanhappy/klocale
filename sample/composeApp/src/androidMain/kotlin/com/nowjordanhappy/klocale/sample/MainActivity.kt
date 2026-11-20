package com.nowjordanhappy.klocale.sample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nowjordanhappy.klocale.KLocale
import com.nowjordanhappy.klocale.openSystemScreen
import com.nowjordanhappy.klocale.wrap

/** A plain ComponentActivity: klocale's platform mode needs only the attachBaseContext override. */
class MainActivity : ComponentActivity() {
    private val languages = listOf("es", "en", "pt", "fr", "it")
    private val klocale = KLocale(supported = languages, fallback = "en")

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(KLocale.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Below 13 there is no per-app system screen: App() shows its own list when this returns false.
        setContent { App(klocale, languages, onOpenSettings = { klocale.openSystemScreen(this) }) }
    }
}
