/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.proxy

sealed interface ProxySettingsEvents {
    data object Save : ProxySettingsEvents
    data object ResetToSystem : ProxySettingsEvents
    data class SetEnabled(val enabled: Boolean) : ProxySettingsEvents
    data class SetHost(val host: String) : ProxySettingsEvents
    data class SetPort(val port: String) : ProxySettingsEvents
    data class SetUsername(val username: String) : ProxySettingsEvents
    data class SetPassword(val password: String) : ProxySettingsEvents
}
