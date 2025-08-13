package com.project.achadoseperdidos.db.fb

import com.google.android.gms.maps.model.LatLng
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.Item
import com.project.achadoseperdidos.model.TipoItem
import java.time.LocalDate

class FBItem {
    var titulo: String? = null
    var descricao: String? = null
    var categoria: CategoriaItem? = null
    var data: LocalDate? = null
    var tipo: TipoItem?= null
    var lat : Double? = null
    var lng : Double? = null

    var recuperado: Boolean = false

    fun toItem() : Item {
        val latlng = if (lat!=null&&lng!=null) LatLng(lat!!, lng!!) else null
        return Item(titulo!!, descricao!!, categoria!!,
            data!!, tipo!!, localizacao = latlng,
            null, recuperado
        );
    }
}

fun Item.toFBItem() : FBItem {
    val fbItem = FBItem()

    fbItem.titulo = this.titulo
    fbItem.descricao = this.descricao
    fbItem.categoria = this.categoria
    fbItem.data = this.data
    fbItem.lat = this.localizacao?.latitude ?: 0.0
    fbItem.lng = this.localizacao?.longitude ?: 0.0
    fbItem.recuperado = this.recuperado

    return fbItem
}