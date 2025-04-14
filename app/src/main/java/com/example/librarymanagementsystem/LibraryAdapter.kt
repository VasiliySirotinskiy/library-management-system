package com.example.librarymanagementsystem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import java.io.Serializable

class LibraryAdapter(private var libraryItems: MutableList<LibraryItem>) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    // Вложенный класс для хранения ссылок на вьюшки элемента списка
    class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_view)
        val itemIcon: ImageView = itemView.findViewById(R.id.item_icon)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemId: TextView = itemView.findViewById(R.id.item_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library, parent, false)
        return LibraryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val item = libraryItems[position]

        // Установка текста
        holder.itemName.text = item.getName()
        holder.itemId.text = "ID: ${item.getId()}"

        // Настройка прозрачности текста для недоступных элементов
        val alphaValue = if (item.isAvailable()) 1f else 0.3f
        holder.itemName.alpha = alphaValue
        holder.itemId.alpha = alphaValue

        // Установка подъема карточки
        val elevationDp = if (item.isAvailable()) 10 else 1
        holder.cardView.cardElevation = elevationDp * holder.itemView.context.resources.displayMetrics.density

        // Установка соответствующей иконки по типу элемента
        val iconRes = when (item) {
            is Book -> R.drawable.ic_book
            is Newspaper -> R.drawable.ic_newspaper
            is Disc -> R.drawable.ic_disc
            else -> R.drawable.ic_default  // Вариант по умолчанию
        }
        holder.itemIcon.setImageResource(iconRes)

        // Обработка клика
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            // Запуск нового экрана для просмотра деталей
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra("libraryItem", item as Serializable)
                putExtra("editable", false)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = libraryItems.size

    // Метод для удаления элемента при свайпе
    fun removeItem(position: Int) {
        libraryItems.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: MutableList<LibraryItem>) {
        libraryItems = newItems
        notifyDataSetChanged()
    }
}