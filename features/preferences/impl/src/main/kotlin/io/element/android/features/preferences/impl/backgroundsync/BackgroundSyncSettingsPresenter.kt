/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.backgroundsync

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.Inject
import io.element.android.libraries.architecture.Presenter
import io.element.android.libraries.preferences.api.store.AppPreferencesStore
import io.element.android.libraries.preferences.api.store.BackgroundSyncMode
import kotlinx.coroutines.launch

@Inject
class BackgroundSyncSettingsPresenter(
    private val appPreferencesStore: AppPreferencesStore,
) : Presenter<BackgroundSyncSettingsState> {
    @Composable
    override fun present(): BackgroundSyncSettingsState {
        val coroutineScope = rememberCoroutineScope()
        val currentMode by appPreferencesStore.getBackgroundSyncModeFlow()
            .collectAsState(initial = BackgroundSyncMode.Disabled)
        var isSaving by remember { mutableStateOf(false) }

        fun handleEvent(event: BackgroundSyncSettingsEvents) {
            when (event) {
                is BackgroundSyncSettingsEvents.Save -> {
                    isSaving = true
                    coroutineScope.launch {
                        appPreferencesStore.setBackgroundSyncMode(currentMode)
                        isSaving = false
                    }
                }
                is BackgroundSyncSettingsEvents.SelectMode -> {
                    // Меняем режим сразу, автосохранение
                    isSaving = true
                    coroutineScope.launch {
                        appPreferencesStore.setBackgroundSyncMode(event.mode)
                        isSaving = false
                    }
                }
            }
        }

        return BackgroundSyncSettingsState(
            currentMode = currentMode,
            isSaving = isSaving,
            eventSink = ::handleEvent,
        )
    }
}
