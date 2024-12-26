package vin.lucas.imdlibrary.ui.activities.books

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.IMDLibraryApplication
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.cases.books.FindBookUseCase
import vin.lucas.imdlibrary.contracts.cases.books.UpdateBookUseCase
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.ui.activities.AuthenticatedActivity
import vin.lucas.imdlibrary.ui.partials.BookForm
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme
import vin.lucas.imdlibrary.values.BookChangePayload

class EditBookActivity : AuthenticatedActivity() {
    private val findBookUseCase: FindBookUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.findBookUseCase
    }

    private val updateBookUseCase: UpdateBookUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.updateBookUseCase
    }

    private val bookId: Long by lazy {
        intent.getLongExtra("bookId", -1)
    }

    private lateinit var book: Book

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            book = findBookUseCase.execute(bookId)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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
                                Text("Editar " + book.title)
                            },
                        )
                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        )
                        {
                            BookForm(
                                book,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                onSubmit = { payload ->
                                    tryUpdateBook(
                                        this,
                                        book,
                                        payload,
                                        updateBookUseCase,
                                    )
                                },
                                submitButton = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = stringResource(id = R.string.edit_content_description),
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .size(20.dp),
                                    )
                                    Text(text = "Salvar")
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

private fun tryUpdateBook(
    context: Activity,
    book: Book,
    payload: BookChangePayload,
    updateBookUseCase: UpdateBookUseCase,
) {
    try {
        updateBookUseCase.execute(book, payload)
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return
    }

    Toast.makeText(context, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
    context.finish()
}

