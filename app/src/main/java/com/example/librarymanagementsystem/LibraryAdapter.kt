package com.example.librarymanagementsystem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class LibraryAdapter(var libraryItems: List<LibraryItem>) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    var onItemClick: ((LibraryItem) -> Unit)? = null

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

        holder.itemName.text = item.getName()
        holder.itemId.text = "ID: ${item.getId()}"
        val alphaValue = if (item.isAvailable()) 1f else 0.3f
        holder.itemName.alpha = alphaValue
        holder.itemId.alpha = alphaValue
        val elevationDp = if (item.isAvailable()) 10 else 1
        holder.cardView.cardElevation = elevationDp * holder.itemView.context.resources.displayMetrics.density
        val iconRes = when (item) {
            is Book -> R.drawable.ic_book
            is Newspaper -> R.drawable.ic_newspaper
            is Disc -> R.drawable.ic_disc
            else -> R.drawable.ic_default
        }
        holder.itemIcon.setImageResource(iconRes)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = libraryItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newItems: List<LibraryItem>) {
        libraryItems = newItems
        notifyDataSetChanged()
    }
}
