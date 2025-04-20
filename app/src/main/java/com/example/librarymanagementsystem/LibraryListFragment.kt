package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LibraryListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private var listener: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun onItemSelected(item: LibraryItem, isNew: Boolean = false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnItemSelectedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Используется изменяемый список для адаптера
        adapter = LibraryAdapter(MainViewModel.getLibraryItems())
        adapter.onItemClick = { item ->
            listener?.onItemSelected(item)
        }
        recyclerView.adapter = adapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = MainViewModel.getLibraryItems()[position]
                    MainViewModel.removeLibraryItem(item)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)

        // Обработка нажатия на FAB для добавления нового элемента
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            listener?.onItemSelected(NewItemPlaceholder(), isNew = true)
        }
        return view
    }

    class NewItemPlaceholder : LibraryItem(0, true, "Новый элемент") {
        override fun getShortInfo(): String = "Новый элемент"
        override fun getDetailedInfo(): String = ""
    }
}

