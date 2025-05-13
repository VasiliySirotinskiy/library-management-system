package com.example.librarymanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.ui.LibraryDetailFragment
import com.example.librarymanagementsystem.ui.LibraryListFragment

class MainActivity : AppCompatActivity(),
    LibraryListFragment.OnItemSelectedListener {

    var twoPane: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        twoPane = findViewById<View>(R.id.detail_fragment_container) != null

        if (twoPane) {
            // Для ландшафтного режима: убеждение, что контейнер списка заполнен
            if (supportFragmentManager.findFragmentById(R.id.list_fragment_container) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.list_fragment_container, LibraryListFragment(), "LIST_FRAGMENT")
                    .commit()
            }
            // Если вдруг восстановился фрагмент из портретного режима (старый контейнер), он удаляется
            val portraitFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (portraitFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(portraitFragment)
                    .commit()
            }
        } else {
            // Для портретного режима: если контейнер пуст, добавляется фрагмент списка
            if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LibraryListFragment(), "LIST_FRAGMENT")
                    .commit()
            }
        }
    }

    override fun onItemSelected(item: LibraryItem?, isNew: Boolean) {
        val detailFragment = LibraryDetailFragment.newInstance(item, isNew)
        if (twoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, detailFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun clearDetailContainer() {
        val detailFragment = supportFragmentManager.findFragmentById(R.id.detail_fragment_container)
        if (detailFragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(detailFragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (twoPane) {
            val detailFragment = supportFragmentManager.findFragmentById(R.id.detail_fragment_container)
            if (detailFragment != null) {
                clearDetailContainer()
                return
            }
        }
        super.onBackPressed()
    }
}