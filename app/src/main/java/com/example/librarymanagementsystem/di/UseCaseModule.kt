package com.example.librarymanagementsystem.di

import com.example.librarymanagementsystem.domain.repository.ILibraryRepository
import com.example.librarymanagementsystem.domain.repository.IGoogleBooksRepository
import com.example.librarymanagementsystem.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UseCaseModule {
    @Provides @Singleton fun provideGetInitial(repo: ILibraryRepository) =
        GetInitialItemsUseCase(repo)
    @Provides @Singleton fun provideLoadAfter(repo: ILibraryRepository) =
        LoadAfterItemsUseCase(repo)
    @Provides @Singleton fun provideLoadBefore(repo: ILibraryRepository) =
        LoadBeforeItemsUseCase(repo)
    @Provides @Singleton fun provideAdd(repo: ILibraryRepository) =
        AddItemUseCase(repo)
    @Provides @Singleton fun provideRemove(repo: ILibraryRepository) =
        RemoveItemUseCase(repo)
    @Provides @Singleton fun provideToggle(repo: ILibraryRepository) =
        ToggleSortUseCase(repo)
    @Provides @Singleton fun provideSearchGB(repo: IGoogleBooksRepository) =
        SearchGoogleBooksUseCase(repo)
}
