package com.project.achadoseperdidos.model

import java.time.LocalDate

enum class TipoItem {
    PERDIDO,
    ENCONTRADO
}

enum class CategoriaItem {
    DOCUMENTO,
    ELETRONICO,
    CHAVE,
    ROUPA,
    LIVRO,
    OUTROS
}

data class Item(
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val data: LocalDate,
    val tipo: TipoItem,
    val localizacao: String? = null,
    val imagemUrl: String? = null,
    val usuario: User
) {}