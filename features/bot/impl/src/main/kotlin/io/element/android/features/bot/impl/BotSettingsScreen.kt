package io.element.android.features.bot.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.element.android.features.bot.api.Command
import io.element.android.libraries.designsystem.components.preferences.PreferenceDivider
import io.element.android.libraries.designsystem.components.preferences.PreferencePage
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BotSettingsScreen(
    isEnabled: Boolean,
    commands: ImmutableList<Command>,
    onToggle: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferencePage(
        title = stringResource(id = R.string.screen_bot_settings_title),
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        BotEnabledSection(
            isEnabled = isEnabled,
            onToggle = onToggle,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                id = R.string.screen_bot_available_commands,
                commands.size,
            ),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        commands.forEach { command ->
            CommandItem(commandName = command.name, commandDescription = command.description)
        }
    }
}

@Composable
private fun BotEnabledSection(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.screen_bot_auto_reply_bot),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = if (isEnabled) {
                    stringResource(id = R.string.screen_bot_enabled)
                } else {
                    stringResource(id = R.string.screen_bot_disabled)
                },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
        )
    }
}

@Composable
private fun CommandItem(
    commandName: String,
    commandDescription: String,
) {
    PreferenceDivider()
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "/$commandName",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = commandDescription,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
