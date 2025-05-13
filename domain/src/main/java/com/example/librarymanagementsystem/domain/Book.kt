package com.example.librarymanagementsystem.domain

data class Book(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val pages: Int,
    val author: String
) : LibraryItem()