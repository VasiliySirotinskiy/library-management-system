package com.example.librarymanagementsystem.domain.repository

import com.example.librarymanagementsystem.domain.model.GoogleBook

interface IGoogleBooksRepository {
    suspend fun search(author: String?, title: String?): List<GoogleBook>
}