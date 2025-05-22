package com.example.librarymanagementsystem

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.model.GoogleBook
import com.example.librarymanagementsystem.domain.usecase.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getInitialUC: GetInitialItemsUseCase,
    private val loadAfterUC: LoadAfterItemsUseCase,
    private val loadBeforeUC: LoadBeforeItemsUseCase,
    private val addItemUC: AddItemUseCase,
    private val removeItemUC: RemoveItemUseCase,
    private val toggleSortUC: ToggleSortUseCase,
    private val searchGoogleUC: SearchGoogleBooksUseCase,
    private val prefs: SharedPreferences
) : ViewModel() {

    companion object {
        private const val PREF_SORT_BY_NAME = "sortByName"
    }

    // Состояние списка
    private val _items = MutableLiveData<List<LibraryItem>>(emptyList())
    val items: LiveData<List<LibraryItem>> = _items

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Состояние Google Books
    private val _gbResults = MutableLiveData<List<GoogleBook>>(emptyList())
    val gbResults: LiveData<List<GoogleBook>> = _gbResults

    private val _gbLoading = MutableLiveData(false)
    val gbLoading: LiveData<Boolean> = _gbLoading

    private val _gbError = MutableLiveData<String?>()
    val gbError: LiveData<String?> = _gbError

    // Внутренний буфер для пагинации
    private var current = mutableListOf<LibraryItem>()

    var sortByName: Boolean
        get() = prefs.getBoolean(PREF_SORT_BY_NAME, true)
        set(v) {
            prefs.edit().putBoolean(PREF_SORT_BY_NAME, v).apply()
        }

    init {
        loadInitial()
    }

    fun loadInitial() {
        viewModelScope.launch {
            _loading.value = true
            try {
                toggleSortUC(sortByName)
                current = getInitialUC().toMutableList()
                _items.value = current
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleSort(byName: Boolean) {
        viewModelScope.launch {
            sortByName = byName
            loadInitial()
        }
    }

    fun loadMoreIfNeeded(position: Int) {
        viewModelScope.launch {
            if (_loading.value == true) return@launch
            val size = current.size
            when {
                position >= size - 10 -> {
                    _loading.value = true
                    val (newItems, removed) = loadAfterUC(current)
                    if (newItems.isNotEmpty()) {
                        repeat(minOf(removed, current.size)) { current.removeAt(0) }
                        current.addAll(newItems)
                        _items.value = current
                    }
                    _loading.value = false
                }
                position <= 10 -> {
                    _loading.value = true
                    val (newItems, removed) = loadBeforeUC(current)
                    if (newItems.isNotEmpty()) {
                        repeat(minOf(removed, current.size)) { current.removeAt(current.lastIndex) }
                        current.addAll(0, newItems)
                        _items.value = current
                    }
                    _loading.value = false
                }
            }
        }
    }

    fun add(item: LibraryItem) {
        viewModelScope.launch {
            _loading.value = true
            try {
                addItemUC(item)
                loadInitial()
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun remove(item: LibraryItem) {
        viewModelScope.launch {
            _loading.value = true
            try {
                removeItemUC(item)
                loadInitial()
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun searchGoogleBooks(author: String?, title: String?) {
        viewModelScope.launch {
            _gbError.value = null
            _gbLoading.value = true
            try {
                _gbResults.value = searchGoogleUC(author, title)
            } catch (e: Exception) {
                _gbError.value = e.message
            } finally {
                _gbLoading.value = false
            }
        }
    }

    fun clearGoogleResults() {
        _gbResults.value = emptyList()
    }
}
