/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2015 Roberto Leinardi
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

package com.leinardi.ubuntucountdownwidget.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {

    private static final GregorianCalendar ubuntuReleaseDate;

    static {
        ubuntuReleaseDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        ubuntuReleaseDate.set(2020, Calendar.APRIL, 23, 0, 0, 0);
    }

    private Utils() {
    }

    public static GregorianCalendar getUbuntuReleaseDate() {
        return (GregorianCalendar) ubuntuReleaseDate.clone();
    }
}
