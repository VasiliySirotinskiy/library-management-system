package com.example.librarymanagementsystem

import androidx.lifecycle.*
import com.example.librarymanagementsystem.repository.LibraryRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _items = MutableLiveData<List<LibraryItem>>()
    val items: LiveData<List<LibraryItem>> = _items

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            try {
                _error.value = null
                val data = LibraryRepository.getItems()
                _items.value = data
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
            }
        }
    }

    fun addItem(item: LibraryItem) {
        viewModelScope.launch {
            try {
                LibraryRepository.addItem(item)
                loadItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun removeItem(item: LibraryItem) {
        viewModelScope.launch {
            try {
                LibraryRepository.removeItem(item)
                loadItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
