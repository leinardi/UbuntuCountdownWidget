/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2023 Roberto Leinardi
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.leinardi.ubuntucountdownwidget.di

import android.app.Application
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.leinardi.ubuntucountdownwidget.BuildConfig
import com.leinardi.ubuntucountdownwidget.api.RestApi
import com.leinardi.ubuntucountdownwidget.coroutine.CoroutineDispatchers
import com.leinardi.ubuntucountdownwidget.serializer.LocalDateSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class AppModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers()

    @Provides
    @Singleton
    fun provideGlanceAppWidgetManager(application: Application): GlanceAppWidgetManager = GlanceAppWidgetManager(application)

    @Provides
    @Singleton
    @IntoSet
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor { Timber.v(it) }.also {
        it.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideSerializersModule() = SerializersModule {
        contextual(LocalDate::class, LocalDateSerializer)
    }

    @Provides
    @Singleton
    fun provideJson(serializerModule: SerializersModule) = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        serializersModule = serializerModule
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideKotlinxSerializationConverterFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptorSet: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        interceptorSet.forEach { addInterceptor(it) }
        retryOnConnectionFailure(false)
        callTimeout(NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRestApi(
        converterFactorySet: Set<@JvmSuppressWildcards Converter.Factory>,
        httpClient: OkHttpClient,
    ): RestApi = Retrofit.Builder()
        .apply { converterFactorySet.forEach { addConverterFactory(it) } }
        .baseUrl(BASE_URL)
        .client(httpClient)
        .build()
        .create()

    companion object {
        private const val BASE_URL = "https://leinardi.github.io/"
        private const val NETWORK_TIMEOUT_IN_SECONDS = 30L
    }
}
