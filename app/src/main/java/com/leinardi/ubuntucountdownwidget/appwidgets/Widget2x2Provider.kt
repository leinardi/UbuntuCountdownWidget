/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2020 Roberto Leinardi
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
package com.leinardi.ubuntucountdownwidget.appwidgets

import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.leinardi.ubuntucountdownwidget.R

class Widget2x2Provider : WidgetProvider() {

    override fun getComponentName(context: Context): ComponentName {
        return ComponentName(context, Widget2x2Provider::class.java)
    }

    override fun getRemoteViews(context: Context, isThemeDark: Boolean): RemoteViews {
        return RemoteViews(context.packageName,
                if (isThemeDark) R.layout.appwidget_2x2_dark else R.layout.appwidget_2x2_light)
    }
}
