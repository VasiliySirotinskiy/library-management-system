package com.example.librarymanagementsystem

object MainViewModel {
    private val libraryItems = mutableListOf<LibraryItem>(
        Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
        Book(1001, true, "Война и мир", 1225, "Лев Толстой"),
        Newspaper(17245, true, "Сельская жизнь", 794, 4),
        Newspaper(17246, true, "Новости дня", 123, 2),
        Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
        Disc(2002, true, "Лучшие хиты", DiscType.CD)
    )

    fun getLibraryItems(): MutableList<LibraryItem> = libraryItems

    fun addLibraryItem(item: LibraryItem) {
        libraryItems.add(item)
    }

    // Новый метод для удаления элемента
    fun removeLibraryItem(item: LibraryItem) {
        libraryItems.remove(item)
    }
}

