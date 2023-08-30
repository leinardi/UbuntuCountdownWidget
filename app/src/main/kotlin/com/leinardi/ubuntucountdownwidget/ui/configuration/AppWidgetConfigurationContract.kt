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

package com.leinardi.ubuntucountdownwidget.ui.configuration

import androidx.compose.runtime.Immutable
import com.leinardi.ubuntucountdownwidget.ui.base.ViewEffect
import com.leinardi.ubuntucountdownwidget.ui.base.ViewEvent
import com.leinardi.ubuntucountdownwidget.ui.base.ViewState
import java.time.LocalDate

@Immutable
object AppWidgetConfigurationContract {
    data class State(
        val releaseDate: LocalDate? = null,
        val useCustomDate: Boolean = false,
        val customDate: LocalDate = LocalDate.now(),
        val useDarkTheme: Boolean = false,
        val showLauncherIcon: Boolean = true,
        val version: String = "",
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnCustomDateSelected(val date: LocalDate) : Event()
        data class OnShowLauncherIconChanged(val enabled: Boolean) : Event()
        data class OnUseCustomDateChanged(val enabled: Boolean) : Event()
        data class OnUseDarkThemeChanged(val enabled: Boolean) : Event()
        data object OnBugReportClicked : Event()
    }

    sealed class Effect : ViewEffect
}
