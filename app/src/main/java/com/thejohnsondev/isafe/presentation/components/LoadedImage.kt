package com.thejohnsondev.isafe.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thejohnsondev.isafe.R

@Composable
fun LoadedImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    errorImageVector: Int? = null,
    placeholderResId: Int? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RectangleShape
) {
    val model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
    errorImageVector?.let {
        model.error(ContextCompat.getDrawable(LocalContext.current, it))
    }
    placeholderResId?.let {
        model.placeholder(ContextCompat.getDrawable(LocalContext.current, it))
    }
    Surface(modifier = modifier, shape = shape, color = backgroundColor) {
        AsyncImage(
            model = model.build(),
            contentDescription = "Image",
            contentScale = contentScale
        )
    }
}