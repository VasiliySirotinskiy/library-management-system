package com.example.librarymanagementsystem

import android.app.Application
import com.example.librarymanagementsystem.di.AppComponent
import com.example.librarymanagementsystem.di.DaggerAppComponent

class LibraryApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }
}
