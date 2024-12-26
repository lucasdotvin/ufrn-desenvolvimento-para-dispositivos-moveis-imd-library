package vin.lucas.imdlibrary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun DataDisplay(
    label: String,
    value: String,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}
