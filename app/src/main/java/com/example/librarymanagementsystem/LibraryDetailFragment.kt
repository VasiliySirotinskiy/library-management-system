package com.example.librarymanagementsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

class LibraryDetailFragment : Fragment() {

    private var item: LibraryItem? = null
    private var isNewItem: Boolean = false

    companion object {
        private const val ARG_ITEM = "item"
        private const val ARG_IS_NEW = "is_new"

        // Метод для создания нового экземпляра фрагмента
        fun newInstance(item: LibraryItem?, isNew: Boolean = false): LibraryDetailFragment {
            val fragment = LibraryDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_ITEM, item)
            bundle.putBoolean(ARG_IS_NEW, isNew)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var imageViewIcon: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewId: TextView
    private lateinit var editTextExtra1: EditText
    private lateinit var editTextExtra2: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_ITEM, LibraryItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(ARG_ITEM) as? LibraryItem
            }
            isNewItem = it.getBoolean(ARG_IS_NEW, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library_detail, container, false)
        imageViewIcon = view.findViewById(R.id.image_view_icon)
        editTextName = view.findViewById(R.id.edit_text_name)
        textViewId = view.findViewById(R.id.text_view_id)
        editTextExtra1 = view.findViewById(R.id.edit_text_extra1)
        editTextExtra2 = view.findViewById(R.id.edit_text_extra2)
        spinnerType = view.findViewById(R.id.spinner_type)
        buttonSave = view.findViewById(R.id.button_save)

        if (item != null && !isNewItem) {
            // Режим просмотра существующего элемента
            populateFields(item!!)
            setFieldsEditable(false)
        } else {
            // Режим добавления нового элемента
            spinnerType.visibility = View.VISIBLE
            val types = listOf("Книга", "Газета", "Диск")
            val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerType.adapter = adapterSpinner
            textViewId.text = "Новый элемент"
            setFieldsEditable(true)
        }

        buttonSave.setOnClickListener {
            if (isNewItem) {
                val type = spinnerType.selectedItem.toString()
                val name = editTextName.text.toString()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Введите название", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val extra1 = editTextExtra1.text.toString()
                val extra2 = editTextExtra2.text.toString()
                // Создание нового элемента с помощью ViewModel для деталей
                val newItem = ItemDetailViewModel().createNewItem(type, name, extra1, extra2)
                // Добавление созданного элемента в общий список (через MainViewModel)
                MainViewModel.addLibraryItem(newItem)
                Toast.makeText(context, "Элемент сохранён", Toast.LENGTH_SHORT).show()
            }
            // В портретном режиме: переход назад
            // В альбомном режиме: очистка правой панели
            if ((activity as? MainActivity)?.twoPane == true) {
                (activity as MainActivity).clearDetailContainer()
            } else {
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun populateFields(item: LibraryItem) {
        editTextName.setText(item.getName())
        textViewId.text = "ID: ${item.getId()}"
        val iconRes = when (item) {
            is Book -> R.drawable.ic_book
            is Newspaper -> R.drawable.ic_newspaper
            is Disc -> R.drawable.ic_disc
            else -> R.drawable.ic_book
        }
        imageViewIcon.setImageResource(iconRes)
        when (item) {
            is Book -> {
                editTextExtra1.setText(item.pages.toString())
                editTextExtra2.setText(item.author)
            }
            is Newspaper -> {
                editTextExtra1.setText(item.issueNumber.toString())
                editTextExtra2.setText(item.month.toString())
            }
            is Disc -> {
                editTextExtra1.setText(item.discType.name)
                editTextExtra2.visibility = View.GONE
            }
            else -> { }
        }
        spinnerType.visibility = View.GONE
    }

    private fun setFieldsEditable(editable: Boolean) {
        editTextName.isEnabled = editable
        editTextExtra1.isEnabled = editable
        editTextExtra2.isEnabled = editable
        spinnerType.isEnabled = editable
        buttonSave.visibility = if (editable) View.VISIBLE else View.GONE
    }
}
