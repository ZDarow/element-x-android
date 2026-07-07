/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.proxy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.Inject
import io.element.android.libraries.architecture.Presenter
import io.element.android.libraries.preferences.api.store.AppPreferencesStore
import io.element.android.libraries.preferences.api.store.ProxyConfig
import kotlinx.coroutines.launch

@Inject
class ProxySettingsPresenter(
    private val appPreferencesStore: AppPreferencesStore,
) : Presenter<ProxySettingsState> {
    @Composable
    override fun present(): ProxySettingsState {
        val coroutineScope = rememberCoroutineScope()
        var proxyConfig by remember { mutableStateOf(ProxyConfig()) }
        var isSaving by remember { mutableStateOf(false) }

        // Загружаем сохранённую конфигурацию при первом рендере
        androidx.compose.runtime.LaunchedEffect(Unit) {
            proxyConfig = appPreferencesStore.getProxyConfig()
        }

        fun handleEvent(event: ProxySettingsEvents) {
            when (event) {
                is ProxySettingsEvents.Save -> {
                    isSaving = true
                    coroutineScope.launch {
                        appPreferencesStore.setProxyConfig(proxyConfig)
                        isSaving = false
                    }
                }
                is ProxySettingsEvents.ResetToSystem -> {
                    coroutineScope.launch {
                        appPreferencesStore.setProxyConfig(ProxyConfig(enabled = false))
                        proxyConfig = ProxyConfig()
                    }
                }
                is ProxySettingsEvents.SetEnabled -> {
                    proxyConfig = proxyConfig.copy(enabled = event.enabled)
                }
                is ProxySettingsEvents.SetHost -> {
                    proxyConfig = proxyConfig.copy(host = event.host)
                }
                is ProxySettingsEvents.SetPort -> {
                    val port = event.port.toIntOrNull() ?: 8080
                    proxyConfig = proxyConfig.copy(port = port)
                }
                is ProxySettingsEvents.SetUsername -> {
                    proxyConfig = proxyConfig.copy(username = event.username.ifBlank { null })
                }
                is ProxySettingsEvents.SetPassword -> {
                    proxyConfig = proxyConfig.copy(password = event.password.ifBlank { null })
                }
            }
        }

        return ProxySettingsState(
            proxyConfig = proxyConfig,
            isSaving = isSaving,
            eventSink = ::handleEvent,
        )
    }
}
