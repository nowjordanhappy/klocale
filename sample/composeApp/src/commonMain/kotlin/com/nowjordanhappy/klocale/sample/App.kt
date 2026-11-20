package com.nowjordanhappy.klocale.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nowjordanhappy.klocale.KLocale
import com.nowjordanhappy.klocale.languageName
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.TextButton
import klocale.sample.composeapp.generated.resources.Res
import klocale.sample.composeapp.generated.resources.change_language
import klocale.sample.composeapp.generated.resources.hello
import org.jetbrains.compose.resources.stringResource

@Composable
fun App(klocale: KLocale, languages: List<String>, onOpenSettings: () -> Boolean) {
    val lang by klocale.current.collectAsState()
    val chosen by klocale.chosen.collectAsState()
    // The system screen where there is one (Android 13+); this list everywhere else.
    var showList by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(Res.string.hello), style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(lang, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(32.dp))
            Button(onClick = { if (!onOpenSettings()) showList = true }) {
                Text(stringResource(Res.string.change_language))
            }
            if (showList) {
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = { klocale.followSystem() }) {
                    Text((if (chosen == null) "● " else "") + "System")
                }
                languages.forEach { code ->
                    TextButton(onClick = { klocale.set(code) }) {
                        Text((if (chosen == code) "● " else "") + languageName(code))
                    }
                }
            }
        }
    }
}
