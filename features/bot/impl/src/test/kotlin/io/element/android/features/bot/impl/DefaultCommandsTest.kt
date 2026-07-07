/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.bot.impl

import com.google.common.truth.Truth.assertThat
import io.element.android.features.bot.api.CommandResult
import io.element.android.services.toolbox.test.strings.FakeStringProvider
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultCommandsTest {

    private fun createDefaultCommands(
        stringProvider: FakeStringProvider = FakeStringProvider(),
    ) = DefaultCommands(
        stringProvider = stringProvider,
    )

    @Test
    fun `getAll - returns all built-in commands`() {
        val commands = createDefaultCommands()
        val all = commands.getAll()
        assertThat(all).hasSize(9)
        assertThat(all.map { it.name }).containsExactly(
            "help", "ping", "time", "echo", "status", "calc", "quote", "uptime", "about"
        )
    }

    @Test
    fun `ping command returns pong response`() = runTest {
        val commands = createDefaultCommands()
        val pingCommand = commands.getAll().first { it.name == "ping" }
        val result = pingCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).isNotEmpty()
    }

    @Test
    fun `echo command with args returns them joined`() = runTest {
        val commands = createDefaultCommands()
        val echoCommand = commands.getAll().first { it.name == "echo" }
        val result = echoCommand.handler(listOf("hello", "world"))
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).isEqualTo("hello world")
    }

    @Test
    fun `echo command without args returns error`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "Usage error")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val echoCommand = commands.getAll().first { it.name == "echo" }
        val result = echoCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Error::class.java)
    }

    @Test
    fun `help command lists all commands`() = runTest {
        val commands = createDefaultCommands()
        val helpCommand = commands.getAll().first { it.name == "help" }
        val result = helpCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).contains("/ping")
        assertThat(success.message).contains("/help")
        assertThat(success.message).contains("/time")
    }

    @Test
    fun `time command returns formatted time`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "time: now")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val timeCommand = commands.getAll().first { it.name == "time" }
        val result = timeCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }

    @Test
    fun `calc command evaluates expression`() = runTest {
        val commands = createDefaultCommands()
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(listOf("2+2*2"))
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).contains("=")
        assertThat(success.message).contains("6.0")
    }

    @Test
    fun `calc command with empty args returns error`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "Calc error")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Error::class.java)
    }

    @Test
    fun `calc command handles division by zero`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "error")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(listOf("1/0"))
        assertThat(result).isInstanceOf(CommandResult.Error::class.java)
    }

    @Test
    fun `quote command returns a random quote`() = runTest {
        val commands = createDefaultCommands()
        val quoteCommand = commands.getAll().first { it.name == "quote" }
        val result = quoteCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).isNotEmpty()
    }

    @Test
    fun `status command returns device info`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "status")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val statusCommand = commands.getAll().first { it.name == "status" }
        val result = statusCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }

    @Test
    fun `uptime command returns uptime string`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "uptime: 0h 0m 0s")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val uptimeCommand = commands.getAll().first { it.name == "uptime" }
        val result = uptimeCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }

    @Test
    fun `about command returns about text`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "Bot v1.0")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val aboutCommand = commands.getAll().first { it.name == "about" }
        val result = aboutCommand.handler(emptyList())
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
    }

    @Test
    fun `calc command evaluates parentheses`() = runTest {
        val commands = createDefaultCommands()
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(listOf("(2+3)*4"))
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).contains("20.0")
    }

    @Test
    fun `calc command handles unary minus`() = runTest {
        val commands = createDefaultCommands()
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(listOf("-5+3"))
        assertThat(result).isInstanceOf(CommandResult.Success::class.java)
        val success = result as CommandResult.Success
        assertThat(success.message).contains("-2.0")
    }

    @Test
    fun `calc command reports parse error for invalid expression`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "error")
        val commands = createDefaultCommands(stringProvider = fakeStringProvider)
        val calcCommand = commands.getAll().first { it.name == "calc" }
        val result = calcCommand.handler(listOf("2+"))
        assertThat(result).isInstanceOf(CommandResult.Error::class.java)
    }
}
