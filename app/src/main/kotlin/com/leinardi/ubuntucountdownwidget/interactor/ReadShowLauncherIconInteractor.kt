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
import android.content.ComponentName
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED
import com.leinardi.ubuntucountdownwidget.ui.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadShowLauncherIconInteractor @Inject constructor(
    private val application: Application,
) {
    operator fun invoke(): Boolean =
        when (application.packageManager.getComponentEnabledSetting(ComponentName(application, MainActivity::class.java))) {
            COMPONENT_ENABLED_STATE_DEFAULT,
            COMPONENT_ENABLED_STATE_ENABLED -> true

            else -> false
        }
}
