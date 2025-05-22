package com.example.librarymanagementsystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.librarymanagementsystem.domain.usecase.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewModelFactory @Inject constructor(
    private val getInitial: GetInitialItemsUseCase,
    private val loadAfter: LoadAfterItemsUseCase,
    private val loadBefore: LoadBeforeItemsUseCase,
    private val addItem: AddItemUseCase,
    private val removeItem: RemoveItemUseCase,
    private val toggleSort: ToggleSortUseCase,
    private val searchGoogle: SearchGoogleBooksUseCase,
    private val prefs: android.content.SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                getInitial, loadAfter, loadBefore, addItem, removeItem, toggleSort, searchGoogle, prefs
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
