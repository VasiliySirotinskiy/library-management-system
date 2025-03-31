package com.example.librarymanagementsystem

import BookStore
import DiscStore
import Manager
import NewspaperKiosk

// Функции для отображения меню и обработки действий пользователя

fun showMainMenu(
    books: MutableList<LibraryItem>,
    newspapers: MutableList<LibraryItem>,
    discs: MutableList<LibraryItem>
): Int {
    val menu = """
        |--- Главное меню ---
        |$SHOW_BOOKS. Показать книги
        |$SHOW_NEWSPAPERS. Показать газеты
        |$SHOW_DISCS. Показать диски
        |$STORE_MENU. Магазин
        |$DIGITIZATION_MENU. Кабинет оцифровки
        |$EXIT. Выход
        |Ваш выбор: 
    """.trimMargin()
    print(menu)
    val input = readLine()?.toIntOrNull() ?: -1
    println()
    return input
}

fun selectLibraryItem(items: MutableList<LibraryItem>): LibraryItem? {
    if (items.isEmpty()) {
        println("Список пуст!")
        println()
        return null
    }

    val listMenu = buildString {
        appendLine("Список объектов:")
        items.forEachIndexed { index, item ->
            appendLine("${index + 1}. ${item.getShortInfo()}")
        }
        appendLine("0. Вернуться к выбору типа объекта")
        append("Выберите объект: ")
    }
    print(listMenu)
    val objChoice = readLine()?.toIntOrNull()
    println()
    if (objChoice == null || objChoice !in 0..items.size) {
        println("Неверный номер объекта!")
        println()
        return null
    }
    return if (objChoice == 0) null else items[objChoice - 1]
}

fun processItemActions(selectedItem: LibraryItem) {
    while (true) {
        val actionMenu = """
            |--- Действия с объектом ---
            |$TAKE_HOME. Взять домой
            |$READ_IN_ROOM. Читать в читальном зале
            |$SHOW_DETAILS. Показать подробную информацию
            |$RETURN_ITEM. Вернуть
            |$BACK. Вернуться к выбору типа объекта
            |Ваш выбор: 
        """.trimMargin()
        print(actionMenu)
        val action = readLine()?.toIntOrNull() ?: continue
        println()
        if (action == BACK) break

        when (action) {
            TAKE_HOME -> when {
                !selectedItem.isAvailable() ->
                    println("Объект недоступен для взятия домой!")
                selectedItem is Newspaper ->
                    println("Газеты нельзя брать домой!")
                else -> {
                    selectedItem.setAvailability(false)
                    println("${getTypeName(selectedItem)} ${selectedItem.getId()} взяли домой")
                }
            }
            READ_IN_ROOM -> when {
                !selectedItem.isAvailable() ->
                    println("Объект недоступен для чтения в читальном зале!")
                selectedItem is Disc ->
                    println("Диски нельзя читать в читальном зале!")
                else -> {
                    selectedItem.setAvailability(false)
                    println("${getTypeName(selectedItem)} ${selectedItem.getId()} взяли в читальный зал")
                }
            }
            SHOW_DETAILS ->
                println(selectedItem.getDetailedInfo())
            RETURN_ITEM -> when {
                selectedItem.isAvailable() ->
                    println("Объект уже доступен! Возврат невозможен!")
                else -> {
                    selectedItem.setAvailability(true)
                    println("${getTypeName(selectedItem)} ${selectedItem.getId()} возвращен")
                }
            }
            else -> println("Неверный выбор действия!")
        }
        println()
    }
}

fun storeInterface() {
    val storeMenu = """
        |--- Магазин ---
        |1. Купить книгу
        |2. Купить диск
        |3. Купить газету
        |0. Вернуться в главное меню
        |Ваш выбор: 
    """.trimMargin()
    print(storeMenu)
    val choice = readLine()?.toIntOrNull() ?: -1
    println()

    val manager = Manager()
    when (choice) {
        1 -> {
            val book = manager.buy(BookStore())
            println("Куплена книга: ${book.getDetailedInfo()}")
        }
        2 -> {
            val disc = manager.buy(DiscStore())
            println("Куплен диск: ${disc.getDetailedInfo()}")
        }
        3 -> {
            val newspaper = manager.buy(NewspaperKiosk())
            println("Куплена газета: ${newspaper.getDetailedInfo()}")
        }
        else -> println("Возврат в главное меню")
    }
    println()
}

fun digitizationInterface() {
    val digitizationMenu = """
        |--- Кабинет оцифровки ---
        |Выберите тип объекта для оцифровки:
        |1. Книга
        |2. Газета
        |0. Вернуться в главное меню
        |Ваш выбор: 
    """.trimMargin()
    print(digitizationMenu)
    val choice = readLine()?.toIntOrNull() ?: -1
    println()

    val cabinet = DigitizationCabinet()
    when (choice) {
        1 -> {
            // Демонстрация
            val sampleBook = Book(90743, true, "Маугли", 202, "Джозеф Киплинг")
            val disc = cabinet.digitize(sampleBook)
            println("Книга '${sampleBook.getName()}' оцифрована в: ${disc.getDetailedInfo()}")
        }
        2 -> {
            // Демонстрация
            val sampleNewspaper = Newspaper(17245, true, "Сельская жизнь", 794, 3)
            val disc = cabinet.digitize(sampleNewspaper)
            println("Газета '${sampleNewspaper.getName()}' оцифрована в: ${disc.getDetailedInfo()}")
        }
        else -> println("Возврат в главное меню")
    }
    println()
}
