package io.element.android.features.bot.impl.settings

import io.element.android.features.bot.api.Command
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class BotSettingsState(
    val isEnabled: Boolean = false,
    val commands: ImmutableList<Command> = persistentListOf(),
    val eventSink: (BotSettingsEvent) -> Unit = {},
)
