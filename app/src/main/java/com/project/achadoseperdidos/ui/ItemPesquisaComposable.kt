package com.project.achadoseperdidos.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.achadoseperdidos.model.Item
import java.text.SimpleDateFormat
import java.util.*
import coil.compose.rememberAsyncImagePainter


@Composable
fun ItemPesquisaComposable(
    item: Item,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true }
    ) {
        Text(
            text = item.titulo ?: "Sem título",
            fontSize = 20.sp
        )
        if (!item.descricao.isNullOrBlank()) {
            Text(
                text = item.descricao,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Fechar")
                }
            },
            title = {
                Text(item.titulo ?: "Sem título")
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Data formatada
                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    Text(
                        text = "Encontrado em: ${formatter.format(item.data.toDate())}",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Imagem 400x400
                    item.imagemUrl?.let { url ->
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = "Imagem do item",
                            modifier = Modifier.size(400.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Telefone do usuário
                    Text(
                        text = "Telefone: ${item.userId}",
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
}
