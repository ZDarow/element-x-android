package io.element.android.features.bot.impl

import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.services.toolbox.api.strings.StringProvider

@Inject
class CommandProcessor(
    private val defaultCommands: DefaultCommands,
    private val stringProvider: StringProvider,
) {

    private val commands: Map<String, Command> by lazy {
        defaultCommands.getAll().associateBy { it.name.lowercase() }
    }

    suspend fun process(input: String): CommandResult? {
        val trimmed = input.trim()
        if (!trimmed.startsWith("/")) return null

        val parts = trimmed.removePrefix("/").split("\\s+".toRegex())
        if (parts.isEmpty()) return null

        val commandName = parts[0].lowercase()
        val args = parts.drop(1)

        if (commandName == "help") {
            return commands["help"]?.handler?.invoke(args) ?: CommandResult.NotFound
        }

        val command = commands[commandName]
        return when {
            command == null -> CommandResult.Error(
                stringProvider.getString(R.string.bot_unknown_command, commandName)
            )
            else -> command.handler.invoke(args)
        }
    }

    fun getCommands(): List<Command> = commands.values.toList()
}
