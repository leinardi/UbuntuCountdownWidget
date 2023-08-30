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

package com.leinardi.ubuntucountdownwidget.ui.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(
    name = "1. Phone - Light theme",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "2. Phone - Dark theme",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "3. Landscape - Light theme",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "4. Landscape - Dark theme",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "5. Foldable - Light theme",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "6. Foldable - Dark theme",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "7. Tablet - Light theme",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "8. Tablet - Dark theme",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
annotation class DevicePreviews
