package com.example.librarymanagementsystem.data.repository

import android.content.SharedPreferences
import com.example.librarymanagementsystem.data.db.LibraryDao
import com.example.librarymanagementsystem.data.db.toDomain
import com.example.librarymanagementsystem.data.db.toEntity
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.repository.ILibraryRepository

class LibraryRepositoryImpl(
    private val dao: LibraryDao,
    private val prefs: SharedPreferences
) : ILibraryRepository {

    companion object {
        private const val SCREEN_COUNT = 3
        private const val APPROX_ON_SCREEN = 10
        private const val PREF_SORT_BY_NAME = "sortByName"
        private val N = SCREEN_COUNT * APPROX_ON_SCREEN
        private val HALF = N / 2
    }

    override var sortByName: Boolean
        get() = prefs.getBoolean(PREF_SORT_BY_NAME, true)
        set(value) = prefs.edit().putBoolean(PREF_SORT_BY_NAME, value).apply()

    override suspend fun initialLoad(): List<LibraryItem> {
        val entities = if (sortByName) {
            dao.loadInitialByName(N)
        } else {
            dao.loadInitialByDate(N)
        }
        return entities.map { it.toDomain() }
    }

    override suspend fun loadAfter(
        current: List<LibraryItem>
    ): Pair<List<LibraryItem>, Int> {
        if (current.isEmpty()) return emptyList<LibraryItem>() to 0
        val lastId = current.last().id
        val entities = if (sortByName) {
            dao.loadAfterByName(lastId, HALF)
        } else {
            dao.loadAfterByDate(lastId, HALF)
        }
        return entities.map { it.toDomain() } to HALF
    }

    override suspend fun loadBefore(
        current: List<LibraryItem>
    ): Pair<List<LibraryItem>, Int> {
        if (current.isEmpty()) return emptyList<LibraryItem>() to 0
        val firstId = current.first().id
        val entities = if (sortByName) {
            dao.loadBeforeByName(firstId, HALF)
        } else {
            dao.loadBeforeByDate(firstId, HALF)
        }
        return entities.map { it.toDomain() } to HALF
    }

    override suspend fun add(item: LibraryItem) {
        dao.insert(item.toEntity())
    }

    override suspend fun remove(item: LibraryItem) {
        dao.delete(item.toEntity())
    }
}
