package com.example.librarymanagementsystem

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.ui.LibraryDetailFragment
import com.example.librarymanagementsystem.ui.LibraryListFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    LibraryListFragment.OnItemSelectedListener {

    private var twoPane: Boolean = false

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as LibraryApp).appComponent.inject(this)

        setContentView(R.layout.activity_main)
        twoPane = findViewById<View>(R.id.detail_fragment_container) != null

        if (twoPane) {
            if (supportFragmentManager.findFragmentById(R.id.list_fragment_container) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.list_fragment_container,
                        LibraryListFragment(),
                        "LIST_FRAGMENT"
                    )
                    .commit()
            }

            supportFragmentManager.findFragmentById(R.id.fragment_container)
                ?.let { frag ->
                    supportFragmentManager.beginTransaction()
                        .remove(frag)
                        .commit()
                }
        } else {
            if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        LibraryListFragment(),
                        "LIST_FRAGMENT"
                    )
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

    override fun onBackPressed() {
        if (twoPane) {
            supportFragmentManager.findFragmentById(R.id.detail_fragment_container)
                ?.let {
                    supportFragmentManager.beginTransaction()
                        .remove(it)
                        .commit()
                    return
                }
        }
        super.onBackPressed()
    }
}
