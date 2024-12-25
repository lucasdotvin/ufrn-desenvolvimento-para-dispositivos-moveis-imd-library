package vin.lucas.imdlibrary.ui.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    var coverUrl by remember { mutableStateOf(book?.coverUrl ?:"") }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
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
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("Editora") },
            )
            Spacer(modifier = Modifier.padding(4.dp))
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
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = coverUrl,
                onValueChange = { coverUrl = it },
                label = { Text("URL da Capa") },
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(BookChangePayload(
                isbn = isbn,
                title = title,
                author = author,
                publisher = publisher,
                publicationYear = publicationYear,
                coverUrl = coverUrl,
            )) },
        ) {
            submitButton()
        }
    }
}
