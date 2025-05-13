package com.example.librarymanagementsystem.domain.usecase

import com.example.librarymanagementsystem.domain.model.GoogleBook
import com.example.librarymanagementsystem.domain.repository.IGoogleBooksRepository

class SearchGoogleBooksUseCase(
    private val repository: IGoogleBooksRepository
) {
    suspend operator fun invoke(author: String?, title: String?): List<GoogleBook> {
        return repository.search(author, title)
    }
}