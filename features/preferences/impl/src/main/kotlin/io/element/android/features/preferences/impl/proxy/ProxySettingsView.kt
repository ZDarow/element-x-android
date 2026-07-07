/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.preferences.impl.proxy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.compound.theme.ElementTheme
import io.element.android.features.preferences.impl.R
import io.element.android.libraries.designsystem.components.preferences.PreferencePage
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.Button
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.theme.components.TextField

@Composable
fun ProxySettingsView(
    state: ProxySettingsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferencePage(
        modifier = modifier,
        onBackClick = onBackClick,
        title = stringResource(id = R.string.screen_proxy_title),
    ) {
        Text(
            text = stringResource(id = R.string.screen_proxy_description),
            style = ElementTheme.typography.fontBodyMdRegular,
            color = ElementTheme.colors.textSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.proxyConfig.host,
            onValueChange = { state.eventSink(ProxySettingsEvents.SetHost(it)) },
            label = stringResource(id = R.string.screen_proxy_host),
            placeholder = "proxy.example.com",
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.proxyConfig.port.toString(),
            onValueChange = { state.eventSink(ProxySettingsEvents.SetPort(it)) },
            label = stringResource(id = R.string.screen_proxy_port),
            placeholder = "8080",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.proxyConfig.username.orEmpty(),
            onValueChange = { state.eventSink(ProxySettingsEvents.SetUsername(it)) },
            label = stringResource(id = R.string.screen_proxy_username),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = state.proxyConfig.password.orEmpty(),
            onValueChange = { state.eventSink(ProxySettingsEvents.SetPassword(it)) },
            label = stringResource(id = R.string.screen_proxy_password),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = stringResource(id = R.string.screen_proxy_save),
            onClick = { state.eventSink(ProxySettingsEvents.Save) },
            enabled = !state.isSaving && state.proxyConfig.host.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            text = stringResource(id = R.string.screen_proxy_reset),
            onClick = { state.eventSink(ProxySettingsEvents.ResetToSystem) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        if (state.isSaving) {
            Text(
                text = stringResource(id = R.string.screen_proxy_saving),
                style = ElementTheme.typography.fontBodySmRegular,
                color = ElementTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@PreviewsDayNight
@Composable
internal fun ProxySettingsViewPreview(
    @PreviewParameter(ProxySettingsStateProvider::class) state: ProxySettingsState
) = ElementPreview {
    ProxySettingsView(
        state = state,
        onBackClick = {},
    )
}
