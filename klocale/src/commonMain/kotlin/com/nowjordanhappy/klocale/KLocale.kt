package com.nowjordanhappy.klocale

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The app's own language, kept in step with the platform's.
 *
 * On Android the default [backend] is the platform one: the system's per-app language on 13+, and below 13 a
 * stored choice applied by [KLocale.Companion.wrap] (call it from `attachBaseContext`) — it works with a plain
 * `ComponentActivity`. Apps on `AppCompatActivity` can use the `klocale-appcompat` artifact instead.
 */
class KLocale(
    private val supported: List<String>,
    private val fallback: String = "en",
    private val onChanged: ((String) -> Unit)? = null,
    private val backend: LocaleBackend = defaultBackend(),
) {
    private val _chosen = MutableStateFlow(backend.chosen())

    /** The language picked in the app, or null while it follows the phone. */
    val chosen: StateFlow<String?> = _chosen.asStateFlow()

    private val _current = MutableStateFlow(resolve(_chosen.value))

    /** The language in effect: the one picked, or the phone's mapped to [supported]. */
    val current: StateFlow<String> = _current.asStateFlow()

    /** The phone's language mapped to [supported] (what [followSystem] goes back to). */
    fun getSystemDefault(): String = mapToSupported(backend.systemLanguage(), supported, fallback)

    fun set(code: String) {
        _chosen.value = code
        _current.value = code
        backend.apply(code)
        onChanged?.invoke(code)
    }

    /** Stops overriding: the app follows the phone's language again. */
    fun followSystem() {
        _chosen.value = null
        backend.apply(null)
        val now = getSystemDefault()
        _current.value = now
        onChanged?.invoke(now)
    }

    private fun resolve(chosen: String?): String =
        chosen?.let { mapToSupported(it, supported, fallback) } ?: getSystemDefault()

    /** Platform helpers hang off here (Android: `wrap`, `localizedContext`). */
    companion object
}

/** Where the app's language is kept on a platform. */
interface LocaleBackend {
    /** The phone's own language (a BCP 47 tag), whatever the app picked. */
    fun systemLanguage(): String

    /** The tag picked in the app, or null while following the phone. */
    fun chosen(): String?

    /** Picks [tag]; null goes back to following the phone. */
    fun apply(tag: String?)
}

/** The platform's default [LocaleBackend]. */
expect fun defaultBackend(): LocaleBackend

/** [tag]'s name in its own language, capitalised: "es" → "Español", "pt-BR" → "Português (Brasil)". */
expect fun languageName(tag: String): String
