/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.bot.impl

import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult

/**
 * Fake [DefaultCommands] для использования в тестах.
 * Позволяет задать произвольный набор команд.
 */
class FakeDefaultCommands(
    val commands: List<Command> = listOf(
        Command("help", "List all commands") { args ->
            CommandResult.Success("Available commands:\n\n/help -- List all commands\n/ping -- Check if bot is running")
        },
        Command("ping", "Check if bot is running") { CommandResult.Success("Pong! Bot is running.") },
    ),
) {
    fun getAll(): List<Command> = commands
}
