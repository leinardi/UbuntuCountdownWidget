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

import com.leinardi.ubuntucountdownwidget.interactor.GetCustomDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetDaysLeftInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetReleaseDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetUseCustomDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetUseDarkThemeInteractor
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class AppWidgetStateProvider @Inject constructor(
    private val getCustomDateInteractor: GetCustomDateInteractor,
    private val getDaysLeftInteractor: GetDaysLeftInteractor,
    private val getReleaseDateInteractor: GetReleaseDateInteractor,
    private val getUseCustomDateInteractor: GetUseCustomDateInteractor,
    private val getUseDarkThemeInteractor: GetUseDarkThemeInteractor,
) {
    suspend fun get(appWidgetId: Int): AppWidgetState {
        Timber.d("Generate new AppWidgetState")
        val darkTheme = getUseDarkThemeInteractor()
        val releaseDate = if (getUseCustomDateInteractor()) getCustomDateInteractor() else getReleaseDateInteractor()
        val daysLeft = getDaysLeftInteractor(releaseDate)
        return when {
            daysLeft > 1 -> AppWidgetState.DaysLeft(
                appWidgetId = appWidgetId,
                darkTheme = darkTheme,
                daysLeft = daysLeft,
            )

            daysLeft <= 0 -> AppWidgetState.ItIsHere(
                appWidgetId = appWidgetId,
                darkTheme = darkTheme,
                releaseNumber = @Suppress("MagicNumber") String.format(Locale.ROOT, "%02d.%02d", releaseDate.year % 100, releaseDate.monthValue),
            )

            else -> AppWidgetState.ComingSoon(
                appWidgetId = appWidgetId,
                darkTheme = darkTheme,
            )
        }
    }
}
