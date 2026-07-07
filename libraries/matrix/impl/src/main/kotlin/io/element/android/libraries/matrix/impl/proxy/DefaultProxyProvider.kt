/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2024, 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.libraries.matrix.impl.proxy

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.core.content.getSystemService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.element.android.libraries.di.annotations.ApplicationContext
import io.element.android.libraries.preferences.api.store.AppPreferencesStore
import io.element.android.libraries.preferences.api.store.ProxyConfig
import timber.log.Timber

/**
 * Предоставляет настройки прокси из пользовательских настроек или системы.
 *
 * Приоритет:
 * 1. Пользовательская конфигурация из AppPreferencesStore (если включена)
 * 2. Системный прокси из настроек Android (global http_proxy)
 */
@ContributesBinding(AppScope::class)
class DefaultProxyProvider(
    @ApplicationContext
    private val context: Context,
    private val appPreferencesStore: AppPreferencesStore,
) : ProxyProvider {
    override suspend fun provides(): String? {
        // Сначала проверяем пользовательские настройки прокси
        val userConfig = appPreferencesStore.getProxyConfig()
        if (userConfig.enabled && userConfig.host.isNotBlank()) {
            val proxyUrl = userConfig.toProxyUrl()
            Timber.d("Using custom proxy: $proxyUrl")
            return proxyUrl
        }

        // Fallback на системный прокси
        val defaultProxy = context.getSystemService<ConnectivityManager>()?.defaultProxy
        if (defaultProxy == null) {
            Timber.d("No default proxy")
            return null
        }
        return Settings.Global.getString(context.contentResolver, Settings.Global.HTTP_PROXY)
            ?.also {
                Timber.d("Using global proxy")
            }
    }
}


