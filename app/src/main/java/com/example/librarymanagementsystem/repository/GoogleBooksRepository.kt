package com.example.librarymanagementsystem.repository

import com.example.librarymanagementsystem.data.model.GoogleBook
import com.example.librarymanagementsystem.data.remote.RemoteModule
import com.example.librarymanagementsystem.data.remote.VolumeResponse

class GoogleBooksRepository {
    private val api = RemoteModule.booksApi

    suspend fun search(author: String?, title: String?): List<GoogleBook> {
        val parts = mutableListOf<String>()
        author?.takeIf { it.length >= 3 }?.let { parts += "inauthor:$it" }
        title?.takeIf  { it.length >= 3 }?.let { parts += "intitle:$it" }
        if (parts.isEmpty()) return emptyList()
        val q = parts.joinToString("+")
        val resp: VolumeResponse = api.searchVolumes(query = q)
        return resp.items.orEmpty().mapNotNull { item ->
            val info = item.info ?: return@mapNotNull null
            val isbn = info.identifiers
                ?.firstOrNull { it.type == "ISBN_13" }
                ?.identifier
                ?: info.identifiers?.firstOrNull()?.identifier
                ?: item.id
            GoogleBook(
                id = isbn ?: item.id ?: "",
                title = info.title.orEmpty(),
                authors = info.authors?.joinToString(", ") ?: "Unknown",
                pageCount = info.pageCount ?: 0
            )
        }
    }
}
