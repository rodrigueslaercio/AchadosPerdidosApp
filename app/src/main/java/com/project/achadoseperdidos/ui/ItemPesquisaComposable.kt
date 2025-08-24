package com.project.achadoseperdidos.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.achadoseperdidos.model.Item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder

@Composable
fun ItemPesquisaComposable(
    item: Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = modifier.weight(1f)) {
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
    }
}
