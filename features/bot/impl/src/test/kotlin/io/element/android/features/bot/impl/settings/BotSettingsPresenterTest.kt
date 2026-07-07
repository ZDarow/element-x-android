/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.bot.impl.settings

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.element.android.features.bot.api.Command
import io.element.android.features.bot.api.CommandResult
import io.element.android.features.bot.impl.CommandProcessor
import io.element.android.features.bot.impl.BotServiceImpl
import io.element.android.features.bot.impl.FakeDefaultCommands
import io.element.android.services.toolbox.test.strings.FakeStringProvider
import io.element.android.tests.testutils.WarmUpRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class BotSettingsPresenterTest {
    @get:Rule
    val warmUpRule = WarmUpRule()

    private fun createPresenter(
        commands: FakeDefaultCommands = FakeDefaultCommands(),
        stringProvider: FakeStringProvider = FakeStringProvider(),
    ): BotSettingsPresenter {
        val commandProcessor = CommandProcessor(
            defaultCommands = commands,
            stringProvider = stringProvider,
        )
        val botService = BotServiceImpl(
            commandProcessor = commandProcessor,
            stringProvider = stringProvider,
        )
        return BotSettingsPresenter(botService = botService)
    }

    @Test
    fun `present - initial state is disabled`() = runTest {
        val presenter = createPresenter()
        moleculeFlow(RecompositionMode.Immediate) {
            presenter.present()
        }.test {
            val state = awaitItem()
            assertThat(state.isEnabled).isFalse()
        }
    }

    @Test
    fun `present - shows available commands`() = runTest {
        val fakeCommands = FakeDefaultCommands(
            commands = listOf(
                Command("ping", "Check bot") { CommandResult.Success("pong") },
            )
        )
        val presenter = createPresenter(commands = fakeCommands)
        moleculeFlow(RecompositionMode.Immediate) {
            presenter.present()
        }.test {
            val state = awaitItem()
            assertThat(state.commands).hasSize(1)
            assertThat(state.commands[0].name).isEqualTo("ping")
        }
    }

    @Test
    fun `present - setEnabled event updates state`() = runTest {
        val presenter = createPresenter()
        moleculeFlow(RecompositionMode.Immediate) {
            presenter.present()
        }.test {
            val initialState = awaitItem()
            assertThat(initialState.isEnabled).isFalse()

            initialState.eventSink(BotSettingsEvent.SetEnabled(true))

            val enabledState = awaitItem()
            assertThat(enabledState.isEnabled).isTrue()
        }
    }
}
