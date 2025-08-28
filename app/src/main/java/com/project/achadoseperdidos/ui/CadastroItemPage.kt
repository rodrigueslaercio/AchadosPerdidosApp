package com.project.achadoseperdidos.ui

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.project.achadoseperdidos.db.fb.FBDatabase
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.Item
import com.project.achadoseperdidos.model.MainViewModel
import com.project.achadoseperdidos.model.MainViewModelFactory
import com.project.achadoseperdidos.model.TipoItem

@Composable
fun CadastroItemPage(lat: Double, lng: Double, onBack: () -> Unit, fbDatabase: FBDatabase, viewModel: MainViewModel) {
//    val fbDB = remember { FBDatabase() }
//    val viewModel: MainViewModel = viewModel(
//        factory = MainViewModelFactory(fbDB)
//    )
    val activity = LocalContext.current as? Activity
    val currentUser = viewModel.user
    val item = viewModel.item
    val ehEdicao = viewModel.item != null

    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoItem.PERDIDO) }
    var categoria by remember { mutableStateOf(CategoriaItem.OUTROS) }
    var recuperado by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        item?.let {
            titulo = it.titulo
            descricao = it.descricao
            tipo = it.tipo
            categoria = it.categoria
        }
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cadastre um item",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(24.dp))

        TipoSelector(
            selectedTipoItem = tipo,
            onTipoItemSelected = { tipo = it }
        )

        Spacer(modifier = Modifier.size(24.dp))

        CategoriaDropdown(
            selectedCategoria = categoria,
            onCategoriaSelected = { categoria = it }
        )

        Spacer(modifier = Modifier.size(24.dp))

        if (ehEdicao) {
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Item recuperado?")
                Spacer(modifier = Modifier.size(8.dp))
                Button(onClick = { recuperado = !recuperado }) {
                    Text(if (recuperado) "Sim" else "Não")
                }
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        if (!ehEdicao) {
            Row() {
                Button(
                    onClick = {
                        if (currentUser != null) {
                            val novoItem = Item(
                                titulo = titulo,
                                descricao = descricao,
                                categoria = categoria,
                                data = Timestamp.now(),
                                tipo = tipo,
                                localizacao = LatLng(lat, lng),
                                imagemUrl = null,
                                recuperado = false,
                                userId = currentUser.phone
                            )
                            viewModel.add(novoItem)
                        } else {
                            println("Usuário não logado")
                        }
                        Toast.makeText(activity,
                            "Registro OK!", Toast.LENGTH_LONG).show()
                    },

                    ) {
                    Text("Cadastrar")
                }
            }
        } else {
            Row {
                Button(
                    onClick = {
                        if (currentUser != null && item != null) {
                            val itemAtualizado = Item(
                                id = item.id,
                                titulo = titulo,
                                descricao = descricao,
                                categoria = categoria,
                                data = item.data,
                                tipo = tipo,
                                localizacao = LatLng(lat, lng),
                                imagemUrl = item.imagemUrl,
                                recuperado = recuperado,
                                userId = currentUser.phone
                            )
                            viewModel.update(itemAtualizado)
                        } else {
                            println("Usuário não logado ou item nulo")
                        }

                        Toast.makeText(
                            activity,
                            "Item atualizado!",
                            Toast.LENGTH_LONG
                        ).show()
                        onBack()
                    }
                ) {
                    Text("Atualizar")
                }
            }
        }
    }
}