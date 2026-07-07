package io.element.android.features.bot.impl.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.BotService
import io.element.android.libraries.architecture.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Inject
class BotSettingsPresenter(
    private val botService: BotService,
) : Presenter<BotSettingsState> {

    @Composable
    override fun present(): BotSettingsState {
        val isEnabled by botService.isEnabled.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        return remember(isEnabled) {
            BotSettingsState(
                isEnabled = isEnabled,
                commands = botService.getAvailableCommands().toImmutableList(),
                eventSink = { event ->
                    when (event) {
                        BotSettingsEvent.OnBackClick -> { /* handled by Node */ }
                        is BotSettingsEvent.SetEnabled -> {
                            coroutineScope.launch {
                                botService.setEnabled(event.enabled)
                            }
                        }
                    }
                }
            )
        }
    }
}
