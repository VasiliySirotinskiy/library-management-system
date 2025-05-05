package com.example.librarymanagementsystem

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.librarymanagementsystem.data.db.LibraryDatabase
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.repository.LibraryRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = LibraryDatabase.get(app).dao
    private val prefs = app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val repo = LibraryRepository(dao, prefs)

    private val _items = MutableLiveData<List<LibraryItem>>(emptyList())
    val items: LiveData<List<LibraryItem>> = _items

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var current = mutableListOf<LibraryItem>()

    init { loadInitial() }

    fun loadInitial() = viewModelScope.launch {
        _loading.value = true
        try {
            current = repo.initialLoad().toMutableList()
            _items.value = current
        } catch (e: Exception) {
            _error.value = e.message
        }
        _loading.value = false
    }

    fun toggleSort(byName: Boolean) = viewModelScope.launch {
        repo.sortByName = byName
        loadInitial()
    }

    fun loadMoreIfNeeded(position: Int) = viewModelScope.launch {
        if (_loading.value == true) return@launch
        val size = current.size
        // вниз
        if (position >= size - 10) {
            _loading.value = true
            val (newItems, removed) = repo.loadAfter(current)
            if (newItems.isNotEmpty()) {
                repeat(minOf(removed, current.size)) { current.removeAt(0) }
                current.addAll(newItems)
                _items.value = current
            }
            _loading.value = false
        }
        // вверх
        else if (position <= 10) {
            _loading.value = true
            val (newItems, removed) = repo.loadBefore(current)
            if (newItems.isNotEmpty()) {
                repeat(minOf(removed, current.size)) { current.removeAt(current.lastIndex) }
                current.addAll(0, newItems)
                _items.value = current
            }
            _loading.value = false
        }
    }

    fun add(item: LibraryItem) = viewModelScope.launch {
        _loading.value = true
        try {
            repo.add(item)
            loadInitial()
        } catch (e: Exception) {
            _error.value = e.message
            _loading.value = false
        }
    }

    fun remove(item: LibraryItem) = viewModelScope.launch {
        _loading.value = true
        try {
            repo.remove(item)
            loadInitial()
        } catch (e: Exception) {
            _error.value = e.message
            _loading.value = false
        }
    }
}
