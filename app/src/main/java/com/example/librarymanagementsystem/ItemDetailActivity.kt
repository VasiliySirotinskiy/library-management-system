package com.example.librarymanagementsystem

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class ItemDetailActivity : AppCompatActivity() {

    private var editable: Boolean = false
    private var isNewItem: Boolean = false

    private lateinit var imageViewIcon: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewId: TextView
    private lateinit var editTextExtra1: EditText
    private lateinit var editTextExtra2: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var buttonSave: Button

    // Получение ViewModel для экрана деталей
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageViewIcon = findViewById(R.id.image_view_icon)
        editTextName = findViewById(R.id.edit_text_name)
        textViewId = findViewById(R.id.text_view_id)
        editTextExtra1 = findViewById(R.id.edit_text_extra1)
        editTextExtra2 = findViewById(R.id.edit_text_extra2)
        spinnerType = findViewById(R.id.spinner_type)
        buttonSave = findViewById(R.id.button_save)

        editable = intent.getBooleanExtra("editable", false)
        isNewItem = intent.getBooleanExtra("isNewItem", false)

        // Если передан уже существующий элемент - режим просмотра
        val currentItem: LibraryItem? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("libraryItem", LibraryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("libraryItem") as? LibraryItem
        }

        if (currentItem != null) {
            // Режим просмотра существующего элемента
            editable = false
            viewModel.setItem(currentItem)
            populateFields(currentItem)
        } else {
            // Режим добавления нового элемента: делаем видимым спиннер для выбора типа
            spinnerType.visibility = Spinner.VISIBLE
            val types = listOf("Книга", "Газета", "Диск")
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerType.adapter = adapterSpinner
            textViewId.text = "Новый элемент"
        }
        setFieldsEditable(editable)

        buttonSave.setOnClickListener {
            if (isNewItem) {
                val type = spinnerType.selectedItem.toString()
                val name = editTextName.text.toString()
                if (name.isEmpty()) {
                    Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val extra1 = editTextExtra1.text.toString()
                val extra2 = editTextExtra2.text.toString()
                // Передача данных во ViewModel, получение готового объекта
                val newItem = viewModel.createNewItem(type, name, extra1, extra2)
                val resultIntent = intent.apply {
                    putExtra("newItem", newItem as Serializable)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                finish()
            }
        }
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
                editTextExtra2.visibility = EditText.GONE
            }
            else -> { }
        }
        spinnerType.visibility = Spinner.GONE
    }

    private fun setFieldsEditable(editable: Boolean) {
        editTextName.isEnabled = editable
        editTextExtra1.isEnabled = editable
        editTextExtra2.isEnabled = editable
        spinnerType.isEnabled = editable
        buttonSave.visibility = if (editable) Button.VISIBLE else Button.GONE
    }
}
