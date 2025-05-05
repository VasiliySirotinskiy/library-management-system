package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementsystem.domain.Book
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.ui.GoogleBooksAdapter
import com.facebook.shimmer.ShimmerFrameLayout

class LibraryListFragment : Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private lateinit var adapter: LibraryAdapter
    private lateinit var gbAdapter: GoogleBooksAdapter
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var progress: ProgressBar
    private lateinit var fab: View
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_library_list, container, false).also { view ->
        shimmer    = view.findViewById(R.id.shimmer_view)
        progress   = view.findViewById(R.id.progress_bar)
        val rv     = view.findViewById<RecyclerView>(R.id.recycler_view_list)
        fab         = view.findViewById(R.id.fab_add)
        val btnLib    = view.findViewById<Button>(R.id.btn_library)
        val btnGBooks = view.findViewById<Button>(R.id.btn_google)
        val form      = view.findViewById<LinearLayout>(R.id.search_form)
        val etAuthor  = view.findViewById<EditText>(R.id.et_author)
        val etTitle   = view.findViewById<EditText>(R.id.et_title)
        val btnSearch = view.findViewById<Button>(R.id.btn_search)

        // Основные адаптеры
        rv.layoutManager = LinearLayoutManager(context)
        adapter    = LibraryAdapter(emptyList())
        gbAdapter  = GoogleBooksAdapter(emptyList())
        rv.adapter = adapter

        // Клики
        adapter.onItemClick = { item -> listener?.onItemSelected(item, false) }
        gbAdapter.onItemLongClick = { book ->
            val newBook = Book(0L, book.title, System.currentTimeMillis(), true, book.pageCount, book.authors)
            vm.add(newBook)
            Toast.makeText(context, "Сохранено в библиотеку", Toast.LENGTH_SHORT).show()
        }

        // Переключение режимов
        btnLib.setOnClickListener {
            form.visibility = View.GONE
            rv.adapter      = adapter
            fab.visibility  = View.VISIBLE
            vm.clearGoogleResults()
            vm.loadInitial()
        }
        btnGBooks.setOnClickListener {
            adapter.updateList(emptyList())
            form.visibility = View.VISIBLE
            rv.adapter      = gbAdapter
            fab.visibility  = View.GONE
        }

        // Валидация
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnSearch.isEnabled = (etAuthor.text.length >= 3 || etTitle.text.length >= 3)
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) = Unit
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) = Unit
        }
        etAuthor.addTextChangedListener(watcher)
        etTitle.addTextChangedListener(watcher)

        // Поиск
        btnSearch.setOnClickListener {
            vm.searchGoogleBooks(
                author = etAuthor.text.toString().takeIf { it.length >= 3 },
                title  = etTitle.text.toString().takeIf { it.length >= 3 }
            )
        }

        // Свайп для удаления
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                vm.items.value?.get(vh.adapterPosition)?.let { vm.remove(it) }
            }
        }).attachToRecyclerView(rv)

        // Наблюдатели загрузки/ошибок
        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (rv.adapter === adapter) {
                shimmer.visibility = if (loading && adapter.itemCount == 0) View.VISIBLE else View.GONE
                rv.visibility      = if (shimmer.visibility == View.GONE) View.VISIBLE else View.GONE
                progress.visibility= if (loading && adapter.itemCount > 0) View.VISIBLE else View.GONE
            }
        }

        vm.gbLoading.observe(viewLifecycleOwner) { loading ->
            if (rv.adapter === gbAdapter) {
                shimmer.visibility = if (loading) View.VISIBLE else View.GONE
                rv.visibility      = if (shimmer.visibility == View.GONE) View.VISIBLE else View.GONE
                progress.visibility= if (loading && gbAdapter.itemCount > 0) View.VISIBLE else View.GONE
            }
        }
        vm.error.observe(viewLifecycleOwner) { err -> err?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() } }
        vm.gbError.observe(viewLifecycleOwner) { err -> err?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() } }

        // Обновление списков
        vm.items.observe(viewLifecycleOwner) { if (rv.adapter === adapter) adapter.updateList(it) }
        vm.gbResults.observe(viewLifecycleOwner) { if (rv.adapter === gbAdapter) gbAdapter.update(it) }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lastPos = (rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                vm.loadMoreIfNeeded(lastPos)
            }
        })

        // FAB
        fab.setOnClickListener { listener?.onItemSelected(null, true) }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_library_list, menu)
        menu.findItem(R.id.action_sort_name).isChecked = vm.sortByName
        menu.findItem(R.id.action_sort_date).isChecked = !vm.sortByName
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_sort_name -> { vm.toggleSort(true); requireActivity().invalidateOptionsMenu(); true }
        R.id.action_sort_date -> { vm.toggleSort(false); requireActivity().invalidateOptionsMenu(); true }
        else -> super.onOptionsItemSelected(item)
    }
}
