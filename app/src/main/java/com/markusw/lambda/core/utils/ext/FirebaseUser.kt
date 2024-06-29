package com.markusw.lambda.core.utils.ext

import com.google.firebase.auth.FirebaseUser
import com.markusw.lambda.core.domain.model.User

fun FirebaseUser.toDomainModel(): User {
    return User(
        id = this.uid,
        displayName = this.displayName ?: "Guest",
        email = this.email ?: "example@email.com",
        photoUrl = this.photoUrl.toString()
    )
}