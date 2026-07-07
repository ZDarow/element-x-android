package io.element.android.features.bot.impl.settings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import kotlinx.collections.immutable.persistentListOf

class BotSettingsStateProvider : PreviewParameterProvider<BotSettingsState> {
    override val values: Sequence<BotSettingsState> = sequenceOf(
        BotSettingsState(
            isEnabled = true,
            commands = persistentListOf(
                Command("ping", "Check if bot is running") { CommandResult.Success("pong") },
                Command("help", "List all commands") { CommandResult.Success("help") },
            ),
        ),
        BotSettingsState(
            isEnabled = false,
            commands = persistentListOf(),
        ),
    )
}
