package vin.lucas.imdlibrary.ui.activities.auth

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.ui.activities.MainActivity
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme

class SignInActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences(
            getString(R.string.auth_shared_preferences_key),
            MODE_PRIVATE
        )

        if (sharedPref.contains(getString(R.string.auth_shared_preferences_username_key))) {
            Toast.makeText(this, "Você já está logado", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                            title = {
                                Text("Entrar")
                            },
                        )
                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        )
                        {
                            SignInForm(
                                this,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SignInForm(
    context: ComponentActivity,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
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
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text("Senha") },
        )
        TextButton(
            onClick = {
                context.startActivity(Intent(context, ForgotActivity::class.java))
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = stringResource(id = R.string.forgot_content_description),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(16.dp),
            )
            Text(
                text = "Esqueceu sua senha ou login?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { authenticate(username, password, context) },
        ) {
            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = stringResource(id = R.string.sign_in_content_description),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
            )
            Text(text = "Entrar")
        }
        Spacer(modifier = Modifier.padding(4.dp))
        TextButton(
            onClick = {
                context.startActivity(Intent(context, SignUpActivity::class.java))
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.sign_up_content_description),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(16.dp),
            )
            Text(
                text = "Ainda não tem uma conta?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

fun authenticate(username: String, password: String, context: ComponentActivity) {
    val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.auth_shared_preferences_key),
        MODE_PRIVATE,
    )

    with(sharedPreferences.edit()) {
        putString(context.getString(R.string.auth_shared_preferences_username_key), username)
        apply()
    }

    Toast.makeText(context, "Autenticado com sucesso!", Toast.LENGTH_SHORT).show()

    context.startActivity(Intent(context, MainActivity::class.java))
}
