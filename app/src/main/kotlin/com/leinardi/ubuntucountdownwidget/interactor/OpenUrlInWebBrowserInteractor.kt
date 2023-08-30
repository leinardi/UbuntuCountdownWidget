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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.leinardi.ubuntucountdownwidget.R
import timber.log.Timber
import javax.inject.Inject

class OpenUrlInWebBrowserInteractor @Inject constructor(
    private val application: Application,
    private val showToastInteractor: ShowToastInteractor,
) {
    operator fun invoke(url: String) {
        Timber.d("Opening URL in browser: $url")
        try {
            application.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (exception: ActivityNotFoundException) {
            Timber.i(exception)
            showToastInteractor(application.getString(R.string.no_browser_installed))
        }
    }
}
