package com.project.achadoseperdidos.model

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _items = mutableListOf<Item>()
    val items
        get() = _items.toList()

    fun remove(item: Item) {
        _items.remove(item)
    }


}