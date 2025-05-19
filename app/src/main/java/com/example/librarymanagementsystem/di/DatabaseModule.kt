package com.example.librarymanagementsystem.di

import android.app.Application
import androidx.room.Room
import com.example.librarymanagementsystem.data.db.LibraryDao
import com.example.librarymanagementsystem.data.db.LibraryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(application: Application): LibraryDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            LibraryDatabase::class.java,
            "library.db"
        ).build()

    @Provides @Singleton
    fun provideDao(db: LibraryDatabase): LibraryDao = db.dao
}
