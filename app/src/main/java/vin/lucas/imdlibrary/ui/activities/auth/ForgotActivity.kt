package vin.lucas.imdlibrary.ui.activities.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.IMDLibraryApplication
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.cases.ResetPasswordUseCase
import vin.lucas.imdlibrary.ui.activities.GuestActivity
import vin.lucas.imdlibrary.ui.input.transformation.CpfVisualTransformation
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme

class ForgotActivity : GuestActivity() {
    private val resetPasswordUseCase: ResetPasswordUseCase by lazy {
        (application as IMDLibraryApplication).serviceContainer.resetPasswordUseCase
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
                                        contentDescription = stringResource(R.string.back_content_description)
                                    )
                                }
                            },
                            title = {
                                Text("Redefinir Senha")
                            },
                        )
                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        )
                        {
                            ForgotForm(
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                onSubmit = { username, cpf, password ->
                                    tryResetPassword(
                                        this,
                                        username,
                                        cpf,
                                        password,
                                        resetPasswordUseCase,
                                    )
                                },
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ForgotForm(
    modifier: Modifier = Modifier,
    onSubmit: (username: String, cpf: String, password: String) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cpf,
            onValueChange = { it ->
                cpf = it.filter { it.isDigit() }.take(11)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CpfVisualTransformation(),
            label = { Text("CPF") },
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text("Nova Senha") },
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(username, cpf, password) },
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.forgot_content_description),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
            )
            Text(text = "Redefinir")
        }
    }
}

fun tryResetPassword(
    context: Activity,
    username: String,
    cpf: String,
    password: String,
    resetPasswordUseCase: ResetPasswordUseCase,
) {
    try {
        resetPasswordUseCase.execute(username, cpf, password)
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return
    }

    Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show()
    context.startActivity(Intent(context, SignInActivity::class.java))
    context.finish()
}
