package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        // Свайп для удаления
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                vm.items.value?.get(pos)?.let { vm.remove(it) }
            }
        }).attachToRecyclerView(rv)

        // Шиммер и прогресс-бар
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
            progress.visibility = if (loading && adapter.itemCount > 0) View.VISIBLE else View.GONE
        }

        vm.items.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        vm.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }

        // Инфинити скролл
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lastPos = (rv.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                vm.loadMoreIfNeeded(lastPos)
            }
        })

        // FAB для добавления
        view.findViewById<View>(R.id.fab_add)
            .setOnClickListener { listener?.onItemSelected(null, true) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_library_list, menu)
        menu.findItem(R.id.action_sort_name).isChecked = vm.sortByName
        menu.findItem(R.id.action_sort_date).isChecked = !vm.sortByName
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                vm.toggleSort(byName = true)
                requireActivity().invalidateOptionsMenu()
                true
            }
            R.id.action_sort_date -> {
                vm.toggleSort(byName = false)
                requireActivity().invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
