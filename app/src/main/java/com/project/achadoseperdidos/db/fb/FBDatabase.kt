package com.project.achadoseperdidos.db.fb

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

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

        if (item.titulo == null || item.titulo!!.isEmpty()) {
            throw RuntimeException("Item with null or empty name!")
        }

        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid)
            .collection("itens").document(item.titulo!!).set(item)

    }

    fun remove(item: FBItem) {
        if (auth.currentUser == null) {
            throw RuntimeException("User not logged in!")
        }

        if (item.titulo == null || item.titulo!!.isEmpty()) {
            throw RuntimeException("Item with null or empty name!")
        }

        val uid = auth.currentUser!!.uid
        db.collection("itens").document(uid)
            .collection("itens").document(item.titulo!!).delete()
    }


}