package com.project.achadoseperdidos.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.achadoseperdidos.db.fb.FBDatabase
import com.project.achadoseperdidos.db.fb.FBUser

class MainViewModel(private val db: FBDatabase) : ViewModel(), FBDatabase.Listener {
    private val _items = mutableListOf<Item>()
    val items
        get() = _items.toList()

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    fun remove(item: Item) {
        _items.remove(item)
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        TODO("Not yet implemented")
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