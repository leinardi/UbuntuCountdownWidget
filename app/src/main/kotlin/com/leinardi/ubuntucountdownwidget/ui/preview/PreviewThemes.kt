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

package com.leinardi.ubuntucountdownwidget.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

/**
 * A MultiPreview annotation for desplaying a @[Composable] method using light and dark themes.
 *
 * Note that the app theme should support dark and light modes for these previews to be different.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "1. Light theme",
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "2. Dark theme",
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "3. Red",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
)
@Preview(
    name = "4. Blue",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
)
@Preview(
    name = "5. Green",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
)
@Preview(
    name = "6. Yellow",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
)
annotation class PreviewThemes
