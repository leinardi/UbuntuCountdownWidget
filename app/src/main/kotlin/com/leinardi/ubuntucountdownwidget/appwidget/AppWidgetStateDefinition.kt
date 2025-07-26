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

package com.leinardi.ubuntucountdownwidget.appwidget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object AppWidgetStateDefinition : GlanceStateDefinition<AppWidgetState> {
    private const val DATA_STORE_FILE_NAME = "AppWidgetState"
    private val Context.datastore by dataStore(DATA_STORE_FILE_NAME, AppWidgetStateSerializer)

    override suspend fun getDataStore(
        context: Context,
        fileKey: String,
    ): DataStore<AppWidgetState> = context.datastore

    override fun getLocation(
        context: Context,
        fileKey: String,
    ): File = context.dataStoreFile(DATA_STORE_FILE_NAME)

    object AppWidgetStateSerializer : Serializer<AppWidgetState> {
        override val defaultValue: AppWidgetState = AppWidgetState.Unavailable()

        override suspend fun readFrom(input: InputStream): AppWidgetState = try {
            Json.decodeFromString(
                AppWidgetState.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            Timber.e(e, "Widget error")
            defaultValue
        }

        @Suppress("IDENTIFIER_LENGTH")
        override suspend fun writeTo(t: AppWidgetState, output: OutputStream) {
            output.use { outputStream ->
                outputStream.write(
                    Json.encodeToString(
                        AppWidgetState.serializer(),
                        t,
                    ).encodeToByteArray(),
                )
            }
        }
    }
}
