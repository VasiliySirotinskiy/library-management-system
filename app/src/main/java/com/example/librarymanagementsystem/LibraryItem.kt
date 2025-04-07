package com.example.librarymanagementsystem

import java.io.Serializable

// Абстрактный класс для объектов библиотеки
abstract class LibraryItem(
    private val id: Int,
    private var isAvailable: Boolean,
    val title: String
) : Serializable {
    fun getId(): Int = id
    fun isAvailable(): Boolean = isAvailable
    fun setAvailability(availability: Boolean) {
        isAvailable = availability
    }
    fun getName(): String = title

    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

class Book(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val pages: Int,
    val author: String
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "$title доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "Книга: $title ($pages стр.) автора: $author, id: ${getId()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
}

class Newspaper(
    id: Int,
    isAvailable: Boolean,
    name: String,
    val issueNumber: Int,
    val month: Int
) : LibraryItem(id, isAvailable, name) {
    override fun getShortInfo(): String =
        "$title доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "Газета: $title, выпуск: $issueNumber (${getMonthName(month)}), id: ${getId()} доступна: ${if (isAvailable()) "Да" else "Нет"}"
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
        "$title доступна: ${if (isAvailable()) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "Диск (${discType.name}): $title доступна: ${if (isAvailable()) "Да" else "Нет"}"
}
