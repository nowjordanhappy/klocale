package com.nowjordanhappy.klocale

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KLocale(
    private val supported: List<String>,
    private val fallback: String = "en",
    private val onChanged: ((String) -> Unit)? = null,
) {
    private val _current = MutableStateFlow(
        mapToSupported(getSystemLanguage(), supported, fallback)
    )
    val current: StateFlow<String> = _current.asStateFlow()

    fun getSystemDefault(): String =
        mapToSupported(getSystemLanguage(), supported, fallback)

    fun set(code: String) {
        _current.value = code
        syncToSystem(code)
        onChanged?.invoke(code)
    }
}
