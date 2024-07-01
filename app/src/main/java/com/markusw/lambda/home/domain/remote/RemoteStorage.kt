package com.markusw.lambda.home.domain.remote

typealias ImageUrl = String

interface RemoteStorage {
    suspend fun uploadImage(uriString: String): ImageUrl
}