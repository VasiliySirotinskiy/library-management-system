package com.example.librarymanagementsystem.domain

import java.io.Serializable

sealed class LibraryItem : Serializable {
    abstract val id: Long
    abstract val title: String
    abstract val addedAt: Long
    abstract val isAvailable: Boolean
}

data class Book(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val pages: Int,
    val author: String
) : LibraryItem()

data class Newspaper(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val issueNumber: Int,
    val month: Int
) : LibraryItem()

data class Disc(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val discType: String
) : LibraryItem()
