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
fun CadastroItemPage(lat: Double, lng: Double, onBack: () -> Unit) {
    val fbDB = remember { FBDatabase() }
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(fbDB)
    )
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoItem.PERDIDO) }
    var categoria by remember { mutableStateOf(CategoriaItem.OUTROS) }
    val user = viewModel.user
    var localizacao by remember { mutableStateOf(viewModel.selectedMarkerPosition) }
    val activity = LocalContext.current as? Activity

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

        Row() {
            Button(
                onClick = {
                    val novoItem = Item(
                        titulo = titulo,
                        descricao = descricao,
                        categoria = categoria,
                        data = Timestamp.now(),
                        tipo = tipo,
                        localizacao = LatLng(lat, lng),
                        imagemUrl = null, // ou algum valor se já tiver upload de imagem
                        recuperado = false
                    )

                    viewModel.add(novoItem)

                    Toast.makeText(activity,
                        "Registro OK!", Toast.LENGTH_LONG).show()
                },

            ) {
                Text("Cadastrar")
            }
        }
    }
}