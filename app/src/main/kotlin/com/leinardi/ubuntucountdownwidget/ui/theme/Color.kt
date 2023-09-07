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

package com.leinardi.ubuntucountdownwidget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.color.ColorProviders
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.FixedColorProvider

val md_theme_light_primary = Color(0xFFE95420)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDBD0)
val md_theme_light_onPrimaryContainer = Color(0xFF3A0B00)
val md_theme_light_secondary = Color(0xFF943B89)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFD7F3)
val md_theme_light_onSecondaryContainer = Color(0xFF390035)
val md_theme_light_tertiary = Color(0xFF6B5E2F)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFF4E2A7)
val md_theme_light_onTertiaryContainer = Color(0xFF221B00)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF251A00)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF251A00)
val md_theme_light_surfaceVariant = Color(0xFFF5DED7)
val md_theme_light_onSurfaceVariant = Color(0xFF53433F)
val md_theme_light_outline = Color(0xFF85736E)
val md_theme_light_inverseOnSurface = Color(0xFFFFEFD3)
val md_theme_light_inverseSurface = Color(0xFF3F2E00)
val md_theme_light_inversePrimary = Color(0xFFFFB59E)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFFE95420)
val md_theme_light_outlineVariant = Color(0xFFD8C2BC)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFFB59E)
val md_theme_dark_onPrimary = Color(0xFF5E1700)
val md_theme_dark_primaryContainer = Color(0xFF852400)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDBD0)
val md_theme_dark_secondary = Color(0xFFFFABEE)
val md_theme_dark_onSecondary = Color(0xFF5C0157)
val md_theme_dark_secondaryContainer = Color(0xFF78226F)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFD7F3)
val md_theme_dark_tertiary = Color(0xFFD7C68D)
val md_theme_dark_onTertiary = Color(0xFF3A3005)
val md_theme_dark_tertiaryContainer = Color(0xFF52461A)
val md_theme_dark_onTertiaryContainer = Color(0xFFF4E2A7)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF251A00)
val md_theme_dark_onBackground = Color(0xFFFFDF9C)
val md_theme_dark_surface = Color(0xFF251A00)
val md_theme_dark_onSurface = Color(0xFFFFDF9C)
val md_theme_dark_surfaceVariant = Color(0xFF53433F)
val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BC)
val md_theme_dark_outline = Color(0xFFA08C87)
val md_theme_dark_inverseOnSurface = Color(0xFF251A00)
val md_theme_dark_inverseSurface = Color(0xFFFFDF9C)
val md_theme_dark_inversePrimary = Color(0xFFE95420)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFFFB59E)
val md_theme_dark_outlineVariant = Color(0xFF53433F)
val md_theme_dark_scrim = Color(0xFF000000)

val seed = Color(0xFFE95420)

val ColorScheme.settingsContainer: Color
    @Composable
    get() = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.inverseOnSurface
    }

val ColorScheme.settingsContent: Color
    @Composable
    get() = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.inverseSurface
    }

val ColorProviders.textLight: ColorProvider
    @Composable
    get() = FixedColorProvider(Color(0xFF1D1D1D))

val ColorProviders.textDark: ColorProvider
    @Composable
    get() = FixedColorProvider(Color(0xFFFFFFFF))
