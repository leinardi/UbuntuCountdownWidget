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

package com.leinardi.ubuntucountdownwidget.cache

import androidx.datastore.preferences.core.longPreferencesKey
import com.leinardi.ubuntucountdownwidget.repository.DataStoreRepository
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ReleaseDateCache @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend fun get(ignoreExpiration: Boolean = false): LocalDate? =
        if (ignoreExpiration || !isExpired()) {
            dataStoreRepository.readValue(RELEASE_DATE_PREF_KEY)?.let { LocalDate.ofEpochDay(it) }
        } else {
            null
        }

    suspend fun get(ignoreExpiration: Boolean = false, defaultValue: LocalDate): LocalDate = get(ignoreExpiration) ?: defaultValue

    suspend fun store(releaseDate: LocalDate, expiresAt: Instant = Instant.now().plus(1, ChronoUnit.MINUTES)) {
        dataStoreRepository.storeValue(RELEASE_DATE_PREF_KEY, releaseDate.toEpochDay())
        dataStoreRepository.storeValue(EXPIRES_AT_PREF_KEY, expiresAt.toEpochMilli())
    }

    suspend fun isExpired(): Boolean =
        dataStoreRepository.readValue(EXPIRES_AT_PREF_KEY)?.let { Instant.ofEpochMilli(it).isBefore(Instant.now()) } != false

    companion object {
        private val RELEASE_DATE_PREF_KEY = longPreferencesKey("ReleaseDateCache.releaseDate")
        private val EXPIRES_AT_PREF_KEY = longPreferencesKey("ReleaseDateCache.expiresAt")
    }
}
