package com.example.librarymanagementsystem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter

    // Получение ViewModel
    private val viewModel: MainViewModel by viewModels()

    // Лаунчер для запуска добавления нового элемента
    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация лаунчера для получения результата из ItemDetailActivity
        addItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val newItem: LibraryItem? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    data?.getSerializableExtra("newItem", LibraryItem::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data?.getSerializableExtra("newItem") as? LibraryItem
                }
                newItem?.let { viewModel.addLibraryItem(it) }
            }
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LibraryAdapter(mutableListOf())
        recyclerView.adapter = adapter

        viewModel.libraryItems.observe(this, Observer { items ->
            adapter.updateItems(items)
        })

        // Настройка свайпа для удаления элемента
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.removeLibraryItem(position)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        // Обработка нажатия на кнопку добавления нового элемента с использованием нового Activity Result API
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra("editable", true)
                putExtra("isNewItem", true)
            }
            addItemLauncher.launch(intent)
        }
    }
}
