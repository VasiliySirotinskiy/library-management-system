package com.example.librarymanagementsystem.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagementsystem.R
import com.example.librarymanagementsystem.domain.model.GoogleBook

class GoogleBooksAdapter(
    private var items: List<GoogleBook>
) : RecyclerView.Adapter<GoogleBooksAdapter.VH>() {

    var onItemLongClick: ((GoogleBook) -> Unit)? = null

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val card       : CardView  = view.findViewById(R.id.card_view)
        val title      : TextView  = view.findViewById(R.id.item_name)
        val authors    : TextView  = view.findViewById(R.id.item_id)
        val pagesCount : TextView  = view.findViewById(R.id.item_extra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_google_book, parent, false)
        return VH(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val book = items[position]
        holder.title.text      = book.title
        holder.authors.text    = book.authors
        holder.pagesCount.text = "стр. ${book.pageCount}"
        holder.card.setOnLongClickListener {
            onItemLongClick?.invoke(book)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(newItems: List<GoogleBook>) {
        items = newItems
        notifyDataSetChanged()
    }
}
