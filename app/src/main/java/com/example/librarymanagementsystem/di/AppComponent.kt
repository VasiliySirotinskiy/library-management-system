package com.example.librarymanagementsystem.di

import android.app.Application
import com.example.librarymanagementsystem.AppViewModelFactory
import com.example.librarymanagementsystem.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    UseCaseModule::class
])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(activity: MainActivity)

    fun viewModelFactory(): AppViewModelFactory
}
