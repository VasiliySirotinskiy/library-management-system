package com.example.librarymanagementsystem.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @Provides @Singleton
    fun provideSharedPrefs(application: Application): SharedPreferences =
        application.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}
