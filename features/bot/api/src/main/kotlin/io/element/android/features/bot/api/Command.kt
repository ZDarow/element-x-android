package io.element.android.features.bot.api

/**
 * Model representing a bot command.
 */
data class Command(
    val name: String,
    val description: String,
    val handler: suspend (args: List<String>) -> CommandResult
)

/**
 * Result of executing a command.
 */
sealed class CommandResult {
    data class Success(val message: String) : CommandResult()
    data class Error(val message: String) : CommandResult()
    object NotFound : CommandResult()
}
