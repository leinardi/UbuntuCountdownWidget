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

import android.app.Application
import android.widget.Toast
import com.leinardi.ubuntucountdownwidget.strictmode.noStrictMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowToastInteractor @Inject constructor(
    private val application: Application,
) {
    private var toast: Toast? = null

    operator fun invoke(text: String, shortLength: Boolean = false, cancelPrevious: Boolean = true) {
        if (cancelPrevious) {
            toast?.run { cancel() }
        }
        // StrictMode complains about wrong context used to show the toast, but it's a false positive since the official documentation states that
        // an application context can and should be used: https://developer.android.com/guide/topics/ui/notifiers/toasts#Basics
        toast = noStrictMode(disableThread = false) {
            Toast.makeText(application, text, if (shortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).apply { show() }
        }
    }
}
