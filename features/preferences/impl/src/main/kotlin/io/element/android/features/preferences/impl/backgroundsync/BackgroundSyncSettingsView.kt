/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.backgroundsync

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.compound.theme.ElementTheme
import io.element.android.compound.tokens.generated.CompoundIcons
import io.element.android.features.preferences.impl.R
import io.element.android.libraries.designsystem.components.preferences.PreferencePage
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.components.list.ListItemContent
import io.element.android.libraries.designsystem.theme.components.ListItem
import io.element.android.libraries.designsystem.theme.components.ListItemStyle
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.preferences.api.store.BackgroundSyncMode

@Composable
fun BackgroundSyncSettingsView(
    state: BackgroundSyncSettingsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferencePage(
        modifier = modifier,
        onBackClick = onBackClick,
        title = stringResource(id = R.string.screen_background_sync_title),
    ) {
        Text(
            text = stringResource(id = R.string.screen_background_sync_description),
            style = ElementTheme.typography.fontBodyMdRegular,
            color = ElementTheme.colors.textSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        BackgroundSyncMode.entries.forEach { mode ->
            val isSelected = state.currentMode == mode
            RadioListItem(
                title = when (mode) {
                    BackgroundSyncMode.Disabled -> stringResource(id = R.string.screen_background_sync_disabled)
                    BackgroundSyncMode.WifiOnly -> stringResource(id = R.string.screen_background_sync_wifi)
                    BackgroundSyncMode.Always -> stringResource(id = R.string.screen_background_sync_always)
                },
                subtitle = when (mode) {
                    BackgroundSyncMode.Disabled -> stringResource(id = R.string.screen_background_sync_disabled_description)
                    BackgroundSyncMode.WifiOnly -> stringResource(id = R.string.screen_background_sync_wifi_description)
                    BackgroundSyncMode.Always -> stringResource(id = R.string.screen_background_sync_always_description)
                },
                isSelected = isSelected,
                onClick = { state.eventSink(BackgroundSyncSettingsEvents.SelectMode(mode)) },
            )
        }
    }
}

@Composable
private fun RadioListItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(
                text = title,
                style = ElementTheme.typography.fontBodyMdRegular,
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = ElementTheme.typography.fontBodySmRegular,
                color = ElementTheme.colors.textSecondary,
            )
        },
        trailingContent = ListItemContent.RadioButton(
            selected = isSelected,
        ),
    )
}

@PreviewsDayNight
@Composable
internal fun BackgroundSyncSettingsViewPreview(
    @PreviewParameter(BackgroundSyncSettingsStateProvider::class) state: BackgroundSyncSettingsState
) = ElementPreview {
    BackgroundSyncSettingsView(
        state = state,
        onBackClick = {},
    )
}
