/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.bot.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.features.bot.impl.FakeDefaultCommands
import io.element.android.services.toolbox.test.strings.FakeStringProvider
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CommandProcessorTest {

    private fun createProcessor(
        commands: FakeDefaultCommands = FakeDefaultCommands(),
        stringProvider: FakeStringProvider = FakeStringProvider(),
    ) = CommandProcessor(
        defaultCommands = commands,
        stringProvider = stringProvider,
    )

    @Test
    fun `process - null for non-command message`() = runTest {
        val processor = createProcessor()
        val result = processor.process("Hello, world!")
        assertThat(result).isNull()
    }

    @Test
    fun `process - null for empty input`() = runTest {
        val processor = createProcessor()
        val result = processor.process("")
        assertThat(result).isNull()
    }

    @Test
    fun `process - null for just slash`() = runTest {
        val processor = createProcessor()
        val result = processor.process("/")
        assertThat(result).isNull()
    }

    @Test
    fun `process - help command returns help list`() = runTest {
        val processor = createProcessor()
        val result = processor.process("/help")
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).contains("Available commands:")
    }

    @Test
    fun `process - calls command handler`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("testcmd", "A test command") { CommandResult.Success("handled!") },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val result = processor.process("/testcmd")
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).isEqualTo("handled!")
    }

    @Test
    fun `process - unknown command returns Error`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "unknown error")
        val processor = createProcessor(stringProvider = fakeStringProvider)
        val result = processor.process("/nonexistent")
        assertThat(result).isInstanceOf(CommandResult.Error::class.java)
    }

    @Test
    fun `process - help command even when overwritten in map`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("help", "Custom help") { CommandResult.Success("custom help text") },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val result = processor.process("/help")
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).isEqualTo("custom help text")
    }

    @Test
    fun `process - passes args to handler`() = runTest {
        var capturedArgs: List<String>? = null
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("echo", "Echo command") { args ->
                    capturedArgs = args
                    CommandResult.Success(args.joinToString(" "))
                },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val result = processor.process("/echo hello world")
        assertThat(capturedArgs).isEqualTo(listOf("hello", "world"))
    }

    @Test
    fun `getCommands - returns all registered commands`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("cmd1", "First") { CommandResult.Success("ok") },
                Command("cmd2", "Second") { CommandResult.Success("ok") },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val allCommands = processor.getCommands()
        assertThat(allCommands).hasSize(2)
        assertThat(allCommands.map { it.name }).containsExactly("cmd1", "cmd2")
    }

    @Test
    fun `process - case insensitive command matching`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("Ping", "Check bot") { CommandResult.Success("pong") },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val result = processor.process("/PING")
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }

    @Test
    fun `process - trims whitespace from input`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("ping", "Check bot") { CommandResult.Success("pong") },
            )
        )
        val processor = createProcessor(commands = fakeCommands)
        val result = processor.process("  /ping  ")
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }
}
