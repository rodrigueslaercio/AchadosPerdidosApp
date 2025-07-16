package com.project.achadoseperdidos.model

import java.time.LocalDate

data class Item(
    val titulo: String,
    val descricao: String,
    val categoria: CategoriaItem,
    val data: LocalDate,
    val tipo: TipoItem,
    val localizacao: String? = null,
    val imagemUrl: String? = null,
    val recuperado: Boolean = false,
    val usuario: User
) {}