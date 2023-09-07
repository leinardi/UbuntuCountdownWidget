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

package com.leinardi.ubuntucountdownwidget.strictmode

import android.os.StrictMode
import com.leinardi.ubuntucountdownwidget.BuildConfig

fun <T> noStrictMode(disableVm: Boolean = true, disableThread: Boolean = true, block: () -> T): T {
    val vmPolicy = StrictMode.getVmPolicy()
    val threadPolicy = StrictMode.getThreadPolicy()
    if (disableVm && BuildConfig.DEBUG) {
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
    }
    if (disableThread && BuildConfig.DEBUG) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().build())
    }
    return block().also {
        if (disableVm && BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(vmPolicy)
        }
        if (disableThread && BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(threadPolicy)
        }
    }
}
