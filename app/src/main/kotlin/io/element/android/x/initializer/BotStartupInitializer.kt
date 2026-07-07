package io.element.android.x.initializer

import android.content.Context
import androidx.startup.Initializer
import io.element.android.features.bot.impl.BotBindings
import io.element.android.libraries.architecture.bindings

class BotStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val botBindings = context.bindings<BotBindings>()
        botBindings.botInitializer().initialize()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
