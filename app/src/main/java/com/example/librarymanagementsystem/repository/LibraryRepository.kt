package com.example.librarymanagementsystem.repository

import com.example.librarymanagementsystem.Book
import com.example.librarymanagementsystem.Disc
import com.example.librarymanagementsystem.DiscType
import com.example.librarymanagementsystem.LibraryItem
import com.example.librarymanagementsystem.Newspaper
import kotlinx.coroutines.delay
import kotlin.random.Random

object LibraryRepository {
    private val items = mutableListOf<LibraryItem>(
        Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
        Book(1001,  true, "Война и мир", 1225, "Лев Толстой"),
        Newspaper(17245, true, "Сельская жизнь", 794, 4),
        Newspaper(17246, true, "Новости дня",   123, 2),
        Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
        Disc(2002, true, "Лучшие хиты",        DiscType.CD)
    )
    private var fetchCount = 0

    suspend fun getItems(): List<LibraryItem> {
        delay(Random.nextLong(100, 2000))
        fetchCount++
        if (fetchCount % 5 == 0) throw Exception("Не удалось загрузить список")
        return items.toList()
    }

    suspend fun addItem(item: LibraryItem) {
        delay(Random.nextLong(100, 2000))
        items.add(item)
    }

    suspend fun removeItem(item: LibraryItem) {
        delay(Random.nextLong(100, 2000))
        items.remove(item)
    }
}
