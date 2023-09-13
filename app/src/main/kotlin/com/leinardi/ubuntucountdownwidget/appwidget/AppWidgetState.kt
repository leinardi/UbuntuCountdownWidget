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

import android.appwidget.AppWidgetManager
import kotlinx.serialization.Serializable

@Serializable
sealed class AppWidgetState {
    abstract val appWidgetId: Int
    abstract val darkTheme: Boolean

    @Serializable
    data class DaysLeft(
        override val appWidgetId: Int,
        override val darkTheme: Boolean,
        val daysLeft: Long,
    ) : AppWidgetState()

    @Serializable
    data class ItIsHere(
        override val appWidgetId: Int,
        override val darkTheme: Boolean,
        val release: String,
    ) : AppWidgetState()

    @Serializable
    data class ComingSoon(
        override val appWidgetId: Int,
        override val darkTheme: Boolean,
    ) : AppWidgetState()

    @Serializable
    data class Unavailable(
        override val appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
        override val darkTheme: Boolean = false,
    ) : AppWidgetState()
}
