package io.element.android.features.bot.impl.settings

sealed interface BotSettingsEvent {
    data object OnBackClick : BotSettingsEvent
    data class SetEnabled(val enabled: Boolean) : BotSettingsEvent
}
