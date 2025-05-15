package com.example.librarymanagementsystem.domain

data class Newspaper(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val issueNumber: Int,
    val month: Int
) : LibraryItem()