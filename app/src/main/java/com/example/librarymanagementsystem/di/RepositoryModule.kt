package com.example.librarymanagementsystem.di

import com.example.librarymanagementsystem.data.repository.GoogleBooksRepositoryImpl
import com.example.librarymanagementsystem.data.repository.LibraryRepositoryImpl
import com.example.librarymanagementsystem.domain.repository.IGoogleBooksRepository
import com.example.librarymanagementsystem.domain.repository.ILibraryRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindLibraryRepo(impl: LibraryRepositoryImpl): ILibraryRepository

    @Binds @Singleton
    abstract fun bindGoogleBooksRepo(impl: GoogleBooksRepositoryImpl): IGoogleBooksRepository
}
