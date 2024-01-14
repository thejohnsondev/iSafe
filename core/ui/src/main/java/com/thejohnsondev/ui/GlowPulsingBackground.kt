package com.thejohnsondev.ui

import android.os.Build
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import com.thejohnsondev.designsystem.Size16

@Composable
fun GlowPulsingBackground(
    modifier: Modifier = Modifier,
    duration: Int = 3000,
    blur: Float = 300f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "blur-background")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        BlurContainer(
            component = {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(Size16)
                        .clip(CircleShape)
                        .scale(scale)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            },
            blur = blur
        )
    }
}