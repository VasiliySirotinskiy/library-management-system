package com.example.librarymanagementsystem.domain.usecase

import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.repository.ILibraryRepository

class AddItemUseCase(
    private val repository: ILibraryRepository
) {
    suspend operator fun invoke(item: LibraryItem) {
        repository.add(item)
    }
}