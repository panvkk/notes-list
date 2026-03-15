package com.example.noteslist.ui.recycler.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemDateTitleBinding
import com.example.noteslist.ui.recycler.holders.DateTitleViewHolder

class DateTitleDelegate : AdapterDelegate<ViewTyped> {

    override fun isForViewType(
        items: List<ViewTyped>,
        position: Int
    ): Boolean {
        return items[position] is ViewTyped.DateTitle
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTyped.DateTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateTitleBinding.inflate(inflater)
        return DateTitleViewHolder(binding)
    }

    override fun onBindViewHolder(
        items: List<ViewTyped>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val item = items[position]
        (holder as? DateTitleViewHolder)?.bind(item as ViewTyped.DateTitle)
    }
}