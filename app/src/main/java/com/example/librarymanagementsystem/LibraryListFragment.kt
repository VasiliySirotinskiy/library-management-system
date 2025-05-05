package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementsystem.domain.LibraryItem
import com.facebook.shimmer.ShimmerFrameLayout

class LibraryListFragment : Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private lateinit var adapter: LibraryAdapter
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var progress: ProgressBar
    private var listener: OnItemSelectedListener? = null

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
    ): View = inflater.inflate(R.layout.fragment_library_list, container, false).also { view ->
        shimmer = view.findViewById(R.id.shimmer_view)
        progress = view.findViewById(R.id.progress_bar)
        val rv = view.findViewById<RecyclerView>(R.id.recycler_view_list)

        rv.layoutManager = LinearLayoutManager(context)
        adapter = LibraryAdapter(emptyList())
        rv.adapter = adapter
        adapter.onItemClick = { item -> listener?.onItemSelected(item, false) }

        // Шиммер для первой загрузки
        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (loading && adapter.itemCount == 0) {
                shimmer.startShimmer()
                shimmer.visibility = View.VISIBLE
                rv.visibility = View.GONE
            } else {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                rv.visibility = View.VISIBLE
            }
            // Прогресс при подгрузке страниц
            progress.visibility = if (loading && adapter.itemCount > 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        vm.items.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        vm.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }

        // Бесконечный скролл
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lastPos = (rv.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                vm.loadMoreIfNeeded(lastPos)
            }
        })

        // FAB
        view.findViewById<View>(R.id.fab_add)
            .setOnClickListener { listener?.onItemSelected(null, true) }
    }
}
