package vin.lucas.imdlibrary.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageWithBlurredClone(
    uri: String,
    contentDescription: String?,
    containerModifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    cloneAlpha: Float = 0.5F,
    cloneBlur: Float = 4F,
) {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = containerModifier,
    ) {
        AsyncImage(
            model = uri,
            modifier = Modifier.fillMaxSize().blur(cloneBlur.dp),
            alpha = cloneAlpha,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        AsyncImage(
            model = uri,
            modifier = imageModifier.fillMaxSize(),
            contentDescription = contentDescription,
        )
    }
}
