package com.example.librarymanagementsystem

fun main() {
    // Создание списков объектов
    val books = mutableListOf<LibraryItem>(
        Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
        Book(1001, true, "Война и мир", 1225, "Лев Толстой")
    )
    val newspapers = mutableListOf<LibraryItem>(
        Newspaper(17245, true, "Сельская жизнь", 794, 4),
        Newspaper(17246, true, "Новости дня", 123, 2)
    )
    val discs = mutableListOf<LibraryItem>(
        Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
        Disc(2002, true, "Лучшие хиты", DiscType.CD)
    )

    while (true) {
        when (val choice = showMainMenu(books, newspapers, discs)) {
            SHOW_BOOKS, SHOW_NEWSPAPERS, SHOW_DISCS -> {
                val currentList: MutableList<LibraryItem> = when (choice) {
                    SHOW_BOOKS -> books
                    SHOW_NEWSPAPERS -> newspapers
                    SHOW_DISCS -> discs
                    else -> mutableListOf()
                }
                val selectedItem = selectLibraryItem(currentList)
                if (selectedItem != null) {
                    processItemActions(selectedItem)
                }
            }
            STORE_MENU -> storeInterface()
            DIGITIZATION_MENU -> digitizationInterface()
            EXIT -> break
            else -> println("Неверный выбор!")
        }
    }
    println("Программа завершена.")
}
