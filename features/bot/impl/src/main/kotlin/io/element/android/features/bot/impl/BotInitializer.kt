package io.element.android.features.bot.impl

import dev.zacsweers.metro.Inject
import io.element.android.features.bot.api.BotService

@Inject
class BotInitializer(
    private val botService: BotService,
) {
    fun initialize() {
        botService.setEnabled(false)
    }
}
