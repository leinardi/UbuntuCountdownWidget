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

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.leinardi.ubuntucountdownwidget.ui.appwidget.AppWidgetContent
import timber.log.Timber

class AppWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override val stateDefinition = AppWidgetStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Timber.d("provideGlance")

        provideContent {
            AppWidgetContent()
        }
    }
}
