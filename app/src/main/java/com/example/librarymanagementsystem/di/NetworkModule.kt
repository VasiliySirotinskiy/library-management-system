package com.example.librarymanagementsystem.di

import com.example.librarymanagementsystem.data.remote.BooksApi
import com.example.librarymanagementsystem.data.remote.RemoteModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NetworkModule {
    @Provides @Singleton
    fun provideBooksApi(): BooksApi = RemoteModule.booksApi
}
