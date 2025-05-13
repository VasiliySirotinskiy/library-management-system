package com.example.librarymanagementsystem.domain

import java.io.Serializable

sealed class LibraryItem : Serializable {
    abstract val id: Long
    abstract val title: String
    abstract val addedAt: Long
    abstract val isAvailable: Boolean
}
