package com.project.achadoseperdidos.db.fb

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.project.achadoseperdidos.model.CategoriaItem
import com.project.achadoseperdidos.model.Item

class FBDatabase {
    interface Listener {
        fun onUserLoaded(user: FBUser)
        fun onUserSignOut()
        fun onItemAdded(item: FBItem)
        fun onItemUpdated(item: FBItem)
        fun onItemRemoved(item: FBItem)
    }

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var itensListReg: ListenerRegistration? = null
    private var listener : Listener? = null

    init {
        auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                itensListReg?.remove()
                listener?.onUserSignOut()
                return@addAuthStateListener
            }

            val refCurrUser = db.collection("users").document(auth.currentUser!!.uid)
            refCurrUser.get().addOnSuccessListener {
                it.toObject(FBUser::class.java)?.let { user ->
                    listener?.onUserLoaded(user)
                }
            }

            itensListReg = refCurrUser.collection("itens")
                .addSnapshotListener { snapshots, ex ->
                    if (ex != null) return@addSnapshotListener

                    snapshots?.documentChanges?.forEach { change ->
                        val fbItem = change.document.toObject(FBItem::class.java)
                        if (change.type == DocumentChange.Type.ADDED) {
                            listener?.onItemAdded(fbItem)
                        } else if (change.type == DocumentChange.Type.MODIFIED) {
                            listener?.onItemUpdated(fbItem)
                        } else if (change.type == DocumentChange.Type.REMOVED) {
                            listener?.onItemRemoved(fbItem)
                        }
                    }
                }
        }
    }

    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }

    fun register(user: FBUser) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid + "").set(user)
    }

    fun add(item: FBItem) {
        if (auth.currentUser == null) {
            throw RuntimeException("User not logged in!")
        }

        if (item.id == null) throw RuntimeException("Item ID is null")

        val uid = auth.currentUser!!.uid
        val docRef = db.collection("users").document(uid)
            .collection("itens")
            .document()
        item.id = docRef.id

        docRef.set(item)

    }

    fun remove(item: FBItem) {
        if (auth.currentUser == null) {
            throw RuntimeException("User not logged in!")
        }

        if (item.id == null) throw RuntimeException("Item ID is null")

        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid)
            .collection("itens").document(item.id!!).delete()
    }

    fun update(item: FBItem) {
        if (auth.currentUser == null) throw RuntimeException("Not logged in")
        val uid = auth.currentUser!!.uid

        val changes = mapOf(
            "titulo" to item.titulo,
            "descricao" to item.descricao,
            "tipo" to item.tipo,
            "categoria" to item.categoria,
            "recuperado" to item.recuperado,
            "lat" to item.lat,
            "lng" to item.lng,
            "userId" to item.userId
        )

        db.collection("users").document(uid)
            .collection("itens")
            .document(item.id!!)
            .update(changes)
    }

    fun searchItemsByArea(
        center: LatLng,
        margin: Double,
        categoria: CategoriaItem,
        onResult: (List<FBItem>) -> Unit
    ) {
        val latMin = center.latitude - margin
        val latMax = center.latitude + margin
        val lngMin = center.longitude - margin
        val lngMax = center.longitude + margin

        db.collectionGroup("itens")
            .whereGreaterThanOrEqualTo("lat", latMin)
            .whereLessThanOrEqualTo("lat", latMax)
            .get()
            .addOnSuccessListener { snapshot ->
                val result = snapshot.documents
                    .mapNotNull { it.toObject(FBItem::class.java) }
                    .filter { fbItem ->
                        fbItem.lng != null &&
                                fbItem.lng!! in lngMin..lngMax &&
                                fbItem.categoria == categoria.name &&
                                fbItem.recuperado == false
                    }
                onResult(result)
            }
    }


}