package vin.lucas.imdlibrary.ui.activities.books

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.IMDLibraryApplication
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.cases.books.DeleteBookUseCase
import vin.lucas.imdlibrary.contracts.cases.books.FindBookUseCase
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.ui.activities.AuthenticatedActivity
import vin.lucas.imdlibrary.ui.components.DataDisplay
import vin.lucas.imdlibrary.ui.components.ImageWithBlurredClone
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme
import java.util.Date

private val loadingBook = Book(
    id = -1,
    isbn = "Carregando...",
    title = "Carregando...",
    author = "Carregando...",
    publisher = "Carregando...",
    publicationYear = 0,
    summary = "Carregando...",
    coverUrl = "",
    createdAt = Date(System.currentTimeMillis()),
    updatedAt = Date(System.currentTimeMillis()),
)

class ShowBookActivity : AuthenticatedActivity() {
    private val findBookUseCase: FindBookUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.findBookUseCase
    }

    private val deleteBookUseCase: DeleteBookUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.deleteBookUseCase
    }

    private val bookId: Long by lazy {
        intent.getLongExtra("bookId", -1)
    }

    private val book = mutableStateOf(loadingBook)

    override fun onResume() {
        super.onResume()

        try {
            book.value = findBookUseCase.execute(bookId)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }
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
                                Text(book.value.title)
                            },
                        )
                    },
                    bottomBar = {
                        BottomAppBar (
                            actions = {
                                DeleteButton(onConfirm = {
                                    tryDeleteBook(
                                        this@ShowBookActivity,
                                        book.value,
                                        deleteBookUseCase
                                    )
                                })
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = {
                                        val intent = Intent(this@ShowBookActivity, EditBookActivity::class.java)
                                        intent.putExtra("bookId", book.value.id)

                                        startActivity(intent)
                                    },
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = stringResource(R.string.edit_content_description),
                                    )
                                }
                            }
                        )
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState()),
                        )
                        {
                            ImageWithBlurredClone(
                                uri = book.value.coverUrl,
                                contentDescription = "Capa do livro ${book.value.title}",
                                containerModifier = Modifier
                                    .height(196.dp)
                                    .fillMaxWidth(),
                                imageModifier = Modifier.padding(16.dp),
                            )
                            Column (
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                            ) {
                                DataDisplay(label = "ISBN", value = book.value.isbn)
                                DataDisplay(label = "Título", value = book.value.title)
                                DataDisplay(label = "Autor", value = book.value.author)
                                DataDisplay(label = "Editora", value = book.value.publisher)
                                DataDisplay(
                                    label = "Ano de Publicação",
                                    value = book.value.publicationYear.toString(),
                                )
                                DataDisplay(
                                    label = "Resumo",
                                    value = book.value.summary,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DeleteButton(onConfirm: () -> Unit) {
    val openDeleteDialog = remember { mutableStateOf(false) }

    IconButton(
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.error,
        ),
        onClick = { openDeleteDialog.value = true },
    ) {
        when {
            openDeleteDialog.value -> {
                DeleteDialog(
                    onDismissRequest = { openDeleteDialog.value = false },
                    onConfirm = {
                        onConfirm()
                        openDeleteDialog.value = false
                    },
                )
            }
        }

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_content_description),
        )
    }
}

@Composable
private fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete_content_description),
            )
        },
        title = {
            Text("Confirmar Remoção")
        },
        text = {
            Text("Tem certeza que deseja remover este livro? Essa ação é irreversível.")
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Text("Remover")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}

private fun tryDeleteBook(
    activity: ShowBookActivity,
    book: Book,
    deleteBookUseCase: DeleteBookUseCase,
) {
    try {
        deleteBookUseCase.execute(book)
    } catch (e: IllegalArgumentException) {
        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
        return
    }

    Toast.makeText(activity, "Livro removido com sucesso", Toast.LENGTH_SHORT).show()
    activity.finish()
}
