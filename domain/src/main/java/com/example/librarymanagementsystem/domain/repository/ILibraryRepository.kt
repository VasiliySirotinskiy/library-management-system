package com.example.librarymanagementsystem.domain.repository

import com.example.librarymanagementsystem.domain.LibraryItem

interface ILibraryRepository {
    var sortByName: Boolean

    suspend fun initialLoad(): List<LibraryItem>
    suspend fun loadAfter(current: List<LibraryItem>): Pair<List<LibraryItem>, Int>
    suspend fun loadBefore(current: List<LibraryItem>): Pair<List<LibraryItem>, Int>
    suspend fun add(item: LibraryItem)
    suspend fun remove(item: LibraryItem)
}