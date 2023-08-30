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

import androidx.lifecycle.viewModelScope
import com.leinardi.ubuntucountdownwidget.interactor.GetAppVersionInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetCustomDateStreamInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetReleaseDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetUseCustomDateStreamInteractor
import com.leinardi.ubuntucountdownwidget.interactor.GetUseDarkThemeStreamInteractor
import com.leinardi.ubuntucountdownwidget.interactor.OpenUrlInWebBrowserInteractor
import com.leinardi.ubuntucountdownwidget.interactor.ReadShowLauncherIconInteractor
import com.leinardi.ubuntucountdownwidget.interactor.StoreCustomDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.StoreShowLauncherIconInteractor
import com.leinardi.ubuntucountdownwidget.interactor.StoreUseCustomDateInteractor
import com.leinardi.ubuntucountdownwidget.interactor.StoreUseDarkThemeInteractor
import com.leinardi.ubuntucountdownwidget.ui.base.BaseViewModel
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationContract.Effect
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationContract.Event
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppWidgetConfigurationViewModel @Inject constructor(
    getCustomDateStreamInteractor: GetCustomDateStreamInteractor,
    getUseCustomDateStreamInteractor: GetUseCustomDateStreamInteractor,
    getUseDarkThemeStreamInteractor: GetUseDarkThemeStreamInteractor,
    private val getAppVersionInteractor: GetAppVersionInteractor,
    private val getReleaseDateInteractor: GetReleaseDateInteractor,
    private val readShowLauncherIconInteractor: ReadShowLauncherIconInteractor,
    private val openUrlInWebBrowserInteractor: OpenUrlInWebBrowserInteractor,
    private val storeCustomDateInteractor: StoreCustomDateInteractor,
    private val storeUseCustomDateInteractor: StoreUseCustomDateInteractor,
    private val storeUseDarkThemeInteractor: StoreUseDarkThemeInteractor,
    private val storeShowLauncherIconInteractor: StoreShowLauncherIconInteractor,
) : BaseViewModel<Event, State, Effect>() {
    init {
        viewModelScope.launch {
            val releaseDate = getReleaseDateInteractor()
            updateState { copy(releaseDate = releaseDate) }
        }
        getCustomDateStreamInteractor().onEach {
            updateState { copy(customDate = it) }
        }.launchIn(viewModelScope)
        getUseCustomDateStreamInteractor().onEach {
            updateState { copy(useCustomDate = it) }
        }.launchIn(viewModelScope)
        getUseDarkThemeStreamInteractor().onEach {
            updateState { copy(useDarkTheme = it) }
        }.launchIn(viewModelScope)
    }

    override fun provideInitialState() = State(
        showLauncherIcon = readShowLauncherIconInteractor(),
        version = getAppVersionInteractor(),
    )

    override fun handleEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.OnBugReportClicked -> openUrlInWebBrowserInteractor(REPORT_A_BUG_URL)
                is Event.OnCustomDateSelected -> storeCustomDateInteractor(event.date)
                is Event.OnShowLauncherIconChanged -> handleOnShowLauncherIconChanged(event.enabled)
                is Event.OnUseCustomDateChanged -> storeUseCustomDateInteractor(event.enabled)
                is Event.OnUseDarkThemeChanged -> storeUseDarkThemeInteractor(event.enabled)
            }
        }
    }

    private fun handleOnShowLauncherIconChanged(enabled: Boolean) {
        storeShowLauncherIconInteractor(enabled)
        updateState { copy(showLauncherIcon = readShowLauncherIconInteractor()) }
    }

    companion object {
        private const val REPORT_A_BUG_URL = "https://github.com/leinardi/UbuntuCountdownWidget/issues"
    }
}
