package com.example.librarymanagementsystem.domain.usecase

import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.repository.ILibraryRepository

class LoadBeforeItemsUseCase(
    private val repository: ILibraryRepository
) {
    suspend operator fun invoke(current: List<LibraryItem>): Pair<List<LibraryItem>, Int> {
        return repository.loadBefore(current)
    }
}