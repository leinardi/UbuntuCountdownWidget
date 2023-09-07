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

package com.leinardi.ubuntucountdownwidget.ui.configuration

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.leinardi.ubuntucountdownwidget.ui.opensourcelibraries.OpenSourceLibrariesScreen

enum class AppWidgetConfigurationDestination(
    val route: String,
    val composable: @Composable AnimatedContentScope.(backStackEntry: NavBackStackEntry) -> Unit,
) {
    MAIN(
        route = CategoryDestinations.MAIN_ROUTE,
        composable = { AppWidgetConfigurationScreen() },
    ),
    OPEN_SOURCE_LICENSES(
        route = CategoryDestinations.OPEN_SOURCE_LICENSES_ROUTE,
        composable = { OpenSourceLibrariesScreen() },
    );

    private object CategoryDestinations {
        const val MAIN_ROUTE = "general"
        const val OPEN_SOURCE_LICENSES_ROUTE = "appearance"
    }
}
