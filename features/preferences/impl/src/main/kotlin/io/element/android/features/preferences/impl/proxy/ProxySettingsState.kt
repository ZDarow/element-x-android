/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.proxy

import io.element.android.libraries.preferences.api.store.ProxyConfig

data class ProxySettingsState(
    val proxyConfig: ProxyConfig = ProxyConfig(),
    val isSaving: Boolean = false,
    val eventSink: (ProxySettingsEvents) -> Unit = {},
)
