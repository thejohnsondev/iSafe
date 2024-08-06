package com.thejohnsondev.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size8

@Composable
fun AccountSettingsItem(
    modifier: Modifier = Modifier,
    accountName: String?,
    onItemClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(Size32))
            .clickable {
                onItemClick()
            },
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(Size8)
                    .clip(RoundedCornerShape(Size24)),
                color = MaterialTheme.colorScheme.background,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(Size16)
                        .size(Size24),
                    imageVector = Icons.Filled.Person, contentDescription = stringResource(com.thejohnsondev.common.R.string.account)
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = accountName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge
                )
                // TODO: commented before settings implementation
//                Text(text = stringResource(com.thejohnsondev.common.R.string.manage_your_account))
            }
        }
    }
}

@Preview
@Composable
private fun AccountSettingsPreview() {
    ISafeTheme {
        AccountSettingsItem(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            accountName = "John Doe",
            {}
        )
    }
}