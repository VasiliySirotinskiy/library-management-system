package com.example.librarymanagementsystem.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("volumes")
    suspend fun searchVolumes(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("fields") fields: String = "items(id,volumeInfo/title,volumeInfo/authors,volumeInfo/pageCount,volumeInfo/industryIdentifiers)"
    ): VolumeResponse
}
