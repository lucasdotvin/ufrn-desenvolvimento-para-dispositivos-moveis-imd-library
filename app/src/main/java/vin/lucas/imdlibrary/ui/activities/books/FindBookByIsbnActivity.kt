package vin.lucas.imdlibrary.ui.activities.books

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.IMDLibraryApplication
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.cases.books.FindBookByIsbnUseCase
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.ui.activities.AuthenticatedActivity
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme

class FindBookByIsbnActivity : AuthenticatedActivity() {
    private val findBookByIsbnUseCase: FindBookByIsbnUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.findBookByIsbnUseCase
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IMDLibraryTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back_content_description),
                                    )
                                }
                            },
                            title = {
                                Text("Pesquisar por ISBN")
                            },
                        )
                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                FindForm(onSubmit = { isbn ->
                                    tryFindBook(
                                        this@FindBookByIsbnActivity,
                                        isbn,
                                        findBookByIsbnUseCase,
                                    )
                                })
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FindForm(
    onSubmit: (isbn: String) -> Unit,
) {
    var isbn by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(isbn) },
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_content_description),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
            )
            Text(text = "Pesquisar")
        }
    }
}

private fun tryFindBook(
    context: Activity,
    isbn: String,
    findBookByIsbnUseCase: FindBookByIsbnUseCase,
) {
    val book: Book

    try {
        book = findBookByIsbnUseCase.execute(isbn)
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(context, ShowBookActivity::class.java)
    intent.putExtra("bookId", book.id)

    context.startActivity(intent)
}
