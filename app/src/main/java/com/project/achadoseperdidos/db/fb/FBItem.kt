package com.project.achadoseperdidos.db.fb

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.Item
import com.project.achadoseperdidos.model.TipoItem

class FBItem {
    var id: String? = null
    var titulo: String? = null
    var descricao: String? = null
    var categoria: String? = null
    var data: com.google.firebase.Timestamp? = null
    var tipo: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var recuperado: Boolean? = false
    var imagemUrl: String? = null
    var userId: String? = null

    fun toItem(): Item {
        return Item(
            id = id ?: "",
            titulo = titulo ?: "",
            descricao = descricao ?: "",
            categoria = CategoriaItem.valueOf(categoria ?: "SELECIONAR"),
            data = data ?:Timestamp.now(),
            tipo = TipoItem.valueOf(tipo ?: "DEFAULT"),
            localizacao = if (lat != null && lng != null) LatLng(lat!!, lng!!) else null,
            imagemUrl = imagemUrl,
            recuperado = recuperado == true,
            userId = userId ?: ""
        )
    }
}

fun Item.toFBItem(): FBItem {
    val fbItem = FBItem()
    fbItem.id = this.id
    fbItem.titulo = this.titulo
    fbItem.descricao = this.descricao
    fbItem.categoria = this.categoria.name
    fbItem.data = this.data
    fbItem.tipo = this.tipo.name
    fbItem.lat = this.localizacao?.latitude
    fbItem.lng = this.localizacao?.longitude
    fbItem.imagemUrl = this.imagemUrl
    fbItem.recuperado = this.recuperado
    fbItem.userId = this.userId
    return fbItem
}