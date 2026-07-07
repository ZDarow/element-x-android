/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.proxy

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.libraries.preferences.api.store.ProxyConfig

open class ProxySettingsStateProvider : PreviewParameterProvider<ProxySettingsState> {
    override val values: Sequence<ProxySettingsState>
        get() = sequenceOf(
            aProxySettingsState(),
            aProxySettingsState(
                proxyConfig = ProxyConfig(
                    enabled = true,
                    host = "proxy.example.com",
                    port = 8080,
                    username = "user",
                    password = "pass",
                ),
            ),
        )
}

fun aProxySettingsState(
    proxyConfig: ProxyConfig = ProxyConfig(),
    isSaving: Boolean = false,
    eventSink: (ProxySettingsEvents) -> Unit = {},
) = ProxySettingsState(
    proxyConfig = proxyConfig,
    isSaving = isSaving,
    eventSink = eventSink,
)
