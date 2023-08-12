package com.thejohnsondev.isafe.presentation.components

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
import androidx.compose.ui.unit.dp
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.presentation.ui.theme.seed
import com.thejohnsondev.isafe.utils.Percent40
import com.thejohnsondev.isafe.utils.Size2

@Preview(showBackground = true)
@Composable
fun ISafeLogo(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = MaterialTheme.typography.displayLarge.fontSize,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        horizontalAlignment = horizontalAlignment
    ) {
        Text(modifier = modifier, text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = fontSize
                ),
            ) {
                append(stringResource(R.string.i))
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSize
                )
            ) {
                append(stringResource(R.string.safe))
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