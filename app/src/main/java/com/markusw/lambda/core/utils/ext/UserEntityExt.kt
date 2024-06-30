package com.markusw.lambda.core.utils.ext

import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.db.User_entity

fun User_entity.toDomainModel(): User {
    return User(
        id = this.id,
        email = this.email,
        photoUrl = this.photoUrl,
        displayName = this.displayName
    )
}