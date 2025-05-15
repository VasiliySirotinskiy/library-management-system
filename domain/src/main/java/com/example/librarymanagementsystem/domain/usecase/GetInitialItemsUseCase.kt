package com.example.librarymanagementsystem.domain.usecase

import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.repository.ILibraryRepository

class GetInitialItemsUseCase(private val repo: ILibraryRepository) {
    suspend operator fun invoke(): List<LibraryItem> = repo.initialLoad()
}