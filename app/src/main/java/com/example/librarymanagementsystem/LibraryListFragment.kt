package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private lateinit var shimmerView: ShimmerFrameLayout
    private var listener: OnItemSelectedListener? = null

    private var isFirstLoad = true
    private var startTime = 0L

    interface OnItemSelectedListener {
        fun onItemSelected(item: LibraryItem?, isNew: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSelectedListener
            ?: throw RuntimeException("$context must implement OnItemSelectedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_library_list, container, false)

        shimmerView = view.findViewById(R.id.shimmer_view)
        recyclerView = view.findViewById(R.id.recycler_view_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = LibraryAdapter(emptyList())
        adapter.onItemClick = { item -> listener?.onItemSelected(item, false) }
        recyclerView.adapter = adapter

        // Инициализация видимости в зависимости от первого показа
        if (isFirstLoad) {
            startTime = System.currentTimeMillis()
            shimmerView.startShimmer()
            recyclerView.visibility = View.GONE
        } else {
            shimmerView.stopShimmer()
            shimmerView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        mainViewModel.items.observe(viewLifecycleOwner) { list ->
            lifecycleScope.launch {
                if (isFirstLoad) {
                    // Обеспечение минимальной длительности шиммера
                    val elapsed = System.currentTimeMillis() - startTime
                    if (elapsed < 1_000) delay(1_000 - elapsed)

                    shimmerView.stopShimmer()
                    shimmerView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    // Шиммер больше не нужен для следующих обновлений
                    isFirstLoad = false
                }
                // Обновление адаптера независимо от первой или последующих загрузок
                adapter.updateList(list)
            }
        }

        mainViewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }

        // Свайп для удаления
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                val item = adapter.libraryItems[pos]
                mainViewModel.removeItem(item)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)

        // Кнопка добавления нового элемента
        view.findViewById<FloatingActionButton>(R.id.fab_add)
            .setOnClickListener {
                listener?.onItemSelected(null, true)
            }

        return view
    }
}
