package com.example.librarymanagementsystem.data.db

import com.example.librarymanagementsystem.domain.Book
import com.example.librarymanagementsystem.domain.Disc
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.Newspaper
import com.example.librarymanagementsystem.data.db.LibraryItemEntity

fun LibraryItemEntity.toDomain(): LibraryItem = when (itemType) {
    "Book" -> Book(
        id          = uid,
        title       = title,
        addedAt     = addedAt,
        isAvailable = isAvailable,
        pages       = pages ?: 0,
        author      = author ?: ""
    )
    "Newspaper" -> Newspaper(
        id          = uid,
        title       = title,
        addedAt     = addedAt,
        isAvailable = isAvailable,
        issueNumber = issueNumber ?: 0,
        month       = month ?: 1
    )
    "Disc" -> Disc(
        id          = uid,
        title       = title,
        addedAt     = addedAt,
        isAvailable = isAvailable,
        discType    = discType ?: "CD"
    )
    else -> throw IllegalArgumentException("Unknown itemType: $itemType")
}

fun LibraryItem.toEntity(): LibraryItemEntity = when (this) {
    is Book -> LibraryItemEntity(
        uid         = id,
        title       = title,
        addedAt     = addedAt,
        isAvailable = isAvailable,
        pages       = pages,
        author      = author,
        itemType    = "Book"
    )
    is Newspaper -> LibraryItemEntity(
        uid           = id,
        title         = title,
        addedAt       = addedAt,
        isAvailable   = isAvailable,
        issueNumber   = issueNumber,
        month         = month,
        itemType      = "Newspaper"
    )
    is Disc -> LibraryItemEntity(
        uid         = id,
        title       = title,
        addedAt     = addedAt,
        isAvailable = isAvailable,
        discType    = discType,
        itemType    = "Disc"
    )
}
