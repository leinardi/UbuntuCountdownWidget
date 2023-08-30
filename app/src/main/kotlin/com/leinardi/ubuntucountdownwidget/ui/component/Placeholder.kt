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

package com.leinardi.ubuntucountdownwidget.ui.component

import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material3.color
import com.google.accompanist.placeholder.material3.fadeHighlightColor
import com.google.accompanist.placeholder.placeholder

// Switch from placeholder foundation to placeholder material once it will support Material 3
// https://github.com/google/accompanist/issues/1151
fun Modifier.placeholder(
    visible: Boolean,
): Modifier = composed {
    val containerColor = MaterialTheme.colorScheme.surface
    Modifier.placeholder(
        visible = visible,
        color = PlaceholderDefaults.color(
            backgroundColor = containerColor,
            contentColor = contentColorFor(containerColor),
        ),
        shape = MaterialTheme.shapes.small,
        highlight = PlaceholderHighlight.fade(
            highlightColor = PlaceholderDefaults.fadeHighlightColor(backgroundColor = containerColor),
            animationSpec = PlaceholderDefaults.fadeAnimationSpec,
        ),
        placeholderFadeTransitionSpec = { spring() },
        contentFadeTransitionSpec = { spring() },
    )
}
