package com.project.achadoseperdidos.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.ui.unit.sp
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.Item
import com.project.achadoseperdidos.model.MainViewModel

@Composable
fun BuscarPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val activity = LocalContext.current as? Activity
    var pesquisa by rememberSaveable { mutableStateOf("") }
    var categoria by remember { mutableStateOf(CategoriaItem.SELECIONAR) }

    val isButtonEnabled = pesquisa.isNotBlank() && categoria != CategoriaItem.SELECIONAR

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .wrapContentSize(Alignment.TopStart)
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

        Button(
            onClick = {
                activity?.let {
                    viewModel.search(pesquisa, categoria, it)
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .width(200.dp)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Pesquisar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de resultados
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.resultadosPesquisa, key = { it.titulo }) { item ->
                ItemPesquisaComposable(item = item, onClick = {
                    // Aqui você pode adicionar ação ao clicar no item
                })
            }
        }
    }
}

@Composable
fun ItemPesquisaComposable(
    item: Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.FavoriteBorder,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = modifier.weight(1f)) {
            Text(
                text = item.titulo,
                fontSize = 20.sp
            )
        }
    }
}
