package com.project.achadoseperdidos

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.achadoseperdidos.db.fb.FBDatabase
import com.project.achadoseperdidos.db.fb.toFBUser
import com.project.achadoseperdidos.model.User
import com.project.achadoseperdidos.ui.theme.AchadosEPerdidosTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AchadosEPerdidosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var repetirSenha by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") } // novo campo
    val activity = LocalContext.current as? Activity

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registre-se", fontSize = 24.sp)

        OutlinedTextField(
            value = nome,
            label = { Text(text = "Digite seu nome") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { nome = it }
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = telefone,
            label = { Text(text = "Digite seu telefone") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { telefone = it }
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = senha,
            label = { Text(text = "Digite sua senha") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { senha = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = repetirSenha,
            label = { Text(text = "Repita sua senha") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { repetirSenha = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(modifier = modifier) {
            Button(
                onClick = {
                    if (telefone.isBlank() || !telefone.matches(Regex("^\\+?[0-9]{8,15}$"))) {
                        Toast.makeText(activity, "Digite um telefone válido!", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    Firebase.auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                FBDatabase().register(User(nome, email, telefone).toFBUser())
                                Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(activity, "Registro Falhou!", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()
                        && repetirSenha.isNotEmpty() && telefone.isNotEmpty())
                        && (senha == repetirSenha)
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.size(24.dp))

            Button(
                onClick = { nome = ""; email = ""; senha = ""; repetirSenha = ""; telefone = "" }
            ) {
                Text("Limpar")
            }
        }
    }
}
