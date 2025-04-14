package com.example.librarymanagementsystem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _libraryItems = MutableLiveData<MutableList<LibraryItem>>().apply {
        value = mutableListOf(
            Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
            Book(1001, true, "Война и мир", 1225, "Лев Толстой"),
            Newspaper(17245, true, "Сельская жизнь", 794, 4),
            Newspaper(17246, true, "Новости дня", 123, 2),
            Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
            Disc(2002, true, "Лучшие хиты", DiscType.CD)
        )
    }
    val libraryItems: LiveData<MutableList<LibraryItem>>
        get() = _libraryItems

    // Метод для добавления нового элемента
    fun addLibraryItem(newItem: LibraryItem) {
        _libraryItems.value?.let { list ->
            list.add(newItem)
            _libraryItems.value = list
        }
    }

    // Метод для удаления элемента по индексу
    fun removeLibraryItem(position: Int) {
        _libraryItems.value?.let { list ->
            if (position in list.indices) {
                list.removeAt(position)
                _libraryItems.value = list
            }
        }
    }
}
