package com.example.librarymanagementsystem.repository

import android.content.SharedPreferences
import com.example.librarymanagementsystem.data.db.LibraryDao
import com.example.librarymanagementsystem.data.db.toDomain
import com.example.librarymanagementsystem.data.db.toEntity
import com.example.librarymanagementsystem.domain.LibraryItem

class LibraryRepository(
    private val dao: LibraryDao,
    private val prefs: SharedPreferences
) {
    private val SCREEN_COUNT = 3
    private val APPROX_ON_SCREEN = 10
    private val N = SCREEN_COUNT * APPROX_ON_SCREEN
    private val HALF = N / 2

    var sortByName: Boolean
        get() = prefs.getBoolean("sortByName", true)
        set(v) = prefs.edit().putBoolean("sortByName", v).apply()

    suspend fun initialLoad(): List<LibraryItem> {
        val entities = if (sortByName) {
            dao.loadInitialByName(N)
        } else {
            dao.loadInitialByDate(N)
        }
        return entities.map { it.toDomain() }
    }

    suspend fun loadAfter(current: List<LibraryItem>): Pair<List<LibraryItem>, Int> {
        if (current.isEmpty()) return emptyList<LibraryItem>() to 0
        val last = current.last()
        val entities = if (sortByName) {
            dao.loadAfterByName(last.id, HALF)
        } else {
            dao.loadAfterByDate(last.id, HALF)
        }
        return entities.map { it.toDomain() } to HALF
    }

    suspend fun loadBefore(current: List<LibraryItem>): Pair<List<LibraryItem>, Int> {
        if (current.isEmpty()) return emptyList<LibraryItem>() to 0
        val first = current.first()
        val entities = if (sortByName) {
            dao.loadBeforeByName(first.id, HALF)
        } else {
            dao.loadBeforeByDate(first.id, HALF)
        }
        return entities.map { it.toDomain() } to HALF
    }

    suspend fun add(item: LibraryItem) {
        dao.insert(item.toEntity())
    }
    suspend fun remove(item: LibraryItem) {
        dao.delete(item.toEntity())
    }
}
