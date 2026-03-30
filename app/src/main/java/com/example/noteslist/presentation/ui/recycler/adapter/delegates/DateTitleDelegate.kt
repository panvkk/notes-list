package com.example.noteslist.presentation.ui.recycler.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.databinding.ItemDateTitleBinding
import com.example.noteslist.presentation.ui.recycler.holders.DateTitleViewHolder

class DateTitleDelegate : AdapterDelegate<ViewTypedModel> {

    override fun isForViewType(
        items: List<ViewTypedModel>,
        position: Int
    ): Boolean {
        return items[position] is ViewTypedModel.DateTitle
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTypedModel.DateTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateTitleBinding.inflate(inflater, parent, false)
        return DateTitleViewHolder(binding)
    }

    override fun onBindViewHolder(
        items: List<ViewTypedModel>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val item = items[position]
        (holder as? DateTitleViewHolder)?.bind(item as ViewTypedModel.DateTitle)
    }
}