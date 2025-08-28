package com.project.achadoseperdidos.model

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.project.achadoseperdidos.db.fb.FBDatabase
import com.project.achadoseperdidos.db.fb.FBItem
import com.project.achadoseperdidos.db.fb.FBUser
import com.project.achadoseperdidos.db.fb.toFBItem
import com.project.achadoseperdidos.ui.nav.Route

class MainViewModel(private val db: FBDatabase) : ViewModel(), FBDatabase.Listener {
    var resultadosPesquisa = mutableStateListOf<Item>()
        private set

    private val _items = mutableStateListOf<Item>()
    val items
        get() = _items.toList()

    private val _globalItems = mutableStateListOf<Item>()
    val globalItems: List<Item>
        get() = _globalItems.toList()

    private var _item = mutableStateOf<Item?>(null)
    var item: Item?
        get() = _item.value
        set(tmp) { _item.value = tmp?.copy() }

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    private val _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    private val _selectedMarkerPosition = mutableStateOf<LatLng?>(null)
    val selectedMarkerPosition: State<LatLng?> = _selectedMarkerPosition


    fun setSelectedMarkerPosition(position: LatLng) {
        _selectedMarkerPosition.value = position
    }

    init {
        db.setListener(this)
    }

    fun add(item: Item) {
        db.add(item.toFBItem())
    }

    fun update(item: Item) {
        db.update(item.toFBItem())
    }

    fun remove(item: Item) {
        _items.remove(item)
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        //TODO("Not yet implemented")
    }

    override fun onItemAdded(item: FBItem) {
        val newItem = item.toItem()
        if (_globalItems.none { it.id == newItem.id }) {
            _globalItems.add(newItem)
        }
        if (newItem.userId == _user.value?.phone) {
            if (_items.none { it.id == newItem.id }) {
                _items.add(newItem)
            }
        }
        _page.value = Route.Home
    }

    override fun onItemUpdated(item: FBItem) {
        val index = _items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _items[index] = item.toItem()
        }
    }

    override fun onItemRemoved(item: FBItem) {
        //TODO("Not yet implemented")
    }

    fun navigateToCadastroItem() {
        val pos = _selectedMarkerPosition.value
        if (pos != null) {
            _page.value = Route.Cadastro(pos.latitude, pos.longitude)
        }
    }

    fun clearSelectedPos() {
        _selectedMarkerPosition.value = null
    }

    fun clearItem() {
        _item.value = null
    }

    fun search(pesquisa: String, categoria: CategoriaItem, context: Context) {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocationName(pesquisa, 1)
        if (addresses.isNullOrEmpty()) return

        val location = LatLng(addresses[0].latitude, addresses[0].longitude)
        val margin = 0.01

        // Filtrar itens globais
        val resultados = _globalItems.filter { item ->
            val lat = item.localizacao?.latitude ?: return@filter false
            val lng = item.localizacao?.longitude ?: return@filter false
            val dentroMargem =
                (lat in (location.latitude - margin)..(location.latitude + margin)) &&
                        (lng in (location.longitude - margin)..(location.longitude + margin))
            val categoriaIgual = item.categoria == categoria
            val naoRecuperado = item.recuperado == false
            dentroMargem && categoriaIgual && naoRecuperado
        }

        resultadosPesquisa.clear()
        resultadosPesquisa.addAll(resultados)
    }
}

class MainViewModelFactory(private val db: FBDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}