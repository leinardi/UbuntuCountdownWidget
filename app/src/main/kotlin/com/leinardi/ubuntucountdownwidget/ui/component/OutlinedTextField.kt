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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.leinardi.ubuntucountdownwidget.ui.constraintlayout.goneIf
import com.leinardi.ubuntucountdownwidget.ui.preview.PreviewThemes
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing

@Suppress("ReusedModifierInstance")
@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    helperMessage: String? = null,
    counterMessage: String? = null,
    errorMessage: String? = null,
    isError: Boolean = errorMessage != null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    passwordToggleEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = if (passwordToggleEnabled) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(!passwordToggleEnabled) }
    val passwordToggleVisualTransformation = if (passwordToggleEnabled) {
        if (passwordVisibility) visualTransformation else PasswordVisualTransformation()
    } else {
        visualTransformation
    }

    val passwordToggleIcon = if (passwordVisibility) {
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }

    val textFieldTrailingIcon: @Composable (() -> Unit) = {
        when {
            isError -> Icon(Icons.Filled.Error, null)
            passwordToggleEnabled -> IconButton(
                onClick = {
                    passwordVisibility = !passwordVisibility
                },
            ) {
                Icon(passwordToggleIcon, null)
            }

            else -> trailingIcon?.invoke()
        }
    }
    ConstraintLayout {
        val (textFieldRef, helperMessageRef, counterMessageRef) = createRefs()
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.constrainAs(textFieldRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = { label?.let { Text(text = it) } },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = textFieldTrailingIcon,
            isError = isError,
            visualTransformation = passwordToggleVisualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
        Text(
            text = if (isError && errorMessage != null) errorMessage else helperMessage.orEmpty(),
            color = if (isError && errorMessage != null) MaterialTheme.colorScheme.error else Color.Unspecified,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .constrainAs(helperMessageRef) {
                    top.linkTo(textFieldRef.bottom)
                    start.linkTo(textFieldRef.start, Spacing.x02)
                    end.linkTo(counterMessageRef.start, Spacing.x01, goneMargin = 12.dp)
                    width = Dimension.fillToConstraints
                    visibility = goneIf { if (isError) errorMessage == null else helperMessage == null }
                }
                .defaultMinSize(minHeight = 16.dp),
        )
        Text(
            text = counterMessage.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .constrainAs(counterMessageRef) {
                    top.linkTo(textFieldRef.bottom)
                    end.linkTo(textFieldRef.end, 12.dp)
                    width = Dimension.fillToConstraints
                    visibility = goneIf { counterMessage == null }
                }
                .defaultMinSize(minHeight = 16.dp),
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextField() {
    AppTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label")
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextFieldEmpty() {
    AppTheme {
        OutlinedTextField(value = "", onValueChange = {}, label = "Label")
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextFieldEmptyHelperMessage() {
    AppTheme {
        OutlinedTextField(value = "", onValueChange = {}, label = "Label", helperMessage = "Helper message")
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextFieldEmptyHelperMessageCounterMessage() {
    AppTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", helperMessage = "Helper text", counterMessage = "5/160")
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextFieldErrorMessage() {
    AppTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", errorMessage = "Error message")
    }
}

@PreviewThemes
@Composable
private fun PreviewOutlinedTextFieldErrorMessageCounterMessage() {
    AppTheme {
        OutlinedTextField(value = "input", onValueChange = {}, label = "Label", errorMessage = "Error message", counterMessage = "5/160")
    }
}
