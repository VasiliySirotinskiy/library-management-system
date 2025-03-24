// Именованные константы для главного меню
const val EXIT = 0
const val SHOW_BOOKS = 1
const val SHOW_NEWSPAPERS = 2
const val SHOW_DISCS = 3

// Именованные константы для меню действий с объектом
const val TAKE_HOME = 1
const val READ_IN_ROOM = 2
const val SHOW_DETAILS = 3
const val RETURN_ITEM = 4
const val BACK = 0

// Абстрактный класс для объектов библиотеки
abstract class LibraryItem(
    private val id: Int,
    private var isAvailable: Boolean,
    private val name: String
) {
    fun getId(): Int = id
    fun isAvailable(): Boolean = isAvailable
    fun setAvailability(availability: Boolean) {
        isAvailable = availability
    }
    fun getName(): String = name

    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

// Класс для книги
class Book(
    id: Int,
    isAvailable: Boolean,
    name: String,
    private val pages: Int,
    val author: String
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "книга: ${getName()} ($pages стр.) автора: $author с id: ${getId()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
}

// Класс для газеты
class Newspaper(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val issueNumber: Int
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "выпуск: $issueNumber газеты ${getName()} с id: ${getId()} доступен: ${if (isAvailable()) "Да" else "Нет"}"
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
        "${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "${discType.name} ${getName()} доступен: ${if (isAvailable()) "Да" else "Нет"}"
}

// Вспомогательная функция для получения названия типа объекта
fun getTypeName(item: LibraryItem): String = when (item) {
    is Book -> "Книга"
    is Newspaper -> "Газета"
    is Disc -> "Диск"
    else -> "Объект"
}

// Функция отображения главного меню и выбора списка объектов
fun showMainMenu(
    books: MutableList<LibraryItem>,
    newspapers: MutableList<LibraryItem>,
    discs: MutableList<LibraryItem>
): MutableList<LibraryItem>? {
    println("\n--- Главное меню ---")
    println("$SHOW_BOOKS. Показать книги")
    println("$SHOW_NEWSPAPERS. Показать газеты")
    println("$SHOW_DISCS. Показать диски")
    println("$EXIT. Выход")
    print("Ваш выбор: ")

    val choice = readLine()?.toIntOrNull() ?: return null
    if (choice == EXIT) return null

    return when (choice) {
        SHOW_BOOKS -> books
        SHOW_NEWSPAPERS -> newspapers
        SHOW_DISCS -> discs
        else -> {
            println("Неверный выбор!")
            null
        }
    }
}

// Функция для выбора объекта из списка
fun selectLibraryItem(items: MutableList<LibraryItem>): LibraryItem? {
    if (items.isEmpty()) {
        println("Список пуст!")
        return null
    }

    println("\nСписок объектов:")
    items.forEachIndexed { index, item ->
        println("${index + 1}. ${item.getShortInfo()}")
    }
    println("0. Вернуться к выбору типа объекта")
    print("Выберите объект: ")

    val objChoice = readLine()?.toIntOrNull() ?: return null
    if (objChoice == 0) return null
    if (objChoice < 1 || objChoice > items.size) {
        println("Неверный номер объекта!")
        return null
    }
    return items[objChoice - 1]
}

// Функция для обработки действий над выбранным объектом
fun processItemActions(selectedItem: LibraryItem) {
    while (true) {
        println("\n--- Действия с объектом ---")
        println("$TAKE_HOME. Взять домой")
        println("$READ_IN_ROOM. Читать в читальном зале")
        println("$SHOW_DETAILS. Показать подробную информацию")
        println("$RETURN_ITEM. Вернуть")
        println("$BACK. Вернуться к выбору типа объекта")
        print("Ваш выбор: ")

        val action = readLine()?.toIntOrNull() ?: continue
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
    }
}

// Основная функция программы
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
        Disc(2002, true, "Лучшие хиты 90х", DiscType.CD)
    )

    while (true) {
        val currentList = showMainMenu(books, newspapers, discs) ?: break

        val selectedItem = selectLibraryItem(currentList)
        if (selectedItem != null) {
            processItemActions(selectedItem)
        }
    }
    println("Программа завершена.")
}
