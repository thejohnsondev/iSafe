package com.thejohnsondev.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.getAppLogo

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    iconTintColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(modifier = modifier.fillMaxSize()) {
        val infiniteTransition = rememberInfiniteTransition(label = EMPTY)

        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Icon(
            modifier = Modifier
                .size(Size48)
                .scale(scale)
                .align(Alignment.Center),
            imageVector = getAppLogo(),
            contentDescription = stringResource(com.thejohnsondev.common.R.string.app_logo),
            tint = iconTintColor
        )
    }
}