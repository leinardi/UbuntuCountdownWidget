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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.leinardi.ubuntucountdownwidget.ui.preview.PreviewThemes
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing

@Composable
fun SettingsMenuLink(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val alpha = if (enabled) 1f else ContentAlpha.disabled
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = Spacing.x01)
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SettingsTileIcon(icon = icon)
        } else {
            Spacer(modifier = Modifier.size(Spacing.x02))
        }
        SettingsTileTexts(title = title, subtitle = subtitle)
        if (action != null) {
            VerticalDivider(
                modifier = Modifier
                    .padding(vertical = Spacing.half)
                    .height(56.dp),
            )
            SettingsTileAction {
                action.invoke()
            }
        }
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLink() {
    AppTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkSubtitle() {
    AppTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkAction() {
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    AppTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
            action = {
                Checkbox(
                    checked = rememberCheckBoxState,
                    onCheckedChange = { newState ->
                        rememberCheckBoxState = newState
                    },
                )
            },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkIcon() {
    AppTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkIconAction() {
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    AppTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
            action = {
                Checkbox(
                    checked = rememberCheckBoxState,
                    onCheckedChange = { newState ->
                        rememberCheckBoxState = newState
                    },
                )
            },
        )
    }
}
