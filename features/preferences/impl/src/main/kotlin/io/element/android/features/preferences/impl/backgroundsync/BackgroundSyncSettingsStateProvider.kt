/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.backgroundsync

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.libraries.preferences.api.store.BackgroundSyncMode

open class BackgroundSyncSettingsStateProvider : PreviewParameterProvider<BackgroundSyncSettingsState> {
    override val values: Sequence<BackgroundSyncSettingsState>
        get() = sequenceOf(
            aBackgroundSyncSettingsState(),
            aBackgroundSyncSettingsState(currentMode = BackgroundSyncMode.WifiOnly),
            aBackgroundSyncSettingsState(currentMode = BackgroundSyncMode.Always),
        )
}

fun aBackgroundSyncSettingsState(
    currentMode: BackgroundSyncMode = BackgroundSyncMode.Disabled,
    isSaving: Boolean = false,
    eventSink: (BackgroundSyncSettingsEvents) -> Unit = {},
) = BackgroundSyncSettingsState(
    currentMode = currentMode,
    isSaving = isSaving,
    eventSink = eventSink,
)
