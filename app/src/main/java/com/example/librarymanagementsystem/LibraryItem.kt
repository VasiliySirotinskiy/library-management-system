package com.example.librarymanagementsystem

// Абстрактный класс для объектов библиотеки и его реализации

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
        "Книга: ${getName()} ($pages стр.) автора: $author, id: ${getId()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
}

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
        "Газета: ${getName()}, выпуск: $issueNumber (${getMonthName(month)}), id: ${getId()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
}

enum class DiscType {
    CD, DVD
}

class Disc(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val discType: DiscType
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "Диск (${discType.name}): ${getName()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
}

// Вспомогательная функция для получения названия типа объекта
fun getTypeName(item: LibraryItem): String = when (item) {
    is Book -> "Книга"
    is Newspaper -> "Газета"
    is Disc -> "Диск"
    else -> "Объект"
}
