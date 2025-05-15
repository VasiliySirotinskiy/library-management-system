package com.example.librarymanagementsystem.domain.usecase

import com.example.librarymanagementsystem.domain.repository.ILibraryRepository

class ToggleSortUseCase(
    private val repository: ILibraryRepository
) {
    operator fun invoke(byName: Boolean) {
        repository.sortByName = byName
    }
}