package vin.lucas.imdlibrary.ui.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload
import java.util.Calendar

private val currentYear by lazy {
    Calendar.getInstance().get(Calendar.YEAR)
}

@Composable
fun BookForm(
    book: Book?,
    modifier: Modifier = Modifier,
    submitButton: @Composable () -> Unit,
    onSubmit: (payload: BookChangePayload) -> Unit
) {
    var isbn by remember { mutableStateOf(book?.isbn ?:"") }
    var title by remember { mutableStateOf(book?.title ?:"") }
    var author by remember { mutableStateOf(book?.author ?:"") }
    var publisher by remember { mutableStateOf(book?.publisher ?:"") }
    var publicationYear by remember { mutableIntStateOf(book?.publicationYear ?: currentYear) }
    var summary by remember { mutableStateOf(book?.summary ?:"") }
    var coverUrl by remember { mutableStateOf(book?.coverUrl ?:"") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = isbn,
                onValueChange = { it ->
                    isbn = it.take(13).filter { it.isDigit() }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("ISBN") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("Editora") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = publicationYear.toString(),
                onValueChange = { it ->
                    publicationYear = it.take(4)
                        .filter { it.isDigit() }
                        .toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Ano de Publicação") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = summary,
                onValueChange = { summary = it },
                label = { Text("Resumo") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = coverUrl,
                onValueChange = { coverUrl = it },
                label = { Text("URL da Capa") },
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(BookChangePayload(
                isbn = isbn,
                title = title,
                author = author,
                publisher = publisher,
                publicationYear = publicationYear,
                summary = summary,
                coverUrl = coverUrl,
            )) },
        ) {
            submitButton()
        }
    }
}
