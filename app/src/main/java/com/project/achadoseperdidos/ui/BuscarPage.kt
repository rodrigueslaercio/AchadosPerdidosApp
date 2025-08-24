package com.project.achadoseperdidos.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.MainViewModel

@Composable
fun BuscarPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val activity = LocalContext.current as? Activity
    var pesquisa by rememberSaveable { mutableStateOf("") }
    var categoria by remember { mutableStateOf(CategoriaItem.SELECIONAR) }
    val isButtonEnabled = pesquisa.isNotBlank() && categoria != CategoriaItem.SELECIONAR

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // espaço para o botão
        ) {
            OutlinedTextField(
                value = pesquisa,
                onValueChange = { pesquisa = it },
                placeholder = { Text("Endereço...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White)
            )

            CategoriaDropdown(
                selectedCategoria = categoria,
                onCategoriaSelected = { categoria = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de resultados ocupa toda a área restante
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.resultadosPesquisa, key = { it.titulo }) { item ->
                    ItemPesquisaComposable(item = item, onClick = {
                        // ação ao clicar
                    })
                }
            }
        }

        // Botão fixo na parte inferior
        Button(
            onClick = {
                activity?.let {
                    viewModel.search(pesquisa, categoria, it)
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(200.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("Pesquisar")
        }
    }
}
