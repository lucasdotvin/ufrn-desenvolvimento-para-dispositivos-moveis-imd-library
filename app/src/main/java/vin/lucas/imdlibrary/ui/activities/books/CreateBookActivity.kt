package vin.lucas.imdlibrary.ui.activities.books

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import vin.lucas.imdlibrary.contracts.cases.books.CreateBookUseCase
import vin.lucas.imdlibrary.ui.activities.AuthenticatedActivity
import vin.lucas.imdlibrary.ui.partials.BookForm
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme
import vin.lucas.imdlibrary.values.BookChangePayload

class CreateBookActivity : AuthenticatedActivity() {
    private val createBookUseCase: CreateBookUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.createBookUseCase
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
                                Text("Cadastrar Livro")
                            },
                        )
                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        )
                        {
                            BookForm(
                                null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                onSubmit = { payload ->
                                    tryCreateBook(
                                        this,
                                        payload,
                                        createBookUseCase,
                                    )
                                },
                                submitButton = {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = stringResource(id = R.string.add_content_description),
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .size(20.dp),
                                    )
                                    Text(text = "Cadastrar")
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

fun tryCreateBook(
    context: Activity,
    payload: BookChangePayload,
    createBookUseCase: CreateBookUseCase,
) {
    try {
        createBookUseCase.execute(payload)
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return
    }

    Toast.makeText(context, "Livro cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
    context.finish()
}
