package com.project.achadoseperdidos.ui

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.project.achadoseperdidos.model.CategoriaItem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaDropdown(
    selectedCategoria: CategoriaItem,
    onCategoriaSelected: (CategoriaItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categorias = CategoriaItem.values().toList()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedCategoria.name,
            onValueChange = { },
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.name) },
                    onClick = {
                        onCategoriaSelected(categoria)
                        expanded = false
                    }
                )
            }
        }
    }
}