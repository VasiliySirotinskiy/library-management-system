package com.example.librarymanagementsystem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_NEW_ITEM = 1001
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private val libraryItems: MutableList<LibraryItem> = mutableListOf(
        Book(90743, true, "Маугли", 202, "Джозеф Киплинг"),
        Book(1001, true, "Война и мир", 1225, "Лев Толстой"),
        Newspaper(17245, true, "Сельская жизнь", 794, 4),
        Newspaper(17246, true, "Новости дня", 123, 2),
        Disc(2001, true, "Дэдпул и Росомаха", DiscType.DVD),
        Disc(2002, true, "Лучшие хиты", DiscType.CD)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LibraryAdapter(libraryItems)
        recyclerView.adapter = adapter

        // Настройка ItemTouchHelper для удаления элемента при свайпе
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
                    adapter.removeItem(position)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        // Обработка добавления нового элемента
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra("editable", true)
                putExtra("isNewItem", true)
            }
            startActivityForResult(intent, REQUEST_NEW_ITEM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEW_ITEM && resultCode == Activity.RESULT_OK && data != null) {
            val newItem: LibraryItem? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                data.getSerializableExtra("newItem", LibraryItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                data.getSerializableExtra("newItem") as? LibraryItem
            }
            if (newItem != null) {
                libraryItems.add(newItem)
                adapter.notifyItemInserted(libraryItems.size - 1)
            }
        }
    }
}
