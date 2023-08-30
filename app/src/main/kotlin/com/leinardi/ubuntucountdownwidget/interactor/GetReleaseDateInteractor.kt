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

package com.leinardi.ubuntucountdownwidget.interactor

import com.leinardi.ubuntucountdownwidget.api.RestApi
import com.leinardi.ubuntucountdownwidget.cache.ReleaseDateCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetReleaseDateInteractor @Inject constructor(
    private val releaseDateCache: ReleaseDateCache,
    private val restApi: RestApi,
) {
    private val mutex = Mutex()
    suspend operator fun invoke(): LocalDate = mutex.withLock {
        releaseDateCache.run { if (releaseDateCache.isExpired()) fetchReleaseDate() else get() } ?: fetchReleaseDate()
    }

    private suspend fun fetchReleaseDate(): LocalDate = try {
        restApi.getReleaseDate().body()?.run {
            releaseDate.apply {
                releaseDateCache.store(this)
            }
        } ?: releaseDateCache.get(ignoreExpiration = true, defaultValue = DEFAULT_RELEASE_DATE)
    } catch (e: IOException) {
        releaseDateCache.get(ignoreExpiration = true, defaultValue = DEFAULT_RELEASE_DATE)
    }

    companion object {
        private val DEFAULT_RELEASE_DATE = LocalDate.of(2023, 10, 12)
    }
}
