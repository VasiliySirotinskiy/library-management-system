// Константы для главного меню
const val EXIT = 0
const val SHOW_BOOKS = 1
const val SHOW_NEWSPAPERS = 2
const val SHOW_DISCS = 3
const val STORE_MENU = 4
const val DIGITIZATION_MENU = 5

// Константы для меню действий с объектом
const val TAKE_HOME = 1
const val READ_IN_ROOM = 2
const val SHOW_DETAILS = 3
const val RETURN_ITEM = 4
const val BACK = 0

// Вспомогательная функция для преобразования номера месяца в кириллическое название
fun getMonthName(month: Int): String = when (month) {
    1 -> "Январь"
    2 -> "Февраль"
    3 -> "Март"
    4 -> "Апрель"
    5 -> "Май"
    6 -> "Июнь"
    7 -> "Июль"
    8 -> "Август"
    9 -> "Сентябрь"
    10 -> "Октябрь"
    11 -> "Ноябрь"
    12 -> "Декабрь"
    else -> "Неизвестный месяц"
}

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
    val issueNumber: Int,
    val month: Int
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "выпуск: $issueNumber газеты ${getName()} (${getMonthName(month)}) с id: ${getId()} доступен: ${if (isAvailable()) "Да" else "Нет"}"
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

// Интерфейс магазина
interface Store<T : LibraryItem> {
    fun sell(): T
}

// Магазин книг
class BookStore : Store<Book> {
    override fun sell(): Book =
        Book(5001, true, "Новая книга", 350, "Неизвестный автор")
}

// Магазин дисков
class DiscStore : Store<Disc> {
    override fun sell(): Disc =
        Disc(6001, true, "Новый диск", DiscType.DVD)
}

// Газетный ларек
class NewspaperKiosk : Store<Newspaper> {
    override fun sell(): Newspaper =
        Newspaper(7001, true, "Новая газета", 101, 5)
}

// Сущность менеджера
class Manager {
    fun <T : LibraryItem> buy(store: Store<T>): T = store.sell()
}

// Кабинет оцифровки
class DigitizationCabinet {
    fun digitize(item: LibraryItem): Disc {
        return Disc(8001, true, "Оцифрованный: ${item.getName()}", DiscType.CD)
    }
}

// Inline функция для фильтрации объектов по типу с использованием reified
inline fun <reified T> List<Any>.filterByType(): List<T> = filterIsInstance<T>()

// Функция отображения главного меню и выбора списка объектов
fun showMainMenu(
    books: MutableList<LibraryItem>,
    newspapers: MutableList<LibraryItem>,
    discs: MutableList<LibraryItem>
): Int {
    println("\n--- Главное меню ---")
    println("$SHOW_BOOKS. Показать книги")
    println("$SHOW_NEWSPAPERS. Показать газеты")
    println("$SHOW_DISCS. Показать диски")
    println("$STORE_MENU. Магазин")
    println("$DIGITIZATION_MENU. Кабинет оцифровки")
    println("$EXIT. Выход")
    print("Ваш выбор: ")

    return readLine()?.toIntOrNull() ?: -1
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

// Функция интерфейса магазина
fun storeInterface() {
    val manager = Manager()
    println("\n--- Магазин ---")
    println("1. Купить книгу")
    println("2. Купить диск")
    println("3. Купить газету")
    println("0. Вернуться в главное меню")
    print("Ваш выбор: ")

    when (readLine()?.toIntOrNull() ?: -1) {
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
}

// Функция интерфейса кабинета оцифровки
fun digitizationInterface() {
    val cabinet = DigitizationCabinet()
    println("\n--- Кабинет оцифровки ---")
    println("Выберите тип объекта для оцифровки:")
    println("1. Книга")
    println("2. Газета")
    println("0. Вернуться в главное меню")
    print("Ваш выбор: ")

    when (readLine()?.toIntOrNull() ?: -1) {
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
}

// Основная функция программы
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
