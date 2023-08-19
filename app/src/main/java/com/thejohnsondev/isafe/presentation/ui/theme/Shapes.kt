package com.thejohnsondev.isafe.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size32
import com.thejohnsondev.isafe.utils.Size8

val TopRounded = Shapes(
    medium = RoundedCornerShape(topStart = Size16, topEnd = Size16),
)

val EqualRounded = Shapes(
    small = RoundedCornerShape(Size8),
    medium = RoundedCornerShape(Size16),
    large = RoundedCornerShape(Size32)
)