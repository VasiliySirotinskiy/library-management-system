package com.example.librarymanagementsystem.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_items")
data class LibraryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,

    val title: String,
    val addedAt: Long,
    val isAvailable: Boolean,

    // Для книг
    val pages: Int? = null,
    val author: String? = null,

    // Для газет
    val issueNumber: Int? = null,
    val month: Int? = null,

    // Для дисков
    val discType: String? = null,

    // Тип элемента: "Book", "Newspaper" или "Disc"
    val itemType: String
)
