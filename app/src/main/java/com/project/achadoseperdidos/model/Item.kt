package com.project.achadoseperdidos.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descricao: String,
    val categoria: CategoriaItem,
    val data: Timestamp,
    val tipo: TipoItem,
    val localizacao: LatLng? = null,
    val imagemUrl: String? = null,
    val recuperado: Boolean = false,
    val userId: String
) {}