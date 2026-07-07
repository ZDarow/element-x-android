package io.element.android.features.bot.impl.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.features.bot.impl.BotSettingsScreen

@Composable
fun BotSettingsView(
    state: BotSettingsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BotSettingsScreen(
        isEnabled = state.isEnabled,
        commands = state.commands,
        onToggle = { state.eventSink(BotSettingsEvent.SetEnabled(it)) },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@PreviewsDayNight
@Composable
internal fun BotSettingsViewPreview(@PreviewParameter(BotSettingsStateProvider::class) state: BotSettingsState) =
    ElementPreview {
        BotSettingsView(
            state = state,
            onBackClick = {},
        )
    }
