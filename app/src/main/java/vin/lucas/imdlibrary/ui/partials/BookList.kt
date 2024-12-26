package vin.lucas.imdlibrary.ui.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.ui.components.ImageWithBlurredClone

@Composable
fun BookList(
    books: List<Book>,
    contentPadding: PaddingValues,
    modifier: Modifier,
    onClick: (Book) -> Unit,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(books) { book ->
            BookItem(book, onClick)
        }
    }
}

@Composable
private fun BookItem(book: Book, onClick: (Book) -> Unit) {
    Card(
        modifier = Modifier
            .height(140.dp)
            .fillMaxWidth()
            .clickable {
                onClick(book)
            },
    ) {
        Row {
            ImageWithBlurredClone(
                uri = book.coverUrl,
                contentDescription = "Capa do livro ${book.title}",
                containerModifier = Modifier.fillMaxHeight().aspectRatio(0.8F),
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = AnnotatedString("#${book.isbn}"),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    letterSpacing = 1.25.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
                )
                Column {
                    Text(
                        text = buildAnnotatedString {
                            append(book.title)

                            withStyle(SpanStyle(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F))) {
                                append(" (${book.publicationYear})")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(book.author)

                            withStyle(SpanStyle(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F))) {
                                append(" \u00B7 ${book.publisher}")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = book.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
