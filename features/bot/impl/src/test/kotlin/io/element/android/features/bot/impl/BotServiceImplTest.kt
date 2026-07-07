/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.bot.impl

import com.google.common.truth.Truth.assertThat
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.services.toolbox.test.strings.FakeStringProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BotServiceImplTest {

    private fun createService(
        commands: FakeDefaultCommands = FakeDefaultCommands(),
        stringProvider: FakeStringProvider = FakeStringProvider(),
    ): BotServiceImpl {
        val commandProcessor = CommandProcessor(
            defaultCommands = commands,
            stringProvider = stringProvider,
        )
        return BotServiceImpl(
            commandProcessor = commandProcessor,
            stringProvider = stringProvider,
        )
    }

    @Test
    fun `isEnabled - defaults to false`() = runTest {
        val service = createService()
        val enabled = service.isEnabled.first()
        assertThat(enabled).isFalse()
    }

    @Test
    fun `setEnabled - updates isEnabled state`() = runTest {
        val service = createService()
        service.setEnabled(true)
        val enabled = service.isEnabled.first()
        assertThat(enabled).isTrue()

        service.setEnabled(false)
        val disabled = service.isEnabled.first()
        assertThat(disabled).isFalse()
    }

    @Test
    fun `processMessage - returns null when disabled`() = runTest {
        val service = createService()
        val result = service.processMessage(
            roomId = "!room:localhost",
            senderId = "@user:localhost",
            message = "/ping"
        )
        assertThat(result).isNull()
    }

    @Test
    fun `processMessage - processes command when enabled`() = runTest {
        val service = createService()
        service.setEnabled(true)
        val result = service.processMessage(
            roomId = "!room:localhost",
            senderId = "@user:localhost",
            message = "/ping"
        )
        assertThat(result).isNotNull()
    }

    @Test
    fun `processMessage - ignores bot's own messages`() = runTest {
        val service = createService()
        service.setEnabled(true)
        val result = service.processMessage(
            roomId = "!room:localhost",
            senderId = "@mx_bot:localhost",
            message = "/ping"
        )
        assertThat(result).isNull()
    }

    @Test
    fun `processMessage - ignores non-command messages`() = runTest {
        val service = createService()
        service.setEnabled(true)
        val result = service.processMessage(
            roomId = "!room:localhost",
            senderId = "@user:localhost",
            message = "Hello, how are you?"
        )
        assertThat(result).isNull()
    }

    @Test
    fun `processMessage - handles unknown command`() = runTest {
        val fakeStringProvider = FakeStringProvider(defaultResult = "Unknown command")
        val service = createService(stringProvider = fakeStringProvider)
        service.setEnabled(true)
        val result = service.processMessage(
            roomId = "!room:localhost",
            senderId = "@user:localhost",
            message = "/nonexistent"
        )
        assertThat(result).isNotNull()
    }

    @Test
    fun `getAvailableCommands - returns command list`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("test", "Test command") { CommandResult.Success("ok") },
            )
        )
        val service = createService(commands = fakeCommands)
        val commands = service.getAvailableCommands()
        assertThat(commands).hasSize(1)
        assertThat(commands[0].name).isEqualTo("test")
    }
}
