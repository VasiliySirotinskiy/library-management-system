package com.example.librarymanagementsystem

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

fun getTypeName(item: LibraryItem): String = when (item) {
    is Book -> "Книга"
    is Newspaper -> "Газета"
    is Disc -> "Диск"
    else -> "Объект"
}

// Inline функция для фильтрации объектов по типу с использованием reified
inline fun <reified T> List<Any>.filterByType(): List<T> = filterIsInstance<T>()
