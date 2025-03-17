// Абстрактный класс для объектов библиотеки
abstract class LibraryItem(
    val id: Int,
    var isAvailable: Boolean,
    val name: String
) {
    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

// Класс для книги
class Book(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val pages: Int,
    val author: String
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "книга: $name ($pages стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"
}

// Класс для газеты
class Newspaper(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val issueNumber: Int
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "выпуск: $issueNumber газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"
}

// Типы дисков
enum class DiscType {
    CD, DVD
}

// Класс для диска
class Disc(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val discType: DiscType
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "${discType.name} $name доступен: ${if (isAvailable) "Да" else "Нет"}"
}

// Вспомогательная функция для получения названия типа объекта
fun getTypeName(item: LibraryItem): String = when (item) {
    is Book -> "Книга"
    is Newspaper -> "Газета"
    is Disc -> "Диск"
    else -> "Объект"
}

// Основная функция с консольным меню
fun main() {
    // Создаем списки объектов
    val books = mutableListOf<LibraryItem>(
        Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
        Book(1001, true, "Война и мир", 1225, "Лев Толстой")
    )
    val newspapers = mutableListOf<LibraryItem>(
        Newspaper(17245, true, "Сельская жизнь", 794),
        Newspaper(17246, true, "Новости дня", 123)
    )
    val discs = mutableListOf<LibraryItem>(
        Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
        Disc(2002, true, "Лучшие хиты", DiscType.CD)
    )

    while (true) {
        println("\n--- Главное меню ---")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("0. Выход")
        print("Ваш выбор: ")

        val choice = readLine()?.toIntOrNull() ?: continue
        if (choice == 0) break

        val currentList: MutableList<LibraryItem> = when (choice) {
            1 -> books
            2 -> newspapers
            3 -> discs
            else -> {
                println("Неверный выбор!")
                continue
            }
        }

        if (currentList.isEmpty()) {
            println("Список пуст!")
            continue
        }

        // Вывод объектов с краткой информацией и порядковым номером
        println("\nСписок объектов:")
        currentList.forEachIndexed { index, item ->
            println("${index + 1}. ${item.getShortInfo()}")
        }
        println("0. Вернуться к выбору типа объекта")
        print("Выберите объект: ")

        val objChoice = readLine()?.toIntOrNull() ?: continue
        if (objChoice == 0) continue
        if (objChoice < 1 || objChoice > currentList.size) {
            println("Неверный номер объекта!")
            continue
        }
        val selectedItem = currentList[objChoice - 1]

        // Меню действий для выбранного объекта
        while (true) {
            println("\n--- Действия с объектом ---")
            println("1. Взять домой")
            println("2. Читать в читальном зале")
            println("3. Показать подробную информацию")
            println("4. Вернуть")
            println("0. Вернуться к выбору типа объекта")
            print("Ваш выбор: ")

            val action = readLine()?.toIntOrNull() ?: continue
            if (action == 0) break

            when (action) {
                1 -> { // Взять домой
                    if (!selectedItem.isAvailable) {
                        println("Объект недоступен для взятия домой!")
                    } else if (selectedItem is Newspaper) {
                        println("Газеты нельзя брать домой!")
                    } else {
                        selectedItem.isAvailable = false
                        println("${getTypeName(selectedItem)} ${selectedItem.id} взяли домой")
                    }
                }
                2 -> { // Читать в читальном зале
                    if (!selectedItem.isAvailable) {
                        println("Объект недоступен для чтения в читальном зале!")
                    } else if (selectedItem is Disc) {
                        println("Диски нельзя читать в читальном зале!")
                    } else {
                        selectedItem.isAvailable = false
                        println("${getTypeName(selectedItem)} ${selectedItem.id} взяли в читальный зал")
                    }
                }
                3 -> { // Показать подробную информацию
                    println(selectedItem.getDetailedInfo())
                }
                4 -> { // Вернуть объект
                    if (selectedItem.isAvailable) {
                        println("Объект уже доступен! Возврат невозможен!")
                    } else {
                        selectedItem.isAvailable = true
                        println("${getTypeName(selectedItem)} ${selectedItem.id} возвращен")
                    }
                }
                else -> println("Неверный выбор действия!")
            }
        }
    }

    println("Программа завершена.")
}
