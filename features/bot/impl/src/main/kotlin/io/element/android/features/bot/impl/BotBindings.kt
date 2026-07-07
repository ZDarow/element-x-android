package io.element.android.features.bot.impl

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BotBindings {
    fun botInitializer(): BotInitializer
}
