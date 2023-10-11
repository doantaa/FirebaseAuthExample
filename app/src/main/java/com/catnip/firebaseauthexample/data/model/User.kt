package com.catnip.firebaseauthexample.data.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val fullName: String,
    val email: String
)

fun FirebaseUser?.toUser() = if(this != null) {
    User(
        fullName = this.displayName.orEmpty(),
        email = this.email.orEmpty()
    )
} else null

