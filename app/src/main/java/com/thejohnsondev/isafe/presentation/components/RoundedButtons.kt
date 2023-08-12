package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.utils.Percent70
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size24
import com.thejohnsondev.isafe.utils.Size48
import com.thejohnsondev.isafe.utils.Size8

@Preview
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.buttons),
    loading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = Percent70),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = Percent70)
    ),
    shape: RoundedCornerShape = RoundedCornerShape(Size16)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(Size8)
            .height(Size48)
            .fillMaxWidth(),
        enabled = !loading && enabled,
        shape = shape,
        colors = colors
    ) {
        if (loading)
            CircularProgressIndicator(
                modifier = Modifier.size(Size24),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        else
            Text(text = text)
    }

}