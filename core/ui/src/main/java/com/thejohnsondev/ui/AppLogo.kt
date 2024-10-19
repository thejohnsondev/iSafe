package com.thejohnsondev.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.thejohnsondev.designsystem.Percent40
import com.thejohnsondev.designsystem.Size2
import com.thejohnsondev.designsystem.ubuntuFontFamily

@Preview(showBackground = true)
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = MaterialTheme.typography.displayLarge.fontSize,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = fontSize
                ),
            ) {
                append(stringResource(com.thejohnsondev.common.R.string.pass))
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSize,
                    fontFamily = ubuntuFontFamily
                )
            ) {
                append(stringResource(com.thejohnsondev.common.R.string.guard))
            }
        })
        Box(
            modifier = Modifier
                .fillMaxWidth(Percent40)
                .height(Size2)
                .background(MaterialTheme.colorScheme.primary)
        ) {

        }
    }
}