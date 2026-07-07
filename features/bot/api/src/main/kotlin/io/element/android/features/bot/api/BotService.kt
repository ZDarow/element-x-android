package io.element.android.features.bot.api

import kotlinx.coroutines.flow.StateFlow

/**
 * Bot auto-reply service.
 */
interface BotService {
    /** Whether the bot is enabled */
    val isEnabled: StateFlow<Boolean>

    /** Enable or disable the bot */
    fun setEnabled(enabled: Boolean)

    /** Process an incoming message */
    suspend fun processMessage(
        roomId: String,
        senderId: String,
        message: String
    ): String?

    /** Get the list of available commands */
    fun getAvailableCommands(): List<Command>
}
