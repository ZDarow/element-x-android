package io.element.android.features.bot.impl

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.BotService
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.services.toolbox.api.strings.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@ContributesBinding(AppScope::class)
@Inject
class BotServiceImpl(
    private val commandProcessor: CommandProcessor,
    private val stringProvider: StringProvider,
) : BotService {

    private val _isEnabled = MutableStateFlow(false)
    override val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()

    private val botUserId = "@mx_bot:localhost"

    override fun setEnabled(enabled: Boolean) {
        _isEnabled.value = enabled
    }

    override suspend fun processMessage(
        roomId: String,
        senderId: String,
        message: String
    ): String? {
        if (!_isEnabled.value) return null
        if (senderId == botUserId) return null
        if (!message.trim().startsWith("/")) return null

        val result = commandProcessor.process(message) ?: return null

        return when (result) {
            is CommandResult.Success -> result.message
            is CommandResult.Error -> result.message
            is CommandResult.NotFound -> stringProvider.getString(R.string.bot_command_not_found)
        }
    }

    override fun getAvailableCommands(): List<Command> = commandProcessor.getCommands()
}
