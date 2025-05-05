package com.example.librarymanagementsystem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.librarymanagementsystem.domain.Book
import com.example.librarymanagementsystem.domain.Disc
import com.example.librarymanagementsystem.domain.LibraryItem
import com.example.librarymanagementsystem.domain.Newspaper

class LibraryDetailFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var existingItem: LibraryItem? = null
    private var isNewItem: Boolean = false
    private var listener: OnItemSavedListener? = null

    interface OnItemSavedListener {
        fun onItemSaved()
    }

    companion object {
        private const val ARG_ITEM = "arg_item"
        private const val ARG_IS_NEW = "arg_is_new"

        fun newInstance(item: LibraryItem?, isNew: Boolean): LibraryDetailFragment {
            return LibraryDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM, item)
                    putBoolean(ARG_IS_NEW, isNew)
                }
            }
        }
    }

    private lateinit var spinnerType: Spinner
    private lateinit var editName: EditText
    private lateinit var textId: TextView
    private lateinit var editExtra1: EditText
    private lateinit var editExtra2: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageIcon: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSavedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            existingItem = bundle.getSerializable(ARG_ITEM) as? LibraryItem
            isNewItem = bundle.getBoolean(ARG_IS_NEW, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_library_detail, container, false)
        spinnerType = view.findViewById(R.id.spinner_type)
        editName    = view.findViewById(R.id.edit_text_name)
        textId      = view.findViewById(R.id.text_view_id)
        editExtra1  = view.findViewById(R.id.edit_text_extra1)
        editExtra2  = view.findViewById(R.id.edit_text_extra2)
        buttonSave  = view.findViewById(R.id.button_save)
        imageIcon   = view.findViewById(R.id.image_view_icon)

        if (existingItem != null && !isNewItem) {
            populateFields(existingItem!!)
            setEditable(false)
        } else {
            setupTypeSpinner()
            textId.text = "Новый элемент"
            setEditable(true)
        }

        buttonSave.setOnClickListener {
            if (isNewItem) {
                val type = spinnerType.selectedItem.toString()
                val name = editName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Введите название", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val now = System.currentTimeMillis()
                val newItem: LibraryItem = when (type) {
                    "Книга" -> {
                        val pages = editExtra1.text.toString().toIntOrNull() ?: 0
                        val author = editExtra2.text.toString().trim()
                        Book(0L, name, now, true, pages, author)
                    }
                    "Газета" -> {
                        val issue = editExtra1.text.toString().toIntOrNull() ?: 0
                        val month = editExtra2.text.toString().toIntOrNull() ?: 1
                        Newspaper(0L, name, now, true, issue, month)
                    }
                    "Диск" -> {
                        val discType = editExtra1.text.toString().uppercase()
                            .let { if (it == "DVD") "DVD" else "CD" }
                        Disc(0L, name, now, true, discType)
                    }
                    else -> throw IllegalArgumentException("Неподдерживаемый тип")
                }
                mainViewModel.add(newItem)
                Toast.makeText(context, "Элемент сохранён", Toast.LENGTH_SHORT).show()
                listener?.onItemSaved()
            }
        }

        return view
    }

    private fun setupTypeSpinner() {
        spinnerType.visibility = View.VISIBLE
        val types = listOf("Книга", "Газета", "Диск")
        spinnerType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            types
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                when (parent.getItemAtPosition(position) as String) {
                    "Книга" -> {
                        editExtra1.hint = "Страницы"
                        editExtra2.hint = "Автор"
                        editExtra2.visibility = View.VISIBLE
                    }
                    "Газета" -> {
                        editExtra1.hint = "Номер выпуска"
                        editExtra2.hint = "Месяц (1–12)"
                        editExtra2.visibility = View.VISIBLE
                    }
                    "Диск" -> {
                        editExtra1.hint = "Тип (CD или DVD)"
                        editExtra2.visibility = View.GONE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    @Suppress("SetTextI18n")
    private fun populateFields(item: LibraryItem) {
        editName.setText(item.title)
        textId.text = "ID: ${item.id}"
        when (item) {
            is Book -> {
                imageIcon.setImageResource(R.drawable.ic_book)
                editExtra1.setText(item.pages.toString())
                editExtra2.setText(item.author)
            }
            is Newspaper -> {
                imageIcon.setImageResource(R.drawable.ic_newspaper)
                editExtra1.setText(item.issueNumber.toString())
                editExtra2.setText(item.month.toString())
            }
            is Disc -> {
                imageIcon.setImageResource(R.drawable.ic_disc)
                editExtra1.setText(item.discType)
                editExtra2.visibility = View.GONE
            }
        }
        spinnerType.visibility = View.GONE
    }

    private fun setEditable(editable: Boolean) {
        editName.isEnabled     = editable
        editExtra1.isEnabled   = editable
        editExtra2.isEnabled   = editable
        spinnerType.isEnabled  = editable
        buttonSave.visibility  = if (editable) View.VISIBLE else View.GONE
    }
}
