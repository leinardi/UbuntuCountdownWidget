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
package com.leinardi.ubuntucountdownwidget.utils

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

object Utils {
    private val UBUNTU_RELEASE_DATE = GregorianCalendar(TimeZone.getTimeZone("GMT")).apply {
        set(2022, Calendar.APRIL, 21, 0, 0, 0)
    }
    val ubuntuReleaseDate: GregorianCalendar
        get() = UBUNTU_RELEASE_DATE.clone() as GregorianCalendar
}
