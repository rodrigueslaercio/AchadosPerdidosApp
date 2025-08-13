package com.project.achadoseperdidos.model

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

data class Item(
    val titulo: String,
    val descricao: String,
    val categoria: CategoriaItem,
    val data: LocalDate,
    val tipo: TipoItem,
    val localizacao: LatLng? = null,
    val imagemUrl: String? = null,
    val recuperado: Boolean = false,
) {}