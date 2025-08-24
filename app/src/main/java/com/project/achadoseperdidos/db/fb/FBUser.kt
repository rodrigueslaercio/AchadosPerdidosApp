package com.project.achadoseperdidos.db.fb

import com.project.achadoseperdidos.model.User

class FBUser {
    var name: String? = null
    var email: String? = null
    var phone: String? = null

    fun toUser() = User(name!!, email!!, phone ?: "")
}

fun User.toFBUser(): FBUser {
    val fbUser = FBUser()
    fbUser.name = this.name
    fbUser.email = this.email
    fbUser.phone = this.phone
    return fbUser
}
