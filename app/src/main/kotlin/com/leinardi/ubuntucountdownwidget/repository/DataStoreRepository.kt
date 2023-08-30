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

package com.leinardi.ubuntucountdownwidget.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val application: Application,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStoreRepository")

    suspend fun clearPreferencesStorage() {
        Timber.d("Clear data store")
        application.dataStore.edit { it.clear() }
    }

    fun <T> observeValue(key: Preferences.Key<T>): Flow<T?> = application.dataStore.getFromLocalStorage(key)

    suspend fun <T> readValue(key: Preferences.Key<T>): T? = observeValue(key).firstOrNull()

    suspend fun <T> readValue(key: Preferences.Key<T>, defaultValue: T): T =
        application.dataStore.getFromLocalStorage(key).map { it ?: defaultValue }.first()

    suspend fun <T> removeKey(key: Preferences.Key<T>) {
        application.dataStore.edit { it.remove(key) }
    }

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        application.dataStore.edit { it[key] = value }
    }

    private fun <T> DataStore<Preferences>.getFromLocalStorage(
        preferencesKey: Preferences.Key<T>,
    ): Flow<T?> =
        data.catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error while reading data store.")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[preferencesKey]
        }
}
