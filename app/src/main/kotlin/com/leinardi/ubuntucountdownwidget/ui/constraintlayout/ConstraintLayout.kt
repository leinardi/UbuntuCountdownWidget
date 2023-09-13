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

package com.leinardi.ubuntucountdownwidget.ui.constraintlayout

import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.Visibility

fun ConstrainScope.goneIf(block: () -> Boolean) = if (block()) Visibility.Gone else Visibility.Visible
fun ConstrainScope.invisibleIf(block: () -> Boolean) = if (block()) Visibility.Invisible else Visibility.Visible
