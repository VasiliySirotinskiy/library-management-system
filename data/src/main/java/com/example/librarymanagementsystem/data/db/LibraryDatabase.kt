package com.example.librarymanagementsystem.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LibraryItemEntity::class], version = 1, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract val dao: LibraryDao

    companion object {
        @Volatile private var INST: LibraryDatabase? = null
        fun get(context: Context): LibraryDatabase =
            INST ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library.db"
                ).build().also { INST = it }
            }
    }
}
