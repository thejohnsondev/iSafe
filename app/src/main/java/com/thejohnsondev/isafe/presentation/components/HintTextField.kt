package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import com.thejohnsondev.isafe.utils.Text20

@Composable
fun HintTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChanged: (String) -> Unit,
    hint: String = "",
    focusRequester: FocusRequester = FocusRequester(),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = Text20,
) {
    Box {
        BasicTextField(
            modifier = modifier
                .focusRequester(focusRequester),
            value = value,
            onValueChange = {
                onValueChanged(it)
            },
            textStyle = TextStyle(
                color = textColor,
                fontSize = fontSize
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = modifier,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = fontSize
                )
            )
        }
    }
}