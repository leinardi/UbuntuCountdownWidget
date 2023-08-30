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

package com.leinardi.ubuntucountdownwidget.ext

import android.content.BroadcastReceiver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

fun BroadcastReceiver.goAsync(
    dispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> Unit,
) {
    val pendingResult = goAsync()

    CoroutineScope(dispatcher).launch {
        try {
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (@Suppress("TooGenericExceptionCaught") throwable: Throwable) {
                Timber.e(throwable, "BroadcastReceiver execution failed", throwable)
            } finally {
                // Nothing can be in the `finally` block after this, as this throws a `CancellationException`
                cancel()
            }
        } finally {
            // This must be the last call, as the process may be killed after calling this.
            try {
                pendingResult.finish()
            } catch (exception: IllegalStateException) {
                // On some OEM devices, this may throw an error about "Broadcast already finished".
                Timber.e(exception, "Error thrown when trying to finish broadcast")
            }
        }
    }
}
