/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.libraries.preferences.api.store

/**
 * Режимы фоновой синхронизации.
 *
 * @property Disabled Останавливать синхронизацию при уходе в фон (поведение по умолчанию).
 * @property WifiOnly Продолжать синхронизацию в фоне только при подключении к WiFi.
 * @property Always Продолжать синхронизацию в фоне всегда.
 */
enum class BackgroundSyncMode(val key: String) {
    Disabled("disabled"),
    WifiOnly("wifi_only"),
    Always("always"),
}
