package com.markusw.lambda.home.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.markusw.lambda.home.domain.remote.ImageUrl
import com.markusw.lambda.home.domain.remote.RemoteStorage
import kotlinx.coroutines.tasks.await

class FirebaseStorageService(
    private val firebaseStorage: FirebaseStorage
): RemoteStorage {

    private val storageRef = firebaseStorage.reference

    override suspend fun uploadImage(uriString: String): ImageUrl {
        val imageUri = Uri.parse(uriString)

        return storageRef
            .child("images/${imageUri.lastPathSegment}")
            .putFile(imageUri)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()
    }

}