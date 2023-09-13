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

package com.leinardi.ubuntucountdownwidget.ui.appwidget

import android.appwidget.AppWidgetManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDefaults
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidgetState
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationActivity
import com.leinardi.ubuntucountdownwidget.ui.theme.AppGlanceTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.textDark
import com.leinardi.ubuntucountdownwidget.ui.theme.textLight

@Composable
fun AppWidgetContent(
    modifier: GlanceModifier = GlanceModifier,
) {
    val state = currentState<AppWidgetState>()
    AppGlanceTheme(darkTheme = false) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val size = with(LocalSize.current) { minOf(width, height) }
            Box(
                modifier = GlanceModifier
                    .size(size, size)
                    .background(
                        if (state.darkTheme) {
                            ImageProvider(R.drawable.background_dark)
                        } else {
                            ImageProvider(R.drawable.background_light)
                        },
                    )
                    .clickable(
                        actionStartActivity<AppWidgetConfigurationActivity>(
                            actionParametersOf(
                                ActionParameters.Key<Int>(AppWidgetManager.EXTRA_APPWIDGET_ID) to state.appWidgetId,
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                when (state) {
                    is AppWidgetState.ComingSoon -> Image(
                        provider = ImageProvider(R.drawable.ubuntu_logo_circle),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier
                            .padding(bottom = size.value.dp / 6)
                            .wrapContentHeight()
                            .width(size.value.dp / 1.8f),
                    )

                    is AppWidgetState.DaysLeft -> Text(
                        text = state.daysLeft.toString(),
                        style = TextDefaults.defaultTextStyle.copy(
                            color = GlanceTheme.colors.primary,
                            fontSize = size.value.sp / 3,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        ),
                    )

                    is AppWidgetState.ItIsHere -> Text(
                        text = state.release,
                        style = TextDefaults.defaultTextStyle.copy(
                            color = GlanceTheme.colors.primary,
                            fontSize = size.value.sp / getMagicNumber(state.release),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        ),
                        maxLines = 1,
                    )

                    is AppWidgetState.Unavailable -> CircularProgressIndicator()
                }
            }
            if (state is AppWidgetState.DaysLeft || state is AppWidgetState.ItIsHere) {
                Box(
                    modifier = GlanceModifier.size(size, size),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Image(
                        provider = if (state.darkTheme) {
                            ImageProvider(
                                R.drawable.ubuntu_logo_dark,
                            )
                        } else {
                            ImageProvider(R.drawable.ubuntu_logo_light)
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier
                            .padding(top = size.value.dp / 16)
                            .wrapContentHeight()
                            .width(size.value.dp / 1.8f),
                    )
                }
            }
            Box(
                modifier = GlanceModifier.size(size, size),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = when (state) {
                        is AppWidgetState.ComingSoon -> LocalContext.current.getString(R.string.coming_soon)
                        is AppWidgetState.DaysLeft -> LocalContext.current.getString(R.string.days_left)
                        is AppWidgetState.ItIsHere -> LocalContext.current.getString(R.string.its_here)
                        is AppWidgetState.Unavailable -> ""
                    },
                    modifier = GlanceModifier.padding(bottom = size.value.dp / 10),
                    style = TextDefaults.defaultTextStyle.copy(
                        color = if (state.darkTheme) GlanceTheme.colors.textDark else GlanceTheme.colors.textLight,
                        fontSize = size.value.sp / 8,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}

@Suppress("MagicNumber")
private fun getMagicNumber(text: String): Float = 0.466667f * text.length + 0.766667f
