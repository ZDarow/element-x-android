package io.element.android.features.bot.impl

import android.os.Build
import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.services.toolbox.api.strings.StringProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Built-in bot commands with localized strings.
 */
@Inject
class DefaultCommands(
    private val stringProvider: StringProvider,
) {

    private val startTime = System.currentTimeMillis()

    fun getAll(): List<Command> = listOf(
        helpCommand(),
        pingCommand(),
        timeCommand(),
        echoCommand(),
        statusCommand(),
        calcCommand(),
        quoteCommand(),
        uptimeCommand(),
        aboutCommand(),
    )

    private fun helpCommand() = Command(
        name = "help",
        description = stringProvider.getString(R.string.bot_help_description),
    ) { _ ->
        val commands = getAll()
        val helpText = buildString {
            appendLine(stringProvider.getString(R.string.bot_help_title))
            appendLine()
            commands.forEach { cmd ->
                appendLine("/${cmd.name} -- ${cmd.description}")
            }
        }
        CommandResult.Success(helpText)
    }

    private fun pingCommand() = Command(
        name = "ping",
        description = stringProvider.getString(R.string.bot_ping_description),
    ) { _ ->
        CommandResult.Success(stringProvider.getString(R.string.bot_ping_response))
    }

    private fun timeCommand() = Command(
        name = "time",
        description = stringProvider.getString(R.string.bot_time_description),
    ) { _ ->
        val formatter = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())
        CommandResult.Success(
            stringProvider.getString(R.string.bot_time_response, formatter.format(Date()))
        )
    }

    private fun echoCommand() = Command(
        name = "echo",
        description = stringProvider.getString(R.string.bot_echo_description),
    ) { args ->
        if (args.isEmpty()) {
            CommandResult.Error(stringProvider.getString(R.string.bot_echo_usage))
        } else {
            CommandResult.Success(args.joinToString(" "))
        }
    }

    private fun statusCommand() = Command(
        name = "status",
        description = stringProvider.getString(R.string.bot_status_description),
    ) { _ ->
        val runtime = Runtime.getRuntime()
        val maxMem = runtime.maxMemory() / 1024 / 1024
        val totalMem = runtime.totalMemory() / 1024 / 1024
        val freeMem = runtime.freeMemory() / 1024 / 1024
        val usedMem = totalMem - freeMem

        val status = buildString {
            appendLine(stringProvider.getString(R.string.bot_status_title))
            appendLine(
                stringProvider.getString(
                    R.string.bot_status_model,
                    Build.MANUFACTURER,
                    Build.MODEL,
                )
            )
            appendLine(
                stringProvider.getString(
                    R.string.bot_status_android,
                    Build.VERSION.RELEASE,
                    Build.VERSION.SDK_INT,
                )
            )
            appendLine(
                stringProvider.getString(
                    R.string.bot_status_memory,
                    usedMem,
                    maxMem,
                )
            )
            appendLine(
                stringProvider.getString(
                    R.string.bot_status_processors,
                    runtime.availableProcessors(),
                )
            )
        }
        CommandResult.Success(status)
    }

    private fun calcCommand() = Command(
        name = "calc",
        description = stringProvider.getString(R.string.bot_calc_description),
    ) { args ->
        if (args.isEmpty()) {
            CommandResult.Error(stringProvider.getString(R.string.bot_calc_usage))
        } else {
            try {
                val expr = args.joinToString("")
                val result = evaluateExpression(expr)
                CommandResult.Success("$expr = $result")
            } catch (e: Exception) {
                CommandResult.Error(
                    stringProvider.getString(R.string.bot_calc_error, e.message ?: "unknown")
                )
            }
        }
    }

    private fun quoteCommand() = Command(
        name = "quote",
        description = stringProvider.getString(R.string.bot_quote_description),
    ) { _ ->
        val quotes = listOf(
            "Programming is the art of telling a computer what you want to do. -- Donald Knuth",
            "First solve the problem, then write the code. -- John Johnson",
            "The best code is the code that doesn't need to be written. -- Jeff Atwood",
            "Simplicity is the ultimate sophistication. -- Leonardo da Vinci",
            "Talk is cheap. Show me the code. -- Linus Torvalds",
            "Any sufficiently advanced technology is indistinguishable from magic. -- Arthur C. Clarke",
            "Debugging is twice as hard as writing the code in the first place. -- Brian Kernighan",
            "First make it work, then make it fast. -- Unknown",
        )
        CommandResult.Success(quotes.random())
    }

    private fun uptimeCommand() = Command(
        name = "uptime",
        description = stringProvider.getString(R.string.bot_uptime_description),
    ) { _ ->
        val uptime = System.currentTimeMillis() - startTime
        val hours = uptime / (1000 * 60 * 60)
        val minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (uptime % (1000 * 60)) / 1000
        CommandResult.Success(
            stringProvider.getString(R.string.bot_uptime_response, hours, minutes, seconds)
        )
    }

    private fun aboutCommand() = Command(
        name = "about",
        description = stringProvider.getString(R.string.bot_about_description),
    ) { _ ->
        CommandResult.Success(stringProvider.getString(R.string.bot_about_text))
    }

    /**
     * Simple math expression evaluator.
     * Supports: +, -, *, /, parentheses.
     */
    private fun evaluateExpression(expr: String): Double {
        val cleaned = expr.replace("\\s+".toRegex(), "")
        return parseExpression(cleaned, intArrayOf(0))
    }

    private fun parseExpression(expr: String, pos: IntArray): Double {
        var result = parseTerm(expr, pos)
        while (pos[0] < expr.length) {
            when (expr[pos[0]]) {
                '+' -> { pos[0]++; result += parseTerm(expr, pos) }
                '-' -> { pos[0]++; result -= parseTerm(expr, pos) }
                else -> break
            }
        }
        return result
    }

    private fun parseTerm(expr: String, pos: IntArray): Double {
        var result = parseFactor(expr, pos)
        while (pos[0] < expr.length) {
            when (expr[pos[0]]) {
                '*' -> { pos[0]++; result *= parseFactor(expr, pos) }
                '/' -> {
                    pos[0]++
                    val divisor = parseFactor(expr, pos)
                    if (divisor == 0.0) throw ArithmeticException("Division by zero")
                    result /= divisor
                }
                else -> break
            }
        }
        return result
    }

    private fun parseFactor(expr: String, pos: IntArray): Double {
        if (pos[0] >= expr.length) throw IllegalArgumentException("Unexpected end of expression")

        return when {
            expr[pos[0]] == '(' -> {
                pos[0]++
                val result = parseExpression(expr, pos)
                if (pos[0] >= expr.length || expr[pos[0]] != ')') {
                    throw IllegalArgumentException("Missing closing parenthesis")
                }
                pos[0]++
                result
            }
            expr[pos[0]] == '-' -> {
                pos[0]++
                -parseFactor(expr, pos)
            }
            expr[pos[0]].isDigit() || expr[pos[0]] == '.' -> {
                val start = pos[0]
                while (pos[0] < expr.length && (expr[pos[0]].isDigit() || expr[pos[0]] == '.')) {
                    pos[0]++
                }
                expr.substring(start, pos[0]).toDouble()
            }
            else -> throw IllegalArgumentException("Unexpected character: ${expr[pos[0]]}")
        }
    }
}
