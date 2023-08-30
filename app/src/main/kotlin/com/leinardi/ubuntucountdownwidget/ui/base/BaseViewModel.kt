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

package com.leinardi.ubuntucountdownwidget.ui.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseViewModel<UiEvent : ViewEvent, UiState : ViewState, UiEffect : ViewEffect> : ViewModel() {
    // State (current state of views)
    // Everything is lazy in order to be able to use SavedStateHandle as initial value
    private val initialState: UiState by lazy { provideInitialState() }
    private val _viewState: MutableState<UiState> by lazy { mutableStateOf(initialState) }
    val viewState: State<UiState> by lazy { _viewState }

    private val runningJobs = AtomicInteger(0)

    // Event (user actions)
    private val _event: MutableSharedFlow<UiEvent> = MutableSharedFlow()

    // Effect (side effects like error messages which we want to show only once)
    private val _effect: Channel<UiEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        _event.onEach {
            handleEvent(it)
        }.launchIn(viewModelScope)
    }

    abstract fun provideInitialState(): UiState

    open fun onLoadingChanged(loading: Boolean) {}

    protected fun updateState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    abstract fun handleEvent(event: UiEvent)

    protected fun sendEffect(effectBuilder: () -> UiEffect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    protected suspend fun load(block: suspend () -> Unit) {
        require(runningJobs.get() >= 0)
        if (runningJobs.get() == 0) {
            onLoadingChanged(true)
        }
        runningJobs.incrementAndGet()
        block()
        if (runningJobs.decrementAndGet() == 0) {
            onLoadingChanged(false)
        }
    }
}

interface ViewState

interface ViewEvent

interface ViewEffect
