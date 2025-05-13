package com.example.librarymanagementsystem.domain

data class Disc(
    override val id: Long,
    override val title: String,
    override val addedAt: Long,
    override val isAvailable: Boolean,
    val discType: String
) : LibraryItem()