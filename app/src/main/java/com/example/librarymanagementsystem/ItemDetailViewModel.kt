package com.example.librarymanagementsystem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ItemDetailViewModel : ViewModel() {

    // LiveData для текущего или создаваемого элемента
    private val _currentItem = MutableLiveData<LibraryItem?>()
    val currentItem: LiveData<LibraryItem?>
        get() = _currentItem

    // Установка существующего элемента для режима просмотра
    fun setItem(item: LibraryItem) {
        _currentItem.value = item
    }

    // Метод для создания нового элемента на основе введённых данных
    fun createNewItem(
        type: String,
        name: String,
        extra1: String,
        extra2: String
    ): LibraryItem {
        val newId = Random.nextInt(1000, 10000)
        val newItem: LibraryItem = when (type) {
            "Книга" -> {
                val pages = extra1.toIntOrNull() ?: 0
                Book(newId, true, name, pages, extra2)
            }
            "Газета" -> {
                val issue = extra1.toIntOrNull() ?: 0
                val month = extra2.toIntOrNull() ?: 1
                Newspaper(newId, true, name, issue, month)
            }
            "Диск" -> {
                val discType = if (extra1.equals("CD", true)) DiscType.CD else DiscType.DVD
                Disc(newId, true, name, discType)
            }
            else -> Book(newId, true, name, 0, "")
        }
        _currentItem.value = newItem
        return newItem
    }
}
